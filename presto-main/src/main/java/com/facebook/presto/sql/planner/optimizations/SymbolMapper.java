/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.sql.planner.optimizations;

import com.facebook.presto.spi.block.SortOrder;
import com.facebook.presto.spi.plan.PlanNode;
import com.facebook.presto.spi.plan.PlanNodeId;
import com.facebook.presto.spi.plan.PlanNodeIdAllocator;
import com.facebook.presto.spi.relation.CallExpression;
import com.facebook.presto.spi.relation.RowExpression;
import com.facebook.presto.spi.relation.RowExpressionRewriter;
import com.facebook.presto.spi.relation.RowExpressionTreeRewriter;
import com.facebook.presto.spi.relation.VariableReferenceExpression;
import com.facebook.presto.sql.planner.OrderingScheme;
import com.facebook.presto.sql.planner.PartitioningScheme;
import com.facebook.presto.sql.planner.Symbol;
import com.facebook.presto.sql.planner.TypeProvider;
import com.facebook.presto.sql.planner.plan.AggregationNode;
import com.facebook.presto.sql.planner.plan.AggregationNode.Aggregation;
import com.facebook.presto.sql.planner.plan.StatisticAggregations;
import com.facebook.presto.sql.planner.plan.StatisticAggregationsDescriptor;
import com.facebook.presto.sql.planner.plan.StatisticsWriterNode;
import com.facebook.presto.sql.planner.plan.TableFinishNode;
import com.facebook.presto.sql.planner.plan.TableWriterNode;
import com.facebook.presto.sql.planner.plan.TopNNode;
import com.facebook.presto.sql.tree.Expression;
import com.facebook.presto.sql.tree.ExpressionRewriter;
import com.facebook.presto.sql.tree.ExpressionTreeRewriter;
import com.facebook.presto.sql.tree.SymbolReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static com.facebook.presto.sql.planner.plan.AggregationNode.groupingSets;
import static com.facebook.presto.sql.relational.OriginalExpressionUtils.castToExpression;
import static com.facebook.presto.sql.relational.OriginalExpressionUtils.castToRowExpression;
import static com.facebook.presto.sql.relational.OriginalExpressionUtils.isExpression;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.Objects.requireNonNull;

public class SymbolMapper
{
    private final Map<String, String> mapping;
    private final TypeProvider types;

    public SymbolMapper(Map<VariableReferenceExpression, VariableReferenceExpression> mapping)
    {
        requireNonNull(mapping, "mapping is null");
        this.mapping = mapping.entrySet().stream().collect(toImmutableMap(entry -> entry.getKey().getName(), entry -> entry.getValue().getName()));
        ImmutableSet.Builder<VariableReferenceExpression> variables = ImmutableSet.builder();
        mapping.entrySet().forEach(entry -> {
            variables.add(entry.getKey());
            variables.add(entry.getValue());
        });
        this.types = TypeProvider.fromVariables(variables.build());
    }

    public SymbolMapper(Map<String, String> mapping, TypeProvider types)
    {
        requireNonNull(mapping, "mapping is null");
        this.mapping = ImmutableMap.copyOf(mapping);
        this.types = requireNonNull(types, "types is null");
    }

    public Symbol map(Symbol symbol)
    {
        String canonical = symbol.getName();
        while (mapping.containsKey(canonical) && !mapping.get(canonical).equals(canonical)) {
            canonical = mapping.get(canonical);
        }
        return new Symbol(canonical);
    }

    public VariableReferenceExpression map(VariableReferenceExpression variable)
    {
        String canonical = variable.getName();
        while (mapping.containsKey(canonical) && !mapping.get(canonical).equals(canonical)) {
            canonical = mapping.get(canonical);
        }
        if (canonical.equals(variable.getName())) {
            return variable;
        }
        return new VariableReferenceExpression(canonical, types.get(new Symbol(canonical)));
    }

    public Expression map(Expression value)
    {
        return ExpressionTreeRewriter.rewriteWith(new ExpressionRewriter<Void>()
        {
            @Override
            public Expression rewriteSymbolReference(SymbolReference node, Void context, ExpressionTreeRewriter<Void> treeRewriter)
            {
                Symbol canonical = map(Symbol.from(node));
                return canonical.toSymbolReference();
            }
        }, value);
    }

    public RowExpression map(RowExpression value)
    {
        if (isExpression(value)) {
            return castToRowExpression(map(castToExpression(value)));
        }
        return RowExpressionTreeRewriter.rewriteWith(new RowExpressionRewriter<Void>()
        {
            @Override
            public RowExpression rewriteVariableReference(VariableReferenceExpression variable, Void context, RowExpressionTreeRewriter<Void> treeRewriter)
            {
                return map(variable);
            }
        }, value);
    }

    public OrderingScheme map(OrderingScheme orderingScheme)
    {
        // SymbolMapper inlines symbol with multiple level reference (SymbolInliner only inline single level).
        ImmutableList.Builder<VariableReferenceExpression> orderBy = ImmutableList.builder();
        ImmutableMap.Builder<VariableReferenceExpression, SortOrder> ordering = ImmutableMap.builder();
        for (VariableReferenceExpression variable : orderingScheme.getOrderBy()) {
            VariableReferenceExpression translated = map(variable);
            orderBy.add(translated);
            ordering.put(translated, orderingScheme.getOrdering(variable));
        }
        return new OrderingScheme(orderBy.build(), ordering.build());
    }

    public AggregationNode map(AggregationNode node, PlanNode source)
    {
        return map(node, source, node.getId());
    }

    public AggregationNode map(AggregationNode node, PlanNode source, PlanNodeIdAllocator idAllocator)
    {
        return map(node, source, idAllocator.getNextId());
    }

    private AggregationNode map(AggregationNode node, PlanNode source, PlanNodeId newNodeId)
    {
        ImmutableMap.Builder<VariableReferenceExpression, Aggregation> aggregations = ImmutableMap.builder();
        for (Entry<VariableReferenceExpression, Aggregation> entry : node.getAggregations().entrySet()) {
            aggregations.put(map(entry.getKey()), map(entry.getValue()));
        }

        return new AggregationNode(
                newNodeId,
                source,
                aggregations.build(),
                groupingSets(
                        mapAndDistinctVariable(node.getGroupingKeys()),
                        node.getGroupingSetCount(),
                        node.getGlobalGroupingSets()),
                ImmutableList.of(),
                node.getStep(),
                node.getHashVariable().map(this::map),
                node.getGroupIdVariable().map(this::map));
    }

    private Aggregation map(Aggregation aggregation)
    {
        return new Aggregation(
                new CallExpression(
                        aggregation.getCall().getDisplayName(),
                        aggregation.getCall().getFunctionHandle(),
                        aggregation.getCall().getType(),
                        aggregation.getArguments().stream().map(this::map).collect(toImmutableList())),
                aggregation.getFilter().map(this::map),
                aggregation.getOrderBy().map(this::map),
                aggregation.isDistinct(),
                aggregation.getMask().map(this::map));
    }

    public TopNNode map(TopNNode node, PlanNode source, PlanNodeId newNodeId)
    {
        ImmutableList.Builder<VariableReferenceExpression> variables = ImmutableList.builder();
        ImmutableMap.Builder<VariableReferenceExpression, SortOrder> orderings = ImmutableMap.builder();
        Set<VariableReferenceExpression> seenCanonicals = new HashSet<>(node.getOrderingScheme().getOrderBy().size());
        for (VariableReferenceExpression variable : node.getOrderingScheme().getOrderBy()) {
            VariableReferenceExpression canonical = map(variable);
            if (seenCanonicals.add(canonical)) {
                seenCanonicals.add(canonical);
                variables.add(canonical);
                orderings.put(canonical, node.getOrderingScheme().getOrdering(variable));
            }
        }

        return new TopNNode(
                newNodeId,
                source,
                node.getCount(),
                new OrderingScheme(variables.build(), orderings.build()),
                node.getStep());
    }

    public TableWriterNode map(TableWriterNode node, PlanNode source)
    {
        return map(node, source, node.getId());
    }

    public TableWriterNode map(TableWriterNode node, PlanNode source, PlanNodeId newNodeId)
    {
        // Intentionally does not use canonicalizeAndDistinct as that would remove columns
        ImmutableList<VariableReferenceExpression> columns = node.getColumns().stream()
                .map(this::map)
                .collect(toImmutableList());

        return new TableWriterNode(
                newNodeId,
                source,
                node.getTarget(),
                map(node.getRowCountVariable()),
                map(node.getFragmentVariable()),
                map(node.getTableCommitContextVariable()),
                columns,
                node.getColumnNames(),
                node.getPartitioningScheme().map(partitioningScheme -> canonicalize(partitioningScheme, source)),
                node.getStatisticsAggregation().map(this::map),
                node.getStatisticsAggregationDescriptor().map(this::map));
    }

    public StatisticsWriterNode map(StatisticsWriterNode node, PlanNode source)
    {
        return new StatisticsWriterNode(
                node.getId(),
                source,
                node.getTarget(),
                node.getRowCountVariable(),
                node.isRowCountEnabled(),
                node.getDescriptor().map(this::map));
    }

    public TableFinishNode map(TableFinishNode node, PlanNode source)
    {
        return new TableFinishNode(
                node.getId(),
                source,
                node.getTarget(),
                map(node.getRowCountVariable()),
                node.getStatisticsAggregation().map(this::map),
                node.getStatisticsAggregationDescriptor().map(descriptor -> descriptor.map(this::map)));
    }

    private PartitioningScheme canonicalize(PartitioningScheme scheme, PlanNode source)
    {
        return new PartitioningScheme(
                scheme.getPartitioning().translate(this::map),
                mapAndDistinctVariable(source.getOutputVariables()),
                scheme.getHashColumn().map(this::map),
                scheme.isReplicateNullsAndAny(),
                scheme.getBucketToPartition());
    }

    private StatisticAggregations map(StatisticAggregations statisticAggregations)
    {
        Map<VariableReferenceExpression, Aggregation> aggregations = statisticAggregations.getAggregations().entrySet().stream()
                .collect(toImmutableMap(entry -> map(entry.getKey()), entry -> map(entry.getValue())));
        return new StatisticAggregations(aggregations, mapAndDistinctVariable(statisticAggregations.getGroupingVariables()));
    }

    private StatisticAggregationsDescriptor<VariableReferenceExpression> map(StatisticAggregationsDescriptor<VariableReferenceExpression> descriptor)
    {
        return descriptor.map(this::map);
    }

    private List<Symbol> mapAndDistinctSymbol(List<Symbol> outputs)
    {
        Set<Symbol> added = new HashSet<>();
        ImmutableList.Builder<Symbol> builder = ImmutableList.builder();
        for (Symbol symbol : outputs) {
            Symbol canonical = map(symbol);
            if (added.add(canonical)) {
                builder.add(canonical);
            }
        }
        return builder.build();
    }

    private List<VariableReferenceExpression> mapAndDistinctVariable(List<VariableReferenceExpression> outputs)
    {
        Set<VariableReferenceExpression> added = new HashSet<>();
        ImmutableList.Builder<VariableReferenceExpression> builder = ImmutableList.builder();
        for (VariableReferenceExpression variable : outputs) {
            VariableReferenceExpression canonical = map(variable);
            if (added.add(canonical)) {
                builder.add(canonical);
            }
        }
        return builder.build();
    }

    public static SymbolMapper.Builder builder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private final ImmutableMap.Builder<VariableReferenceExpression, VariableReferenceExpression> mappingsBuilder = ImmutableMap.builder();

        public SymbolMapper build()
        {
            return new SymbolMapper(mappingsBuilder.build());
        }

        public void put(VariableReferenceExpression from, VariableReferenceExpression to)
        {
            mappingsBuilder.put(from, to);
        }
    }
}

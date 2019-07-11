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
package com.facebook.presto.cli;

import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.eq;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import com.diffblue.deeptestutils.Reflector;
import com.diffblue.deeptestutils.mock.DTUMemberMatcher;
import com.facebook.presto.cli.FormatUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import io.airlift.units.DataSize.Unit;
import io.airlift.units.DataSize;
import io.airlift.units.Duration;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.expectation.PowerMockitoStubber;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

@RunWith(PowerMockRunner.class)
public class FormatUtilsTest {

    @Rule public final ExpectedException thrown = ExpectedException.none();

    @Rule public final Timeout globalTimeout = new Timeout(10000);

    /* testedClasses: FormatUtils */
    // Test written by Diffblue Cover.

    @Test
    public void constructorOutputVoid() throws InvocationTargetException, InstantiationException,
        IllegalAccessException, NoSuchMethodException {

        // Act, creating object to test constructor
        final Class<?> classUnderTest = Reflector.forName("com.facebook.presto.cli.FormatUtils");
        final Constructor<?> ctor = classUnderTest.getDeclaredConstructor();
        ctor.setAccessible(true);
        final FormatUtils objectUnderTest = (FormatUtils)ctor.newInstance();

        // Method returns void, testing that no exception is thrown
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatCountInputPositiveOutputNotNull() throws Exception {

        // Arrange
        final long count = 3500L;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn("3").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);

        // Act
        final String retval = FormatUtils.formatCount(count);

        // Assert result
        Assert.assertEquals("3K", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatCountInputPositiveOutputNotNull2() throws Exception {

        // Arrange
        final long count = 8_116_649_282_859_531_264L;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn("1a 2b 3c").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);

        // Act
        final String retval = FormatUtils.formatCount(count);

        // Assert result
        Assert.assertEquals("1a 2b 3cQ", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatCountInputPositiveOutputNotNull3() throws Exception {

        // Arrange
        final long count = 15_892_578_125L;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn("\'").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);

        // Act
        final String retval = FormatUtils.formatCount(count);

        // Assert result
        Assert.assertEquals("\'B", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({Duration.class, FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatCountRateInputPositiveInfinityNotNullFalseOutputNotNull() throws Exception {

        // Arrange
        final double count = Double.POSITIVE_INFINITY;
        final Duration duration = PowerMockito.mock(Duration.class);
        final Method getValueMethod =
            DTUMemberMatcher.method(Duration.class, "getValue", TimeUnit.class);
        PowerMockito.doReturn(0.0)
            .when(duration, getValueMethod)
            .withArguments(or(isA(TimeUnit.class), isNull(TimeUnit.class)));
        final boolean longForm = false;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn(",").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);

        // Act
        final String retval = FormatUtils.formatCountRate(count, duration, longForm);

        // Assert result
        Assert.assertEquals(",", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({Duration.class, FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatCountRateInputPositiveNotNullFalseOutputNotNull() throws Exception {

        // Arrange
        final double count = 0x0.0400010001ap-1022 /* 3.47669e-310 */;
        final Duration duration = PowerMockito.mock(Duration.class);
        final Method getValueMethod =
            DTUMemberMatcher.method(Duration.class, "getValue", TimeUnit.class);
        PowerMockito.doReturn(-0x1p-1021 /* -4.45015e-308 */)
            .when(duration, getValueMethod)
            .withArguments(or(isA(TimeUnit.class), isNull(TimeUnit.class)));
        final boolean longForm = false;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn(",").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);

        // Act
        final String retval = FormatUtils.formatCountRate(count, duration, longForm);

        // Assert result
        Assert.assertEquals(",", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({Duration.class, FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatCountRateInputPositiveNotNullTrueOutputNotNull() throws Exception {

        // Arrange
        final double count = 0x0.0400010001ap-1022 /* 3.47669e-310 */;
        final Duration duration = PowerMockito.mock(Duration.class);
        final Method getValueMethod =
            DTUMemberMatcher.method(Duration.class, "getValue", TimeUnit.class);
        PowerMockito.doReturn(-0x1p-1021 /* -4.45015e-308 */)
            .when(duration, getValueMethod)
            .withArguments(or(isA(TimeUnit.class), isNull(TimeUnit.class)));
        final boolean longForm = true;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn(",").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);

        // Act
        final String retval = FormatUtils.formatCountRate(count, duration, longForm);

        // Assert result
        Assert.assertEquals(",/s", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({Duration.class, FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatCountRateInputPositiveNotNullTrueOutputNotNull2() throws Exception {

        // Arrange
        final double count = 0x1.876c168399acep-19 /* 2.91632e-06 */;
        final Duration duration = PowerMockito.mock(Duration.class);
        final Method getValueMethod =
            DTUMemberMatcher.method(Duration.class, "getValue", TimeUnit.class);
        PowerMockito.doReturn(0x0.0001800604p-1022 /* 5.0931e-313 */)
            .when(duration, getValueMethod)
            .withArguments(or(isA(TimeUnit.class), isNull(TimeUnit.class)));
        final boolean longForm = true;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn("\u0000 ").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);

        // Act
        final String retval = FormatUtils.formatCountRate(count, duration, longForm);

        // Assert result
        Assert.assertEquals("\u0000/s", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({Duration.class, FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatCountRateInputZeroNotNullFalseOutputNotNull() throws Exception {

        // Arrange
        final double count = 0.0;
        final Duration duration = PowerMockito.mock(Duration.class);
        final Method getValueMethod =
            DTUMemberMatcher.method(Duration.class, "getValue", TimeUnit.class);
        PowerMockito.doReturn(0.0)
            .when(duration, getValueMethod)
            .withArguments(or(isA(TimeUnit.class), isNull(TimeUnit.class)));
        final boolean longForm = false;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn("1234").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);

        // Act
        final String retval = FormatUtils.formatCountRate(count, duration, longForm);

        // Assert result
        Assert.assertEquals("1234", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({DataSize.class, Duration.class, FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatDataRateInputNotNullNotNullFalseOutputNotNull() throws Exception {

        // Arrange
        final DataSize dataSize = PowerMockito.mock(DataSize.class);
        final Method toBytesMethod = DTUMemberMatcher.method(DataSize.class, "toBytes");
        PowerMockito.doReturn(-4L).when(dataSize, toBytesMethod).withNoArguments();
        final Duration duration = PowerMockito.mock(Duration.class);
        final Method getValueMethod =
            DTUMemberMatcher.method(Duration.class, "getValue", TimeUnit.class);
        PowerMockito.doReturn(Double.NEGATIVE_INFINITY)
            .when(duration, getValueMethod)
            .withArguments(or(isA(TimeUnit.class), isNull(TimeUnit.class)));
        final boolean longForm = false;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn(",").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);
        final DataSize dataSize1 = PowerMockito.mock(DataSize.class);
        final Method toBytesMethod1 = DTUMemberMatcher.method(DataSize.class, "toBytes");
        PowerMockito.doReturn(0L).when(dataSize1, toBytesMethod1).withNoArguments();
        PowerMockito.whenNew(DataSize.class)
            .withParameterTypes(double.class, DataSize.Unit.class)
            .withArguments(anyDouble(), or(isA(DataSize.Unit.class), isNull(DataSize.Unit.class)))
            .thenReturn(dataSize1);

        // Act
        final String retval = FormatUtils.formatDataRate(dataSize, duration, longForm);

        // Assert result
        Assert.assertEquals(",B", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({DataSize.class, Duration.class, FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatDataRateInputNotNullNotNullFalseOutputNotNull2() throws Exception {

        // Arrange
        final DataSize dataSize = PowerMockito.mock(DataSize.class);
        final Method toBytesMethod = DTUMemberMatcher.method(DataSize.class, "toBytes");
        PowerMockito.doReturn(0L).when(dataSize, toBytesMethod).withNoArguments();
        final Duration duration = PowerMockito.mock(Duration.class);
        final Method getValueMethod =
            DTUMemberMatcher.method(Duration.class, "getValue", TimeUnit.class);
        PowerMockito.doReturn(-0.0)
            .when(duration, getValueMethod)
            .withArguments(or(isA(TimeUnit.class), isNull(TimeUnit.class)));
        final boolean longForm = false;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn("a\'b\'c").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);
        final DataSize dataSize1 = PowerMockito.mock(DataSize.class);
        final Method toBytesMethod1 = DTUMemberMatcher.method(DataSize.class, "toBytes");
        PowerMockito.doReturn(-8_796_093_022_208L).when(dataSize1, toBytesMethod1).withNoArguments();
        PowerMockito.whenNew(DataSize.class)
            .withParameterTypes(double.class, DataSize.Unit.class)
            .withArguments(anyDouble(), or(isA(DataSize.Unit.class), isNull(DataSize.Unit.class)))
            .thenReturn(dataSize1);

        // Act
        final String retval = FormatUtils.formatDataRate(dataSize, duration, longForm);

        // Assert result
        Assert.assertEquals("a\'b\'cB", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({DataSize.class, Duration.class, FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatDataRateInputNotNullNotNullTrueOutputNotNull() throws Exception {

        // Arrange
        final DataSize dataSize = PowerMockito.mock(DataSize.class);
        final Method toBytesMethod = DTUMemberMatcher.method(DataSize.class, "toBytes");
        PowerMockito.doReturn(-4L).when(dataSize, toBytesMethod).withNoArguments();
        final Duration duration = PowerMockito.mock(Duration.class);
        final Method getValueMethod =
            DTUMemberMatcher.method(Duration.class, "getValue", TimeUnit.class);
        PowerMockito.doReturn(Double.NEGATIVE_INFINITY)
            .when(duration, getValueMethod)
            .withArguments(or(isA(TimeUnit.class), isNull(TimeUnit.class)));
        final boolean longForm = true;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn("A1B2C3").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);
        final DataSize dataSize1 = PowerMockito.mock(DataSize.class);
        final Method toBytesMethod1 = DTUMemberMatcher.method(DataSize.class, "toBytes");
        PowerMockito.doReturn(17_592_186_044_416L).when(dataSize1, toBytesMethod1).withNoArguments();
        PowerMockito.whenNew(DataSize.class)
            .withParameterTypes(double.class, DataSize.Unit.class)
            .withArguments(anyDouble(), or(isA(DataSize.Unit.class), isNull(DataSize.Unit.class)))
            .thenReturn(dataSize1);

        // Act
        final String retval = FormatUtils.formatDataRate(dataSize, duration, longForm);

        // Assert result
        Assert.assertEquals("A1B2C3TB/s", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({DataSize.class, Duration.class, FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatDataRateInputNotNullNotNullTrueOutputNotNull2() throws Exception {

        // Arrange
        final DataSize dataSize = PowerMockito.mock(DataSize.class);
        final Method toBytesMethod = DTUMemberMatcher.method(DataSize.class, "toBytes");
        PowerMockito.doReturn(0L).when(dataSize, toBytesMethod).withNoArguments();
        final Duration duration = PowerMockito.mock(Duration.class);
        final Method getValueMethod =
            DTUMemberMatcher.method(Duration.class, "getValue", TimeUnit.class);
        PowerMockito.doReturn(-0.0)
            .when(duration, getValueMethod)
            .withArguments(or(isA(TimeUnit.class), isNull(TimeUnit.class)));
        final boolean longForm = true;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn("1a 2b 3c").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);
        final DataSize dataSize1 = PowerMockito.mock(DataSize.class);
        final Method toBytesMethod1 = DTUMemberMatcher.method(DataSize.class, "toBytes");
        PowerMockito.doReturn(-8_796_093_022_208L).when(dataSize1, toBytesMethod1).withNoArguments();
        PowerMockito.whenNew(DataSize.class)
            .withParameterTypes(double.class, DataSize.Unit.class)
            .withArguments(anyDouble(), or(isA(DataSize.Unit.class), isNull(DataSize.Unit.class)))
            .thenReturn(dataSize1);

        // Act
        final String retval = FormatUtils.formatDataRate(dataSize, duration, longForm);

        // Assert result
        Assert.assertEquals("1a 2b 3cB/s", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({DataSize.class, FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatDataSizeInputNotNullFalseOutputNotNull() throws Exception {

        // Arrange
        final DataSize size = PowerMockito.mock(DataSize.class);
        final Method toBytesMethod = DTUMemberMatcher.method(DataSize.class, "toBytes");
        PowerMockito.doReturn(268_435_456L).when(size, toBytesMethod).withNoArguments();
        final boolean longForm = false;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn("3").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);

        // Act
        final String retval = FormatUtils.formatDataSize(size, longForm);

        // Assert result
        Assert.assertEquals("3M", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({DataSize.class, FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatDataSizeInputNotNullFalseOutputNotNull2() throws Exception {

        // Arrange
        final DataSize size = PowerMockito.mock(DataSize.class);
        final Method toBytesMethod = DTUMemberMatcher.method(DataSize.class, "toBytes");
        PowerMockito.doReturn(14_636_698_788_954_111L).when(size, toBytesMethod).withNoArguments();
        final boolean longForm = false;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn("").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);

        // Act
        final String retval = FormatUtils.formatDataSize(size, longForm);

        // Assert result
        Assert.assertEquals("P", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({DataSize.class, FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatDataSizeInputNotNullTrueOutputNotNull() throws Exception {

        // Arrange
        final DataSize size = PowerMockito.mock(DataSize.class);
        final Method toBytesMethod = DTUMemberMatcher.method(DataSize.class, "toBytes");
        PowerMockito.doReturn(65_536L).when(size, toBytesMethod).withNoArguments();
        final boolean longForm = true;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn("BAZ").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);

        // Act
        final String retval = FormatUtils.formatDataSize(size, longForm);

        // Assert result
        Assert.assertEquals("BAZKB", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({DataSize.class, FormatUtils.class, DecimalFormat.class})
    @Test
    public void formatDataSizeInputNotNullTrueOutputNotNull2() throws Exception {

        // Arrange
        final DataSize size = PowerMockito.mock(DataSize.class);
        final Method toBytesMethod = DTUMemberMatcher.method(DataSize.class, "toBytes");
        PowerMockito.doReturn(34_359_738_368L).when(size, toBytesMethod).withNoArguments();
        final boolean longForm = true;
        final DecimalFormat decimalFormat = PowerMockito.mock(DecimalFormat.class);
        final Method formatMethod =
            DTUMemberMatcher.method(DecimalFormat.class, "format", double.class);
        PowerMockito.doReturn("1234").when(decimalFormat, formatMethod).withArguments(anyDouble());
        PowerMockito.whenNew(DecimalFormat.class)
            .withParameterTypes(String.class)
            .withArguments(or(isA(String.class), isNull(String.class)))
            .thenReturn(decimalFormat);

        // Act
        final String retval = FormatUtils.formatDataSize(size, longForm);

        // Assert result
        Assert.assertEquals("1234GB", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({Preconditions.class, Strings.class})
    @Test
    public void formatProgressBarInputNegativeNegativeNegativeNegativeOutputNotNull()
        throws Exception {

        // Setup mocks
        PowerMockito.mockStatic(Strings.class);
        PowerMockito.mockStatic(Preconditions.class);

        // Arrange
        final int width = -18_657_433;
        final int complete = -1_636_707_711;
        final int running = -544_088_049;
        final int total = -2_147_483_632;
        final Method repeatMethod =
            DTUMemberMatcher.method(Strings.class, "repeat", String.class, int.class);
        ((PowerMockitoStubber)PowerMockito.doReturn("\'").doReturn("1").doReturn("2"))
            .when(Strings.class, repeatMethod)
            .withArguments(or(isA(String.class), isNull(String.class)), anyInt());

        // Act
        final String retval = FormatUtils.formatProgressBar(width, complete, running, total);

        // Assert result
        Assert.assertEquals("\'12", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({Preconditions.class, Strings.class})
    @Test
    public void formatProgressBarInputNegativeNegativePositiveNegativeOutputNotNull()
        throws Exception {

        // Setup mocks
        PowerMockito.mockStatic(Strings.class);
        PowerMockito.mockStatic(Preconditions.class);

        // Arrange
        final int width = -712_859_373;
        final int complete = -953_991_806;
        final int running = 691_038_840;
        final int total = -1;
        final Method repeatMethod =
            DTUMemberMatcher.method(Strings.class, "repeat", String.class, int.class);
        ((PowerMockitoStubber)PowerMockito.doReturn("\'").doReturn("1").doReturn("2"))
            .when(Strings.class, repeatMethod)
            .withArguments(or(isA(String.class), isNull(String.class)), anyInt());

        // Act
        final String retval = FormatUtils.formatProgressBar(width, complete, running, total);

        // Assert result
        Assert.assertEquals("\'12", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({Preconditions.class, Strings.class})
    @Test
    public void formatProgressBarInputNegativeNegativePositiveNegativeOutputNotNull2()
        throws Exception {

        // Setup mocks
        PowerMockito.mockStatic(Strings.class);
        PowerMockito.mockStatic(Preconditions.class);

        // Arrange
        final int width = -654_141_895;
        final int complete = -1_993_662_590;
        final int running = 899_733_656;
        final int total = -1_073_741_825;
        final Method repeatMethod =
            DTUMemberMatcher.method(Strings.class, "repeat", String.class, int.class);
        ((PowerMockitoStubber)PowerMockito.doReturn("\'").doReturn("1").doReturn("2"))
            .when(Strings.class, repeatMethod)
            .withArguments(or(isA(String.class), isNull(String.class)), anyInt());

        // Act
        final String retval = FormatUtils.formatProgressBar(width, complete, running, total);

        // Assert result
        Assert.assertEquals("\'12", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({Preconditions.class, Strings.class})
    @Test
    public void formatProgressBarInputNegativePositivePositiveNegativeOutputNotNull()
        throws Exception {

        // Setup mocks
        PowerMockito.mockStatic(Strings.class);
        PowerMockito.mockStatic(Preconditions.class);

        // Arrange
        final int width = -504_820_728;
        final int complete = 1_928_592_727;
        final int running = 699_355_253;
        final int total = -94_068;
        final Method repeatMethod =
            DTUMemberMatcher.method(Strings.class, "repeat", String.class, int.class);
        ((PowerMockitoStubber)PowerMockito.doReturn("\'").doReturn("1").doReturn("2"))
            .when(Strings.class, repeatMethod)
            .withArguments(or(isA(String.class), isNull(String.class)), anyInt());

        // Act
        final String retval = FormatUtils.formatProgressBar(width, complete, running, total);

        // Assert result
        Assert.assertEquals("\'12", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest(Strings.class)
    @Test
    public void formatProgressBarInputPositivePositiveOutputNotNull() throws Exception {

        // Setup mocks
        PowerMockito.mockStatic(Strings.class);

        // Arrange
        final int width = 1_572_868;
        final int tick = 2_097_154;
        final Method repeatMethod =
            DTUMemberMatcher.method(Strings.class, "repeat", String.class, int.class);
        ((PowerMockitoStubber)PowerMockito.doReturn("3").doReturn("3").doReturn("1"))
            .when(Strings.class, repeatMethod)
            .withArguments(or(isA(String.class), isNull(String.class)), anyInt());

        // Act
        final String retval = FormatUtils.formatProgressBar(width, tick);

        // Assert result
        Assert.assertEquals("3<3>1", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({Preconditions.class, Strings.class})
    @Test
    public void formatProgressBarInputPositivePositivePositivePositiveOutputNotNull()
        throws Exception {

        // Setup mocks
        PowerMockito.mockStatic(Strings.class);
        PowerMockito.mockStatic(Preconditions.class);

        // Arrange
        final int width = 201_062_390;
        final int complete = 557_088_511;
        final int running = 2_101_690_626;
        final int total = 1;
        final Method repeatMethod =
            DTUMemberMatcher.method(Strings.class, "repeat", String.class, int.class);
        ((PowerMockitoStubber)PowerMockito.doReturn("\'").doReturn("1").doReturn("2"))
            .when(Strings.class, repeatMethod)
            .withArguments(or(isA(String.class), isNull(String.class)), anyInt());

        // Act
        final String retval = FormatUtils.formatProgressBar(width, complete, running, total);

        // Assert result
        Assert.assertEquals("\'12", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest(Strings.class)
    @Test
    public void formatProgressBarInputZeroZeroOutputNotNull() throws Exception {

        // Setup mocks
        PowerMockito.mockStatic(Strings.class);

        // Arrange
        final int width = 0;
        final int tick = 0;
        final Method repeatMethod =
            DTUMemberMatcher.method(Strings.class, "repeat", String.class, int.class);
        ((PowerMockitoStubber)PowerMockito.doReturn("3").doReturn("3").doReturn("1"))
            .when(Strings.class, repeatMethod)
            .withArguments(or(isA(String.class), isNull(String.class)), anyInt());

        // Act
        final String retval = FormatUtils.formatProgressBar(width, tick);

        // Assert result
        Assert.assertEquals("3<3>1", retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest(Strings.class)
    @Test
    public void formatProgressBarInputZeroZeroZeroZeroOutputNull() throws Exception {

        // Setup mocks
        PowerMockito.mockStatic(Strings.class);

        // Arrange
        final int width = 0;
        final int complete = 0;
        final int running = 0;
        final int total = 0;
        final Method repeatMethod =
            DTUMemberMatcher.method(Strings.class, "repeat", String.class, int.class);
        PowerMockito.doReturn(null)
            .when(Strings.class, repeatMethod)
            .withArguments(or(isA(String.class), isNull(String.class)), anyInt());

        // Act
        final String retval = FormatUtils.formatProgressBar(width, complete, running, total);

        // Assert result
        Assert.assertNull(retval);
    }

    // Test written by Diffblue Cover.
    @PrepareForTest({Ints.class, Duration.class})
    @Test
    public void formatTimeInputNotNullOutputNotNull() throws Exception {

        // Setup mocks
        PowerMockito.mockStatic(Ints.class);

        // Arrange
        final Duration duration = PowerMockito.mock(Duration.class);
        final Method roundToMethod = DTUMemberMatcher.method(Duration.class, "roundTo", TimeUnit.class);
        PowerMockito.doReturn(0L)
            .when(duration, roundToMethod)
            .withArguments(or(isA(TimeUnit.class), isNull(TimeUnit.class)));
        final Method saturatedCastMethod =
            DTUMemberMatcher.method(Ints.class, "saturatedCast", long.class);
        PowerMockito.doReturn(1).when(Ints.class, saturatedCastMethod).withArguments(anyLong());

        // Act
        final String retval = FormatUtils.formatTime(duration);

        // Assert result
        Assert.assertEquals("\u0000\u0000\u0000\u0000:1", retval);
    }

    // Test written by Diffblue Cover.
    @Test
    public void pluralizeInputNotNullNegativeOutputNotNull() {

        // Arrange
        final String word = "3";
        final int count = -2_147_483_647;

        // Act
        final String retval = FormatUtils.pluralize(word, count);

        // Assert result
        Assert.assertEquals("3s", retval);
    }

    // Test written by Diffblue Cover.
    @Test
    public void pluralizeInputNotNullPositiveOutputNotNull() {

        // Arrange
        final String word = "3";
        final int count = 1;

        // Act
        final String retval = FormatUtils.pluralize(word, count);

        // Assert result
        Assert.assertEquals("3", retval);
    }
}

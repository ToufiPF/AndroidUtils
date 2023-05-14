package ch.epfl.toufi.android_utils

import ch.epfl.toufi.android_utils.LogicExtensions.reduceAll
import ch.epfl.toufi.android_utils.LogicExtensions.reduceAny
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LogicExtensionsTest {

    @Test
    fun booleanArrayReduceAllReturnsTrueIfEmpty() {
        assertTrue(BooleanArray(0).reduceAll())
    }

    @Test
    fun booleanArrayReduceAnyReturnsFalseIfEmpty() {
        assertFalse(BooleanArray(0).reduceAny())
    }

    @Test
    fun booleanArrayReduceAnyReturnsTrueIffSomeElementIsTrue() {
        assertFalse(BooleanArray(20) { false }.reduceAny())
        assertTrue(BooleanArray(15) { it == 5 }.reduceAny())
        assertTrue(BooleanArray(5) { it != 3 }.reduceAny())
        assertTrue(BooleanArray(10) { true }.reduceAny())
    }

    @Test
    fun booleanArrayReduceAllReturnsTrueIffAllElementsAreTrue() {
        assertFalse(BooleanArray(20) { false }.reduceAll())
        assertFalse(BooleanArray(15) { it == 5 }.reduceAll())
        assertFalse(BooleanArray(5) { it != 3 }.reduceAll())
        assertTrue(BooleanArray(10) { true }.reduceAll())
    }
}

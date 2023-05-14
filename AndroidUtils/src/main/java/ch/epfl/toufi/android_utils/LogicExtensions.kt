package ch.epfl.toufi.android_utils

/**
 * Extension functions that can run w/o android runtime
 */
object LogicExtensions {

    /**
     * @return true if no element in the array is false.
     * If it's empty, returns true.
     */
    fun BooleanArray.reduceAll(): Boolean = this.all { it }

    /**
     * @return true if any element in the array is true.
     * If it's empty, returns false
     */
    fun BooleanArray.reduceAny(): Boolean = this.any { it }
}

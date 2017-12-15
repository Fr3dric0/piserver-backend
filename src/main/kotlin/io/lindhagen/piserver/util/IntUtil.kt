package io.lindhagen.piserver.util

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    15/12/2017
 */
open class IntUtil

/**
 * Ensures the given Integer stays between a range.
 * Does not mutate the existing instance
 * */
fun Int.conformToRange(min: Int, max: Int): Int = when {
    this < min -> min
    this > max -> max
    else -> this
}
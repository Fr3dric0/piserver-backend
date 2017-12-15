package io.lindhagen.piserver.util

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    15/12/2017
 */
open class FloatUtil

/**
 * Ensures a given Float stays between the given range.
 * Does not mutate the existing instance
 * */
fun Float.conformToRange(min: Float, max: Float): Float = when {
    this < min -> min
    this > max -> max
    else -> this
}
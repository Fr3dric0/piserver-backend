package io.lindhagen.piserver.util

/**
 * @author:     Fredrik F. Lindhagen <fred.lindh96@gmail.com>
 * @created:    03/12/2017
 */
open class StringUtil

/**
 * Checks if the string contains any numbers
 * */
fun String.containsNumbers() = this.hasProperty { it.isDigit() }

/**
 * Checks if the string contains an uppercase letter
 * */
fun String.containsUpperCaseLetters() = this.hasProperty { it.isUpperCase() }

/**
 * Checks if the string contains a lowercase letter
 * */
fun String.containsLowerCaseLetters() = this.hasProperty { it.isLowerCase() }

/**
 * Iterate over each char in string,
 * stops and returns true if propCheck returns true.
 *
 * @param predicate Testing function
 * */
fun String.hasProperty(predicate: (c: Char) -> Boolean): Boolean {
    for (c in this) {
        if (predicate(c)) {
            return true
        }
    }

    return false
}
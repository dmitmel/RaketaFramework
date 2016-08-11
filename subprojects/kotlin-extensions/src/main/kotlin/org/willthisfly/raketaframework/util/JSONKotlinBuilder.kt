package org.willthisfly.raketaframework.util

import java.util.*

/**
 * JSON DSL for Kotlin.
 *
 * ### Example
 *
 * ```kotlin
 * data class Address(val streetAddress: String, val city: String, val postalCode: String)
 *
 * data class PhoneNumber(val type: String, val number: String)
 *
 * data class Person(val firstName: String, val lastName: String,
 *                   val isAlive: Boolean, val age: Int,
 *                   val address: Address, val phoneNumbers: Set<PhoneNumber>) {
 *     fun toJSON(): JSONKotlinBuilder.JSONMap {
 *         return JSONKotlinBuilder.json {
 *             "firstName" withValue firstName
 *             "lastName" withValue lastName
 *             "isAlive" withValue isAlive
 *             "age" withValue age
 *             "address" withValue map {
 *                 "streetAddress" withValue address.streetAddress
 *                 "city" withValue address.city
 *                 "postalCode" withValue address.postalCode
 *             }
 *             "phoneNumbers" withValue list {
 *                 for (phoneNumber in phoneNumbers) {
 *                     "type" withValue phoneNumber.type
 *                     "number" withValue phoneNumber.number
 *                 }
 *             }
 *         } // End of JSON
 *     }
 * }
 * ```
 *
 * @see JSONEntry
 * @see JSONMap
 * @see JSONList
 */
object JSONKotlinBuilder {
    fun json(jsonSupplier: JSONMap.() -> Unit): JSONMap = JSONMap().apply(jsonSupplier)

    /**
     * Represents map in JSON tree.
     *
     * ### Usage
     *
     * ```kotlin
     * // Multiline usage
     * map {
     *     entry("key1", "value1")
     *     entry("key2", "value2")
     * }
     *
     * // Single-line usage
     * map("key1" to "value1", "key2" to "value2")
     * ```
     *
     * @see JSONEntry
     */
    class JSONMap : HashMap<String, Any> {
        constructor(vararg pairs: Pair<String, Any>) : super(mapOf(*pairs))
        constructor() : super()
        constructor(initialCapacity: Int) : super(initialCapacity)
        constructor(m: MutableMap<String, Any>) : super(m)

        fun entry(key: String, value: Any) {
            put(key, value)
        }

        fun entry(key: String, valueSupplier: () -> Any) {
            put(key, valueSupplier.invoke())
        }

        fun entry(entrySupplier: JSONEntry.() -> Unit) {
            val entry = JSONEntry().apply(entrySupplier)
            put(entry.key(), entry.value())
        }

        infix fun String.setValue(value: Any) = this.withValue(value)
        infix fun String.with(value: Any) = this.withValue(value)
        infix fun String.pointsTo(value: Any) = this.withValue(value)
        infix fun String.to(value: Any) = this.withValue(value)
        infix fun String.withValue(value: Any) {
            put(this, value)
        }

        fun list(listSupplier: JSONList.() -> Unit): JSONList = JSONList().apply(listSupplier)

        fun list(vararg items: Any): JSONList = JSONList(*items)

        fun map(entriesSupplier: JSONMap.() -> Unit): JSONMap = JSONMap().apply(entriesSupplier)

        fun map(vararg pairs: Pair<String, Any>) = JSONMap(*pairs)
    }

    /**
     * Represents entry in JSON map.
     *
     * ### Usage
     *
     * ```kotlin
     * map {
     *     entry("key", "value")          // Simple usage
     *
     *     entry("key") { "value" }       // Useful for maps/lists for values
     *
     *     // Extended syntax for long keys and values
     *     entry {
     *         key("key")      // or 'key = ...'
     *         value("value")  // or 'value = ...'
     *     }
     *
     *     "someKey" withValue "value"    // Some DSL for english-like code
     *                                    // You can also use 'with', 'pointsTo', 'to' or 'setValue' instead of 'withValue'
     * }
     * ```
     */
    class JSONEntry {
        var key: String? = null
        var value: Any? = null

        fun key(): String = key!!
        fun key(key: String) {
            this.key = key
        }

        fun value(): Any = value!!
        fun value(value: Any) {
            this.value = value
        }
    }

    /**
     * Represents list in JSON tree.
     *
     * ### Usage
     *
     * ```kotlin
     * // Multiline usage
     * list {
     *     ...
     * }
     *
     * // Single-line usage
     * list(...)
     * ```
     *
     * ### Adding things in multiline lists
     *
     * ```kotlin
     * // Adding list
     * list {
     *     list(1, 2, 3)         // []          => [[1, 2, 3]]
     *     list { 4, 5, 6 }      // [[1, 2, 3]] => [[1, 2, 3], [4, 5, 6]]
     * }
     *
     * // Adding one item
     * list {
     *     item(1)      // []     => [1]
     *     item(2)      // [1]    => [1, 2]
     *     item(3)      // [1, 2] => [1, 2, 3]
     * }
     *
     * // Adding multiple items
     * list {
     *     items(1, 2, 3)       // []        => [1, 2, 3]
     *     items(4, 5, 6)       // [1, 2, 3] => [1, 2, 3, 4, 5, 6]
     * }
     *
     * // Adding maps
     * list {
     *     map { entry("one", 1)}        // []           => [{"one": 1}]
     *     map { entry("1", "one") }     // [{"one": 1}] => [{"one": 1}, {"1", "one"}]
     * }
     * ```
     */
    class JSONList : ArrayList<Any> {
        constructor(vararg items: Any) : super(listOf(*items))
        constructor() : super()
        constructor(c: MutableCollection<Any>) : super(c)

        fun list(listSupplier: JSONList.() -> Unit) {
            add(JSONList().apply(listSupplier))
        }

        fun list(vararg items: Any) {
            add(JSONList(*items))
        }

        fun items(vararg items: Any) {
            addAll(arrayListOf(*items))
        }

        fun item(item: Any) {
            add(item)
        }

        fun map(entriesSupplier: JSONMap.() -> Unit) {
            add(JSONMap().apply(entriesSupplier))
        }
    }
}

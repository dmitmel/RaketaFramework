package org.willthisfly.raketaframework

import java.util.*

operator fun RequestParams.plus(entry: Pair<String, Any>): RequestParams {
    val result = HashMap<String, Any>()
    result.putAll(this)
    result += entry
    return RequestParams(result)
}

operator fun RequestParams.plus(entry: Map.Entry<String, Any>): RequestParams {
    val result = RequestParams()
    result.putAll(this)
    result += entry
    return result
}

operator fun RequestParams.plus(entries: Iterable<Map.Entry<String, Any>>): RequestParams {
    val result = RequestParams()
    result.putAll(this)
    result += entries
    return result
}

operator fun RequestParams.plus(map: Map<String, Any>): RequestParams {
    val result = HashMap<String, Any>()
    result.putAll(this)
    result += map
    return RequestParams(result)
}

operator fun RequestParams.plusAssign(entry: Pair<String, Any>) {
    put(entry.first, entry.second)
}

operator fun RequestParams.plusAssign(entry: Map.Entry<String, Any>) {
    put(entry.key, entry.value)
}

operator fun RequestParams.plusAssign(entries: Iterable<Map.Entry<String, Any>>) {
    entries.forEach { put(it.key, it.value) }
}

operator fun RequestParams.plusAssign(map: Map<String, Any>) {
    this += map.entries
}

operator fun RequestParams.minusAssign(key: String) {
    remove(key)
}

operator fun <T> RequestParams.get(key: String): T = get(key)

operator fun <T> RequestParams.get(key: String, orElse: T): T = getOrElse(key, orElse)

operator fun RequestParams.set(key: String, value: Any) {
    put(key, value)
}

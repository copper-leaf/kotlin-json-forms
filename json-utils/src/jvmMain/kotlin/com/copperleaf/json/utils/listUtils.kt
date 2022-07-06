package com.copperleaf.json.utils

public fun <T> List<T>.takeHead(): Pair<T, List<T>> {
    return this.first() to this.drop(1)
}

public inline fun <T, U> List<T>.takeHead(mapHead: (T) -> U): Pair<U, List<T>> {
    return mapHead(this.first()) to this.drop(1)
}

public fun <T> List<T>.takeHead(numberOfHeadElements: Int): Pair<List<T>, List<T>> {
    return this.take(numberOfHeadElements) to this.drop(numberOfHeadElements)
}

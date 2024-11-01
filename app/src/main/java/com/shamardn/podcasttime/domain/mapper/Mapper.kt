package com.shamardn.podcasttime.domain.mapper

abstract class Mapper<I, O> {
    abstract fun map(input: I): O
    abstract fun reverseMap(input: O): I
    open fun mapList(input: List<I>): List<O> {
        return if (input.isEmpty()) emptyList()
        else input.map {
            map(it)
        }
    }

}
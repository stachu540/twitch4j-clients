package com.github.twitch4j.clients.mapper

import java.io.IOException
import kotlin.reflect.KClass

@Throws(IOException::class)
fun <T : Any> IBody.getAs(type: KClass<T>): T = getAs(type.java)

@Throws(IOException::class)
inline fun <reified T : Any> IBody.getAs(): T = getAs(T::class)

@Throws(IOException::class)
fun <T : Any> IMapper.mapFrom(body: IBody, type: KClass<T>): T = mapFrom(body, type.java)

@Throws(IOException::class)
inline fun <reified T : Any> IMapper.mapFrom(body: IBody): T = mapFrom(body, T::class)

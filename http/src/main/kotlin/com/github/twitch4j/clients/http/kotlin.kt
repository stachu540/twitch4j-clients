package com.github.twitch4j.clients.http

import com.github.twitch4j.clients.http.rest.Rest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.reflect.KClass

@JvmOverloads
fun <T : Any> HttpClient.get(endpoint: String, type: KClass<T>, spec: RequestSpec.() -> Unit = {}): Rest<T> =
	get(endpoint, type.java, spec)

@JvmOverloads
inline fun <reified T : Any> HttpClient.get(endpoint: String, noinline spec: RequestSpec.() -> Unit = {}): Rest<T> =
	get(endpoint, T::class, spec)

@JvmOverloads
fun <T : Any> HttpClient.post(endpoint: String, type: KClass<T>, spec: RequestSpec.() -> Unit = {}): Rest<T> =
	post(endpoint, type.java, spec)

@JvmOverloads
inline fun <reified T : Any> HttpClient.post(endpoint: String, noinline spec: RequestSpec.() -> Unit = {}): Rest<T> =
	post(endpoint, T::class, spec)

@JvmOverloads
fun <T : Any> HttpClient.put(endpoint: String, type: KClass<T>, spec: RequestSpec.() -> Unit = {}): Rest<T> =
	put(endpoint, type.java, spec)

@JvmOverloads
inline fun <reified T : Any> HttpClient.put(endpoint: String, noinline spec: RequestSpec.() -> Unit = {}): Rest<T> =
	put(endpoint, T::class, spec)

@JvmOverloads
fun <T : Any> HttpClient.patch(endpoint: String, type: KClass<T>, spec: RequestSpec.() -> Unit = {}): Rest<T> =
	patch(endpoint, type.java, spec)

@JvmOverloads
inline fun <reified T : Any> HttpClient.patch(endpoint: String, noinline spec: RequestSpec.() -> Unit = {}): Rest<T> =
	patch(endpoint, T::class, spec)

@JvmOverloads
fun <T : Any> HttpClient.delete(endpoint: String, type: KClass<T>, spec: RequestSpec.() -> Unit = {}): Rest<T> =
	delete(endpoint, type.java, spec)

@JvmOverloads
inline fun <reified T : Any> HttpClient.delete(endpoint: String, noinline spec: RequestSpec.() -> Unit = {}): Rest<T> =
	delete(endpoint, T::class, spec)

@JvmOverloads
fun <T : Any> HttpClient.options(endpoint: String, type: KClass<T>, spec: RequestSpec.() -> Unit = {}): Rest<T> =
	options(endpoint, type.java, spec)

@JvmOverloads
inline fun <reified T : Any> HttpClient.options(
	endpoint: String,
	noinline spec: RequestSpec.() -> Unit = {}
): Rest<T> =
	options(endpoint, T::class, spec)

suspend fun <T : Any> Rest<T>.await(scope: CoroutineScope = GlobalScope) = async(scope).await()

fun <T : Any> Rest<T>.async(scope: CoroutineScope = GlobalScope) = scope.async {
	suspendCancellableCoroutine<T> {
		enqueue(it::resume, it::cancel)
	}
}

fun <T : Any> Rest<Iterable<T>>.flow(scope: CoroutineScope = GlobalScope): Flow<T> =
	runBlocking(scope.coroutineContext) {
		await(this)
	}.asFlow()

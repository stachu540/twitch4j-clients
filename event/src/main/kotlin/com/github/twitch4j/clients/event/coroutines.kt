package com.github.twitch4j.clients.event

import java.util.function.Consumer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking

class CoroutineEventManger(private val channel: Channel<Event> = Channel(Channel.CONFLATED)) : EventManager {

	@Suppress("UNCHECKED_CAST")
	override fun registerHandler(event: Any) {
		EventUtils.fetchEvents(event)
			.forEach { (type, result) -> onEvent(type as Class<Event>, result as Consumer<Event>) }
	}

	override fun <T : Event?> onEvent(type: Class<T>, result: Consumer<T>) {
		runBlocking {
			channel.receiveAsFlow().filter { type.isInstance(it) }
				.map { type.cast(it) }.onEach { result.accept(it) }
				.collect()
		}
	}

	override fun handle(event: Event) {
		channel.sendBlocking(event)
	}
}

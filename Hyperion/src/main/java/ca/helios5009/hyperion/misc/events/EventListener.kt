package ca.helios5009.hyperion.misc.events

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference

class EventListener() {
	var value = AtomicReference("")
	private val triggerFunctions = mutableListOf<Event>()
	private val queue = AtomicReference(mutableListOf<String>())
	private val scopes = mutableListOf<Job>()

	fun Subscribe(function: Event) {
		triggerFunctions.add(function)
	}

	fun call(newValue: String) {
		if (newValue.lowercase() == "nothing") {
			return
		}
		queue.get().add(newValue)
		triggerFunctions.forEach {
			if (it.event.lowercase() == newValue) {
				queue.get().remove(newValue)
				scopes.add(CoroutineScope(Dispatchers.Default).launch {
					it.run()
					return@launch
				})
			}
		}
	}

	fun isInQueue(message: String): Boolean {
		if (queue.get().isEmpty()) {
			return false
		}

		if (queue.get().contains(message)) {
			queue.get().remove(message)
			return true
		}
		return false
	}

	fun clearQueue() {
		queue.get().clear()
	}

	fun clearScopes() {
		scopes.forEach {
			it.cancel()
		}
		scopes.clear()
	}

}

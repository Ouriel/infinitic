/**
 * "Commons Clause" License Condition v1.0
 *
 * The Software is provided to you by the Licensor under the License, as defined below, subject to
 * the following condition.
 *
 * Without limiting other conditions in the License, the grant of rights under the License will not
 * include, and the License does not grant to you, the right to Sell the Software.
 *
 * For purposes of the foregoing, “Sell” means practicing any or all of the rights granted to you
 * under the License to provide to third parties, for a fee or other consideration (including
 * without limitation fees for hosting or consulting/ support services related to the Software), a
 * product or service whose value derives, entirely or substantially, from the functionality of the
 * Software. Any license notice or attribution required by the License must also include this
 * Commons Clause License Condition notice.
 *
 * Software: Infinitic
 *
 * License: MIT License (https://opensource.org/licenses/MIT)
 *
 * Licensor: infinitic.io
 */
package io.infinitic.inMemory

import io.github.oshai.kotlinlogging.KotlinLogging
import io.infinitic.common.data.MillisDuration
import io.infinitic.common.emitters.EmitterName
import io.infinitic.common.messages.Message
import io.infinitic.common.transport.Topic
import io.infinitic.common.transport.interfaces.InfiniticProducer
import io.infinitic.inMemory.channels.InMemoryChannels
import io.infinitic.inMemory.channels.id
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InMemoryInfiniticProducer(
  private val mainChannels: InMemoryChannels,
  private val eventListenerChannels: InMemoryChannels
) : InfiniticProducer {

  private val scope = CoroutineScope(Dispatchers.IO)

  override val emitterName = EmitterName("InMemory")

  override suspend fun <T : Message> internalSendTo(
    message: T,
    topic: Topic<out T>,
    after: MillisDuration
  ) {
    topic.channelsForMessage(message).forEach {
      logger.trace { "Topic $topic(${it.id}): sending $message after $after" }
      when {
        after <= 0 -> it.send(message)
        else -> scope.launch {
          delay(after.millis)
          it.send(message)
        }
      }
      logger.debug { "Topic $topic(${it.id}): sent $message" }
    }
  }

  private fun <S : Message> Topic<out S>.channelsForMessage(message: S): List<Channel<S>> {
    val entity = message.entity()

    return listOf(
        with(mainChannels) { channel(entity) },
        with(eventListenerChannels) { channel(entity) },
    )
  }

  companion object {
    private val logger = KotlinLogging.logger {}
  }
}



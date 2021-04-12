/**
 * "Commons Clause" License Condition v1.0
 *
 * The Software is provided to you by the Licensor under the License, as defined
 * below, subject to the following condition.
 *
 * Without limiting other conditions in the License, the grant of rights under the
 * License will not include, and the License does not grant to you, the right to
 * Sell the Software.
 *
 * For purposes of the foregoing, “Sell” means practicing any or all of the rights
 * granted to you under the License to provide to third parties, for a fee or
 * other consideration (including without limitation fees for hosting or
 * consulting/ support services related to the Software), a product or service
 * whose value derives, entirely or substantially, from the functionality of the
 * Software. Any license notice or attribution required by the License must also
 * include this Commons Clause License Condition notice.
 *
 * Software: Infinitic
 *
 * License: MIT License (https://opensource.org/licenses/MIT)
 *
 * Licensor: infinitic.io
 */

package io.infinitic.pulsar.workers

import io.infinitic.common.tasks.engine.transport.SendToTaskEngine
import io.infinitic.common.tasks.executors.messages.TaskExecutorEnvelope
import io.infinitic.common.tasks.executors.messages.TaskExecutorMessage
import io.infinitic.pulsar.transport.PulsarMessageToProcess
import io.infinitic.tasks.TaskExecutorRegister
import io.infinitic.tasks.executor.worker.startTaskExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import org.apache.pulsar.client.api.Consumer

typealias PulsarTaskExecutorMessageToProcess = PulsarMessageToProcess<TaskExecutorMessage>

fun CoroutineScope.startPulsarTaskExecutors(
    concurrency: Int,
    taskExecutorRegister: TaskExecutorRegister,
    consumer: Consumer<TaskExecutorEnvelope>,
    sendToTaskEngine: SendToTaskEngine
) {
    val inputChannel = Channel<PulsarTaskExecutorMessageToProcess>()
    val outputChannel = Channel<PulsarTaskExecutorMessageToProcess>()

    // launch n=concurrency coroutines running task executors
    repeat(concurrency) {
        startTaskExecutor(
            "pulsar-task-executor-$it",
            taskExecutorRegister,
            inputChannel,
            outputChannel,
            sendToTaskEngine
        )
    }

    // coroutine pulling pulsar commands messages
    pullMessages(consumer, inputChannel)

    // coroutine acknowledging pulsar commands messages
    acknowledgeMessages(consumer, outputChannel)
}
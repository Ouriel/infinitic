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

package io.infinitic.workflows.engine.handlers

import io.infinitic.common.clients.messages.SendToChannelCompleted
import io.infinitic.common.workflows.data.commands.CommandReturnValue
import io.infinitic.common.workflows.engine.messages.SendToChannel
import io.infinitic.common.workflows.engine.state.WorkflowState
import io.infinitic.workflows.engine.helpers.commandCompleted
import io.infinitic.workflows.engine.transport.WorkflowEngineOutput

suspend fun sendToChannel(
    workflowEngineOutput: WorkflowEngineOutput,
    state: WorkflowState,
    msg: SendToChannel
) {
    state.receivingChannels.firstOrNull {
        it.channelName == msg.channelName
    }?.also {
        state.receivingChannels.remove(it)

        commandCompleted(
            workflowEngineOutput,
            state,
            it.methodRunId,
            it.commandId,
            CommandReturnValue(msg.channelEvent.serializedData)
        )
    }

    // if client is waiting, send output back to it
    if (msg.clientWaiting) {
        workflowEngineOutput.sendToClientResponse(
            state,
            SendToChannelCompleted(
                clientName = msg.clientName,
                channelEventId = msg.channelEventId
            )
        )
    }
}
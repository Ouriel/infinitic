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
package io.infinitic.tests.inline

import io.infinitic.Test
import io.infinitic.exceptions.WorkflowExecutorException
import io.infinitic.exceptions.WorkflowFailedException
import io.infinitic.exceptions.workflows.InvalidInlineException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay

internal class InlineWorkflowTests :
  StringSpec(
      {
        val client = Test.client

        val inlineWorkflow = client.newWorkflow(InlineWorkflow::class.java)

        "Inline task" {
          inlineWorkflow.inline1(7) shouldBe "2 * 7 = 14"

          delay(5000)
        }

        "Inline task with asynchronous task inside" {
          val error = shouldThrow<WorkflowFailedException> { inlineWorkflow.inline2(21) }

          val workflowTaskFailed = error.deferredException as WorkflowExecutorException
          workflowTaskFailed.lastFailure.exception!!.name shouldBe InvalidInlineException::class.java.name
        }

        "Inline task with synchronous task inside" {
          val error = shouldThrow<WorkflowFailedException> { inlineWorkflow.inline3(14) }

          val workflowTaskFailed = error.deferredException as WorkflowExecutorException
          workflowTaskFailed.lastFailure.exception!!.name shouldBe InvalidInlineException::class.java.name
        }
      },
  )

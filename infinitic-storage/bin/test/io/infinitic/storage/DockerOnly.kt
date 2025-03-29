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
package io.infinitic.storage

import io.kotest.core.annotation.EnabledCondition
import io.kotest.core.spec.Spec
import kotlin.reflect.KClass
import org.testcontainers.DockerClientFactory

internal class DockerOnly : EnabledCondition {
  override fun enabled(kclass: KClass<out Spec>) = shouldRun

  companion object {
    val shouldRun by lazy {
      // if Docker is not available, skip the tests
      // On GitHub, we must run all tests
      (System.getenv("GITHUB_RUN_NUMBER") != null) ||
          DockerClientFactory.instance().isDockerAvailable
    }
  }
}

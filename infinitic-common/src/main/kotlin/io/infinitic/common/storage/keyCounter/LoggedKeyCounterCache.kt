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

package io.infinitic.common.storage.keyCounter

import mu.KotlinLogging

class LoggedKeyCounterCache(
    val cache: KeyCounterCache
) : KeyCounterCache by cache {

    private val logger = KotlinLogging.logger {}

    override fun get(key: String): Long? {
        val value = cache.get(key)
        logger.debug { "key $key - getCounter $value" }

        return value
    }

    override fun set(key: String, amount: Long) {
        logger.debug { "key $key - setCounter $amount" }
        cache.set(key, amount)
    }

    override fun incr(key: String, amount: Long) {
        logger.debug { "key $key - incrCounter $amount" }
        cache.incr(key, amount)
    }
}

package com.zenaton.engine.attributes.tasks

import com.zenaton.engine.attributes.types.Data

data class TaskAttemptError(override val data: ByteArray) : Data(data)

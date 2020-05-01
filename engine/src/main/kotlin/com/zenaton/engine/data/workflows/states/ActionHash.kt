package com.zenaton.engine.data.workflows.states

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

data class ActionHash @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(@get:JsonValue val hash: String)

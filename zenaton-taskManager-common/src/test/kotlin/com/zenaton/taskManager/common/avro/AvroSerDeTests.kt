package com.zenaton.taskManager.common.avro

import com.zenaton.common.avro.AvroSerDe
import com.zenaton.taskManager.common.utils.TestFactory
import com.zenaton.taskManager.states.AvroTaskEngineState
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AvroSerDeTests : StringSpec({
    "Avro state should be SerDe-reversible through ByteBuffer" {
        // given
        val state = TestFactory.random(AvroTaskEngineState::class)
        // when
        val serState = AvroSerDe.serialize(state)
        val deState = AvroSerDe.deserialize<AvroTaskEngineState>(serState)
        // then
        state shouldBe deState
    }

    "Avro state should be SerDe-reversible through ByteArray" {
        // given
        val state = TestFactory.random(AvroTaskEngineState::class)
        // when
        val serState = AvroSerDe.serializeToByteArray(state)
        val deState = AvroSerDe.deserializeFromByteArray<AvroTaskEngineState>(serState)
        // then
        state shouldBe deState
    }
})

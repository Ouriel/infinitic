package com.zenaton.engine.messages

enum class MessageType(val className: String?) {
    WORKFLOW_DISPATCHED(WorkflowDispatched::class.qualifiedName),
    TASK_DISPATCHED(TaskDispatched::class.qualifiedName)
}
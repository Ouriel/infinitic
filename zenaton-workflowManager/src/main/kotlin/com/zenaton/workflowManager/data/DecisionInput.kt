package com.zenaton.workflowManager.data

import com.zenaton.workflowManager.data.branches.Branch
import com.zenaton.workflowManager.data.properties.PropertyStore

data class DecisionInput(
    val branches: List<Branch>,
    val store: PropertyStore
)

package com.torim1999.fl

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FederatedClientTest {

    @Test
    fun `client initializes with weights`() = runBlocking {
        val config = FederatedClientConfig(
            clientId = "test_client_init",
            serverAddress = "localhost",
            serverPort = 50051,
            learningRate = 0.01,
            epochs = 1
        )
        val client = FederatedClient(config)
        // Accessing private members for testing purposes, typically done via reflection or public getters
        val currentModelWeightsField = client.javaClass.getDeclaredField("currentModelWeights")
        currentModelWeightsField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val weights = currentModelWeightsField.get(client) as Map<String, Double>

        assertNotNull(weights)
        assertTrue(weights.isNotEmpty())
        assertTrue(weights.containsKey("feature1"))
        assertTrue(weights.containsKey("feature2"))
    }

    @Test
    fun `client can start and complete training rounds`() = runBlocking {
        val config = FederatedClientConfig(
            clientId = "test_client_training",
            serverAddress = "localhost",
            serverPort = 50051,
            learningRate = 0.01,
            epochs = 2
        )
        val client = FederatedClient(config)
        // This test primarily checks if the startTraining function runs without throwing exceptions
        // and completes its iterations. More detailed tests would involve mocking server interactions.
        client.startTraining()
        assertTrue(true, "Training completed without exceptions.")
    }
}

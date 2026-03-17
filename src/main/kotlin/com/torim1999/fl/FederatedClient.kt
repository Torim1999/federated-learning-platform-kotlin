
package com.torim1999.fl

import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

// Represents a simple model update
data class ModelUpdate(val clientId: String, val weights: Map<String, Double>)

// Configuration for the federated learning client
data class FederatedClientConfig(
    val clientId: String,
    val serverAddress: String,
    val serverPort: Int,
    val learningRate: Double,
    val epochs: Int
)

class FederatedClient(private val config: FederatedClientConfig) {

    private val currentModelWeights: ConcurrentHashMap<String, Double> = ConcurrentHashMap()

    init {
        // Initialize with some dummy weights for demonstration
        currentModelWeights["feature1"] = Random.nextDouble()
        currentModelWeights["feature2"] = Random.nextDouble()
        println("Client ${config.clientId} initialized with model weights: $currentModelWeights")
    }

    // Simulates training a local model and generating an update
    private fun trainLocalModel(): ModelUpdate {
        println("Client ${config.clientId} training local model for ${config.epochs} epochs...")
        // In a real scenario, this would involve actual model training on local data
        // For now, we simulate a small update to the weights
        val updatedWeights = currentModelWeights.mapValues { (_, value) ->
            value + (Random.nextDouble() - 0.5) * config.learningRate
        }.toMutableMap()
        currentModelWeights.putAll(updatedWeights)
        println("Client ${config.clientId} finished local training. New weights: $currentModelWeights")
        return ModelUpdate(config.clientId, currentModelWeights.toMap())
    }

    // Simulates sending the model update to the server
    private suspend fun sendUpdateToServer(update: ModelUpdate) {
        println("Client ${config.clientId} sending update to server at ${config.serverAddress}:${config.serverPort}...")
        // In a real scenario, this would be a gRPC or HTTP call to the federated server
        delay(1000) // Simulate network delay
        println("Client ${config.clientId} update sent.")
    }

    // Simulates receiving a global model from the server
    private suspend fun receiveGlobalModel(): Map<String, Double> {
        println("Client ${config.clientId} waiting for global model from server...")
        // In a real scenario, this would be a gRPC or HTTP call to receive the global model
        delay(1500) // Simulate network delay
        val globalWeights = mapOf("feature1" to Random.nextDouble(), "feature2" to Random.nextDouble())
        println("Client ${config.clientId} received global model: $globalWeights")
        return globalWeights
    }

    // Main function to start the federated learning process for this client
    suspend fun startTraining() = coroutineScope {
        println("Client ${config.clientId} starting federated training process.")
        repeat(config.epochs) {
            val localUpdate = trainLocalModel()
            sendUpdateToServer(localUpdate)
            val globalModel = receiveGlobalModel()
            currentModelWeights.putAll(globalModel) // Update local model with global model
            println("Client ${config.clientId} updated local model with global model.")
            delay(2000) // Simulate round interval
        }
        println("Client ${config.clientId} finished federated training.")
    }
}

fun main() = runBlocking {
    val clientConfig = FederatedClientConfig(
        clientId = "client_alpha",
        serverAddress = "localhost",
        serverPort = 50051,
        learningRate = 0.01,
        epochs = 3
    )
    val client = FederatedClient(clientConfig)
    client.startTraining()
}

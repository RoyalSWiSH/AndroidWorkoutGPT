package bio.gelly.workoutplanner.data.model


data class ApiRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double,
    val max_tokens: Int
)

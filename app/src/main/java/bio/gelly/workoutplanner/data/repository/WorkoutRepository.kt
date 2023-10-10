package bio.gelly.workoutplanner.data.repository

import bio.gelly.workoutplanner.data.datasource.WorkoutDataSource
import bio.gelly.workoutplanner.data.datasource.WorkoutRemoteDataSource
import bio.gelly.workoutplanner.data.model.ApiRequest
import bio.gelly.workoutplanner.data.model.Message
import bio.gelly.workoutplanner.data.model.WorkoutInfo
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody

class WorkoutRepository(private val workoutDataSource: WorkoutDataSource) {

    // Function to fetch workout data
    suspend fun getWorkoutInfo(): Result<ResponseBody> {
        return try {
            // You can implement the logic to fetch workout data from a data source (e.g., API, database)
            val apiRequest = ApiRequest(
                model = "gpt-3.5-turbo",
                messages = listOf(Message(role = "user", content = "Say this is a test!")),
                temperature = 0.7
            )
            val requestBodyJson = Gson().toJson(apiRequest)

            val requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyJson)

            val result = workoutDataSource.fetchWorkoutData(requestBody)
            when (result) {
                is Result.Success -> {
                    val workoutInfo = result.data
                    // Handle the successful response here
                    // TODO: Cast responsebody to workoutinfo

                    Result.Success(workoutInfo)
                }
                is Result.Error -> {
                    val errorMessage = result.errorMessage
                    // Handle the error response here
                    Result.Error("Error when casting Result")
                }
            }


        } catch (e: Exception) {
            Result.Error("Failed to fetch workout data: ${e.message}")
        }
    }

    // Function to save workout data (if needed)
    suspend fun saveWorkoutInfo(workoutInfo: WorkoutInfo) {
        // You can implement the logic to save workout data to a data source (e.g., database)
        workoutDataSource.saveWorkoutData(workoutInfo)
    }
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val errorMessage: String) : Result<Nothing>()
}
//
//interface WorkoutRepository {
//
//    suspend fun getWorkoutInfo(): Result<WorkoutInfo>
//
//    suspend fun saveWorkoutInfo(workoutInfo: WorkoutInfo)
//
//}
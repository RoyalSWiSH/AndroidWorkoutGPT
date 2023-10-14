package bio.gelly.workoutplanner.data.datasource


import android.util.Log
import bio.gelly.workoutplanner.data.model.ChatGPTResponseDTO
import bio.gelly.workoutplanner.data.model.WorkoutData
import bio.gelly.workoutplanner.data.model.WorkoutInfo
import bio.gelly.workoutplanner.network.retrofit.WorkoutApiService
import bio.gelly.workoutplanner.data.repository.Result
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Deferred
//import kotlinx.coroutines.await
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.IOException

//import okhttp3.RequestBody

class WorkoutRemoteDataSource(private val apiService: WorkoutApiService):WorkoutDataSource {

    override suspend fun fetchWorkoutData(requestBody: RequestBody): Result<WorkoutInfo> {
        println("fetchWorkoutData before Coroutine")

        return withContext(Dispatchers.IO) {
            try {
                val response =
                    apiService.fetchWorkoutData(requestBody) // Ensure the network request is complete
                println("WorkoutRemoteDataSource fetchWorkoutData")

                // Use ApiService's parseResponseBody function to parse the response
                return@withContext parseResponseBody(response)
            } catch (e: Exception) {
                // Handle network errors and exceptions, and return them as a Result.Error
                println("Network error: ${e.message}")
                return@withContext Result.Error("Network error: ${e.message}")
            }
        }
    }

    override suspend fun saveWorkoutData(workoutInfo: WorkoutInfo) {
        // Replace this with actual network request logic to save data
        // For example, make an API call to save workout data on a server
        /* Your network request code to save data here */
    }

    private fun parseResponseBody(response: Response<ResponseBody>): Result<WorkoutInfo> {
        return try {
            println("parseResponseBody 1")
            if (response.isSuccessful) {
                response.body()?.use { responseBody ->
                    val content = responseBody.string()
                    val gson = Gson()
                    val chatGTPResponseDTO = gson.fromJson(content, ChatGPTResponseDTO::class.java)
                    val workoutInfo = WorkoutInfo(id = chatGTPResponseDTO.id, workoutData = WorkoutData(workoutPlan = chatGTPResponseDTO.choices[0].message.content) )
                    println("parseResponseBody")
                    println(workoutInfo.toString())
                    Result.Success(workoutInfo)

                } ?: Result.Error("ResponseBody is null")
            } else {
                // Handle the error response and return it as Result.Error
                val errorBody = response.errorBody()
                val errorMessage = parseErrorResponseBody(errorBody)
                println("Response Error")
                Result.Error(errorMessage)
            }
        } catch (e: IOException) {
            // Log the exception for debugging and return an error result
            println("Parsing error: ${e.message}")
            Result.Error("Failed to parse the response: ${e.message}")
        }

    }


    private fun parseErrorResponseBody(errorBody: ResponseBody?): String {
        println("Parse Error")
        return try {
            errorBody?.string() ?: "Unknown error"
        } catch (e: Exception) {
            "Error parsing error response: ${e.message}"
        }
    }
}
// Define the WorkoutDataSource interface
interface WorkoutDataSource {

    suspend fun fetchWorkoutData(requestBody: RequestBody): Result<WorkoutInfo>

    suspend fun saveWorkoutData(workoutInfo: WorkoutInfo)

}
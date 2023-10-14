package bio.gelly.workoutplanner.network.retrofit

//import okhttp3.RequestBody
import bio.gelly.workoutplanner.data.model.WorkoutInfo
import bio.gelly.workoutplanner.data.repository.Result
//import com.google.gson.Gson
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.io.IOException


interface WorkoutApiService {

    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer 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"
    )
    @POST("chat/completions")
    suspend fun fetchWorkoutData(@Body requestBody: RequestBody): Response<ResponseBody>

    // Function to parse the successful response body

//    fun parseResponseBody(response: Response<ResponseBody>): Result<WorkoutInfo> {
//       return try {
//            println("parseResponseBody 1")
//            if (response.isSuccessful) {
//                response.body()?.use { responseBody ->
//                    val content = responseBody.string()
//                    val gson = Gson()
//                    val workoutInfo = gson.fromJson(content, WorkoutInfo::class.java)
//                    println("parseResponseBody")
//                    println(workoutInfo.toString())
//                    Result.Success(workoutInfo)
//
//                } ?: Result.Error("ResponseBody is null")
//            } else {
//                // Handle the error response and return it as Result.Error
//                val errorBody = response.errorBody()
//                val errorMessage = parseErrorResponseBody(errorBody)
//                println("Response Error")
//                Result.Error(errorMessage)
//            }
//        } catch (e: IOException) {
//            // Log the exception for debugging and return an error result
//            println("Parsing error: ${e.message}")
//            Result.Error("Failed to parse the response: ${e.message}")
//        }
//
//    }

    // Function to parse the error response body
//    fun parseErrorResponseBody(errorBody: ResponseBody?): String {
//        println("Parse Error")
//        return try {
//            errorBody?.string() ?: "Unknown error"
//        } catch (e: Exception) {
//            "Error parsing error response: ${e.message}"
//        }
//    }
}
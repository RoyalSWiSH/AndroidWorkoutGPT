package bio.gelly.workoutplanner.network.retrofit

//import okhttp3.RequestBody
import bio.gelly.workoutplanner.data.model.WorkoutInfo
import bio.gelly.workoutplanner.data.repository.Result
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface WorkoutApiService {

    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer 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"
    )
    @POST("chat/completions")
    suspend fun fetchWorkoutData(@Body requestBody: okhttp3.RequestBody): Response<ResponseBody>

    // Function to parse the successful response body
    fun parseResponseBody(response: Response<ResponseBody>): Result<ResponseBody> {
        if (response.isSuccessful) {
            val responseBody = response.body()
            if (responseBody != null) {
                // Parse the response and return it as Result.Success
                return Result.Success(responseBody)
            } else {
                return Result.Error("Response body is null")
            }
        } else {
            // Handle the error response and return it as Result.Error
            val errorBody = response.errorBody()
            val errorMessage = parseErrorResponseBody(errorBody)
            return Result.Error(errorMessage)
        }
    }

    // Function to parse the error response body
    fun parseErrorResponseBody(errorBody: ResponseBody?): String {
        return try {
            errorBody?.string() ?: "Unknown error"
        } catch (e: Exception) {
            "Error parsing error response: ${e.message}"
        }
    }
}
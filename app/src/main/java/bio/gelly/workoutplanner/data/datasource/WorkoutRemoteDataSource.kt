package bio.gelly.workoutplanner.data.datasource


import bio.gelly.workoutplanner.data.model.WorkoutInfo
import bio.gelly.workoutplanner.network.retrofit.WorkoutApiService
import bio.gelly.workoutplanner.data.repository.Result
import okhttp3.RequestBody
import okhttp3.ResponseBody

//import okhttp3.RequestBody

class WorkoutRemoteDataSource(private val apiService: WorkoutApiService):WorkoutDataSource {

    override suspend fun fetchWorkoutData(requestBody: RequestBody): Result<ResponseBody> {
        try {
            val response = apiService.fetchWorkoutData(requestBody)

            // Use ApiService's parseResponseBody function to parse the response
            return apiService.parseResponseBody(response)
        } catch (e: Exception) {
            // Handle network errors and exceptions, and return them as a Result.Error
            return Result.Error("Network error: ${e.message}")
        }
    }
    override suspend fun saveWorkoutData(workoutInfo: WorkoutInfo) {
        // Replace this with actual network request logic to save data
        // For example, make an API call to save workout data on a server
        /* Your network request code to save data here */
    }

}


// Define the WorkoutDataSource interface
interface WorkoutDataSource {

    suspend fun fetchWorkoutData(requestBody: RequestBody): Result<ResponseBody>

    suspend fun saveWorkoutData(workoutInfo: WorkoutInfo)

}
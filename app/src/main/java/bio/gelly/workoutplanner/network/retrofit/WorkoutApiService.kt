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
// TODO: Remove OpenAI token
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer $OPEN_API_KEY"
    )
    @POST("chat/completions")
    suspend fun fetchWorkoutData(@Body requestBody: RequestBody): Response<ResponseBody>

}
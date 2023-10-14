package bio.gelly.workoutplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import bio.gelly.workoutplanner.ui.theme.WorkoutPlannerTheme
import bio.gelly.workoutplanner.ui.viewmodel.WorkoutViewModel
import bio.gelly.workoutplanner.data.repository.Result
import androidx.lifecycle.viewmodel.compose.viewModel



//import bio.gelly.workoutplanner.data.model.WorkoutInfo
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import bio.gelly.workoutplanner.data.datasource.WorkoutDataSource
import bio.gelly.workoutplanner.data.datasource.WorkoutRemoteDataSource
import bio.gelly.workoutplanner.data.model.WorkoutInfo
import bio.gelly.workoutplanner.data.repository.WorkoutRepository
import bio.gelly.workoutplanner.network.retrofit.WorkoutApiService
import bio.gelly.workoutplanner.ui.viewmodel.WorkoutViewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels

//import androidx.lifecycle.LiveData
//import androidx.lifecycle.liveData
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    private fun createWorkoutApiService(): WorkoutApiService {
        // Initialize Retrofit and create an instance of the Retrofit service class

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS) // Set the connection timeout to 30 seconds
            .readTimeout(40, TimeUnit.SECONDS) // Set the read timeout to 30 seconds
            .writeTimeout(10, TimeUnit.SECONDS) // Set the write timeout to 30 seconds
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(WorkoutApiService::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = createWorkoutApiService() // Initialize your API service
        val workoutDataSource = WorkoutRemoteDataSource(apiService) // Create the data source
        val workoutRepository = WorkoutRepository(workoutDataSource) // Create the repository

        setContent {
            WorkoutPlannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    Greeting("Android")

                    // Create ViewModelFactory and ViewModel
                    val viewModelFactory = WorkoutViewModelFactory(workoutRepository)
                    val workoutViewModel = ViewModelProvider(this, viewModelFactory).get(WorkoutViewModel::class.java)

                    // UI Composable
                    WorkoutScreen(workoutViewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
@Composable
fun WorkoutScreen(workoutViewModel: WorkoutViewModel) {
//    val workoutInfoState by workoutViewModel.workoutInfoState.observeAsState()
//    private val workoutInfoState = MutableStateFlow<Result<WorkoutInfo>>(Result.Loading)
    val workoutInfoState by workoutViewModel.workoutInfoState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (workoutInfoState) {
            is Result.Success -> {
                val workoutInfo = (workoutInfoState as Result.Success<WorkoutInfo>).data
                Text(text = "Workout Plan: ${workoutInfo.workoutData.workoutPlan}")
            }
            is Result.Error -> {
                val errorMessage = (workoutInfoState as Result.Error).errorMessage
                Text(text = "Error: $errorMessage")
            }
            Result.Loading -> {
//                Text(text = "Loading...")
                CircularProgressIndicator()
            }
            else -> {
                // Handle other states if needed
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { workoutViewModel.refreshWorkoutInfo() },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Fetch Workout Info")
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorkoutPlannerTheme {
        Greeting("Android")
    }
}
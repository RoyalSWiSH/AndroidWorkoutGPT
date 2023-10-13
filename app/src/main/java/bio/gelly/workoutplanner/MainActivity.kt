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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
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
import androidx.lifecycle.ViewModelProvider
import bio.gelly.workoutplanner.data.datasource.WorkoutDataSource
import bio.gelly.workoutplanner.data.datasource.WorkoutRemoteDataSource
import bio.gelly.workoutplanner.data.repository.WorkoutRepository
import bio.gelly.workoutplanner.network.retrofit.WorkoutApiService
import bio.gelly.workoutplanner.ui.viewmodel.WorkoutViewModelFactory
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels

//import androidx.lifecycle.LiveData
//import androidx.lifecycle.liveData
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : ComponentActivity() {
    private fun createWorkoutApiService(): WorkoutApiService {
        // Initialize Retrofit and create an instance of the Retrofit service class
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/v1/")
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
                    Greeting("Android")

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

//    val result: Result<WorkoutInfo> by workoutViewModel.fetchWorkoutInfo().observeAsState()

//    val workoutInfoLiveData: LiveData<Result<WorkoutInfo>> = workoutViewModel.workoutInfoState
//    val workoutInfoState by workoutInfoLiveData.observeAsState(initial = Result.Loading)
//    val workoutViewModel: WorkoutViewModel = hiltViewModel()

//    val workoutInfoState by workoutViewModel.workoutInfoState.collectAsState()
//    val workoutViewModel: WorkoutViewModel =  viewModel()
    val workoutInfoState by workoutViewModel.workoutInfoState.observeAsState()

    Column(
       modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (workoutInfoState) {
            is Result.Success<ResponseBody> -> {
                val workoutInfo = (workoutInfoState as Result.Success<ResponseBody>).data
                // Render the UI with the successful data
//                Text(text = "Workout Plan: ${workoutInfo.workoutData.workoutPlan}")

                Text(text = "Workout Plan: $workoutInfo")
                println(workoutInfo.toString())
            }

            is Result.Error -> {
                val errorMessage = (workoutInfoState as Result.Error).errorMessage
                // Render the UI with an error message
                Text(text = "Error: $errorMessage")
            }

            Result.Loading -> {
                // Render loading UI
                Text(text = "Loading...")
            }
            else -> {
                // Render loading UI or handle other states
            }
        }
    }
        // Button to fetch the workout
        Button(
            onClick = { workoutViewModel.refreshWorkoutInfo() },
            content = { Text("Fetch Workout Info") }
        )
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorkoutPlannerTheme {
        Greeting("Android")
    }
}
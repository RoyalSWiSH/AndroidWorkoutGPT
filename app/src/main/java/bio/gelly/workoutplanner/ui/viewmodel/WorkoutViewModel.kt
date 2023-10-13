package bio.gelly.workoutplanner.ui.viewmodel

import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.Factory


import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
//import androidx.lifecycle.liveData
import bio.gelly.workoutplanner.data.model.WorkoutInfo
import bio.gelly.workoutplanner.data.repository.WorkoutRepository
import bio.gelly.workoutplanner.data.repository.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class WorkoutViewModel(private val workoutRepository: WorkoutRepository) : ViewModel() {

//    private val _workoutInfoState = MutableLiveData<Result<WorkoutInfo>>()
//    val workoutInfoState: LiveData<Result<WorkoutInfo>> = _workoutInfoState

    private val _responseBodyInfoState = MutableLiveData<Result<ResponseBody>>()
    val responseBody: LiveData<Result<ResponseBody>> = _responseBodyInfoState

    val workoutInfoState: LiveData<Result<ResponseBody>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val workoutInfo = fetchWorkoutInfo()

            emit(workoutInfo)
        } catch (e: Exception) {
            emit(Result.Error(e.localizedMessage ?: "An error occurred"))
        }
    }

    private suspend fun fetchWorkoutInfo(): Result<ResponseBody> {
        return workoutRepository.getWorkoutInfo()
    }

    // Function to fetch workout information
    fun refreshWorkoutInfo() {
        viewModelScope.launch {
            try {
                val workoutInfo = fetchWorkoutInfo()
//                workoutInfoState.emit(Result.Success(workoutInfo))
            } catch (e: Exception) {
//                workoutInfoState.emit(Result.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }

//    fun fetchWorkoutInfo() {
//        // Perform the data fetching operation, e.g., using a repository
//        viewModelScope.launch {
//            val result = repository.getWorkoutInfo()
//            _responseBodyInfoState.value = result
////            _workoutInfoState.value = result
//        }
//    }
}

class WorkoutViewModelFactory(private val workoutRepository: WorkoutRepository) : ViewModelProvider.Factory {
     override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            return WorkoutViewModel(workoutRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

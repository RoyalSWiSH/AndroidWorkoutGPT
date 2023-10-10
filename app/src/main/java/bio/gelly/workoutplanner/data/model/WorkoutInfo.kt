package bio.gelly.workoutplanner.data.model

data class WorkoutInfo(val id: String, val workoutData: WorkoutData)

data class WorkoutData(val workoutPlan: String)

fun ChatGPTResponseDTO.toWorkoutInfoMap(): WorkoutInfo {
    return WorkoutInfo(
        id = this.id,
        workoutData = WorkoutData(
            workoutPlan = this.choices.firstOrNull()?.message?.content.orEmpty()
        )
    )
}
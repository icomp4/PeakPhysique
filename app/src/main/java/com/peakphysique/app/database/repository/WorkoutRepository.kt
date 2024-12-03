import com.peakphysique.app.database.dao.WorkoutDao
import com.peakphysique.app.model.SetEntity
import com.peakphysique.app.model.WorkoutEntity
import com.peakphysique.app.model.WorkoutWithSets
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime


class WorkoutRepository(private val workoutDao: WorkoutDao) {
    val allWorkouts: Flow<List<WorkoutWithSets>> = workoutDao.getAllWorkoutsWithSets()

    fun getWorkoutsInRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<WorkoutWithSets>> =
        workoutDao.getWorkoutsInRange(startDate, endDate)

    suspend fun saveWorkout(workout: WorkoutEntity, sets: List<SetEntity>) {
        workoutDao.insertWorkoutWithSets(workout, sets)
    }

    suspend fun deleteWorkout(workout: WorkoutEntity) {
        workoutDao.deleteWorkout(workout)
    }
}
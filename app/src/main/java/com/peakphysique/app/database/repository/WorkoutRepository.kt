import com.peakphysique.app.database.dao.WorkoutDao
import com.peakphysique.app.model.SetEntity
import com.peakphysique.app.model.WorkoutEntity
import com.peakphysique.app.model.WorkoutWithSets
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Repository class that serves as a single source of truth for workout data.
 * Abstracts the data source from the rest of the application and provides
 * clean APIs for accessing and modifying workout data.
 *
 * @property workoutDao Data Access Object for workout-related database operations
 */
class WorkoutRepository(private val workoutDao: WorkoutDao) {
    /**
     * Observable flow of all workouts with their associated sets.
     * Updates are automatically emitted when the underlying data changes.
     */
    val allWorkouts: Flow<List<WorkoutWithSets>> = workoutDao.getAllWorkoutsWithSets()

    /**
     * Retrieves workouts within a specified date range.
     * Useful for filtering workout history and generating reports.
     *
     * @param startDate The beginning of the date range (inclusive)
     * @param endDate The end of the date range (inclusive)
     * @return Flow of workouts with their sets within the specified range
     */
    fun getWorkoutsInRange(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<WorkoutWithSets>> =
        workoutDao.getWorkoutsInRange(startDate, endDate)

    /**
     * Saves a workout and its associated sets to the database.
     * Handles the transaction to ensure data consistency.
     *
     * @param workout The workout entity to save
     * @param sets List of sets associated with the workout
     */
    suspend fun saveWorkout(workout: WorkoutEntity, sets: List<SetEntity>) {
        workoutDao.insertWorkoutWithSets(workout, sets)
    }

    /**
     * Deletes a workout and its associated sets from the database.
     * The deletion of associated sets is handled by Room's cascade delete.
     *
     * @param workout The workout entity to delete
     */
    suspend fun deleteWorkout(workout: WorkoutEntity) {
        workoutDao.deleteWorkout(workout)
    }
}
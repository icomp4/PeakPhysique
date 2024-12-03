/**
 * ViewModel responsible for managing workout tracking functionality.
 * Handles the state management for active workout sets and provides access to workout history.
 */
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peakphysique.app.database.AppDatabase
import com.peakphysique.app.model.SetEntity
import com.peakphysique.app.model.WorkoutEntity
import com.peakphysique.app.model.WorkoutSet
import com.peakphysique.app.model.WorkoutWithSets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

class TrackingViewModel(application: Application) : AndroidViewModel(application) {
    // Repository instance for handling data operations
    private val repository: WorkoutRepository

    // StateFlow containing all workouts with their associated sets
    val allWorkouts: StateFlow<List<WorkoutWithSets>>

    // StateFlow containing workouts from the last 30 days
    val recentWorkouts: StateFlow<List<WorkoutWithSets>>

    // Mutable state for tracking sets in the current workout session
    private val _workoutSets = MutableStateFlow<List<WorkoutSet>>(emptyList())
    // Public immutable state for observing current workout sets
    val workoutSets: StateFlow<List<WorkoutSet>> = _workoutSets.asStateFlow()

    init {
        // Initialize database and repository
        val database = AppDatabase.getDatabase(application)
        repository = WorkoutRepository(database.workoutDao())

        // Configure flow for all workouts with lazy initialization
        allWorkouts = repository.allWorkouts.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

        // Configure flow for recent workouts (last 30 days) with lazy initialization
        recentWorkouts = repository.getWorkoutsInRange(
            startDate = LocalDateTime.now().minusDays(30),
            endDate = LocalDateTime.now()
        ).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )
    }

    /**
     * Adds a new set to the current workout session.
     * @param set The WorkoutSet to be added to the current session
     */
    fun addSet(set: WorkoutSet) {
        _workoutSets.update { currentSets ->
            currentSets + set
        }
    }

    /**
     * Removes a set from the current workout session by its ID.
     * @param setId The unique identifier of the set to remove
     */
    fun removeSet(setId: String) {
        _workoutSets.update { currentSets ->
            currentSets.filterNot { it.id == setId }
        }
    }

    /**
     * Saves the current workout session to the database.
     * Creates a new workout with the current timestamp and associates all tracked sets with it.
     * Clears the current session after saving.
     */
    fun saveWorkout() {
        viewModelScope.launch {
            val workoutId = UUID.randomUUID().toString()
            val workout = WorkoutEntity(
                id = workoutId,
                date = LocalDateTime.now()
            )

            // Convert WorkoutSet objects to SetEntity objects for database storage
            val sets = _workoutSets.value.map { set ->
                SetEntity(
                    id = set.id,
                    workoutId = workoutId,
                    name = set.name,
                    reps = set.reps,
                    weight = set.weight,
                    notes = set.notes
                )
            }

            repository.saveWorkout(workout, sets)
            _workoutSets.value = emptyList()
        }
    }

    /**
     * Deletes a workout and its associated sets from the database.
     * @param workout The WorkoutEntity to be deleted
     */
    fun deleteWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
        }
    }

    /**
     * Clears all sets from the current workout session without saving.
     */
    fun clearWorkout() {
        _workoutSets.value = emptyList()
    }
}
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
    private val repository: WorkoutRepository

    val allWorkouts: StateFlow<List<WorkoutWithSets>>

    val recentWorkouts: StateFlow<List<WorkoutWithSets>>

    private val _workoutSets = MutableStateFlow<List<WorkoutSet>>(emptyList())
    val workoutSets: StateFlow<List<WorkoutSet>> = _workoutSets.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = WorkoutRepository(database.workoutDao())

        allWorkouts = repository.allWorkouts.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

        recentWorkouts = repository.getWorkoutsInRange(
            startDate = LocalDateTime.now().minusDays(30),
            endDate = LocalDateTime.now()
        ).stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )
    }

    fun addSet(set: WorkoutSet) {
        _workoutSets.update { currentSets ->
            currentSets + set
        }
    }

    fun removeSet(setId: String) {
        _workoutSets.update { currentSets ->
            currentSets.filterNot { it.id == setId }
        }
    }

    fun saveWorkout() {
        viewModelScope.launch {
            val workoutId = UUID.randomUUID().toString()
            val workout = WorkoutEntity(
                id = workoutId,
                date = LocalDateTime.now()
            )

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

    fun deleteWorkout(workout: WorkoutEntity) {
        viewModelScope.launch {
            repository.deleteWorkout(workout)
        }
    }

    fun clearWorkout() {
        _workoutSets.value = emptyList()
    }
}
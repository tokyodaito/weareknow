import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SimpleScheduler(
    private val mainContext: CoroutineContext = Dispatchers.Main,
    private val ioContext: CoroutineContext = Dispatchers.IO
) {

    private val jobs = mutableMapOf<String, Job>()

    fun schedule(
        id: String,
        delay: Long = 0,
        repeatInterval: Long = 0,
        action: suspend () -> Unit
    ) {
        try {
            cancel(id)

            val job = CoroutineScope(mainContext).launch {
                if (delay > 0) {
                    delay(delay)
                }

                if (repeatInterval > 0) {
                    while (isActive) {
                        withContext(ioContext) { action() }
                        delay(repeatInterval)
                    }
                } else {
                    withContext(ioContext) { action() }
                }
            }

            jobs[id] = job
        } catch (ex: Exception) {
            Log.e("Scheduler", ex.message, ex)
            cancel(id)
        }
    }

    fun cancel(id: String) {
        jobs[id]?.cancel()
        jobs.remove(id)
    }

    fun cancelAll() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
    }
}
package com.bogsnebes.weareknow.accessibility.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SimpleWorker {
    suspend fun <T> executeInBackground(block: () -> T): T = withContext(Dispatchers.IO) {
        suspendCoroutine { cont ->
            try {
                val result = block()
                cont.resume(result)
            } catch (e: Exception) {
                cont.resumeWithException(e)
            }
        }
    }
}
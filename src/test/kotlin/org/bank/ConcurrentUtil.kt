package org.bank

import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

suspend fun executeConcurrentlyAndWaitForCompletion(times: Int, block: () -> Unit) {
    val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()).asCoroutineDispatcher()

    val jobs = ArrayList<Job>()
    coroutineScope {
        repeat(times) {
            jobs.add(
                launch(executor) {
                    try {
                        block.invoke()
                    } catch (e: Exception) {
                        // skip object optimistic locking exceptions
                    }
                }
            )
        }
    }
    jobs.forEach { it.join() }
}

suspend fun executeInParallelAndWaitForCompletion(blocks: List<() -> Unit>) {
    val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()).asCoroutineDispatcher()

    val jobs = ArrayList<Job>()
    coroutineScope {
        blocks.forEach {
            val job = launch(executor) {
                try {
                    it.invoke()
                } catch (e: Exception) {
                    // skip object optimistic locking exceptions
                }
            }
            jobs.add(job)
        }
    }
    jobs.forEach { it.join() }
}

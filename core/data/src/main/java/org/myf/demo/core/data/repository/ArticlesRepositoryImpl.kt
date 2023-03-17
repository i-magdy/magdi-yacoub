package org.myf.demo.core.data.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.myf.demo.core.common.annotation.Articles
import org.myf.demo.core.common.annotation.Dispatcher
import org.myf.demo.core.common.annotation.MyDispatchers
import org.myf.demo.core.common.model.ArticleModel
import javax.inject.Inject

class ArticlesRepositoryImpl @Inject constructor(
    @Articles private val reference: CollectionReference,
    @Dispatcher(MyDispatchers.IO) ioDispatcher: CoroutineDispatcher
    ): ArticlesRepository {
    private val coroutine: CoroutineScope = CoroutineScope(
        context = ioDispatcher + SupervisorJob()
    )
    private var fetchJob: Job? = null

    override fun getArticles(): Flow<List<ArticleModel>> =
        callbackFlow {
            fetchJob?.cancel()
            val list = ArrayList<ArticleModel>()
            val task = reference.get()
            task.addOnSuccessListener {
                for (document in it.documents){
                    val article = document.toObject(ArticleModel::class.java)
                    if (article != null){
                        list.add(article)
                    }
                }
                fetchJob = coroutine.launch {
                    trySendBlocking(list)
                        .onFailure { t ->
                            t?.printStackTrace()
                            channel.close()
                        }

                    channel.close()
                }
            }.addOnFailureListener {
                fetchJob = coroutine.launch {
                    trySendBlocking(emptyList())
                        .onFailure { t->
                            t?.printStackTrace()
                            channel.close()
                        }
                    channel.close()
                }
            }

            awaitClose { Log.d("articles","channel closed") }
        }

    override fun cancelJob() {
        fetchJob?.cancel()
        coroutine.coroutineContext.cancelChildren()
    }
}
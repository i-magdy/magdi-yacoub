package org.myf.demo.core.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Source
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import org.myf.demo.core.common.annotation.Dispatcher
import org.myf.demo.core.common.annotation.MyDispatchers.IO
import org.myf.demo.core.common.annotation.PatientDatabaseRef
import org.myf.demo.core.common.model.CurrentPatientModel
import javax.inject.Inject

class CurrentPatientRepositoryImpl @Inject constructor(
    @PatientDatabaseRef val db: CollectionReference,
    @Dispatcher(IO) val ioDispatcher: CoroutineDispatcher
): CurrentPatientRepository {

    override val patient: MutableStateFlow<CurrentPatientModel> = MutableStateFlow(
        CurrentPatientModel()
    )

    private val coroutine = CoroutineScope(ioDispatcher + SupervisorJob())
    private var job: Job? = null

    override suspend fun getPatientAsync(): Deferred<Boolean> {
        val deferred = CompletableDeferred<Boolean>()
        val query = db.whereEqualTo("uid",Firebase.auth.uid)
        query.get(Source.SERVER)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    deferred.complete(!it.result.isEmpty)
                    if (!it.result.isEmpty){
                        for (snapshot in it.result.documents){
                            val model = snapshot.toObject(CurrentPatientModel::class.java)
                            if (model != null){
                                job?.cancel()
                                job = coroutine.launch {
                                    patient.emit(model)
                                }
                            }
                        }
                    }
                }else{
                    deferred.cancel()
                }
            }
        return object : Deferred<Boolean> by deferred{}
    }

    override suspend fun getPatientAsync(id: String): Deferred<Boolean> {
        val deferred = CompletableDeferred<Boolean>()
        val query = db.whereEqualTo("id",id)
        query.get(Source.SERVER)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    deferred.complete(!it.result.isEmpty)
                    if (!it.result.isEmpty){
                        for (snapshot in it.result.documents){
                            val model = snapshot.toObject(CurrentPatientModel::class.java)
                            if (model != null){
                                job?.cancel()
                                job = coroutine.launch {
                                    patient.getAndUpdate { model }
                                }
                            }
                        }
                    }
                }else{
                    deferred.cancel()
                }
            }
        return object : Deferred<Boolean> by deferred{}
    }

    override fun cancelJob() {
        coroutine.coroutineContext.cancelChildren()
        job?.cancel()
    }
}
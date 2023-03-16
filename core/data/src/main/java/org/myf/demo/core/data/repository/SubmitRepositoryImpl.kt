package org.myf.demo.core.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.myf.demo.core.common.annotation.Dispatcher
import org.myf.demo.core.common.annotation.MyDispatchers.IO
import org.myf.demo.core.common.annotation.PatientDatabaseRef
import org.myf.demo.core.datastore.PatientDataRepo
import org.myf.demo.core.datastore.PatientModel
import org.myf.demo.core.common.model.CurrentPatientModel
import javax.inject.Inject

class SubmitRepositoryImpl @Inject constructor(
    private val patientRepo: PatientDataRepo,
    @Dispatcher(IO) val ioDispatcher: CoroutineDispatcher,
    @PatientDatabaseRef val db: CollectionReference
): SubmitRepository {

    override val patient: MutableStateFlow<PatientModel> = MutableStateFlow(
        PatientModel()
    )
    private val coroutine = CoroutineScope(ioDispatcher + SupervisorJob())

    init {
        coroutine.launch {
            patientRepo.getPatientMessage().collect{ patientModel ->
                patient.emit(patientModel)
            }
        }
    }


    override suspend fun createPatientAsync(): Deferred<Boolean> = coroutineScope {
        val deferred = CompletableDeferred<Boolean>(this.coroutineContext.job)
        val p = patient.value
        db.add(CurrentPatientModel(
            id = p.id,
            uid = Firebase.auth.uid ?: "",
            email = p.email,
            name = p.name,
            primaryPhone = p.primaryPhone,
            secondaryPhone = p.secondaryPhone
        )).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                deferred.complete(task.isSuccessful)
            }else{
                deferred.complete(false)
            }
        }.addOnFailureListener { deferred.cancel() }
        return@coroutineScope object : Deferred<Boolean> by deferred{}
    }

    override fun cancelJob() {
        coroutine.coroutineContext.cancelChildren()
    }
}
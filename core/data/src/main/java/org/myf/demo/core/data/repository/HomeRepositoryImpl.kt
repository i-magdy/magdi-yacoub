package org.myf.demo.core.data.repository

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import org.myf.demo.core.common.annotation.Dispatcher
import org.myf.demo.core.common.annotation.Home
import org.myf.demo.core.common.annotation.MyDispatchers.IO
import org.myf.demo.core.common.model.HomeModel
import org.myf.demo.core.common.model.StoryModel
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    @Home private val reference: CollectionReference,
    @Dispatcher(IO) ioDispatcher: CoroutineDispatcher
): HomeRepository {

    private val coroutine: CoroutineScope = CoroutineScope(
        context = ioDispatcher + SupervisorJob()
    )
    private var fetchJob: Job? = null


    override fun getData(): Flow<HomeModel> =   callbackFlow {
        fetchJob?.cancel()
        val task = reference.document("data").get()
        task.addOnSuccessListener {
            val home = it.toObject(HomeModel::class.java)!!
            fetchJob = coroutine.launch {
                trySendBlocking(home)
                    .onFailure { t ->
                        t?.printStackTrace()
                        channel.close()
                    }

                channel.close()
            }
        }.addOnFailureListener {
            fetchJob = coroutine.launch {
                trySendBlocking(HomeModel())
                    .onFailure { t->
                        t?.printStackTrace()
                        channel.close()
                    }
                channel.close()
            }
        }

        awaitClose { Log.d("articles","channel closed") }
    }

    override fun push() {
       /* val list: ArrayList<StoryModel> = arrayListOf()
        list.add(
            StoryModel(
            title = "كارما هاشم",
            supportText = "زي ما ربنا خلاك سبب في شفاء قلوب كتير، لسة في آلاف الحالات اللي محتاجة حبك بجد \n" +
                    "اتبرع وساعدنا نداوي قلوب أكتر.",
            url = "https://www.youtube.com/watch?v=hJ3Yu7OuII4",
            img = "https://firebasestorage.googleapis.com/v0/b/magdi-yacoup.appspot.com/o/covers%2Fkarma-story%20%5Bhome%5D.jpg?alt=media&token=b5e048b0-8c04-4262-b570-531be554f6b0"
            )
        )
        list.add(StoryModel(
            title = "علياء علي",
            supportText = "الأستاذ على عبد الرحمن والد الطفلة علياء التي تم بحمد الله علاجها في المؤسسة",
            url = "https://www.youtube.com/watch?v=pXoXuNJUt0Q",
            img = "https://firebasestorage.googleapis.com/v0/b/magdi-yacoup.appspot.com/o/covers%2Faliaa-story%20%5Bhome%5D.jpg?alt=media&token=797daf66-5049-47e7-bb05-dfc3b92af152"
        ))
        list.add(StoryModel(
            title = "يوسف",
            supportText = "في مؤسسة مجدى يعقوب للقلب عملنا عملية تغيير صمام وعملية ارتجاع مريق للطفل يوسف. ودلوقتي يوسف قادر يمارس حياته زي كل الأطفال اللي في سنه ورجعله الأمل في الحياة من تاني. مساهمتك لمؤسسة مجدى يعقوب للقلب هتغير حياة ناس كتير غير يوسف",
            url = "https://www.youtube.com/watch?v=uyp54hRBhVI",
            img = "https://firebasestorage.googleapis.com/v0/b/magdi-yacoup.appspot.com/o/covers%2Fyousef-story%20%5Bhome%5D.jpg?alt=media&token=8be5ae1e-3443-4f93-8bff-6983a5480db1"
        ))
        list.add(StoryModel(
            title = "مليكة محمد",
            supportText = "زي ما ربنا خلاك سبب في شفاء قلوب كتير، لسة في آلاف الحالات اللي محتاجة حبك بجد.  اتبرع وساعدنا نداوي قلوب أكتر.",
            url = "https://www.youtube.com/watch?v=7oWJZsEvzUY",
            img = "https://firebasestorage.googleapis.com/v0/b/magdi-yacoup.appspot.com/o/covers%2Fmalika-story%20%5Bhome%5D.jpg?alt=media&token=295b9e54-624d-42ec-9f01-9a5f947b5202"
        ))
        list.add(StoryModel(
            title = "عيد الأم",
            supportText = "قلب الأم هو الأقوى، لكن عند صحة ولادها بيكون الأضعف.. اتبرع لمؤسسة مجدى يعقوب للقلب وخليك السبب في إنقاذ قلب طفل وراحة بال أسر كتير.",
            url = "https://www.youtube.com/watch?v=2y0-oVSGjLI",
            img = "https://firebasestorage.googleapis.com/v0/b/magdi-yacoup.appspot.com/o/covers%2Fmother-anniversary%20%5Bhome%5D.jpg?alt=media&token=7c0a4798-c32f-4599-849f-671950de1c05"
        ))
        Log.e("list",list.size.toString())
        reference.document("data").update("stories",list).addOnCompleteListener { }.addOnFailureListener { Log.e("push",it.message.toString()) }*/
    }

    override fun cancelJob() {
        fetchJob?.cancel()
        coroutine.coroutineContext.cancelChildren()
    }
}
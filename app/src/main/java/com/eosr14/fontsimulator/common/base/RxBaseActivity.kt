package com.eosr14.fontsimulator.common.base

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.trello.rxlifecycle3.LifecycleProvider
import com.trello.rxlifecycle3.LifecycleTransformer
import com.trello.rxlifecycle3.RxLifecycle
import com.trello.rxlifecycle3.android.ActivityEvent
import com.trello.rxlifecycle3.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

@SuppressLint("Registered")
abstract class RxBaseActivity : AppCompatActivity(), LifecycleProvider<ActivityEvent> {

    private val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()

    override fun lifecycle(): Observable<ActivityEvent> = lifecycleSubject.hide()

    override fun <T : Any?> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> =
        RxLifecycle.bindUntilEvent(lifecycleSubject, event);


    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> =
        RxLifecycleAndroid.bindActivity(lifecycleSubject);

    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(ActivityEvent.START)
    }

    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(ActivityEvent.RESUME)
    }

    override fun onPause() {
        super.onPause()
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
    }

    override fun onStop() {
        super.onStop()
        lifecycleSubject.onNext(ActivityEvent.STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleSubject.onNext(ActivityEvent.DESTROY)
    }

}
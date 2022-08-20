package ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.mainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.MainRepository
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.local.student.Student
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val mainRepository: MainRepository
) :ViewModel() {

    private lateinit var netDisposable :Disposable
    private val errorData = MutableLiveData<String>()

    init {

        mainRepository
            .refreshData()
            .subscribeOn(Schedulers.io())
            .subscribe( object :CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    netDisposable = d
                }

                override fun onComplete() {}

                override fun onError(e: Throwable) {
                    errorData.postValue( e.message ?: "unknown error!" )
                }

            } )

    }

    fun getAllData() :LiveData<List<Student>> {
        return mainRepository.getAllStudent()
    }
    fun getErrorData() :LiveData<String> {
        return errorData
    }
    fun removeStudent(student : Student) :Completable {
        return mainRepository.removeStudent(student)
    }

    override fun onCleared() {
        netDisposable.dispose()
        super.onCleared()
    }

}
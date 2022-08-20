package ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model

import androidx.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.Single
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.api.ApiService
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.local.student.Student
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.local.student.StudentDao
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.util.BASE_URL
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.util.studentToJsonObject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository(
    private val apiService: ApiService,
    private val studentDao: StudentDao
) {

    fun getAllStudent(): LiveData<List<Student>> {
        return studentDao.getAllData()
    }

    // caching
    fun refreshData(): Completable {
        return apiService
            .getAllStudents()
            .doOnSuccess {
                studentDao.insertAll(it)
            }
            .ignoreElement()
    }

    fun insertStudent(student: Student): Completable {
        return apiService
            .insertStudent( studentToJsonObject(student) )
            .doOnComplete {
                studentDao.insertOrUpdate(student)
            }
    }

    fun updateStudent(student: Student): Completable {
        return apiService
            .updateStudent(student.id, studentToJsonObject(student))
            .doOnComplete {
                studentDao.insertOrUpdate(student)
            }
    }

    fun removeStudent(student : Student): Completable {
        return apiService
            .deleteStudent(student.id)
            .doOnComplete {
                studentDao.remove(student.id)
            }
    }

}
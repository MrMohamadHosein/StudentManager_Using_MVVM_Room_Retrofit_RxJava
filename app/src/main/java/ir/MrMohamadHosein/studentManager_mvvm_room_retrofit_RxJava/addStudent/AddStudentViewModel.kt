package ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.addStudent

import androidx.lifecycle.ViewModel
import io.reactivex.Completable
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.MainRepository
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.local.student.Student

class AddStudentViewModel (
    private val mainRepository: MainRepository) :ViewModel() {

    fun insertNewStudent(student: Student): Completable {
        return mainRepository.insertStudent(student)
    }

    fun updateStudent(student: Student): Completable {
        return mainRepository.updateStudent(student)
    }

}
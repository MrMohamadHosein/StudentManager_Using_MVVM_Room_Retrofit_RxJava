package ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.addStudent.AddStudentViewModel
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.mainScreen.MainViewModel
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.MainRepository

class MainViewModelFactory(private val mainRepository: MainRepository) :ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(mainRepository) as T
    }

}

class AddStudentViewModelFactory(private val mainRepository: MainRepository) :ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddStudentViewModel(mainRepository) as T
    }

}
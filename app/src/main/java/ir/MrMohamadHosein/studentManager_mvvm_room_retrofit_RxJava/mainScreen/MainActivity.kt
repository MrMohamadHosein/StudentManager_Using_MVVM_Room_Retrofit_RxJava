package ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.mainScreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.addStudent.AddStudentActivity
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.databinding.ActivityMainBinding
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.MainRepository
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.local.MyDatabase
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.local.student.Student
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.util.ApiServiceSingleton
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.util.MainViewModelFactory
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.util.asyncRequest
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.util.showToast

class MainActivity : AppCompatActivity(), StudentAdapter.StudentEvent {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: StudentAdapter
    private val compositeDisposable = CompositeDisposable()
    private lateinit var mainViewModel: MainViewModel

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain)
        initRecycler()

        mainViewModel = ViewModelProvider(
            this,
            MainViewModelFactory(
                MainRepository(
                    ApiServiceSingleton.apiService!!,
                    MyDatabase.getDatabase(applicationContext).studentDao
                )
            )
        ).get(MainViewModel::class.java)

        binding.btnAddStudent.setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }

        mainViewModel.getAllData().observe(this) {
            refreshRecyclerData(it)
        }

        mainViewModel.getErrorData().observe(this) {
            Log.e("testLog", it)
        }

    }
    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onItemClicked(student: Student, position: Int) {
        val intent = Intent(this, AddStudentActivity::class.java)
        intent.putExtra("student", student)
        startActivity(intent)
    }
    override fun onItemLongClicked(student: Student, position: Int) {
        val dialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
        dialog.contentText = "Delete this Item?"
        dialog.cancelText = "cancel"
        dialog.confirmText = "confirm"

        dialog.setOnCancelListener {
            dialog.dismiss()
        }

        dialog.setConfirmClickListener {
            deleteDataFromServer(student, position)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun refreshRecyclerData(newData: List<Student>) {
        myAdapter.refreshData(newData)
    }
    private fun deleteDataFromServer(student: Student, position: Int) {

        mainViewModel
            .removeStudent(student)
            .asyncRequest()
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onComplete() {
                    showToast("student removed :)")
                }

                override fun onError(e: Throwable) {
                    showToast("error -> " + e.message ?: "null")
                }
            })

    }
    private fun initRecycler() {
        val myData = arrayListOf<Student>()
        myAdapter = StudentAdapter(myData, this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }
}
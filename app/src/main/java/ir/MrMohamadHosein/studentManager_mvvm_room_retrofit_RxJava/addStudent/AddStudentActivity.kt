package ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.addStudent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import io.reactivex.CompletableObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.local.student.Student
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.databinding.ActivityMain2Binding
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.mainScreen.MainViewModel
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.MainRepository
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.local.MyDatabase
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.util.*

class AddStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private lateinit var addStudentViewModel: AddStudentViewModel
    private val compositeDisposable = CompositeDisposable()
    private var isInserting = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMain2)

        addStudentViewModel = ViewModelProvider(
            this,
            AddStudentViewModelFactory(
                MainRepository(
                    ApiServiceSingleton.apiService!!,
                    MyDatabase.getDatabase(applicationContext).studentDao
                )
            )
        ).get(AddStudentViewModel::class.java)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.edtFirstName.requestFocus()

        val testMode = intent.getParcelableExtra<Student>("student")
        isInserting = (testMode == null)
        if (!isInserting) {
            logicUpdateStudent()
        }

        binding.btnDone.setOnClickListener {
            if (isInserting) {
                addNewStudent()
            } else {
                updateStudent(testMode!!)
            }
        }


    }
    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    private fun logicUpdateStudent() {
        binding.btnDone.text = "update"

        val dataFromIntent = intent.getParcelableExtra<Student>("student")!!
        binding.edtScore.setText(dataFromIntent.score.toString())
        binding.edtCourse.setText(dataFromIntent.course)

        val splitedName = dataFromIntent.name.split(" ")
        binding.edtFirstName.setText(splitedName[0])
        binding.edtLastName.setText(splitedName[(splitedName.size - 1)])
    }

    private fun updateStudent(student : Student) {

        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val score = binding.edtScore.text.toString()
        val course = binding.edtCourse.text.toString()

        if (
            firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            course.isNotEmpty() &&
            score.isNotEmpty()
        ) {



            addStudentViewModel
                .updateStudent(
                    Student(student.id ,firstName + " " + lastName, course, score.toInt())
                )
                .asyncRequest()
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        showToast("student updated :)")
                        onBackPressed()
                    }

                    override fun onError(e: Throwable) {
                        showToast("error -> " + e.message ?: "null")
                    }
                })

        } else {
            showToast("لطفا اطلاعات را کامل وارد کنید")
        }
    }
    private fun addNewStudent() {

        val firstName = binding.edtFirstName.text.toString()
        val lastName = binding.edtLastName.text.toString()
        val score = binding.edtScore.text.toString()
        val course = binding.edtCourse.text.toString()

        if (
            firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            course.isNotEmpty() &&
            score.isNotEmpty()
        ) {




            addStudentViewModel
                .insertNewStudent(
                    Student(0,firstName + " " + lastName, course, score.toInt())
                )
                .asyncRequest()
                .subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onComplete() {
                        showToast("student inserted :)")
                        onBackPressed()
                    }

                    override fun onError(e: Throwable) {
                        showToast("error -> " + e.message ?: "null")
                    }

                })


        } else {
            showToast("لطفا اطلاعات را کامل وارد کنید")
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return true
    }

}
package ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.local.student

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "student")
data class Student(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val name: String,

    val course: String,

    val score: Int

) : Parcelable
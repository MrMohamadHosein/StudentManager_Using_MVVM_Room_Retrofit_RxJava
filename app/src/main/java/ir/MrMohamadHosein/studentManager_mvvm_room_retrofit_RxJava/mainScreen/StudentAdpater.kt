package ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.mainScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.databinding.ItemMainBinding
import ir.MrMohamadHosein.studentManager_mvvm_room_retrofit_RxJava.model.local.student.Student

class StudentAdapter(val data: ArrayList<Student>, val studentEvent: StudentEvent) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    lateinit var binding: ItemMainBinding

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(student: Student) {

            binding.txtName.text = student.name
            binding.txtCourse.text = student.course
            binding.txtScore.text = student.score.toString()
            binding.txtHarfAval.text = student.name[0].uppercaseChar().toString()

            itemView.setOnClickListener {
                studentEvent.onItemClicked(student, adapterPosition)
            }

            itemView.setOnLongClickListener {
                studentEvent.onItemLongClicked(student, adapterPosition)
                true
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StudentViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bindViews(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newData: List<Student>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    interface StudentEvent {
        fun onItemClicked(student: Student, position: Int)
        fun onItemLongClicked(student: Student, position: Int)
    }


}
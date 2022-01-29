package leylashakirzyanova.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val editTask = findViewById<EditText>(R.id.editTaskView)
        val taskPosition = intent.getIntExtra("position", 0)
        val taskToEdit = intent.getStringExtra("task")
        editTask.setText(taskToEdit)
    }
}
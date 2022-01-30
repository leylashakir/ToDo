package leylashakirzyanova.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val editTaskView = findViewById<EditText>(R.id.editTaskView)
        val taskToEdit = intent.getStringExtra("task")
        val position = intent.getIntExtra("position", 0)
        editTaskView.setText(taskToEdit)

        findViewById<Button>(R.id.editTaskButton).setOnClickListener{
            val editedTask = editTaskView.text.toString()
            val data = Intent()
            data.putExtra("edited task", editedTask)
            data.putExtra("position", position)
            setResult(RESULT_OK, data)
            finish()
        }

    }

}
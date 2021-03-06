package leylashakirzyanova.todo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    val startForResult = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bundle = result.data
            val name = bundle?.getStringExtra("edited task").toString()
            val position = bundle?.getIntExtra("position", 0)
            if (position != null && name != null) {
                listOfTasks[position] = name
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                //1. Remove the item from the list
                listOfTasks.removeAt(position)
                //2. Notify the adapter that data changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        val onClickListener = object : TaskItemAdapter.OnClickListener {
            override fun onItemClicked(position: Int) {
                val i = Intent(this@MainActivity, EditActivity::class.java)
                val taskToEdit = listOfTasks[position]
                i.putExtra("task", taskToEdit)
                i.putExtra("position", position)
                startForResult.launch(i)
            }
        }

        loadItems()

        // Look up the RecyclerView in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener, onClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //Set up the button and input field so that the user can enter a task and add it to the list
        findViewById<Button>(R.id.button).setOnClickListener {
            //Grab the data the user enters into the addTaskField textview
            val userInput = inputTextField.text.toString()

            //Add the new task to the list of tasks
            listOfTasks.add(userInput)

            //Notify the adapter that data has been updated and add it to the end of the list
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //Reset the addTaskField
            inputTextField.setText("")

            saveItems()
        }
    }

    //Save the data that the user input by writing and reading from a file

    //Get the needed file
    fun getDataFile() : File {
        return File(filesDir, "data.txt")
    }

    //Load items by reading every line in the file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    //Save items by writing into the file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

    }

}
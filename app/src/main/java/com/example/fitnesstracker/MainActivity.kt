package com.example.fitnesstracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var trainingAdapter: TrainingAdapter
    private var trainingList: MutableList<Training> = mutableListOf()

    private lateinit var editTextDistance: EditText
    private lateinit var editTextTime: EditText
    private lateinit var editTextCalories: EditText
    private lateinit var seekBarIntensity: SeekBar
    private lateinit var radioGroupActivityType: RadioGroup
    private lateinit var buttonAddTraining: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewTrainings)
        recyclerView.layoutManager = LinearLayoutManager(this)

        editTextDistance = findViewById(R.id.editTextDistance)
        editTextTime = findViewById(R.id.editTextTime)
        editTextCalories = findViewById(R.id.editTextCalories)
        seekBarIntensity = findViewById(R.id.seekBarIntensity)
        radioGroupActivityType = findViewById(R.id.radioGroupActivityType)
        buttonAddTraining = findViewById(R.id.buttonAddTraining)

        trainingList = loadTrainings()

        trainingAdapter = TrainingAdapter(trainingList) { training ->
            showTrainingDetails(training)
        }
        recyclerView.adapter = trainingAdapter

        buttonAddTraining.setOnClickListener {
            val distance = editTextDistance.text.toString().toDoubleOrNull() ?: 0.0
            val time = editTextTime.text.toString().toIntOrNull() ?: 0
            val calories = editTextCalories.text.toString().toIntOrNull() ?: 0
            val intensity = seekBarIntensity.progress
            val activityType = when (radioGroupActivityType.checkedRadioButtonId) {
                R.id.radioButtonWalk -> "Spacer"
                R.id.radioButtonRun -> "Bieg"
                else -> "Trening siłowy"
            }

            val newTraining = Training(distance, time, calories, intensity, activityType)
            trainingList.add(newTraining)
            trainingAdapter.notifyItemInserted(trainingList.size - 1)
            saveTrainings(trainingList)
        }
    }

    private fun showTrainingDetails(training: Training) {
        val message = """
            Dystans: ${training.distance} km
            Czas: ${training.time} min
            Kalorie: ${training.calories}
            Intensywność: ${getIntensityString(training.intensity)}
        """.trimIndent()

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun getIntensityString(intensity: Int): String {
        return when (intensity) {
            in 0..3 -> "Niska"
            in 4..7 -> "Średnia"
            else -> "Wysoka"
        }
    }

    private fun saveTrainings(trainings: List<Training>) {
        val gson = Gson()
        val json = gson.toJson(trainings)
        openFileOutput("trainings.json", MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
    }

    private fun loadTrainings(): MutableList<Training> {
        return try {
            val json = openFileInput("trainings.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val type = object : TypeToken<List<Training>>() {}.type
            gson.fromJson(json, type)
        } catch (e: FileNotFoundException) {
            mutableListOf()
        }
    }
}

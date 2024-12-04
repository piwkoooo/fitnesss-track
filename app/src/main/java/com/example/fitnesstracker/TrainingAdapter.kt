package com.example.fitnesstracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrainingAdapter(
    private val trainingList: List<Training>,
    private val onClick: (Training) -> Unit
) : RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_training, parent, false)
        return TrainingViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val training = trainingList[position]
        holder.bind(training)
    }

    override fun getItemCount(): Int = trainingList.size

    inner class TrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val distanceTextView: TextView = itemView.findViewById(R.id.textDistance)
        private val timeTextView: TextView = itemView.findViewById(R.id.textTime)
        private val caloriesTextView: TextView = itemView.findViewById(R.id.textCalories)
        private val intensityTextView: TextView = itemView.findViewById(R.id.textIntensity)

        fun bind(training: Training) {
            distanceTextView.text = "Dystans: ${training.distance} km"
            timeTextView.text = "Czas: ${training.time} min"
            caloriesTextView.text = "Kalorie: ${training.calories}"
            intensityTextView.text = "Intensywność: ${getIntensityString(training.intensity)}"

            itemView.setOnClickListener { onClick(training) }
        }

        private fun getIntensityString(intensity: Int): String {
            return when (intensity) {
                in 0..3 -> "Niska"
                in 4..7 -> "Średnia"
                else -> "Wysoka"
            }
        }
    }
}

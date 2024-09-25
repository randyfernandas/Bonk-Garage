package com.example.bonkgarage.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bonkgarage.CarsDetailsActivity
import com.example.bonkgarage.R
import com.google.firebase.firestore.FirebaseFirestore

class CarsListAdapter(private val listener: (HashMap<String, Any>) -> Unit) :
    RecyclerView.Adapter<CarsListAdapter.Holder>() {

    private val cars = mutableListOf<HashMap<String, Any>>()

    // Get car data from Firestore on creation using initializer block
    init {
        val db = FirebaseFirestore.getInstance()
        val carCollection = db.collection("car_models")
        carCollection.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val carData = document.data!!  // Access document data directly
                    cars.add(carData as HashMap<String, Any>)
                    notifyItemInserted(cars.size - 1) // Notify adapter of new item
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: $exception")
            }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgCar: ImageView = itemView.findViewById(R.id.imgCars)
        val txtCarName: TextView = itemView.findViewById(R.id.txtCarName)

        fun bind(carData: HashMap<String, Any>) {
            val name = carData["name"] as String

            txtCarName.text = name

            val context = itemView.context
            var imageResource = R.drawable.car_black  // Default image

            when {
                name.lowercase().contains("datsun") -> {
                    imageResource = R.drawable.car_yellow
                }
                name.lowercase().contains("nismo") -> {
                    imageResource = R.drawable.car_orange
                }
                name.lowercase().contains("supra") -> {
                    imageResource = R.drawable.car_white
                }
                name.lowercase().contains("silvia") -> {
                    imageResource = R.drawable.car_purple
                }
                name.lowercase().contains("nsx") -> {
                    imageResource = R.drawable.car_red
                }
                name.lowercase().contains("Skyline") -> {
                    imageResource = R.drawable.car_black
                }
                name.lowercase().contains("stagea") -> {
                    imageResource = R.drawable.car_silver
                }
            }

            Glide.with(context)
                .load(imageResource)
                .into(imgCar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_cars_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return cars.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val carData = cars[position]
        holder.bind(carData)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CarsDetailsActivity::class.java)
            intent.putExtra("carData", carData) // Pass car data to the activity
            holder.itemView.context.startActivity(intent)
        }
    }
}

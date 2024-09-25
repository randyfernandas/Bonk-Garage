package com.example.bonkgarage.BestSeller

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide 
import com.example.bonkgarage.MenuActivity
import com.example.bonkgarage.Model.Transaction
import com.example.bonkgarage.Model.Transactions
import com.example.bonkgarage.R
import com.example.bonkgarage.quantity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BestSellerActivity : AppCompatActivity() {

    private lateinit var bestSellerImage: ImageView
    private lateinit var bestSellerName: TextView
    private lateinit var bestSellerBrand: TextView
    private lateinit var bestSellerPrice: TextView
    private lateinit var bestSellerDescription: TextView
    private lateinit var backButton : ImageButton
    private lateinit var etBuyCar: EditText
    private lateinit var removebtn: ImageButton
    private lateinit var addbtn: ImageButton
    private lateinit var buybtn : Button
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    var quantity = 1
    val TRANSACTION_ADDED = "transaction_added"
    var imageResource = R.drawable.car_yellow

    var uid =" "
    var email =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_best_seller)


        bestSellerImage = findViewById(R.id.imgBestSellerCar)
        bestSellerName = findViewById(R.id.txtBestSellerName)
        bestSellerBrand = findViewById(R.id.txtBestSellerBrand)
        bestSellerPrice = findViewById(R.id.txtBestSellerPrice)
        bestSellerDescription = findViewById(R.id.txtBestSellerDescription)
        backButton = findViewById(R.id.back_button)
        etBuyCar = findViewById(R.id.etBuyCar)
        removebtn = findViewById(R.id.removebtn)
        buybtn = findViewById(R.id.buybtn)
        addbtn = findViewById(R.id.addbtn)
        etBuyCar.setText(quantity.toString())

        // remove button click
        removebtn.setOnClickListener {
            if(quantity == 1)
            {
                Toast.makeText(this,"Doll amount cannot be zero!",Toast.LENGTH_SHORT).show()
            }
            if (quantity > 1) {
                quantity--
                etBuyCar.setText(quantity.toString())
            }
        }

        // add button click
        addbtn.setOnClickListener {
            quantity++
            etBuyCar.setText(quantity.toString())
        }




        // Fetch bestseller car data from Firestore
        fetchBestSellerData()


    }

    private fun fetchBestSellerData() {
        val db = FirebaseFirestore.getInstance()
        val carCollection = db.collection("car_models")

        carCollection
            .whereEqualTo("Tag", "bestseller")  // Filter by "bestseller" tag
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    
                    return@addOnSuccessListener
                }

                val carData = documents.firstOrNull()?.data ?: return@addOnSuccessListener

                // Load car image (replace with your local car_yellow image resource)
                Glide.with(this).load(R.drawable.car_yellow).into(bestSellerImage)



                val intent = intent
                auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser
                if(currentUser != null)
                { uid = currentUser.uid
                    email = currentUser.email.toString()
                }

                // Set car details from Firestore data
                val carName = carData["name"] as String
                val carBrand = carData["brand"] as String
                val carPrice = carData["price"] as Double
                val carDesc = carData["description"] as String

                bestSellerName.text = carName
                bestSellerBrand.text = carBrand
                bestSellerPrice.text = String.format("%.2f", carPrice)
                bestSellerDescription.text = carDesc
            }
            .addOnFailureListener { exception ->
               
                exception.printStackTrace()
            }

        buybtn.setOnClickListener {
            quantity = etBuyCar.text.toString().toInt()// Get buy quantity

           
            if (quantity < 0) {
                Toast.makeText(this, "Invalid quantity. Please enter a positive value.",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            bestSellerImage = findViewById(R.id.imgBestSellerCar)
            bestSellerName = findViewById(R.id.txtBestSellerName)
            bestSellerBrand = findViewById(R.id.txtBestSellerBrand)
            bestSellerPrice = findViewById(R.id.txtBestSellerPrice)
            bestSellerDescription = findViewById(R.id.txtBestSellerDescription)

            // Extract car details from received intent or UI elements as needed
            val carName = bestSellerName.text.toString()
            val carPrice = bestSellerPrice.text.toString().toDouble()
            quantity *= carPrice.toInt()
            val carImg = imageResource
            var documentId = ""

            val transaction = Transaction(carImg, quantity, carPrice, carName, uid, email, documentId)
            db.collection("transactions").add(transaction)
                .addOnSuccessListener { documentReference -> documentId = documentReference.id
                    transaction.documentId = documentId
                    Toast.makeText(this, "Transaction added!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    // Handle transaction addition failure
                    e.printStackTrace()
                    Toast.makeText(this, "Transaction failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            Transactions.addTransactions(carImg, quantity, carPrice, carName, uid, email,documentId)
            val intentNavigate = Intent(this, MenuActivity::class.java)
            startActivity(intentNavigate)
            finish()
        }

        backButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}

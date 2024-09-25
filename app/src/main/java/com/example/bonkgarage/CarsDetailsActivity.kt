
package com.example.bonkgarage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.bonkgarage.Model.Transaction
import com.example.bonkgarage.Model.Transactions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


var quantity = 1
val TRANSACTION_ADDED = "transaction_added"

class CarsDetailsActivity : AppCompatActivity() {

    private lateinit var carImageView: ImageView
    private lateinit var carNameTextView: TextView
    private lateinit var carBrandTextView: TextView
    private lateinit var carPriceTextView: TextView
    private lateinit var carDescTextView: TextView
    private lateinit var backButton: ImageButton
    private lateinit var etBuyCar: EditText
    private lateinit var removebtn: ImageButton
    private lateinit var addbtn: ImageButton
    private lateinit var buybtn : Button
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_details)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        carImageView = findViewById(R.id.imgCarsDetail)
        carNameTextView = findViewById(R.id.txtCarsNameField)
        carBrandTextView = findViewById(R.id.txtCarsBrandField)
        carPriceTextView = findViewById(R.id.txtCarsPriceField)
        carDescTextView = findViewById(R.id.txtCarsDescField)
        buybtn = findViewById(R.id.buybtn)
        backButton = findViewById(R.id.back_button)
        etBuyCar = findViewById(R.id.etBuyCar)
        removebtn = findViewById(R.id.removebtn)
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


        val intent = intent
        var imageResource = R.drawable.car_black
        val currentUser = auth.currentUser

        var uid =" "
        var email =""

        if(currentUser != null)
        { uid = currentUser.uid
         email = currentUser.email.toString()
        }
        if (intent.hasExtra("carData")) {
            val carData = intent.getSerializableExtra("carData") as HashMap<String, Any>
            // Extract car details from carData
            val carName = carData["name"] as String
            val carBrand = carData["brand"] as String
            val carPrice = carData["price"] as Double
            val carDesc = carData["description"] as String

            // Set car details on UI elements
            carNameTextView.text = carName
            carBrandTextView.text = carBrand
            carPriceTextView.text = String.format("%.2f", carPrice)  // Format price with 2 decimal places
            carDescTextView.text = carDesc



            when {
                carName.lowercase().contains("datsun") -> {
                    imageResource = R.drawable.car_yellow
                }
                carName.lowercase().contains("nismo") -> {
                    imageResource = R.drawable.car_orange
                }
                carName.lowercase().contains("supra") -> {
                    imageResource = R.drawable.car_white
                }
                carName.lowercase().contains("silvia") -> {
                    imageResource = R.drawable.car_purple
                }
                carName.lowercase().contains("nsx") -> {
                    imageResource = R.drawable.car_red
                }
                carName.lowercase().contains("Skyline") -> {
                    imageResource = R.drawable.car_black
                }
                carName.lowercase().contains("stagea") -> {
                    imageResource = R.drawable.car_silver
                }
            }
                Glide.with(this)
                    .load(imageResource)
                    .into(carImageView)

        } else {
            finish()
        }

        buybtn.setOnClickListener {
             quantity = etBuyCar.text.toString().toInt()// Get buy quantity

            if (quantity <= 0) {
                Toast.makeText(this, "Invalid quantity. Please enter a positive value.", LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Extract car details from received intent or UI elements as needed
            val carName = carNameTextView.text.toString()
            val carPrice = carPriceTextView.text.toString().toDouble()  
            quantity *= carPrice.toInt()
            val carImg = imageResource
            var documentId = ""

            val transaction = Transaction(carImg, quantity, carPrice, carName, uid, email, documentId)
            val docRef = db.collection("transactions").add(transaction)
            docRef.addOnSuccessListener { documentReference ->
                transaction.documentId = documentReference.id
                Toast.makeText(this, "Transaction added!", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener { e ->
                    // Handle transaction addition failure
                    e.printStackTrace()
                    Toast.makeText(this, "Transaction failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
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
            Transactions.addTransactions(carImg, quantity, carPrice, carName, uid, email,transaction.documentId)
            val intentNavigate = Intent(this, MenuActivity::class.java)
            startActivity(intentNavigate)
            finish()
        }


        backButton.setOnClickListener {
            val intent = Intent(this,MenuActivity::class.java)
            startActivity(intent)
        }


    }
}

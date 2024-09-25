package com.example.bonkgarage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bonkgarage.OTP.OTPActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class RegisterActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etUsername:EditText
    private lateinit var etPassword:EditText
    private lateinit var etEmail:EditText
    private lateinit var etPhoneNumber:EditText
    private lateinit var btnRegister:Button
    private lateinit var txtLogin:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        etUsername = findViewById(R.id.etUsername) 
        etPassword = findViewById(R.id.etPassword)
        etEmail = findViewById(R.id.etEmail)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        btnRegister = findViewById(R.id.btnRegister)
        txtLogin = findViewById((R.id.txtLogin))

        btnRegister.setOnClickListener {


            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val phoneNumber = etPhoneNumber.text.toString().trim()
            var isValid = 0;

            do {
                if (username.isEmpty()) {
                    Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (email.isEmpty()) {
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (phoneNumber.isEmpty() ) {
                    Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    isValid = 1;
                }

            } while (isValid != 1)

            // Firebase user registration
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user  // Get the newly created user
                        if (user != null) {
                            storeUserData(user.uid)  // Store user data in Firestore
                        }
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Registration failed, display a message to the user
                        Toast.makeText(
                            this,
                            "Registration failed. ${task.exception}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        txtLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun storeUserData(userId: String) {
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")


        val userData = hashMapOf(
            "email" to etEmail.text.toString().trim() ,
            "username" to etUsername.text.toString().trim(),
            "phone" to etPhoneNumber.text.toString().trim()
        )

        usersRef.document(userId).set(userData)
            .addOnSuccessListener {
                // User data successfully stored
            }
            .addOnFailureListener { exception ->
                // Handle errors during data storage
                exception.printStackTrace()
            }
    }
}

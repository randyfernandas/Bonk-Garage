package com.example.bonkgarage

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginActivity:AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var btnLogin: Button
    private lateinit var etPassword: EditText
    private lateinit var etEmail: EditText
    private lateinit var txtRegister: TextView
    private lateinit var btnGoogle: ImageView


    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail() // Optional: Request user's email
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()
        txtRegister = findViewById(R.id.txtRegister)
        btnGoogle = findViewById(R.id.btnGoogle)
        val btnLogin = findViewById<Button>(R.id.btnLogin)



        btnLogin.setOnClickListener {
            val email = findViewById<EditText>(R.id.etUsername).text.toString().trim()
            val password = findViewById<EditText>(R.id.etPassword).text.toString().trim()

            etPassword = EditText(this)
            etPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD


            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(
                            this,
                            MenuActivity::class.java
                        )
                        startActivity(intent)
                        finish()
                    } else {
                        // Login failed, display an error message to the user
                        Toast.makeText(this, "Login failed: ${task.exception}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }

        btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            // Google Sign In successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { authResult ->
                    if (authResult.isSuccessful) {
                        // Sign in success, navigate to menu screen
                        val intent = Intent(this, MenuActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Sign in failed, display a message to the user
                        Toast.makeText(this, "Sign in failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: ApiException) {
            // Handle sign in exceptions
            Toast.makeText(this, "Sign in failed.", Toast.LENGTH_SHORT).show()
        }


    }



}



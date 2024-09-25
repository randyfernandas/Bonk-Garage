package com.example.bonkgarage.OTP

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.bonkgarage.LoginActivity
import com.example.bonkgarage.MenuActivity
import com.example.bonkgarage.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


var otp = 1234
class OTPActivity : AppCompatActivity() {

    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button

    private val SEND_SMS_PERMISSION_CODE = 100
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        messageEditText = findViewById(R.id.messageEdit)
        sendButton = findViewById(R.id.sendBtn)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        var currentUserId = currentUser?.uid

        if (currentUserId == null) {
            Log.w(TAG, "No current user found!")
            return
        }


        db.collection("users").document(currentUserId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val phoneNumber = documentSnapshot.getString("phoneNumber")
                    if (phoneNumber.isNullOrEmpty()) {
                        Toast.makeText(this, "Phone number not found", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }
                    Log.d("phone number", "phone number $phoneNumber")
                    checkSendSmsPermission()
                    requestSendSmsPermission()
                    sendOTP(phoneNumber)
                } else {
                    Log.w(TAG, "User document not found for current user!")
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error getting user document:", e)
            }


        sendButton.setOnClickListener {
            val enteredOTP = messageEditText.text.toString().toInt()  // Assuming user enters OTP here

            Handler(mainLooper).postDelayed({
                if (enteredOTP == otp) {
                    Toast.makeText(this, "OTP verification successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MenuActivity::class.java))
                } else {
                    Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
                }
            }, 2000)
        }
    }

    private fun checkSendSmsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestSendSmsPermission() {
        requestPermissions(arrayOf(Manifest.permission.SEND_SMS), SEND_SMS_PERMISSION_CODE)
    }

    private fun sendOTP(phoneNumber: String) {
         otp = (Math.random() * 9000).toInt() + 1000
        val message = "Your OTP is: $otp"

        if (checkSendSmsPermission()) {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "OTP sent to your phone number", Toast.LENGTH_SHORT).show()
        }
    }

}

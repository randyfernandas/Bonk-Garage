package com.example.bonkgarage.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.bonkgarage.LoginActivity
import com.example.bonkgarage.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    lateinit var usernameTextView: TextView
    lateinit var emailTextView: TextView
    lateinit var loggedOutButton: Button

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Firestore and Authentication instances
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        usernameTextView = view.findViewById(R.id.txtUsernameField)
        emailTextView = view.findViewById(R.id.txtEmailField)
        loggedOutButton = view.findViewById(R.id.btnLogout)

        // Get the current user
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            // Fetch user data from Firestore based on UID
            db.collection("users").document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val username = documentSnapshot.getString("username")
                        val email = documentSnapshot.getString("email")
                        usernameTextView.text = username
                        emailTextView.text = email
                    } else {
                        // Handle case where user document is not found
                        usernameTextView.text = "Username not available"
                        emailTextView.text = "Email not available"
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
        } else {
            // Handle case where no user is signed in
            usernameTextView.text = "Please sign in"
            emailTextView.text = ""
        }

        loggedOutButton.setOnClickListener {
            auth.signOut() // Sign out the user from Firebase Authentication
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}

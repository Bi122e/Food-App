package com.example.foodapp.ui.home

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodapp.R
import com.example.foodapp.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        setView()
    }

    private fun setView() {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            firebaseFirestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document->
                    if (document.exists()) {
                        val userData = document.data
                        Log.d("FireStore", "user data $userData")

                        val name = document.getString("name")
                        val email = document.getString("email")
                        val uid = document.getString("uid")

                        binding.txt1.text = name
                        binding.txt2.text = email
                        binding.txt3.text = uid
                    } else {
                        Log.d("Firestore", "User document not found")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Firestore", "Error $exception")
                }
        }
    }
}
package com.example.foodapp.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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


//        firebaseFirestore = FirebaseFirestore.getInstance()
//        firebaseAuth = FirebaseAuth.getInstance()




        setupNavBottomBar()
    }


    // Navigation bar
    private fun setupNavBottomBar() {

        binding.bottomBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_explore -> {
                    Toast.makeText(this, "Explore clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_search -> {
                    Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_shopping -> {
                    Toast.makeText(this, "Shopping clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_person -> {
                    Toast.makeText(this, "Person clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }


//    private fun setView() {
//        val userId = firebaseAuth.currentUser?.uid
//        if (userId != null) {
//            firebaseFirestore.collection("users").document(userId)
//                .get()
//                .addOnSuccessListener { document->
//                    if (document.exists()) {
//                        val userData = document.data
//                        Log.d("FireStore", "user data $userData")
//
//                        val name = document.getString("name")
//                        val email = document.getString("email")
//                        val uid = document.getString("uid")
//
//                    } else {
//                        Log.d("Firestore", "User document not found")
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    Log.d("Firestore", "Error $exception")
//                }
//        }
//    }
}

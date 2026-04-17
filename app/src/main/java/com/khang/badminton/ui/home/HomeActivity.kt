package com.khang.badminton.ui.home
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.widget.PopupMenu
import com.khang.badminton.R
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.bumptech.glide.Glide
import com.khang.badminton.databinding.ActivityHomeBinding
import com.khang.badminton.ui.auth.login.LoginActivity


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val db = Firebase.firestore

    //private  val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUser()

        setupMenu()
    }

    private fun loadUser() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->

                val avatarUrl = document.getString("avatar")

                Glide.with(this)
                    .load(avatarUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(binding.imgAvatar)

                Log.d("User", "Avatar: $avatarUrl")
            }
    }

    private fun setupMenu() {
        binding.imgAvatar.setOnClickListener {

            val popup = PopupMenu(this, binding.imgAvatar)

            popup.menu.add("View Profile")
            popup.menu.add("Change Password")
            popup.menu.add("Logout")

            popup.setOnMenuItemClickListener {

                when (it.title) {
                    "View Profile" -> {
                        Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                    }
                    "Change Password" -> {
                        Toast.makeText(this, "Change Password", Toast.LENGTH_SHORT).show()
                    }
                    "Logout" -> {
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)
                    }
                }

                true
            }

            popup.show()
        }
    }

}
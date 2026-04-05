package com.khang.badminton.ui.auth.signup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.khang.badminton.databinding.ActivitySignupBinding
import com.khang.badminton.viewmodel.AuthViewModel

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            handleSignup()
        }

        binding.tvBackToLogin.setOnClickListener {
            finish()
        }

        observeViewModel()
    }

    private fun handleSignup() {
        val name = binding.etName.text.toString()
        val email = binding.etRegEmail.text.toString()
        val phone = binding.etPhone.text.toString()
        val password = binding.etRegPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        // 🔥 validate trước
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.register(email, password, name, phone)
    }

    private fun observeViewModel() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is com.khang.badminton.utils.AuthState.Loading -> {
                    Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
                }
                is com.khang.badminton.utils.AuthState.Success -> {
                    Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is com.khang.badminton.utils.AuthState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}

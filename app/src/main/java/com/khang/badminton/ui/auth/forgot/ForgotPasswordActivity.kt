package com.khang.badminton.ui.auth.forgot

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.khang.badminton.databinding.ActivityForgotPasswordBinding
import com.khang.badminton.utils.AuthState
import com.khang.badminton.viewmodel.AuthViewModel

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReset.setOnClickListener {
            val email = binding.etResetEmail.text.toString()
            viewModel.forgotPassword(email)
        }

        binding.tvBackToLoginFromReset.setOnClickListener {
            finish()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthState.Loading -> {
                    Toast.makeText(this, "Sending...", Toast.LENGTH_SHORT).show()
                }
                is AuthState.Success -> {
                    Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is AuthState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
}

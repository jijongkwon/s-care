package com.scare.ui.mobile.login

import GoogleLoginRepository
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class LoginActivity : ComponentActivity() {
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>
    private lateinit var googleLoginRepository: GoogleLoginRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleLoginRepository = GoogleLoginRepository(this)

        signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data

            googleLoginRepository.handleSignInResult(data) { account ->
                val resultIntent = Intent().apply {
                    putExtra("profileUrl", account?.photoUrl?.toString())
                }

                setResult(RESULT_OK, resultIntent)
                finish() // LoginActivity 종료
            }
        }
        signInLauncher.launch(googleLoginRepository.getSignInIntent())
    }
}
package com.scare.ui.mobile.login

import GoogleLoginRepository
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider

class LoginActivity : ComponentActivity() {
    private lateinit var googleLoginRepository: GoogleLoginRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        googleLoginRepository = GoogleLoginRepository(this)

        // Google 로그인 인텐트 시작
        val signInIntent = googleLoginRepository.getSignInIntent()
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            googleLoginRepository.handleSignInResult(data) { account ->
                if (account != null) {
                    // 로그인 성공 시 MainActivity로 결과 반환
                    val resultIntent = Intent().apply {
                        putExtra("imageUrl", account.photoUrl.toString())
                        putExtra("accessToken", account.idToken)
                    }
                    setResult(RESULT_OK, resultIntent)
                } else {
                    setResult(RESULT_CANCELED)
                }
                finish() // LoginActivity 종료
            }
        }
    }

    companion object {
        const val REQUEST_CODE_SIGN_IN = 9001
    }
}
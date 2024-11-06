import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.scare.BuildConfig

class GoogleLoginRepository(context: Context) {
    private val googleLoginClient: GoogleSignInClient
    private val CLIENT_ID: String = BuildConfig.CLIENT_ID

    init {
        // Google Sign-In 옵션 설정
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail() // 사용자 이메일 요청
            .requestIdToken(CLIENT_ID) // 인증 토큰 요청
            .build()

        // GoogleSignInClient 초기화
        googleLoginClient = GoogleSignIn.getClient(context, gso)
    }

    // 로그인 화면을 시작하기 위한 Intent 제공
    fun getSignInIntent(): Intent = googleLoginClient.signInIntent

    // 로그인 결과 처리 함수
    fun handleSignInResult(data: Intent?, onResult: (GoogleSignInAccount?) -> Unit) {
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
        task.addOnCompleteListener { completedTask ->
            // 로그인 성공 시 GoogleSignInAccount 반환, 실패 시 null 반환
            onResult(completedTask.result.takeIf { completedTask.isSuccessful })
        }
    }

    // 로그아웃 처리 함수
    fun signOut() {
        googleLoginClient.signOut()
    }
}
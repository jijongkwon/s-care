package com.scare.data.member.network

import com.google.android.gms.common.api.Response
import com.scare.data.member.dto.Auth.LoginRequestDTO
import com.scare.data.member.dto.Auth.LoginResponseDTO
import com.scare.data.member.dto.Auth.RefreshRequestDTO
import com.scare.data.member.dto.User.UserInfoResponseDTO
import com.scare.data.member.repository.Auth.TokenRepository
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

object RetrofitClient {
    private const val BASE_URL = "https://k11a408.p.ssafy.io"

    private lateinit var tokenRepository: TokenRepository
    private lateinit var tokenInterceptor: TokenInterceptor

    fun init(tokenRepo: TokenRepository) {
        tokenRepository = tokenRepo
        // TokenInterceptor를 tokenRepository 초기화 후 생성
        tokenInterceptor = TokenInterceptor(tokenRepository) { apiService } // apiService 제공
    }

    // OkHttpClient에 TokenInterceptor 추가 (tokenRepository 초기화 이후에만 접근 가능)
    private val okHttpClient: OkHttpClient
        get() {
            checkInitialized() // tokenRepository 초기화 확인
            return OkHttpClient.Builder()
                .addInterceptor(tokenInterceptor) // TokenInterceptor에 tokenRepository 주입
                .build()
        }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // OkHttpClient 추가
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ApiService 초기화
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // tokenRepository가 초기화되지 않았을 경우 예외 처리
    private fun checkInitialized() {
        if (!RetrofitClient::tokenRepository.isInitialized) {
            throw IllegalStateException("TokenRepository must be initialized before using RetrofitClient")
        }
    }
}

interface ApiService {

    //로그인
    @POST("/api/v1/members/auth/login")
    fun login(@Body loginRequestDTO: LoginRequestDTO): Call<Unit>

    //토큰재발급
    @POST("/api/v1/members/auth/reissue")
    fun refreshToken(@Body refreshToken: RefreshRequestDTO): Call<Unit>

    //회원정보조회
    @GET("user/info") // 실제 API 엔드포인트에 맞게 수정
    suspend fun getUserInfo(): UserInfoResponseDTO
}

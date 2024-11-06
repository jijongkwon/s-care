package com.scare.data.member.network

import android.content.Context
import android.util.Log
import com.scare.BuildConfig
import com.scare.data.member.dto.Auth.LoginRequestDTO
import com.scare.data.member.dto.Auth.RefreshRequestDTO
import com.scare.data.member.dto.User.UserAPIResponseDTO
import com.scare.data.member.repository.Auth.TokenRepository
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import java.net.CookieManager
import java.net.CookiePolicy

object RetrofitClient {
    private const val BASE_URL = BuildConfig.BASE_URL

    lateinit var tokenRepository: TokenRepository
    private lateinit var tokenInterceptor: TokenInterceptor

    fun init(tokenRepo: TokenRepository) {
        tokenRepository = tokenRepo
        // TokenInterceptor를 tokenRepository 초기화 후 생성
        tokenInterceptor = TokenInterceptor(tokenRepository) { apiService } // apiService 제공
    }

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    // OkHttpClient에 TokenInterceptor 추가 (tokenRepository 초기화 이후에만 접근 가능)
    private val okHttpClient: OkHttpClient
        get() {
            checkInitialized() // tokenRepository 초기화 확인

            // CookieManager 및 JavaNetCookieJar 설정
            val cookieManager = CookieManager().apply {
                setCookiePolicy(CookiePolicy.ACCEPT_ALL) // 모든 쿠키를 허용
            }

            Log.d("RetrofitClient", "OkHttpClient created ${cookieManager}")

            return OkHttpClient.Builder()
                .cookieJar(JavaNetCookieJar(cookieManager))
                .addInterceptor(loggingInterceptor) //HTTP 로그 보고 싶을때, 안 보고 싶으면 여기 주석해주세요
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
    @GET("/api/v1/members/{memberId}")
    suspend fun getUserInfo(@Path("memberId") memberId: Long): UserAPIResponseDTO

    //로그아웃
    @POST("/api/v1/members/auth/logout")
    fun logout(): Call<Unit>
    
    //탈퇴
    @PATCH("/api/v1/members/auth/{memberId}/withdraw")
    fun withdraw(@Path("memberId") memberId: Long): Call<Unit>
}

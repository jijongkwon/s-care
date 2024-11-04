package com.scare.data.network

import com.scare.data.dto.Auth.LoginRequestDTO
import com.scare.data.dto.Auth.LoginResponseDTO
import com.scare.data.dto.Auth.RefreshRequestDTO
import com.scare.data.dto.User.UserInfoResponseDTO
import com.scare.data.repository.Auth.TokenRepository
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

    // OkHttpClient에 TokenInterceptor 추가
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(tokenRepository, apiService))
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // OkHttpClient 추가
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

interface ApiService {

    //로그인
    @POST("/api/v1/members/auth/login")
    fun login(@Body loginRequestDTO: LoginRequestDTO): Call<LoginResponseDTO>

    //토큰재발급
    @POST("auth/refresh")
    fun refreshToken(@Body refreshToken: RefreshRequestDTO): Call<LoginResponseDTO>

    //회원정보조회
    @GET("user/info") // 실제 API 엔드포인트에 맞게 수정
    suspend fun getUserInfo(): UserInfoResponseDTO
}

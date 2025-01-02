package com.masonk.repositorysearch

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// object 클래스
// 싱글톤 패턴을 구현하기 위해 사용
// 해당 클래스의 인스턴스가 하나만 생성, 인스턴스를 명시적으로 생성할 필요가 없으며 처음 접근할 때 자동으로 생성
// 전역적으로 접근 가능
object APIClient {
    private const val BASE_URL = "https://api.github.com/"

    // 네트워크 요청 처리에 사용되는 OkHttpClient
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { // 인터셉터를 추가하여 모든 네트워크 요청을 가로채고 Authorization 헤더를 추가
            // 환경 변수에서 토큰 가져오기
            val token = System.getenv("GITHUB_TOKEN")

            val request = it.request() // 기존 요청 가져오기
                .newBuilder() // 기존 요청을 기반으로 새로운 요청을 생성
                .addHeader("Authorization", "Bearer $token") // 헤더 추가
                .build() // 최종 요청 생성

            it.proceed(request) // 수정된 요청 계속 진행
        }
        .build()

    // 네트워크 요청을 처리하기 위한 Retrofit 객체
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) // 모든 네트워크 요청의 기준
        .client(okHttpClient) // 네트워크 요청을 처리할 때 지정된 OkHttpClient를 사용하도록 설정
        .addConverterFactory(GsonConverterFactory.create()) // JSON 데이터를 GSON으로 변환하기 위한 컨버터
        .build()
}
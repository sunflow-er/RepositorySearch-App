package com.masonk.repositorysearch.network

import com.masonk.repositorysearch.model.Repo
import com.masonk.repositorysearch.model.UserDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    // 특정 사용자의 저장소 목록을 가져옴
    // 1. listRepos 메서드가 호출되면, username 매개변수에 전달된 값이 URL의 {username} 부분 대체
    // 2. 완성된 URL로 HTTP GET 요청
    // 3. 서버로부터 JSON 응답이 오면, GsonConverterFactory를 통해 List<Repo> 객체로 변환
    // 4. 변환된 데이터는 Call<List<Repo>> 객체를 통해 반환
    @GET("users/{username}/repos")
    fun listRepos(@Path("username") username: String, @Query("page") page: Int): Call<List<Repo>> // Path Parameter

    // 사용자 검색
    // 1. searchUsers 메서드가 호출되면, query 매개변수에 전달된 값이 URL의 q 쿼리 파라미터로 사용
    // 2. 완성된 URL로 HTTP GET 요청
    // 3. 서버로부터 JSON 응답이 오면, GsonConverterFactory를 통해 UserDto 객체로 변환
    // 4. 변환된 데이터는 Call<UserDto> 객체를 통해 반환
    @GET("search/users")
    fun searchUsers(@Query("q") query: String): Call<UserDto>
}
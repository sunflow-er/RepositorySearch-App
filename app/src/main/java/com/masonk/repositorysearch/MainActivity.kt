package com.masonk.repositorysearch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.masonk.repositorysearch.adapter.UserAdapter
import com.masonk.repositorysearch.databinding.ActivityMainBinding
import com.masonk.repositorysearch.model.Repo
import com.masonk.repositorysearch.model.UserDto
import com.masonk.repositorysearch.network.GithubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // 리사이클러뷰 어댑터
    private lateinit var userAdapter: UserAdapter

    // Handler는 Looper와 함께 작동하여 메시지나 Runnable 객체를 큐에 넣고,
    // 이를 특정 스레드(주로 메인 스레드)에서 실행할 수 있도록 함
    private val handler = Handler(Looper.getMainLooper())

    // 검색하려는 단어를 저장하는 변수
    private var searchFor: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 리사이클러뷰 어댑터 생성
        userAdapter = UserAdapter {
            // onClick(User) 콜백 함수
            val intent = Intent(this@MainActivity, RepoActivity::class.java)
            intent.putExtra("username", it.username)
            startActivity(intent)
        }

        // 리사이클러뷰 설정
        binding.userRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }

        // Runnable 객체
        // 주로 멀티스레딩, 지연 실행, 반복 실행을 위해 사용
        val runnable = Runnable {
            searchUser()
        }

        // 입력값에 대한 사용자 검색 작업
        binding.searchEditText.addTextChangedListener {
            // debouncing
            // 사용자가 입력을 멈춘 후 일정 시간 동안 추가 입력이 없을 때만 특정 작업을 수행하도록 하는 기법
            // 불필요한 연속 호출을 방지하고 성능을 최적화

            // 현재 텍스트 searchFor에 저장
            searchFor = it.toString() 
            
            // 이전에 예약된 runnable 제거, 중복 호출 방지
            handler.removeCallbacks(runnable)
            
            // 300ms 후에 runnable 실행 예약
            handler.postDelayed(runnable, 300)
        }


    }

    private fun searchUser() {
        // Retrofit 객체를 이용하여 GithubService 인터페이스의 구현체 생성
        // GithubService 인터페이스는 API 호출을 정의한 메서드를 포함
        // Retrofit은 정의된 메서드를 바탕으로 관련 네트워크 요청을 처리하는 구현체를 자동으로 생성
        val githubService = APIClient.retrofit.create(GithubService::class.java)

        // searchUsers 네트워크 요청
        // Retrofit은 OkHttp의 동작을 감싸서 콜백 메서드들을 메인 스레드에서 실행되도록 함
        githubService.searchUsers(searchFor).enqueue(object : Callback<UserDto> {
            override fun onResponse(p0: Call<UserDto>, p1: Response<UserDto>) {
                // 응답받은 데이터 (사용자 리스트)
                val userList = p1.body()?.items

                // 리사이클러뷰 어댑터에 전달
                userAdapter.submitList(userList)
            }

            override fun onFailure(p0: Call<UserDto>, p1: Throwable) {
                Toast.makeText(this@MainActivity, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                p1.printStackTrace()
            }

        })
    }
}
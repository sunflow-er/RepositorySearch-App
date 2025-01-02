package com.masonk.repositorysearch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.masonk.repositorysearch.adapter.RepoAdapter
import com.masonk.repositorysearch.databinding.ActivityRepoBinding
import com.masonk.repositorysearch.model.Repo
import com.masonk.repositorysearch.network.GithubService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RepoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepoBinding
    private lateinit var repoAdapter: RepoAdapter

    // 페이지
    private var page = 0

    // 다음 페이지가 있는지
    private var hasMore = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // MainActivity에서 전달된 username 값을 가져옴, 값이 없으면 함수 종료
        val username = intent.getStringExtra("username") ?: return

        binding.usernameTextView.text = username

        repoAdapter = RepoAdapter {
            // onClick(Repo) 콜백 함수
            // 선택된 레포지토리의 URL을 웹 브라우저에서 엶
            val intent = Intent(
                Intent.ACTION_VIEW, // 주어진 URI를 표시하는 액션
                Uri.parse(it.htmlUrl) // 문자열을 URI 객체로 변환
            )
            startActivity(intent)
        }

        val linearLayoutManager = LinearLayoutManager(this@RepoActivity)

        binding.repoRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = repoAdapter
        }

        // paging
        binding.repoRecyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // 레이아웃 매니저가 관리하고 있는 아이템 수
                val totalCount = linearLayoutManager.itemCount // 1부터 시작

                // 마지막으로 보여지는 아이템의 position 값
                val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition() // 0부터 시작
                
                // 리사이클러뷰 아이템의 끝에 도달했고,
                // 다음 페이지가 있을 때
                if(lastVisiblePosition >= totalCount - 1 && hasMore) {
                    // 페이지 증가
                    page += 1
                    
                    // 다음 페이지도 보여주기
                    listRepo(username, page)
                }
            }
        })

        // 레포지토리 조회
        // 첫 페이지 보여주기
        listRepo(username, 0)
    }

    private fun listRepo(username: String, page: Int) {
        // Retrofit 객체를 이용하여 GithubService 인터페이스의 구현체 생성
        val githubService = APIClient.retrofit.create(GithubService::class.java)

        // listRepos 네트워크 요청
        githubService.listRepos(username, page).enqueue(object : Callback<List<Repo>> {
            override fun onResponse(p0: Call<List<Repo>>, p1: Response<List<Repo>>) {
                val repoList = p1.body()

                // 한 페이지당 가져올 수 있는 최대 레포지토리 개수는 30개
                // 가져온 레포지토리 수가 30이면 다음 페이지 있음(true)
                // 30보다 적으면 다음 페이지 없음(false)
                hasMore = (repoList?.count() == 30)

                // 기존의 리스트에 새로 가져온 리스트 추가
                repoAdapter.submitList(repoAdapter.currentList + repoList.orEmpty())
            }

            override fun onFailure(p0: Call<List<Repo>>, p1: Throwable) {

            }

        })
    }
}
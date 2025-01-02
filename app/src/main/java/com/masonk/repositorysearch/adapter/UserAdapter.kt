package com.masonk.repositorysearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.masonk.repositorysearch.databinding.ItemUserBinding
import com.masonk.repositorysearch.model.User

// ListAdapter는 내부적으로 DiffUtil을 사용하여 데이터 변경을 자동으로 처리
// submitList() 메서드를 통해 새로운 리스트를 전달하면, DiffUtil이 두 리스트를 비교하여 변경된 부분만 업데이트
class UserAdapter(
    val onClick: (User) -> Unit // 사용자가 아이템을 클릭했을 때 실행될 콜백 함수
) : ListAdapter< // RecyclerView의 성능을 최적화하기 위해 설계된 어댑터
        User, // RecyclerView에서 표시할 데이터 타입
        UserAdapter.UserHolder // 뷰홀더
        >(diffUtil) {

    // 개별 아이템 뷰를 관리
    inner class UserHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // 뷰에 데이터를 바인딩
        fun bind(user: User) {
            binding.usernameTextView.text = user.username

            // 사용자가 아이템을 클릭했을 때
            binding.root.setOnClickListener {
                // onClick 콜백 함수 호출
                onClick(user)
            }
        }
    }

    companion object {
        // submitList 메서드를 호출하면 DiffUtil이 두 리스트를 비교하여 변경된 부분만 RecyclerView에 반영
        val diffUtil = object : DiffUtil.ItemCallback<User>() {
            // 동일한 아이템인지 확인
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            // 두 아이템이 동일한 데이터를 가지고 있는지 확인
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

        }
    }

    // 새로운 뷰홀더를 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context), // parent.context는 RecyclerView의 컨텍스트를 의미
                parent, // 해당 View가 어떤 ViewGroup에 속하게 될지를 지정
                false // View는 코드에서 명시적으로 추가될 때까지 부모에 추가되지 않음
            )
        )
    }

    // 뷰홀더에 데이터를 바인딩
    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(
            currentList[position] // ListAdapter 클래스에서 제공하는 프로퍼티, 현재 어댑터에 바인딩된 데이터 리스트이자 최신 업데이트 리스트
        )
    }
}
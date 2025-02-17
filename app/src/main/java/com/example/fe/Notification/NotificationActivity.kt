package com.example.fe.Notification

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.R
import com.example.fe.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 설정
        setSupportActionBar(binding.toolbar)

        // 뒤로가기 버튼 클릭 시 현재 액티비티 종료
        binding.btnBack.setOnClickListener {
            finish()
        }

        // 더미 데이터 (없으면 빈 리스트)
        val notifications = listOf(
            AppNotification(R.drawable.ic_notification, "알림 1", "내용 1", "12월 15일"),
            AppNotification(R.drawable.ic_notification, "알림 2", "내용 2", "12월 15일"),
            AppNotification(R.drawable.ic_notification, "알림 3", "내용 3", "12월 15일")
        )

        if (notifications.isEmpty()) {
            // 데이터가 없으면 메시지 표시
            binding.notificationRecyclerView.visibility = View.GONE
            binding.emptyMessage.visibility = View.VISIBLE
        } else {
            // 데이터가 있으면 RecyclerView 표시
            binding.notificationRecyclerView.visibility = View.VISIBLE
            binding.emptyMessage.visibility = View.GONE

            // RecyclerView 설정
            binding.notificationRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.notificationRecyclerView.adapter = NotificationAdapter(notifications)
        }
    }
}

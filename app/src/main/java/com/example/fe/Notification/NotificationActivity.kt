package com.example.fe.Notification

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R


class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        // 툴바 설정
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar) // 툴바를 액티비티의 ActionBar로 설정

        // 뒤로가기 버튼 활성화
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 뒤로가기 아이콘 클릭 이벤트 처리
        toolbar.setNavigationOnClickListener {
            finish() // 뒤로가기 버튼 클릭 시 액티비티 종료
        }

        val recyclerView = findViewById<RecyclerView>(R.id.notification_recycler_view)
        val emptyMessage = findViewById<TextView>(R.id.empty_message)

        // 더미 데이터 (없으면 빈 리스트)
        val notifications = listOf<AppNotification>(
            AppNotification(R.drawable.ic_notification, "알림 1", "내용 1", "12월 15일"),
            AppNotification(R.drawable.ic_notification, "알림 2", "내용 2", "12월 15일"),
            AppNotification(R.drawable.ic_notification, "알림 3", "내용 3", "12월 15일")
        ) // 빈 리스트로 변경 가능

        if (notifications.isEmpty()) {
            // 데이터가 없으면 메시지 표시
            recyclerView.visibility = View.GONE
            emptyMessage.visibility = View.VISIBLE
        } else {
            // 데이터가 있으면 RecyclerView 표시
            recyclerView.visibility = View.VISIBLE
            emptyMessage.visibility = View.GONE

            // RecyclerView 설정
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = NotificationAdapter(notifications)
        }
    }
}
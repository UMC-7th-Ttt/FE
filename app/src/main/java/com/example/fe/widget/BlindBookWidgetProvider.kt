package com.example.fe.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.fe.BookDetail.BookDetailActivity
import com.example.fe.JohnRetrofitClient
import com.example.fe.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlindBookWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        Log.d("BlindBookWidgetProvider", "onUpdate called with appWidgetIds: ${appWidgetIds.joinToString()}")

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    @SuppressLint("RemoteViewLayout")
    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.blindbook_widget)
        Log.d("BlindBookWidgetProvider", "updateAppWidget called for appWidgetId: $appWidgetId")

        // Make API call
        val api = JohnRetrofitClient.getClient(context).create(BlindBookWidgetInterface::class.java)
        api.getQuotes().enqueue(object : Callback<BlindBookResponse> {
            override fun onResponse(call: Call<BlindBookResponse>, response: Response<BlindBookResponse>) {
                Log.d("BlindBookWidgetProvider", "API call onResponse called with response: $response")
                if (response.isSuccessful) {
                    val result = response.body()?.result
                    val quote = result?.mainSentences ?: "No quote available"
                    val bookId = result?.id ?: 0
                    views.setTextViewText(R.id.widget_text, quote)

                    // Set up the intent that starts BookDetailActivity
                    val intent = Intent(context, BookDetailActivity::class.java).apply {
                        putExtra("BOOK_ID", bookId)
                    }
                    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                    views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent)

                    appWidgetManager.updateAppWidget(appWidgetId, views)
                } else {
                    Log.e("BlindBookWidgetProvider", "API call failed with response: $response")
                    views.setTextViewText(R.id.widget_text, "Failed to load quote")
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }

            override fun onFailure(call: Call<BlindBookResponse>, t: Throwable) {
                Log.e("BlindBookWidgetProvider", "API call onFailure called with throwable: $t")
                views.setTextViewText(R.id.widget_text, "Failed to load quote")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        })
    }
}
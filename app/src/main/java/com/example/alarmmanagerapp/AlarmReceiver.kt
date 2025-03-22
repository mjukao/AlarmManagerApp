package com.example.alarmmanagerapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.media.MediaPlayer
class AlarmReceiver : BroadcastReceiver() {
    //การใช้แฟ้มเสียงหรือวิดีทัศน์ (2 คะแนน)
    private var mediaPlayer: MediaPlayer? = null
    override fun onReceive(context: Context, intent: Intent?) {
        Toast.makeText(context, "Time to wake up!", Toast.LENGTH_LONG).show()
        // Play alarm sound
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound)  // เสียงที่ต้องการใช้
        mediaPlayer?.start()
    }
}

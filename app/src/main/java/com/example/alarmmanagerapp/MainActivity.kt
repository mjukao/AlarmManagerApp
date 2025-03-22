package com.example.alarmmanagerapp

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmmanagerapp.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*
import android.widget.Toast
import android.content.res.Configuration
import java.util.Locale
import android.media.MediaPlayer

class MainActivity : AppCompatActivity() {
    //การใช้ตัวแปร (1 คะแนน)
    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmManager: AlarmManager
    private lateinit var calendar: Calendar
    private lateinit var pendingIntent: PendingIntent
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        calendar = Calendar.getInstance()

        val buttons = listOf(
            binding.selectTimeBtn,
            binding.setAlarmBtn,
            binding.cancelAlarmBtn,
            binding.closeAppBtn,
            binding.languageSwitchBtn
        )
        //การวนซ้ำ (3 คะแนน)
        buttons.forEach { button ->
            button.setOnClickListener {
                when (button.id) {
                    R.id.selectTimeBtn -> showTimePicker()
                    R.id.setAlarmBtn -> setAlarm()
                    R.id.cancelAlarmBtn -> cancelAlarm()
                    R.id.closeAppBtn -> closeApp()
                    R.id.languageSwitchBtn -> languageSwitchBtn()
                }
            }
        }
    }

    private fun showTimePicker() {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(0)
            .setMinute(0)
            .setTitleText("Select Alarm Time")
            .build()

        picker.show(supportFragmentManager, "")

        picker.addOnPositiveButtonClickListener {
            val selectedHour = picker.hour
            val selectedMinute = picker.minute
            //การป้อนข้อมูล (Edit 2 คะแนน)
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            binding.selectedTime.text = formattedTime

            calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            calendar.set(Calendar.MINUTE, selectedMinute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm() {
        // Use the selected time from MaterialTimePicker to set the alarm
        val intent = Intent(applicationContext, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(
            applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )


        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        // Show success message
        Toast.makeText(this, "Alarm set successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun cancelAlarm() {
        //การตัดสินใจ (3 คะแนน)
        if (::pendingIntent.isInitialized) {
            alarmManager.cancel(pendingIntent)
            //การแสดงข้อความออกมาทาง Logcat (3 คะแนน)
            Log.d("AlarmManager", "Alarm canceled.")
        } else {
            Log.e("AlarmManager", "PendingIntent not initialized!")
        }
    }

    private fun closeApp() {
        finish()  // Close current activity
    }
    //UI สองภาษา (ไทย / อังกฤษ) (4 คะแนน)
    private fun languageSwitchBtn() {
        val currentLocale = resources.configuration.locale.language
        if (currentLocale == "th") {
            setLocale("en")  // Change to English
            Toast.makeText(this, "Language switched to English", Toast.LENGTH_SHORT).show()
        } else {
            setLocale("th")  // Change to Thai
            Toast.makeText(this, "เปลี่ยนเป็นภาษาไทย", Toast.LENGTH_SHORT).show()
        }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setLocale(localeCode: String) {
        val locale = Locale(localeCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        this.resources.updateConfiguration(config, this.resources.displayMetrics)
    }

    private fun playAlarmSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound)
        mediaPlayer?.start()
    }

    private fun stopAlarmSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }
}
//รูป Icon เป็นของตัวเอง (Icon) (2 คะแนน)
//โปรแกรมทำงานบนโทรศัพท์จริง (ไม่ใช้ emulator) (3 คะแนน )
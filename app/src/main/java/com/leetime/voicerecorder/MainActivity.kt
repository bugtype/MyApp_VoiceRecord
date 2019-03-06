package com.leetime.voicerecorder

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaRecorder
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.widget.Toast
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.Manifest.permission
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.Manifest.permission.RECORD_AUDIO




class MainActivity : AppCompatActivity() {


    private var myAudioRecorder: MediaRecorder? = null
    private var outputFile: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()
        initRecorder()
        initBinding()
    }
    fun initRecorder(){
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp"
        myAudioRecorder = MediaRecorder()
        myAudioRecorder?.let {
            it.setAudioSource(MediaRecorder.AudioSource.MIC)
            it.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            it.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
            it.setOutputFile(outputFile)
        }

    }
    fun initBinding(){
        btn_recorde
            .clicks()
            .subscribe{
                try {
                    myAudioRecorder?.prepare();
                    myAudioRecorder?.start();
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
            }
        btn_record_stop
            .clicks()
            .subscribe {
                myAudioRecorder?.stop();
                myAudioRecorder?.release();
                myAudioRecorder = null;
                Toast.makeText(getApplicationContext(), "Audio Recorder stopped", Toast.LENGTH_LONG).show();
            }
    }

    val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100
    val MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 200

    fun checkPermission() {

        val permissionCheck_RECORD = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        val permissionCheck_WRITE = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //(ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.RECORD_AUDIO
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {

                // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다

            } else {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO
                )

                // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다

            }
        }
    }
}

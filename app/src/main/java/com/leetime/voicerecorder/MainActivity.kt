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
import android.media.MediaPlayer
import android.util.Log
import java.io.File
import com.gun0912.tedpermission.TedPermissionBase.getDeniedPermissions
import com.gun0912.tedpermission.TedPermissionBase.isGranted
import com.tedpark.tedpermission.rx2.TedRx2Permission
import javax.security.auth.callback.Callback



class MainActivity : AppCompatActivity() {


    private var myAudioRecorder: MediaRecorder? = null
    private var outputFile: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission{
            if (it) { initSetting() }
        }


    }
    fun initSetting(){
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
        btn_play
            .clicks()
            .subscribe {
                val mediaPlayer = MediaPlayer()
                try {
                    Log.v("Test", File(outputFile).exists().toString())
                    mediaPlayer.setDataSource(outputFile)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    Toast.makeText(applicationContext, "Playing Audio", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    // make something
                    e.printStackTrace()
                }
            }
    }

    fun checkPermission(callback: ((Boolean)->Unit)) {

        val title = "hhhh"
        val msg = "msg"

        TedRx2Permission.with(this)
            .setRationaleTitle(title)
            .setRationaleMessage(msg) // "we need permission for read contact and find your location"
            .setPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request()
            .subscribe({ tedPermissionResult ->
                if (tedPermissionResult.isGranted()) {
                    callback(true)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                } else {
                    callback(false)
                    Toast.makeText(
                        this,
                        "Permission Denied\n" + tedPermissionResult.getDeniedPermissions().toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }, { throwable -> })
    }
}


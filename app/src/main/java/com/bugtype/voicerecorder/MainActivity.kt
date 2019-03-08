package com.bugtype.voicerecorder

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaRecorder
import android.widget.Toast
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.util.Log
import java.io.File
import com.tedpark.tedpermission.rx2.TedRx2Permission
import android.view.Menu
import android.view.MenuItem
import com.bugtype.voicerecorder.AppManager.outputFolder
import com.bugtype.voicerecorder.Extensions.toSimpleString
import java.util.*


class MainActivity : AppCompatActivity() {


    private var myAudioRecorder: MediaRecorder? = null
    private var outputFile: String? = null
    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission {
            if (it) {
                initSetting()
            }
        }

    }

    // MARK: - Init Setting
    private fun initSetting() {
        initVariables()
        initBinding()

        btn_recorde.setBackgroundColor(Color.GREEN)
    }

    private fun initVariables() {
        val folderFileObject = File(outputFolder)
        if (!folderFileObject.exists()) {
            folderFileObject.mkdir()
        }
    }

    private fun initRecorder() {
        val nowDate = Date().toSimpleString()
        myAudioRecorder = null
        myAudioRecorder = MediaRecorder()
        outputFile = "$outputFolder/$nowDate.3gp"
        myAudioRecorder?.let {
            it.setAudioSource(MediaRecorder.AudioSource.MIC)
            it.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            it.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
            it.setOutputFile(outputFile)
        }

        try {
            myAudioRecorder?.prepare();
            myAudioRecorder?.start();
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Toast.makeText(this, "Audio Recording started", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("CheckResult")
    private fun initBinding() {
        btn_recorde
            .clicks()
            .subscribe {
                if (!isRecording) {
                    this.initRecorder()
                    btn_recorde.setBackgroundColor(Color.RED)
                    btn_recorde.text = getString(R.string.main_stop_recoding)
                }
                else
                {
                    btn_recorde.setBackgroundColor(Color.GREEN)
                    btn_recorde.text = getString(R.string.main_start_recoding)
                    myAudioRecorder?.stop();
                    myAudioRecorder?.release();
                    myAudioRecorder = null;
                    Toast.makeText(this, "Audio Recorder stopped", Toast.LENGTH_LONG).show();
                }
                isRecording = !isRecording
            }
    }

    @SuppressLint("CheckResult")
    private fun checkPermission(callback: ((Boolean) -> Unit)) {

        val title = "Please Grant Permissions"
        val msg = "Please Grant Permissions"

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

    // MARK: - Create Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        if (id == R.id.action_more) {
            val intent = Intent(this, RecodingFilesListActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}


package com.leetime.voicerecorder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.leetime.voicerecorder.AppManager.outputFolder
import com.leetime.voicerecorder.Model.VoiceFile
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_record_files.*
import java.io.File


class RecodingFilesListActivity : AppCompatActivity() {

    var voiceFileList: ArrayList<VoiceFile> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_files)
        getFileList()
    }

    private fun updateUI(){

        Observable
            .fromIterable(voiceFileList)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                txt_file_list.text = txt_file_list.text.toString() + it.name + "\n"
            }
    }

    private fun getFileList() {
        val fileList = File(outputFolder)
            .walkTopDown()
            .toList()
        Observable
            .fromIterable(fileList)
            .map { it }
            .subscribe (
                { voiceFileList.add ( VoiceFile(it.name, it.absolutePath) ) }, // onSuccess
                { it.printStackTrace() }, // onError,
                { updateUI() }
            )
            .dispose()
    }

}


package com.leetime.voicerecorder

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.leetime.voicerecorder.AppManager.outputFolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_record_files.*
import java.io.File

class RecodingFilesListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_files)
        getFileList()
    }

    private fun getFileList() {

        val fileList = File(outputFolder)
            .walkTopDown()
            .toList()
        Observable
            .fromIterable(fileList)
            .map { it }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.v("test", it.toString())
                txt_file_list.text = txt_file_list.text.toString() + it.toString()

            }

    }

}

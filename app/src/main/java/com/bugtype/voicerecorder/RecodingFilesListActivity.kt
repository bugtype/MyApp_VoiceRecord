package com.bugtype.voicerecorder

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugtype.voicerecorder.AppManager.outputFolder
import com.bugtype.voicerecorder.Model.VoiceFile
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_record_files.*
import java.io.File


class RecodingFilesListActivity : AppCompatActivity() {


    private lateinit var linearLayoutManager: LinearLayoutManager
    private var voiceFileList: ArrayList<VoiceFile> = ArrayList()
    private var adapter: RecodingFilesAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_files)
        initRecycleView()
        getFileList()
    }
    private fun initRecycleView(){
        adapter = RecodingFilesAdapter(voiceFileList)
        linearLayoutManager = LinearLayoutManager(this)
        rcycView_fileList.layoutManager = linearLayoutManager
        rcycView_fileList?.adapter = adapter
    }


    @SuppressLint("CheckResult")
    private fun updateUI(){

        Observable
            .fromIterable(voiceFileList)
            .subscribeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                adapter?.notifyDataSetChanged()
                rcycView_fileList.invalidate()
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


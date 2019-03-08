package com.bugtype.voicerecorder

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bugtype.voicerecorder.Model.VoiceFile
import kotlinx.android.synthetic.main.activity_file_list_item.view.*
import android.text.method.TextKeyListener.clear
import android.widget.Toast
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.schedulers.IoScheduler
import java.io.File
import java.lang.Exception


class RecodingFilesAdapter(private var items: ArrayList<VoiceFile>): RecyclerView.Adapter<RecodingFilesAdapter.ViewHolder>() {
    private var context: Context? = null

    override fun getItemCount(): Int {
        return items.size
    }


    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var fileDTO = items[position]
        holder?.txtName?.text = fileDTO.name
        holder?.txtName
            ?.clicks()
            ?.subscribe {
                val mediaPlayer = MediaPlayer()
                try {
                    mediaPlayer.setDataSource(fileDTO.path)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    Toast.makeText(context, "Play Recording File", Toast.LENGTH_LONG).show();
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
            .inflate(R.layout.activity_file_list_item, parent, false)
        context = parent.context

        return ViewHolder(itemView)
    }


    // MARK:- Holder
    class ViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var txtName: TextView? = null

        init {
            this.txtName = row.txt_file_name
        }
    }
}

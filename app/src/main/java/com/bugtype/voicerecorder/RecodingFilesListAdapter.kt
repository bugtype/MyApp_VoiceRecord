package com.bugtype.voicerecorder

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bugtype.voicerecorder.Model.VoiceFile
import kotlinx.android.synthetic.main.activity_file_list_item.view.*
import android.text.method.TextKeyListener.clear



class RecodingFilesAdapter(private var items: ArrayList<VoiceFile>): RecyclerView.Adapter<RecodingFilesAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        Log.v("tttt", items.size.toString())
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var fileDTO = items[position]
        Log.v("ttttt", fileDTO.name)
        holder?.txtName?.text = fileDTO.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
            .inflate(R.layout.activity_file_list_item, parent, false)

        return ViewHolder(itemView)
    }
    class ViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var txtName: TextView? = null

        init {
            this.txtName = row.txt_file_name
        }
    }
}

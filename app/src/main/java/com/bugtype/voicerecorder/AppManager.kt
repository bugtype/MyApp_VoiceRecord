package com.bugtype.voicerecorder

import android.os.Environment

object AppManager{
    val outputFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/voiceRecorder"
}
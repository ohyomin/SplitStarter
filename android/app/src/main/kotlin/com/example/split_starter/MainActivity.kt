package com.example.split_starter

import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityOptionsCompat
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity: FlutterActivity() {
    private var isInit = false;
    private var handler: Handler? = null
    private var musicLauncher = MusicLauncher(this)

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        if (isInit) return

        handler = Handler(Looper.getMainLooper())
        musicLauncher.play {
            handler!!.postDelayed({
                launchApplication()
                handler!!.postDelayed({
                    musicLauncher.dispose()
                    finish()
                }, 2000)
            }, 2000)
        }

        isInit = true
    }

    private fun launchApplication() {
        val intent = packageManager.getLaunchIntentForPackage("com.naver.vibe") ?: Intent()
        intent.data = Uri.parse("vibe://player/...")
        context.startActivity(intent)
    }

    private fun launchMultiApplication() {
        Log.e("hmhm", "hmhm launch")
        val intents = mutableListOf<Intent>().apply{
            add(packageManager.getLaunchIntentForPackage("com.naver.vibe") ?: Intent())
            add(packageManager.getLaunchIntentForPackage("com.skt.tmap.ku") ?: Intent())
        }

        Log.e("hmhm", "hmhm intents $intents")

        intents.forEach {
            it.action = Intent.ACTION_MAIN
            it.addCategory(Intent.CATEGORY_LAUNCHER)
            it.flags = Intent.FLAG_RECEIVER_NO_ABORT or Intent.FLAG_RECEIVER_FOREGROUND
        }

        //public static final int WINDOWING_MODE_SPLIT_SCREEN_PRIMARY = 3
        val bundle = ActivityOptionsCompat.makeBasic().toBundle()
        bundle?.run {
            putInt("android.activity.windowingMode", 3)
            putInt("android.activity.splitScreenCreateMode", 0)
        }

        Log.e("hmhm", "hmhm bundle $bundle")
        startActivities(intents.toTypedArray(), bundle)
    }
}


package com.example.javatpoint.kotlinmediaplayer



import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.processNextEventInCurrentThread
import java.util.logging.Handler

import android.os.Handler as OsHandler
import java.lang.Runnable as Runnable

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler = OsHandler()
    private var pause:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val playBtn = findViewById<Button>(R.id.playBtn)
        val pauseBtn = findViewById<Button>(R.id.pauseBtn)
        val stopBtn = findViewById<Button>(R.id.stopBtn)
        val seek_bar = findViewById<SeekBar>(R.id.seek_bar)
        val tv_pass = findViewById<TextView>(R.id.tv_pass)
        val tv_due = findViewById<TextView>(R.id.tv_due)

        mediaPlayer = MediaPlayer.create(applicationContext,R.raw.mo)

        // Start the media player
        playBtn.setOnClickListener{

                if(!(!pause)){
                 mediaPlayer.start()
                 pause = false
                  Toast.makeText(this,"media playing",Toast.LENGTH_SHORT).show()
             }else{
              //  mediaPlayer.prepare()
                mediaPlayer.start()

                 Toast.makeText(this,"media playing",Toast.LENGTH_SHORT).show()

             }


            playBtn.isEnabled = false
             pauseBtn.isEnabled = true
             stopBtn.isEnabled = true

             mediaPlayer.setOnCompletionListener {
                 playBtn.isEnabled = true
                 pauseBtn.isEnabled = false
                 stopBtn.isEnabled = false
                 Toast.makeText(this,"end",Toast.LENGTH_SHORT).show()
             }
        }
        // Pause the media player
        pauseBtn.setOnClickListener {
            if(mediaPlayer.isPlaying){
                mediaPlayer.pause()
                pause = true
                playBtn.isEnabled = true
                pauseBtn.isEnabled = false
                stopBtn.isEnabled = true
                Toast.makeText(this,"media pause",Toast.LENGTH_SHORT).show()
            }
        }
        // Stop the media player
        stopBtn.setOnClickListener{
            if(mediaPlayer.isPlaying || pause.equals(true)){
                pause = false
                try{
                    mediaPlayer.stop()
                    mediaPlayer.reset()
                    mediaPlayer.release()
                   // handler.removeCallbacks(runnable)
                    mediaPlayer = MediaPlayer.create(applicationContext,R.raw.mo)

                }catch(e: NumberFormatException){
                    android.widget.Toast.makeText(this,"media not stop", android.widget.Toast.LENGTH_SHORT).show()
                }
                seek_bar.setProgress(0)



                playBtn.isEnabled = true
                pauseBtn.isEnabled = false
                stopBtn.isEnabled = false
                tv_pass.text = ""
                tv_due.text = ""
                Toast.makeText(this,"media stop",Toast.LENGTH_SHORT).show()
            }
        }
        // Seek bar change listener
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    mediaPlayer.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
        seek_bar.max = mediaPlayer.seconds

        runnable = Runnable {
            seek_bar.progress = mediaPlayer.currentSeconds

            tv_pass.text = "${mediaPlayer.currentSeconds} sec"
            val diff = mediaPlayer.seconds - mediaPlayer.currentSeconds
            tv_due.text = "$diff sec"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)

    }
    // Method to initialize seek bar and audio stats


}
// Creating an extension property to get the media player time duration in seconds
val MediaPlayer.seconds:Int
    get() {
        return this.duration / 1000
    }
// Creating an extension property to get media player current position in seconds
val MediaPlayer.currentSeconds:Int
    get() {
        return this.currentPosition/1000
    }
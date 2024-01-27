package com.example.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.view.MotionEvent
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.controller.databinding.ActivityMainBinding
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress


var speed: Int = 0
var reverse: Int = 0
var direction: Int = 0
var myTargetIP = "not set"
var myTargetPort = "not set"
var throttle: Long = 50

private lateinit var binding: ActivityMainBinding


class SoftOptions {
    var remoteHost: String = "192.168.0.12"
    var remotePort: Int = 4210

    constructor()

    init {}
}

// Global
val mySettings = SoftOptions()

class MainActivity : AppCompatActivity() {

    private fun sendUDP(messageStr: String) {
        // Hack Prevent crash (sending should be done using an async task)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        try {
            //Open a port to send the package
            val socket = DatagramSocket()
            socket.broadcast = true
            val sendData = messageStr.toByteArray()
            val sendPacket = DatagramPacket(
                sendData,
                sendData.size,
                InetAddress.getByName(mySettings.remoteHost),
                mySettings.remotePort
            )
            socket.send(sendPacket)
        } catch (e: IOException) {
            //            Log.e(FragmentActivity.TAG, "IOException: " + e.message)
        }
    }

    private fun updateControls(){
        Thread.sleep(throttle) // To avoid overloading the receiver.

        val message = "$direction$speed$reverse"
        sendUDP(message)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Thread {
            while (true){updateControls()}
        }.start()
        val view = binding.root
        setContentView(view)

        val resultTv = this.findViewById<TextView>(R.id.txtShowIP)
        resultTv.text = "Target device IP is set as " + mySettings.remoteHost
        val resultTvPort = this.findViewById<TextView>(R.id.txtShowPort)
        resultTvPort.text = "Target device port is set as " + mySettings.remotePort
        val speedText = this.findViewById<TextView>(R.id.speedView)


        binding.gas.setOnSeekBarChangeListener( object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val progressVal = progress + 1

                if( progressVal == 10){
                    speed = 0
                    speedText.text = "0"
                } else if(progressVal < 10){
                    speed = 10 - progressVal
                    speedText.text = (10 - progressVal).toString()
                    reverse = 1
                } else {
                    speed = progressVal - 10
                    speedText.text = (progressVal - 10).toString()
                    reverse = 0
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    seekBar.progress = 10
                    speed = 0
                    reverse = 0
                    speedText.text = "0"
                }
            }

        })

        binding.btnLeft.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                direction = 0
            }
            if (event.action == MotionEvent.ACTION_UP) {
                direction = 1
            }
            true
        }

        binding.btnRight.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                direction = 2
            }
            if (event.action == MotionEvent.ACTION_UP) {
                direction = 1
            }
            true
        }

        binding.btnSettings.setOnClickListener {
            val intent = Intent(this, Settings::class.java);
            startActivity(intent)
        }
    }
}




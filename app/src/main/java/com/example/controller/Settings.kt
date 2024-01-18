package com.example.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.controller.databinding.ActivitySettingsBinding

private lateinit var binding: ActivitySettingsBinding

class Settings : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var editEnterip = mySettings.RemoteHost
        var editEnterport = mySettings.RemotePort
        setContentView(R.layout.activity_settings)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.editEnterip.setText(mySettings.RemoteHost.toString())
        binding.editEnterport.setText(mySettings.RemotePort.toString())

        binding.btnSetip.setOnClickListener {
            var finalIP = findViewById<EditText>(R.id.editEnterip)
            var textFromEditText = finalIP.text.toString() // access text this way
            mySettings.RemoteHost = textFromEditText.toString()
            var finalPort = findViewById<EditText>(R.id.editEnterport)
            var textfromEnterport = finalPort.text.toString().toInt()
            //finalPort = "1238"
            mySettings.RemotePort = textfromEnterport.toInt()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        binding.btnCancelip.setOnClickListener {
            var editEnterip = "null"
            var editEnterport = "null"
            myTargetIP = editEnterip
            myTargetPort = editEnterport
            val intent = Intent(this, MainActivity::class.java);
            intent.putExtra("myTargetIP", myTargetIP)
            intent.putExtra("myTargetPort", myTargetPort)
            startActivity(intent)
        }
    }
}

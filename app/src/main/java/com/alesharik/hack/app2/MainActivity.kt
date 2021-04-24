package com.alesharik.hack.app2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.zxing.integration.android.IntentIntegrator

import com.google.zxing.integration.android.IntentResult

import android.content.Intent
import androidx.navigation.fragment.NavHostFragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.google.zxing.client.result.ResultParser

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

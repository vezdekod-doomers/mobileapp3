package com.alesharik.hack.app2.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.alesharik.hack.app2.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.google.zxing.client.result.ResultParser
import com.google.zxing.integration.android.IntentIntegrator

class ScanQRFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IntentIntegrator.forSupportFragment(this).setDesiredBarcodeFormats(listOf(IntentIntegrator.QR_CODE)).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                findNavController().navigateUp()
            } else {
                val parsed = ResultParser.parseResult(Result(result.contents, result.rawBytes, arrayOf(), BarcodeFormat.QR_CODE))
                QrInfoFragment.info = parsed
                findNavController().navigate(R.id.qrInfoFragment)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_qr, container, false)
    }
}

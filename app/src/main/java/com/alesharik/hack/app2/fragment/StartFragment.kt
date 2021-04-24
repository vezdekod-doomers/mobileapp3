package com.alesharik.hack.app2.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.alesharik.hack.app2.R
import kotlinx.android.synthetic.main.fragment_start.view.*
import android.content.Intent
import android.graphics.BitmapFactory

import com.google.zxing.DecodeHintType
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.client.result.ResultParser
import com.journeyapps.barcodescanner.DecoderFactory
import com.journeyapps.barcodescanner.DefaultDecoderFactory


class StartFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_start, container, false)
        view.scan_btn.setOnClickListener {
            findNavController().navigate(R.id.scanQRFragment)
        }
        view.gallery_select_btn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE)

        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE) {
            data?.let {
                requireContext().contentResolver.openInputStream(it.data!!)?.use {
                    val bitmap = BitmapFactory.decodeStream(it)
                    val pixels = IntArray(bitmap.width * bitmap.height)
                    bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
                    val result = decoderFactory.createDecoder(mapOf<DecodeHintType, Nothing>())
                        .decode(RGBLuminanceSource(bitmap.width, bitmap.height, pixels))
                    QrInfoFragment.info = ResultParser.parseResult(result)
                    findNavController().navigate(R.id.qrInfoFragment)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private const val PICK_IMAGE = 1
        private val decoderFactory: DecoderFactory = DefaultDecoderFactory()
    }
}

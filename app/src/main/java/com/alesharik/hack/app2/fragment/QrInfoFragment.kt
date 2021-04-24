package com.alesharik.hack.app2.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.net.wifi.WifiNetworkSuggestion
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import com.alesharik.hack.app2.R
import com.google.zxing.client.result.*
import kotlinx.android.synthetic.main.fragment_qr_info.view.*

class QrInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_qr_info, container, false)
        val inflate = view.content
        info?.let {
            inflate.addText(it.displayResult)
            when (it) {
                is AddressBookParsedResult -> {
                    it.emails.withIndex().forEach {
                        inflate.addButton("Написать на емейл ${it.value}") {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:?to=${it.value}"))
                            startActivity(intent)
                        }
                    }
                    it.phoneNumbers.withIndex().forEach {
                        inflate.addButton("Позвонить на ${it.value}") {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${it.value}"))
                            startActivity(intent)
                        }
                        inflate.addButton("Написать SMS на ${it.value}") {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:${it.value}"))
                            startActivity(intent)
                        }
                    }
                    it.urLs.withIndex().forEach {
                        inflate.addButton("Перейти на ${it.value}") {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.value))
                            startActivity(intent)
                        }
                    }
                }
                is CalendarParsedResult -> { }
                is EmailAddressParsedResult -> inflate.addButton("Написать") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:?subject=${it.subject}&body=${it.body}&to=${it.tos.first()}"))
                    startActivity(intent)
                }
                is ExpandedProductParsedResult -> {}
                is GeoParsedResult -> inflate.addButton("Открыть на картах") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.geoURI))
                    intent.`package` = "com.google.android.apps.maps"
                    startActivity(intent)
                }
                is ISBNParsedResult -> {}
                is ProductParsedResult -> {}
                is SMSParsedResult -> inflate.addButton("Отправить") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.smsuri))
                    startActivity(intent)
                }
                is TelParsedResult -> inflate.addButton("Позвонить") {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse(it.telURI))
                    startActivity(intent)
                }
                is TextParsedResult -> {}
                is URIParsedResult -> inflate.addButton("Перейти") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.uri))
                    startActivity(intent)
                }
                is VINParsedResult -> {}
                is WifiParsedResult -> {
                    val manager = requireContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
                    inflate.addButton("Добавить в известные сети") {
                        val suggestion = WifiNetworkSuggestion.Builder()
                            .setSsid(it.ssid)
                            .setIsHiddenSsid(it.isHidden)
                        if (it.networkEncryption == "WPA") {
                            suggestion.setWpa2Passphrase(it.password)
                        }
                        manager.addNetworkSuggestions(listOf(suggestion.build()))
                    }
                }
            }
        }
        view.back.setOnClickListener {
            findNavController().navigate(R.id.startFragment)
        }
        return view
    }

    companion object {
        var info: ParsedResult? = null
    }
}

private fun LinearLayout.addText(txt: String) {
    val text = TextView(this.context)
    text.text = txt
    this.addView(text)
}

private fun LinearLayout.addButton(title: String, callback: () -> Unit) {
    val btn = Button(this.context)
    btn.text = title
    btn.setOnClickListener { callback() }
    addView(btn)
}

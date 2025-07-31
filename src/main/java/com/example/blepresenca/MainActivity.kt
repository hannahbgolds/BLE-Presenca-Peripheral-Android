package com.example.blepresenca

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.ParcelUuid
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var advertiser: BluetoothLeAdvertiser? = null

    private lateinit var statusText: TextView
    private lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa UI
        statusText = findViewById(R.id.statusText)
        startButton = findViewById(R.id.btnStartBle)

        // Inicializa Bluetooth
        bluetoothAdapter = (getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter

        // Solicita permissões se necessário
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
        }

        // Configura clique do botão
        startButton.setOnClickListener {
            startAdvertising("aluno1234")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Log.i("BLE", "✅ Permissões concedidas")
                statusText.text = "Permissões concedidas. Pronto para anunciar."
            } else {
                Log.e("BLE", "❌ Permissões negadas")
                statusText.text = "Permissões negadas. Ative nas configurações."
            }
        }
    }

    private fun startAdvertising(studentId: String) {
        if (!bluetoothAdapter.isEnabled) {
            Log.e("BLE", "Bluetooth desativado")
            statusText.text = "Ative o Bluetooth para anunciar presença."
            return
        }

        if (!bluetoothAdapter.isMultipleAdvertisementSupported) {
            Log.e("BLE", "BLE Advertising não suportado")
            statusText.text = "Dispositivo não suporta BLE Advertising."
            return
        }

        advertiser = bluetoothAdapter.bluetoothLeAdvertiser

        val uuid = UUID.nameUUIDFromBytes(studentId.toByteArray())
        val serviceUuid = ParcelUuid(uuid)

        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setConnectable(false)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .build()

        val data = AdvertiseData.Builder()
            .addServiceUuid(serviceUuid)
            .setIncludeDeviceName(true)
            .build()

        advertiser?.startAdvertising(settings, data, object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                super.onStartSuccess(settingsInEffect)
                Log.i("BLE", "✅ Anúncio iniciado")
                statusText.text = "Anunciando presença como \"$studentId\"..."
            }

            override fun onStartFailure(errorCode: Int) {
                super.onStartFailure(errorCode)
                Log.e("BLE", "❌ Falha ao anunciar: $errorCode")
                statusText.text = "Erro ao anunciar: código $errorCode"
            }
        })
    }
}

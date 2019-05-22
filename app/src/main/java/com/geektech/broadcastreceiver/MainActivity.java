package com.geektech.broadcastreceiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvWifi, tvBluetooth, tvCharge;
    private WifiManager wifiManager;
    private BluetoothManager bluetoothManager;
    private BatteryManager batteryManager;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWifi = findViewById(R.id.tvWiFiState);
        tvBluetooth = findViewById(R.id.tvBluetoothState);
        tvCharge = findViewById(R.id.tvChargeState);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        bluetoothManager = (BluetoothManager) getApplicationContext().getSystemService(Context.BLUETOOTH_SERVICE);
        batteryManager = (BatteryManager) getApplicationContext().getSystemService(Context.BATTERY_SERVICE);
        audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        registerAllReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerAllReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(wifiStateReceiver);
        unregisterReceiver(bluetoothStateReceiver);
        unregisterReceiver(batteryStateReceiver);
    }

    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            switch (wifiStateExtra) {
                case WifiManager.WIFI_STATE_ENABLED:
                    tvWifi.setText("WiFi is ON");
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    tvWifi.setText("WiFi is OFF");
                    break;
            }
        }
    };
    private BroadcastReceiver bluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF) {
                tvBluetooth.setText("Bluetooth is OFF");
            } else {
                tvBluetooth.setText("Bluetooth is ON");
            }
        }
    };
    private BroadcastReceiver batteryStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                tvCharge.setText("IS charging");
            } else {
                tvCharge.setText("NOT charging");
            }
        }
    };
    public void registerAllReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(wifiStateReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        registerReceiver(bluetoothStateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(batteryStateReceiver, intentFilter);
    }
}

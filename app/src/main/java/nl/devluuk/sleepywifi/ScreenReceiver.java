package nl.devluuk.sleepywifi;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;

public class ScreenReceiver extends BroadcastReceiver {

    boolean wifiWasOn = false;
    boolean bluetoothWasOn = false;
    boolean bluetoothState;
    boolean appState = false;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        bluetoothState = prefs.getBoolean(context.getResources().getString(R.string.bluetooth_state), false);
        appState = prefs.getBoolean(context.getResources().getString(R.string.app_state), false);

        PackageManager pm = context.getPackageManager();
        final boolean deviceHasBluetooth = pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);

        if (appState) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                if (wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                    wifiWasOn = true;
                } else {
                    wifiWasOn = false;
                }
                if (bluetoothState) {
                    if (deviceHasBluetooth) {
                        if (bluetoothAdapter.isEnabled()) {
                            bluetoothAdapter.disable();
                            bluetoothWasOn = true;
                        } else {
                            bluetoothWasOn = false;
                        }
                    }
                }
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                if (wifiWasOn) {
                    wifiManager.setWifiEnabled(true);
                }
                if (bluetoothState) {
                    if (deviceHasBluetooth) {
                        if (bluetoothWasOn) {
                            bluetoothAdapter.enable();
                        }
                    }
                }
            }
        }
    }

}



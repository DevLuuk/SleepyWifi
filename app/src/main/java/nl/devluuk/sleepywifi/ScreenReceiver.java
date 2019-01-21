package nl.devluuk.sleepywifi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {

    private WifiManager wifiManager;
    private BluetoothAdapter bluetoothAdapter;
    boolean wifiWasOn = false;
    boolean bluetoothWasOn = false;
    boolean bluetoothState;
    private static final String TAG = ScreenReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {

        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        bluetoothState = prefs.getBoolean(context.getResources().getString(R.string.bluetooth_state), false);
        //boolean wifiEnabled = wifiManager.isWifiEnabled();

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
                wifiWasOn = true;

            } else {
                wifiWasOn = false;
            }
            if (bluetoothState) {
                if (bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.disable();
                    Log.v(TAG, "Bluetooth is Sleeping");
                    bluetoothWasOn = true;
                } else {
                    bluetoothWasOn = false;
                }
            }
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            if (wifiWasOn) {
                wifiManager.setWifiEnabled(true);
            }
            if (bluetoothState) {
                if (bluetoothWasOn) {
                    bluetoothAdapter.enable();
                }
            }
        }
    }

}



package nl.devluuk.sleepywifi;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class ScreenReceiver extends BroadcastReceiver {

    WifiManager wifiManager;
    boolean wifiWasOn = false;
    boolean bluetoothWasOn = false;
    boolean bluetoothState;
    boolean appState = false;
    int delayTime;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        bluetoothState = prefs.getBoolean(context.getResources().getString(R.string.bluetooth_state), false);
        appState = prefs.getBoolean(context.getResources().getString(R.string.app_state), false);
        delayTime = prefs.getInt(context.getResources().getString(R.string.key_power_off_time), 1);

        PackageManager pm = context.getPackageManager();
        final boolean deviceHasBluetooth = pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);

        if (appState) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                if (wifiManager.isWifiEnabled()) {
                    new Sleep(this).execute();
                    wifiManager.setWifiEnabled(false);
                    wifiWasOn = true;
                } else {
                    wifiWasOn = false;
                }
                if (bluetoothState && deviceHasBluetooth) {
                    if (bluetoothAdapter.isEnabled()) {
                        bluetoothAdapter.disable();
                        bluetoothWasOn = true;
                    } else {
                        bluetoothWasOn = false;
                    }

                }
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                if (wifiWasOn) {
                    wifiManager.setWifiEnabled(true);
                }
                if (bluetoothState && deviceHasBluetooth) {
                    if (bluetoothWasOn) {
                        bluetoothAdapter.enable();
                    }
                }
            }
        }
    }
    private class Sleep extends AsyncTask<Void, Void, Void> {

        private WeakReference<ScreenReceiver> activityReference;

        // only retain a weak reference to the activity
        Sleep(ScreenReceiver context) {
            activityReference = new WeakReference<>(context);
        }

        protected Void doInBackground(Void... params) {
            try {
                Log.v(TAG, "Sleeping.......");
                TimeUnit.SECONDS.sleep(delayTime);
                wifiManager.setWifiEnabled(false);
                Log.v(TAG, "Wifi is offfff");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}



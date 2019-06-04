package nl.devluuk.sleepywifi;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class ScreenReceiver extends BroadcastReceiver {

    private WifiManager wifiManager;
    private BluetoothAdapter bluetoothAdapter;
    boolean wifiWasOn = false;
    boolean bluetoothWasOn = false;
    boolean bluetoothState;
    boolean appState;
    private static final String TAG = ScreenReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        bluetoothState = prefs.getBoolean(context.getResources().getString(R.string.bluetooth_state), false);
        appState = prefs.getBoolean(context.getResources().getString(R.string.app_state), true);

        PackageManager pm = context.getPackageManager();
        final boolean deviceHasBluetooth = pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH);

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                if (wifiManager.isWifiEnabled()) {
                    new Sleep(this).execute();
                    wifiWasOn = true;
                } else {
                    wifiWasOn = false;
                }
                if (bluetoothState) {
                    if (deviceHasBluetooth) {
                        if (bluetoothAdapter.isEnabled()) {
                            bluetoothAdapter.disable();
                            Log.v(TAG, "Bluetooth is Sleeping");
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
    private class Sleep extends AsyncTask<Void, Void, Void> {

        private WeakReference<ScreenReceiver> activityReference;

        // only retain a weak reference to the activity
        Sleep(ScreenReceiver context) {
            activityReference = new WeakReference<>(context);
        }

        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(5);
                wifiManager.setWifiEnabled(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}



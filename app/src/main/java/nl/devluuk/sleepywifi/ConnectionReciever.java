package nl.devluuk.sleepywifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.util.Log;

public class ConnectionReciever extends BroadcastReceiver {
    private WifiManager wifiManager;
    private static final String TAG = ConnectionReciever.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);


        boolean wifiEnabled = wifiManager.isWifiEnabled();
        Log.i(TAG, String.valueOf(wifiEnabled));

        if(wifiEnabled) {
            wifiManager.setWifiEnabled(false);
        }

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if(wifiEnabled) {
                wifiManager.setWifiEnabled(false);
            }
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

        }
    }
}

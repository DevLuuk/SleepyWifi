package nl.devluuk.sleepywifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import static android.app.Service.START_STICKY;

public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;
    private WifiManager wifiManager;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        boolean wifiEnabled = wifiManager.isWifiEnabled();

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if(wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
                wasScreenOn = false;
            }
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            wifiManager.setWifiEnabled(true);
            wasScreenOn = true;
        }
    }

}

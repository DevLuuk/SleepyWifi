package nl.devluuk.sleepywifi;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
        wifiManager.setWifiEnabled(false);

        boolean wifiEnabled = wifiManager.isWifiEnabled();

        if(wifiEnabled) {
            wifiManager.setWifiEnabled(false);
        }
    }
}

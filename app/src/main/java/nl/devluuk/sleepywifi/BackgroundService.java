package nl.devluuk.sleepywifi;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class BackgroundService extends Service {

    private static final String TAG = BackgroundService.class.getSimpleName();
    protected ScreenReceiver screenReciever;

    @Override
    public void onCreate() {
        super.onCreate();

        final IntentFilter filter = new IntentFilter();
        this.screenReciever = new ScreenReceiver();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(this.screenReciever, filter);
//            setPreference(true);
//            Log.i(TAG, "BackgroundService is on");

    }

    @Override
    public void onDestroy() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (screenReciever != null) {
            unregisterReceiver(screenReciever);
            screenReciever = null;
        }
        //setPreference(false);
        //Log.i(TAG, "BackgroundService is off");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
}

package nl.devluuk.sleepywifi;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class BackgroundService extends Service {

    protected ConnectionReciever connectionReciever;

    @Override
    public void onCreate() {
        super.onCreate();
        this.connectionReciever = new ConnectionReciever();
        IntentFilter screenStateFilter = new IntentFilter();
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(connectionReciever, screenStateFilter);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(connectionReciever);
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

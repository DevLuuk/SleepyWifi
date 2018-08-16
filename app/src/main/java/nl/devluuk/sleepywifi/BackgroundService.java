package nl.devluuk.sleepywifi;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service {

    private static final String TAG = BackgroundService.class.getSimpleName();
    protected ScreenReceiver screenReciever;

    @Override
    public void onCreate() {
        super.onCreate();

        this.screenReciever = new ScreenReceiver();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(this.screenReciever, filter);
        Log.i(TAG, "Backgroundservice is on");
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(screenReciever);
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

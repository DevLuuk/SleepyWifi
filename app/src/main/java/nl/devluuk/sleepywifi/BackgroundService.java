package nl.devluuk.sleepywifi;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service {

    private static final String TAG = BackgroundService.class.getSimpleName();
    protected ConnectionReciever connectionReciever;
    protected ScreenReceiver screenReciever;

    @Override
    public void onCreate() {
        super.onCreate();

//        IntentFilter screenStateFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        this.screenReciever = new ScreenReceiver();
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
//        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(this.screenReciever, filter);
        Log.i(TAG, "Backgroundservice is on");
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

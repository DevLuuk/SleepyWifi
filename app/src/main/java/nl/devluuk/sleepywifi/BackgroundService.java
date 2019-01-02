package nl.devluuk.sleepywifi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class BackgroundService extends Service {

    private static final String TAG = BackgroundService.class.getSimpleName();
    protected ScreenReceiver screenReciever;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "background_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "SleepyWifi",
                    NotificationManager.IMPORTANCE_MIN);
            channel.setSound(null, null);
            channel.enableVibration(false);
            channel.setShowBadge(false);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("SleepyWifi background service")
                    .setAutoCancel(true)
                    .setContentText("").build();

            startForeground(1, notification);
        }


        final IntentFilter filter = new IntentFilter();
        this.screenReciever = new ScreenReceiver();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(this.screenReciever, filter);
        new DismissNotification().execute();
        // remove start notification
        //stopForeground(false);
    }

    private class DismissNotification extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(5);
                //Thread.sleep(5000);
                stopForeground(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Void... progress) {

        }

        protected void onPostExecute(Void result) {

        }
    }


    @Override
    public void onDestroy() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (screenReciever != null) {
            unregisterReceiver(screenReciever);
            screenReciever = null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        } else {
            stopSelf();
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

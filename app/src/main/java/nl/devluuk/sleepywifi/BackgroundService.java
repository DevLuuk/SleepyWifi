package nl.devluuk.sleepywifi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class BackgroundService extends Service {

    protected ScreenReceiver screenReceiver;

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
        this.screenReceiver = new ScreenReceiver();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(this.screenReceiver, filter);
        new DismissNotification(this).execute();
    }

    private class DismissNotification extends AsyncTask<Void, Void, Void> {

        private WeakReference<BackgroundService> activityReference;

        // only retain a weak reference to the activity
        DismissNotification(BackgroundService context) {
            activityReference = new WeakReference<>(context);
        }

        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(5);
                stopForeground(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    public void onDestroy() {
        if (screenReceiver != null) {
            unregisterReceiver(screenReceiver);
            screenReceiver = null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        } else {
            stopSelf();
        }
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

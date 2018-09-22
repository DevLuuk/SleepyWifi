package nl.devluuk.sleepywifi;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class OnStart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            context.startService(new Intent(context, BackgroundService.class));

        }
//        Intent serviceIntent = new Intent(context, BackgroundService.class);
//        ContextCompat.startForegroundService(context, serviceIntent );

    }
}

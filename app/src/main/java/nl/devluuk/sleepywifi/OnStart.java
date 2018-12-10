package nl.devluuk.sleepywifi;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class OnStart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, BackgroundService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                context.startForegroundService(i);
            } else {
                context.startService(i);
            }
        }
    }
}

package nl.devluuk.sleepywifi;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

public class OnStart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, BackgroundService.class));
        //if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //context.startService(new Intent(context, BackgroundService.class));
//            Intent i = new Intent(context, BackgroundService.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i);

        }

    }
//}

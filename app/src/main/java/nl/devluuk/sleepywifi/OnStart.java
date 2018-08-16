package nl.devluuk.sleepywifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class OnStart extends BroadcastReceiver {

    private static final String TAG = OnStart.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, BackgroundService.class));
        Log.i(TAG, "AutoStart is on");
    }
}

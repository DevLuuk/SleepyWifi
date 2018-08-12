package nl.devluuk.sleepywifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnStart extends BroadcastReceiver {
    @Override
    public void onReceive (Context context, Intent intent) {
        context.startService(new Intent(context, MainActivity.class));

    }
}

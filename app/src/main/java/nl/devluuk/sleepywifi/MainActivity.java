package nl.devluuk.sleepywifi;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public String state;
    public Boolean status;
    public Drawable playIcon;
    public Drawable grayIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageButton = findViewById(R.id.powerButton);
        getStatus();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playOrPauseService(imageButton);
            }
        });

        startService(new Intent(this, BackgroundService.class));

    }

    public void playOrPauseService(ImageView image) {
        final TextView stateText = findViewById(R.id.OnOffText);
        if (getStatus()) {
            stopService(new Intent(this, BackgroundService.class));

            grayIcon = getResources().getDrawable(R.drawable.ic_launcher_round_gray, null);
            makeGrayIcon(grayIcon);
            image.setImageDrawable(grayIcon);

            stateText.setText("Current state is: " + setState());
        } else {
            startService(new Intent(this, BackgroundService.class));

            playIcon = getResources().getDrawable(R.drawable.ic_launcher_round, null);

            image.setImageDrawable(playIcon);

            stateText.setText("Current state is: " + setState());
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private Drawable makeGrayIcon(Drawable icon){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        icon.setColorFilter(filter);
        return icon;
    }

    private boolean getStatus() {
        status = isMyServiceRunning(BackgroundService.class);
        return status;
    }

    private String setState() {
        if (status) {
            state = "OFF";
        } else {
            state = "ON";
        }
        return state;
    }

}

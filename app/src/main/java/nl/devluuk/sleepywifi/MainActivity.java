package nl.devluuk.sleepywifi;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


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
        checkPrefOnStart(imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playOrPauseService(imageButton);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        status = prefs.getBoolean("app_state", true);
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        PreferenceManager.getDefaultSharedPreferences(this)
//                .unregisterOnSharedPreferenceChangeListener(this);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();
        if (menuItemSelected == R.id.action_about) {
            Intent aboutActivity = new Intent(this, AboutActivity.class);
            startActivity(aboutActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void playOrPauseService(ImageView image) {
        final TextView stateText = findViewById(R.id.OnOffText);
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //prefs.registerOnSharedPreferenceChangeListener(this);

        // Check the current state inside the sharedprefs
        if (checkPrefStatus()) {
            stopService(new Intent(this, BackgroundService.class));

            setPreference(false);

            grayIcon = getResources().getDrawable(R.drawable.ic_launcher_round_gray, null);
            makeGrayIcon(grayIcon, 0);
            image.setImageDrawable(grayIcon);
            // Update current state text
            setStateText(stateText);

        } else {
            startService(new Intent(this, BackgroundService.class));

            setPreference(true);

            playIcon = getResources().getDrawable(R.drawable.ic_launcher_round, null);

            image.setImageDrawable(playIcon);
            // Update current state text
            setStateText(stateText);
        }
    }

    public void checkPrefOnStart(ImageView image) {
        final TextView stateText = findViewById(R.id.OnOffText);

        if (checkPrefStatus()) {
            startService(new Intent(this, BackgroundService.class));
            //setPreference(true);
            //Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
            playIcon = getResources().getDrawable(R.drawable.ic_launcher_round, null);

            image.setImageDrawable(playIcon);
            Log.i(TAG, "service is started");
            // Update current state text
            setStateText(stateText);
        } else {
            grayIcon = getResources().getDrawable(R.drawable.ic_launcher_round_gray, null);
            makeGrayIcon(grayIcon, 0);
            image.setImageDrawable(grayIcon);
            setStateText(stateText);
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

    public boolean getBackgroundStatus() {
        return status = isMyServiceRunning(BackgroundService.class);
    }

    public boolean checkPrefStatus() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return status = prefs.getBoolean("app_state", true);
    }

    public void setPreference(boolean status) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putBoolean("app_state", status);
        editor.apply();
    }

    private void setStateText(TextView stateText) {
        if (checkPrefStatus()) {
            state = "ON";
        } else {
            state = "OFF";
        }
        stateText.setText("The app service is: " + state);
    }

//    private void setStateText(TextView stateText) {
//        if (checkPrefStatus()) {
//            state = "OFF";
//        } else {
//            state = "ON";
//        }
//        stateText.setText("Current state is: " + state);
//    }

    private void setStateInToast() {
        if (checkPrefStatus()) {
            Toast.makeText(this, "Value is true", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Value is false", Toast.LENGTH_SHORT).show();
        }

    }

    private Drawable makeGrayIcon(Drawable icon, int colorID) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(colorID);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        icon.setColorFilter(filter);
        return icon;
    }

//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if (key.equals("app_state")) {
//            sharedPreferences.getBoolean(key, true);
//        }
//    }
}

package nl.devluuk.sleepywifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public String state;
    public Boolean status;
    public Drawable playIcon;
    public Drawable grayIcon;
    private PowerManager powerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView imageButton = findViewById(R.id.powerButton);
        powerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
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
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        status = prefs.getBoolean(getResources().getString(R.string.app_state), false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();
        if (menuItemSelected == R.id.action_settings) {
            Intent settingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(settingsActivity);
            return true;
        }
        if (menuItemSelected == R.id.action_about) {
            Intent aboutActivity = new Intent(this, AboutActivity.class);
            startActivity(aboutActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void playOrPauseService(ImageView image) {
        final TextView stateText = findViewById(R.id.OnOffText);
        final TextView stateDesc = findViewById(R.id.OnOffDescription);
        // Check the current state inside the sharedprefs
        if (checkPrefStatus(getResources().getString(R.string.app_state))) {
            stopService(new Intent(this, BackgroundService.class));
            setPreference(false);

            grayIcon = getResources().getDrawable(R.drawable.ic_launcher_round_gray, null);
            makeGrayIcon(grayIcon, 0);
            image.setImageDrawable(grayIcon);
            // Update current state text
            setStateText(stateText, stateDesc);

        } else {
            startForegroundService(new Intent(this, BackgroundService.class));
            setPreference(true);

            playIcon = getResources().getDrawable(R.drawable.ic_launcher_round, null);

            image.setImageDrawable(playIcon);
            // Update current state text
            setStateText(stateText, stateDesc);
        }
    }

    private void checkIgnoringBattery() {
        String packageName = this.getPackageName();
        PowerManager pm = getSystemService(PowerManager.class);

        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            Intent i =
                    new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                            .setData(Uri.parse("package:" + packageName));

            startActivity(i);
        }

    }

    public void checkPrefOnStart(ImageView image) {
        final TextView stateText = findViewById(R.id.OnOffText);
        final TextView stateDesc = findViewById(R.id.OnOffDescription);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        if (checkPrefStatus(getResources().getString(R.string.app_state))) {
            checkIgnoringBattery();
            startForegroundService(new Intent(this, BackgroundService.class));
            playIcon = getResources().getDrawable(R.drawable.ic_launcher_round, null);

            image.setImageDrawable(playIcon);
            Log.i(TAG, "service is started");
            // Update current state text
            setStateText(stateText, stateDesc);
        } else {
            grayIcon = getResources().getDrawable(R.drawable.ic_launcher_round_gray, null);
            makeGrayIcon(grayIcon, 0);
            image.setImageDrawable(grayIcon);
            setStateText(stateText, stateDesc);
        }

    }

    private void showAlertDialog(Context context) {
        final Intent batteryIntent = new Intent();
        AlertDialog.Builder batteryDialog = new AlertDialog.Builder(context);
        batteryDialog.setTitle(R.string.request_battery_opt);
        batteryDialog.setMessage(R.string.warning_battery_opt);
        batteryDialog.setCancelable(true);

        batteryDialog.setPositiveButton(
                R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        batteryIntent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                        startActivity(batteryIntent);
                    }
                });

        batteryDialog.setNegativeButton(
                R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = batteryDialog.create();
        alert.show();
    }

    public boolean checkPrefStatus(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return status = prefs.getBoolean(key, false);
    }

    public void setPreference(boolean status) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putBoolean(getResources().getString(R.string.app_state), status);
        editor.apply();
    }

    private void setStateText(TextView stateText, TextView stateDesc) {
        String stateDescription;
        if (checkPrefStatus(getResources().getString(R.string.app_state))) {
            state = getResources().getString(R.string.on);
            stateDescription = getResources().getString(R.string.off);
        } else {
            state = getResources().getString(R.string.off);
            stateDescription = getResources().getString(R.string.on);
        }
        stateText.setText(getResources().getString(R.string.app_state_title) + " " + state);
        stateDesc.setText(getResources().getString(R.string.app_state_desc1) + " " + stateDescription + " " + getResources().getString(R.string.app_state_desc2));
    }


    private Drawable makeGrayIcon(Drawable icon, int colorID) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(colorID);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        icon.setColorFilter(filter);
        return icon;
    }

    /**
     * Called when a shared preference is changed, added, or removed. This
     * may be called even if a preference is set to its existing value.
     *
     * <p>This callback will be run on your main thread.
     *
     * @param sharedPreferences The {@link SharedPreferences} that received
     *                          the change.
     * @param key               The key of the preference that was changed, added, or
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.bluetooth_state))) {
            sharedPreferences.getBoolean(key, false);
        }
        if (key.equals(getString(R.string.app_state))) {
            sharedPreferences.getBoolean(key, false);
        }
    }
}

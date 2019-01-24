package nl.devluuk.sleepywifi;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.view.MenuItem;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = this.getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);

    }
        // TODO Fully implement onPreferenceListener
//    public static void bluetoothListener(SwitchPreference bluetoothSwitch){
//        if (bluetoothSwitch != null) {
//            bluetoothSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference arg0, Object isBluetoothOnObject) {
//                    boolean isBluetoothOn = (Boolean) isBluetoothOnObject;
//                    if (isBluetoothOn) {
//                        requestBluetoothPermission();
//                    }
//                    return true;
//                }
//            });
//        }
//    }

    private void requestBluetoothPermission(){
        Dexter.withActivity(this).withPermissions(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()){
                    Toast.makeText(getApplicationContext(), "All permissions are granted", Toast.LENGTH_SHORT).show();
                }

                if (report.isAnyPermissionPermanentlyDenied()){
                    Toast.makeText(getApplicationContext(), "Not all permissions are granted", Toast.LENGTH_SHORT).show();
                    showWarningDialog();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toast.makeText(getApplicationContext(), "Error occurred ", Toast.LENGTH_LONG).show();
            }
        }).onSameThread().check();
    }

    private void showWarningDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app need permissions to use this feature.");
        builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


}

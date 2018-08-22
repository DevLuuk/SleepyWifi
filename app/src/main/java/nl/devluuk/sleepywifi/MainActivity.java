package nl.devluuk.sleepywifi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public int state;
    public String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TextView stateText = findViewById(R.id.OnOffText);
        Button startButton = (Button) findViewById(R.id.OnOffButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateText.setText(state);
            }
        });

        startService(new Intent(this, BackgroundService.class));

    }

}

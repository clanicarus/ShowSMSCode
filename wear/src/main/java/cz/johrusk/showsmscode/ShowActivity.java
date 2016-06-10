package cz.johrusk.showsmscode;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import timber.log.Timber;

/**
 * Activity which  code and sender of received SMS.
 *
 * @author Josef Hruska (pepa.hruska@gmail.com)
 */

public class ShowActivity extends Activity {

    private TextView tv_code;
    private TextView tv_sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] vibrationPattern = {0, 500, 50, 300};
        //-1 - don't repeat
        final int indexInPatternToRepeat = -1;
        vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);

        Timber.d("ShowActivity");
        final String sArray = getIntent().getStringExtra("code");
        final String[] parts = sArray.split("/");

        Timber.d("Code / Sender: " + parts[0] + " / " + parts[1]);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                tv_code = (TextView) stub.findViewById(R.id.text);
                tv_code.setText(parts[0]);
                tv_sender = (TextView) stub.findViewById(R.id.sender);
                tv_sender.setText(parts[1]);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("OnResume");
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Timber.d("OnRestart");
    }
}
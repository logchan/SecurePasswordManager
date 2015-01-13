package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.hashingscheme.HashingScheme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class InitActivity extends Activity {

    private ApplicationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        // set manager
        manager = ApplicationManager.getInstanceSafe(getApplicationContext());

        // load settings
        manager.loadAppSettings();

        manager.reloadAllSchemes();

        // prepare going to main
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                gotoMain();
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void gotoMain() {
        manager.switchActivity(this, MainActivity.class, ApplicationState.MAIN);
        finish();
    }
}

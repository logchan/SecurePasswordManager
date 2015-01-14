package hk.ust.ustac.team8.securepasswordmanager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class InitActivity extends Activity {

    private final static Integer delay = 2000;

    private ApplicationManager manager;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        // set image
        imageView = (ImageView) findViewById(R.id.initImage);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.drawable.spmlogo_72);

        // set manager
        manager = ApplicationManager.getInstanceSafe(getApplicationContext());

        // load settings
        manager.loadAppSettings();

        // prepare going to main
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                gotoMain();
            }
        };
        handler.postDelayed(runnable, delay);
    }

    private void gotoMain() {
        manager.switchActivity(this, MainActivity.class, ApplicationState.MAIN);
        finish();
    }
}

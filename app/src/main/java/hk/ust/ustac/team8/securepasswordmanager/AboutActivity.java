package hk.ust.ustac.team8.securepasswordmanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


public class AboutActivity extends Activity {

    private ApplicationManager manager;

    private ImageView imageView;

    private TextView contentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // set manager
        manager = ApplicationManager.getInstanceSafe(getApplicationContext());

        // set reference
        imageView = (ImageView) findViewById(R.id.aboutImage);
        contentText = (TextView) findViewById(R.id.aboutContent);

        // set image
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(R.drawable.spmlogo_72);
    }

    @Override
    public void onBackPressed() {
        manager.popState(null);
        finish();
    }
}

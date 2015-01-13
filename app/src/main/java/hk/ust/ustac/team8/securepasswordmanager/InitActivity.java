package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.hashingscheme.HashingScheme;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.LinkedList;

public class InitActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        // setup the ApplicationManager
        try {
            ApplicationManager.setUp(getApplicationContext());
        }
        catch (InstantiationException ex) {

        }

        // load schemes
        ApplicationManager manager = ApplicationManager.getInstance();
        manager.reloadAllSchemes();

        LinkedList<HashingScheme> schemes = manager.getAllSchemes();
        TextView view = (TextView) findViewById(R.id.loadingInfo);
        view.setText("Size: " + schemes.size());
    }

}

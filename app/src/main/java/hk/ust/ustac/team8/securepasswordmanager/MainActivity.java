package hk.ust.ustac.team8.securepasswordmanager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements Button.OnClickListener, MenuItem.OnMenuItemClickListener {

    private ApplicationManager manager;

    private Button genPwdBtn;

    private Button manScheBtn;

    private Button manInfoBtn;

    private MenuItem settingBtn;

    private MenuItem aboutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set manager
        manager = ApplicationManager.getInstanceSafe(getApplicationContext());

        // set reference
        genPwdBtn = (Button) findViewById(R.id.mainGenPwdBtn);
        manScheBtn = (Button) findViewById(R.id.mainManScheBtn);
        manInfoBtn = (Button) findViewById(R.id.mainManInfoBtn);
        settingBtn = (MenuItem) findViewById(R.id.action_settings);
        aboutBtn = (MenuItem) findViewById(R.id.action_about);

        // set listener
        genPwdBtn.setOnClickListener(this);
        manScheBtn.setOnClickListener(this);
        manInfoBtn.setOnClickListener(this);
        settingBtn.setOnMenuItemClickListener(this);
        aboutBtn.setOnMenuItemClickListener(this);
    }

    /*** Menu Starts ***/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /*** Menu Ends ***/

    @Override
    public void onClick(View view) {
        ApplicationState nextState = ApplicationState.MAIN;

        switch (view.getId()) {
            case R.id.mainGenPwdBtn:
                nextState = ApplicationState.SELECT_SCHEME_FOR_PASSWORD_GEN;
                break;
            case R.id.mainManScheBtn:
                nextState = ApplicationState.SELECT_SCHEME_FOR_EDIT;
                break;
            case R.id.mainManInfoBtn:
                nextState = ApplicationState.SELECT_SCHEME_FOR_LIST_INFO;
                break;
        }

        manager.switchActivity(this, SchemeListActivity.class, nextState);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_settings:
                manager.switchActivity(this, SettingActivity.class, ApplicationState.SETTING);
                break;
            case R.id.action_about:
                // TODO: navigate to about page
                break;
            default:
                return false;
        }
        return true;
    }
}

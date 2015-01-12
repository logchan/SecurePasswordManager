package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.hashingscheme.HashingScheme;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class SchemeListActivity extends Activity {

    private ApplicationManager manager;

    private SimpleAdapter adapter;

    private ArrayList<HashMap<String,String>> schemeList = new ArrayList<HashMap<String,String>>();

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_list);

        // set manager
        if (!ApplicationManager.alreadySetUp()) {
            try {
                ApplicationManager.setUp(getApplicationContext());
            }
            catch (Exception e) {
                // omit
            }
        }
        manager = ApplicationManager.getInstance();

        // set references
        listView = (ListView) findViewById(R.id.schemeList);

        // get items
        manager.reloadAllSchemes();
        LinkedList<HashingScheme> schemes = manager.getAllSchemes();
        for (HashingScheme scheme : schemes) {
            HashMap<String,String> item = new HashMap<String,String>();
            item.put("name", scheme.getName());
            item.put("description", scheme.getDescription());
            schemeList.add(item);
        }

        adapter = new SimpleAdapter(this, schemeList, android.R.layout.simple_list_item_2,
                new String[] { "name", "description"}, new int[] { android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scheme_list, menu);
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
}

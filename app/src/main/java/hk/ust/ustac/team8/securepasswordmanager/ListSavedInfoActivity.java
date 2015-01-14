package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.applicationutility.AppFileUtility;
import hk.ust.ustac.team8.generalutility.AndroidUtility;
import hk.ust.ustac.team8.hashingscheme.HashingScheme;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


public class ListSavedInfoActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ApplicationManager manager;

    private SimpleAdapter adapter;

    private ArrayList<HashMap<String,String>> infoList = new ArrayList<HashMap<String,String>>();

    private HashingScheme scheme;

    private ListView listView;

    private TextView subTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_saved_info);

        // set manager
        manager = ApplicationManager.getInstanceSafe(getApplicationContext());

        // set references
        listView = (ListView) findViewById(R.id.savedInfoList);
        subTitleText = (TextView) findViewById(R.id.savedInfoSubTitle);

        // set listeners
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        // get data and set
        setSchemeByState();

        setSubTitleText();

        reloadListView();
    }

    private void setSchemeByState() {
        if (manager.getSettings().currentState == ApplicationState.LIST_INFO) {
            if (manager.getSettings().carriedInfo == null) {
                manager.popState(null);
                AndroidUtility.activityExceptionExit(this, "Null carried info for LIST_INFO");
            }

            Object obj = manager.getSettings().carriedInfo[0];
            if (obj.getClass() != String.class) {
                manager.popState(null);
                AndroidUtility.activityExceptionExit(this, "Non-string for LIST_INFO");
            }

            String schemeName = (String)obj;
            HashingScheme tScheme = manager.getSchemeByName(schemeName);
            if (tScheme == null) {
                manager.popState(null);
                AndroidUtility.activityExceptionExit(this, "Scheme not exist for LIST_INFO");
            }

            scheme = tScheme;
        }
        else {
            manager.popState(null);
            AndroidUtility.activityExceptionExit(this, "Invalid state for ListSavedInfo");
        }
    }

    private void reloadListView() {

        LinkedList<String> infos = new LinkedList<String>();
        try {
            infos = AppFileUtility.getAllSavedInfoOfScheme(getApplicationContext(), scheme.getName());
        }
        catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.failed_loading_infolist) ,
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        infoList.clear();

        for (String info : infos) {
            HashMap<String, String> item = new HashMap<String, String>();
            item.put("name", info);
            item.put("description", "");
            infoList.add(item);
        }

        adapter = new SimpleAdapter(this, infoList, android.R.layout.simple_list_item_2,
                new String[] { "name", "description"}, new int[] { android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }

    private void setSubTitleText() {
        String text = getString(R.string.subtitle_activity_list_saved_info);
        text = text.replace("%1", scheme.getName());

        subTitleText.setText(text);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        HashMap<String, String> item = (HashMap<String, String>) adapter.getItem(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        HashMap<String, String> item = (HashMap<String, String>) adapter.getItem(position);
        return true;
    }

    @Override
    public void onBackPressed() {
        manager.popState(null);
        finish();
    }
}

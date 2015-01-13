package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.applicationutility.AppFileUtility;
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

        // TODO: remove debug code
        manager.reloadAllSchemes();
        manager.getSettings().currentScheme = manager.getAllSchemes().getFirst();

        // get data and set
        scheme = manager.getSettings().currentScheme;

        setSubTitleText();

        LinkedList<String> infos = new LinkedList<String>();
        try {
            infos = AppFileUtility.getAllSavedInfoOfScheme(getApplicationContext(), scheme.getName());
        }
        catch (Exception e) {
            //TODO: notify failure
        }

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
        String text = "Scheme: ";
        text += scheme.getName();
        text += '\n';
        text += "Tap to view, hold to delete";

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
        //TODO: implement back
    }
}

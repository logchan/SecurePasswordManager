package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.generalutility.AndroidUtility;
import hk.ust.ustac.team8.hashingscheme.HashingScheme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class SchemeListActivity extends Activity implements Button.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ApplicationManager manager;

    private SimpleAdapter adapter;

    private ArrayList<HashMap<String,String>> schemeList = new ArrayList<HashMap<String,String>>();

    private ListView listView;

    private Button backBtn;

    private Button newBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_list);

        // set manager
        manager = ApplicationManager.getInstanceSafe(getApplicationContext());

        // set references
        listView = (ListView) findViewById(R.id.schemeList);
        backBtn = (Button) findViewById(R.id.schemeListBackBtn);
        newBtn = (Button) findViewById(R.id.schemeListNewSchemeBtn);

        // set listeners
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        backBtn.setOnClickListener(this);
        newBtn.setOnClickListener(this);

        // get items and set adapter
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.schemeListBackBtn:
                //TODO: implement back
                break;
            case R.id.schemeListNewSchemeBtn:
                //TODO: implement new
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        HashMap<String, String> item = (HashMap<String, String>) listView.getAdapter().getItem(position);
        //TODO: implement scheme chosen
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        HashMap<String, String> item = (HashMap<String, String>) listView.getAdapter().getItem(position);
        //TODO: implement scheme edit/delete
        return true;
    }
}

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
        reloadAllItems();
    }

    private void reloadAllItems() {
        schemeList.clear();

        LinkedList<HashingScheme> schemes = manager.getAllSchemes();
        for (HashingScheme scheme : schemes) {
            HashMap<String,String> item = new HashMap<String,String>();
            item.put("name", scheme.getName());
            item.put("description", scheme.getDescription());
            schemeList.add(item);
        }

        adapter = new SimpleAdapter(this, schemeList, android.R.layout.simple_list_item_2,
                new String[] { "name", "description" }, new int[] { android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.schemeListBackBtn:
                onBackPressed();
                break;
            case R.id.schemeListNewSchemeBtn:
                manager.switchActivity(this, SchemeEditActivity.class, ApplicationState.EDIT_SCHEME_NEW);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        HashMap<String, String> item = (HashMap<String, String>) listView.getAdapter().getItem(position);
        switch (manager.getSettings().currentState) {
            case SELECT_SCHEME_FOR_EDIT:
                manager.switchActivity(this, SchemeEditActivity.class, ApplicationState.EDIT_SCHEME_CURRENT,
                        item.get("name"));
                break;
            case SELECT_SCHEME_FOR_PASSWORD_GEN:
                manager.switchActivity(this, InformationInputActivity.class, ApplicationState.INPUT_INFO_FOR_PASSWORD_GEN,
                        item.get("name"));
                break;
            case SELECT_SCHEME_FOR_LIST_INFO:
                manager.switchActivity(this, ListSavedInfoActivity.class, ApplicationState.LIST_INFO,
                        item.get("name"));
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        final String[] options =  new String[] { getString(R.string.edit_this_scheme),
                getString(R.string.delete_this_scheme),
        };

        final HashMap<String, String> item = (HashMap<String, String>) listView.getAdapter().getItem(position);
        final Activity activity = this;

        // create a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(item.get("name"));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String selected = options[which];
                if (selected.equals(getString(R.string.edit_this_scheme))) {
                    manager.switchActivity(activity , SchemeEditActivity.class, ApplicationState.EDIT_SCHEME_CURRENT,
                            item.get("name"));
                }
                else if (selected.equals(getString(R.string.delete_this_scheme))) {
                    promptDeleteScheme(item.get("name"));
                }
            }
        });
        builder.show();

        return true;
    }

    private void promptDeleteScheme(final String schemeName) {
        AlertDialog dialog = AndroidUtility.createSimpleAlertDialog(this, "Delete \"" + schemeName + "\"",
                getString(R.string.are_you_sure), getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // yes
                        deleteScheme(schemeName);
                    }
                }, getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // no
                    }
                });
        dialog.show();
    }

    private void deleteScheme(String schemeName) {
        manager.deleteSchemeByName(schemeName);
        reloadAllItems();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (manager.getSettings().lastState == ApplicationState.EDIT_SCHEME_CURRENT ||
                manager.getSettings().lastState == ApplicationState.EDIT_SCHEME_NEW) {
            manager.reloadAllSchemes();
            reloadAllItems();
        }
    }
}

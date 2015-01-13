package hk.ust.ustac.team8.securepasswordmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hk.ust.ustac.team8.generalutility.AndroidUtility;
import hk.ust.ustac.team8.hashingscheme.HashingScheme;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeCrypto;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeField;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeFieldType;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeTransform;

public class InformationInputActivity extends Activity implements Button.OnClickListener,
        AdapterView.OnItemClickListener, AndroidUtility.PromptOneInputReceiver {

    private HashingScheme scheme;

    private ApplicationManager manager;

    private SimpleAdapter adapter;

    private ArrayList<HashMap<String,String>> fieldList = new ArrayList<HashMap<String,String>>();

    private ListView listView;

    private Button procBtn;

    private Button loadBtn;

    private Button saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_input);

        // set manager
        manager = ApplicationManager.getInstanceSafe(getApplicationContext());

        // set reference
        listView = (ListView) findViewById(R.id.infoFillList);
        procBtn = (Button) findViewById(R.id.infoFillProceedBtn);
        loadBtn = (Button) findViewById(R.id.infoFillLoadBtn);
        saveBtn = (Button) findViewById(R.id.infoFillSaveBtn);

        // set listener
        listView.setOnItemClickListener(this);
        procBtn.setOnClickListener(this);
        loadBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        // get fields
        setSchemeByState();

        initListView(false);
    }

    private void setSchemeByState() {
        if (manager.getSettings().currentState != ApplicationState.INPUT_INFO_FOR_PASSWORD_GEN) {
            AndroidUtility.activityExceptionExit(this, "Invalid state for INPUT_INFO");
        }

        if (manager.getSettings().carriedInfo == null || manager.getSettings().carriedInfo.length == 0) {
            AndroidUtility.activityExceptionExit(this, "No carried info for INPUT_INFO");
        }

        Object obj = manager.getSettings().carriedInfo[0];
        if (obj.getClass() != String.class) {
            AndroidUtility.activityExceptionExit(this, "Non-string carried for INPUT_INFO");
        }

        String name = (String) obj;
        scheme = manager.getSchemeByName(name);
        if (scheme == null) {
            AndroidUtility.activityExceptionExit(this, "Scheme for INPUT_INFO not found");
        }

        int fieldCount = scheme.getFieldCount();
        for (int i = 0; i < fieldCount; ++i) {
            HashingSchemeField field = scheme.getField(i);
            field.setValue("");
        }
    }

    private void initListView(boolean showValues) {
        fieldList.clear();

        int fieldCount = scheme.getFieldCount();
        for (int i = 0; i < fieldCount; ++i) {
            HashingSchemeField field = scheme.getField(i);
            HashMap<String, String> item= new HashMap<String, String>();
            item.put("name", field.getName());

            String desc = field.getDescription();
            if (showValues && field.getValue().length() > 0) {
                desc = "Filled: " + field.getValue() + '\n' + desc;
            }

            item.put("description", desc);
            fieldList.add(item);
        }

        adapter = new SimpleAdapter(this, fieldList, android.R.layout.simple_list_item_2,
                new String[] { "name", "description"}, new int[] { android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        manager.popState(null);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.infoFillLoadBtn:
                //TODO: implement load
                break;
            case R.id.infoFillSaveBtn:
                //TODO: implement save
                break;
            case R.id.infoFillProceedBtn:
                //TODO: implement proceed
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        HashingSchemeField field = scheme.getField(position);
        AndroidUtility.promptForOneInput(this, field.getName(), field.getValue(), getString(R.string.field_hint),
                this, position);
    }

    @Override
    public void receivePromptOneInput(String output, Object... extraInfo) {
        if (output == null || extraInfo == null || extraInfo.length == 0) {
            return;
        }

        Object obj = extraInfo[0];
        if (obj.getClass() != int.class && obj.getClass() != Integer.class) {
            return;
        }

        int index = (Integer) obj;
        HashingSchemeField field = scheme.getField(index);
        field.setValue(output);
        initListView(true);
    }
}

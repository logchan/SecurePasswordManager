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

public class InformationInputActivity extends Activity implements Button.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    // TODO: remove this after debug
    private HashingScheme generateSampleScheme() {
        HashingScheme scheme = new HashingScheme("The scheme", "the desc", HashingSchemeCrypto.MD5, HashingSchemeTransform.NO_TRANSFORM);
        scheme.addField(new HashingSchemeField(HashingSchemeFieldType.STRING, "field1", "desc1"));
        scheme.addField(new HashingSchemeField(HashingSchemeFieldType.STRING, "field2", "desc2"));
        return scheme;
    }

    private HashingScheme scheme;

    private ApplicationManager manager;

    private SimpleAdapter adapter;

    private ArrayList<HashMap<String,String>> fieldList = new ArrayList<HashMap<String,String>>();

    private ListView listView;

    private Button backBtn;

    private Button procBtn;

    private Button loadBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_input);

        // set manager
        manager = ApplicationManager.getInstanceSafe(getApplicationContext());

        // set reference
        listView = (ListView) findViewById(R.id.infoFillList);
        backBtn = (Button) findViewById(R.id.infoFillBackBtn);
        procBtn = (Button) findViewById(R.id.infoFillProceedBtn);
        loadBtn = (Button) findViewById(R.id.infoFillLoadBtn);

        // set listener
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        backBtn.setOnClickListener(this);
        procBtn.setOnClickListener(this);
        loadBtn.setOnClickListener(this);

        // get fields
        scheme = generateSampleScheme();
        int fieldCount = scheme.getFieldCount();
        for (int i = 0; i < fieldCount; ++i) {
            HashingSchemeField field = scheme.getField(i);
            HashMap<String, String> item= new HashMap<String, String>();
            item.put("Name",field.getName());
            item.put("Description",field.getDescription());
            fieldList.add(item);
        }
        adapter = new SimpleAdapter(this, fieldList, android.R.layout.simple_list_item_2,
                new String[] { "Name", "Description"}, new int[] { android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.infoFillBackBtn:
                //TODO: implement back
                break;
            case R.id.infoFillLoadBtn:
                //TODO: implement load
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
        HashMap<String, String> item = (HashMap<String, String>) adapter.getItem(position);
        //TODO: implement field chosen
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        HashMap<String, String> item = (HashMap<String, String>) adapter.getItem(position);
        //TODO: implement field edit/delete
        return false;
    }
}

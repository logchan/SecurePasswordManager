package hk.ust.ustac.team8.securepasswordmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import hk.ust.ustac.team8.generalutility.AndroidUtility;
import hk.ust.ustac.team8.hashingscheme.HashingScheme;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeCrypto;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeField;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeFieldType;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeTransform;


public class InformationInputActivity extends Activity implements AdapterView.OnItemClickListener {

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

    private ListView listView;

    private ArrayList<HashMap<String,String>> fieldList = new ArrayList<HashMap<String,String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_input);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_information_input, menu);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Object o = listView.getItemAtPosition(position);
        AlertDialog dialog = AndroidUtility.createSimpleAlertDialog(this, "Type", o.getClass().getName(), "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }, null, null);
        dialog.show();
    }
}

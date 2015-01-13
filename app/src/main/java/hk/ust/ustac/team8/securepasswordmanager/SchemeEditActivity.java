package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.applicationutility.AppUtility;
import hk.ust.ustac.team8.generalutility.AndroidUtility;
import hk.ust.ustac.team8.hashingscheme.HashingScheme;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeCrypto;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeField;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeFieldType;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeTransform;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class SchemeEditActivity extends Activity implements Button.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        AndroidUtility.PromptOneInputReceiver {

    private ApplicationManager manager;

    private SimpleAdapter adapter;

    private ArrayList<HashMap<String,String>> theList = new ArrayList<HashMap<String,String>>();

    private HashingScheme scheme;

    private String oldName;

    private ListView listView;

    private Button doneBtn;

    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_edit);

        // set manager
        manager = ApplicationManager.getInstanceSafe(getApplicationContext());

        // set references
        listView = (ListView) findViewById(R.id.schemeEditList);
        doneBtn = (Button) findViewById(R.id.schemeEditDoneBtn);
        addBtn = (Button) findViewById(R.id.schemeEditAddBtn);

        // set listeners
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        doneBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);

        // get items and set adapter
        setSchemeByState();

        initTheList();

    }

    private void setSchemeByState() {
        switch (manager.getSettings().currentState) {
            case EDIT_SCHEME_CURRENT:
                // editing an existing scheme
                if (manager.getSettings().carriedInfo == null) {
                    manager.popState(null);
                    AndroidUtility.activityExceptionExit(this, "Null carried info for EDIT_SCHEME_CURRENT");
                }

                Object obj = manager.getSettings().carriedInfo[0];
                if (obj.getClass() != String.class) {
                    manager.popState(null);
                    AndroidUtility.activityExceptionExit(this, "Non-string for EDIT_SCHEME_CURRENT");
                }

                String schemeName = (String)obj;
                HashingScheme tScheme = manager.getSchemeByName(schemeName);
                if (tScheme == null) {
                    manager.popState(null);
                    AndroidUtility.activityExceptionExit(this, "Scheme not exist for EDIT_SCHEME_CURRENT");
                }

                scheme = tScheme;
                break;

            case EDIT_SCHEME_NEW:
                scheme = new HashingScheme("A Scheme", "This scheme is for...", HashingSchemeCrypto.MD5, HashingSchemeTransform.NO_TRANSFORM);
                break;

            default:
                manager.popState(null);
                AndroidUtility.activityExceptionExit(this, "Invalid state for SchemeEdit");
        }
        oldName = scheme.getName();
    }

    /**
     * Given the name and description, this method create a corresponding HashMap object and put it
     * into the list
     * @param name name
     * @param description description
     */
    private void putItemInTheList(String name, String description) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("name", name);
        item.put("description", description);
        theList.add(item);
    }

    /**
     * initialize the list by adding scheme properties (name, desc, crypto, etc) and then fields
     */
    private void initTheList() {
        theList.clear();

        putItemInTheList(scheme.getName(), "Name of the scheme");
        putItemInTheList(scheme.getDescription(), "Description of the scheme");
        putItemInTheList(scheme.getCrypto().toString(), "Hashing method of the scheme");
        putItemInTheList(getString(AppUtility.getTransformStringID(scheme.getTransform())), "Transform after hashing");

        int fieldCount = scheme.getFieldCount();
        for (int i = 0; i < fieldCount; ++i) {
            HashingSchemeField field = scheme.getField(i);
            putItemInTheList(field.getName(), field.getDescription());
        }

        adapter = new SimpleAdapter(this, theList, android.R.layout.simple_list_item_2,
                new String[] { "name", "description"}, new int[] { android.R.id.text1, android.R.id.text2});
        listView.setAdapter(adapter);
    }

    private void editHashingField(final int index) {
        HashingSchemeField field = scheme.getField(index);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.edit_field));
        final View view = getLayoutInflater().inflate(R.layout.dialog_edit_field, null);
        final EditText nameED = (EditText)view.findViewById(R.id.fieldNameEdit);
        final EditText descED = (EditText)view.findViewById(R.id.fieldDescEdit);
        nameED.setText(field.getName());
        descED.setText(field.getDescription());

        builder.setView(view);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                applyHashingFieldEdit(index, nameED.getText().toString(), descED.getText().toString());
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing~
            }
        });

        builder.show();
    }

    private void applyHashingFieldEdit(int index, String name, String desc) {
        HashingSchemeField field = scheme.getField(index);
        field.setName(name);
        field.setDescription(desc);
        initTheList();
    }

    private void performSaveAndExit() {
        if (!scheme.getName().equals(oldName)) {
            if (manager.haveSchemeOfName(scheme.getName())) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.scheme_name_exists), Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            else {
                manager.renameScheme(oldName, scheme.getName());
            }
        }
        manager.saveScheme(scheme);
        manager.popState(null);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.schemeEditDoneBtn:
                performSaveAndExit();
                break;
            case R.id.schemeEditAddBtn:
                scheme.addField(new HashingSchemeField(HashingSchemeFieldType.STRING, "new field",
                        "this field is for..."));
                editHashingField(scheme.getFieldCount() - 1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog = AndroidUtility.createSimpleAlertDialog(this, getString(R.string.ask_save),
                null, getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        performSaveAndExit();
                    }
                }, getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                        manager.popState(null);
                        finish();
                    }
                });
        dialog.show();
    }

    /*
    position convension:
    0 - scheme name
    1 - scheme description
    2 - hashing method
    3 - transform
    4 -> fields
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        HashMap<String, String> item = (HashMap<String, String>) adapter.getItem(position);
        switch (position) {
            case 0:
                AndroidUtility.promptForOneInput(this, getString(R.string.scheme_name),
                        scheme.getName(), getString(R.string.scheme_name), this, "name");
                break;
            case 1:
                AndroidUtility.promptForOneInput(this, getString(R.string.scheme_description),
                        scheme.getDescription(), getString(R.string.scheme_description), this, "description");
                break;
            case 2:
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.md5_is_the_only_one), Toast.LENGTH_SHORT);
                toast.show();
                break;
            case 3:
                promptTransformSelect();
                break;
            default:
                editHashingField(position - 4);
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        HashMap<String, String> item = (HashMap<String, String>) adapter.getItem(position);
        if (position >= 4) {
            promptDeletefield(position - 4);
            return true;
        }
        else {
            return false;
        }
    }

    private void promptTransformSelect() {
        final String[] options =  new String[] { getString(R.string.trans_no_transform),
                getString(R.string.trans_to_mixed),
        };

        final Activity activity = this;

        // create a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.scheme_transform));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String selected = options[which];
                if (selected.equals(getString(R.string.trans_no_transform))) {
                    scheme.setTransform(HashingSchemeTransform.NO_TRANSFORM);
                }
                else if (selected.equals(getString(R.string.trans_to_mixed))) {
                    scheme.setTransform(HashingSchemeTransform.MIXED_UPPER_AND_LOWER_CASE);
                }
                initTheList();
            }
        });
        builder.show();
    }

    private void promptDeletefield(final int index) {
        AlertDialog dialog = AndroidUtility.createSimpleAlertDialog(this, "Delete \"" + scheme.getField(index).getName() + "\"",
                getString(R.string.are_you_sure), getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // yes
                        deleteField(index);
                    }
                }, getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // no
                    }
                });
        dialog.show();
    }

    private void deleteField(int index) {
        scheme.removeField(index);
        initTheList();
    }

    @Override
    public void receivePromptOneInput(String output, Object... extraInfo) {
        if (output == null || extraInfo == null || extraInfo.length == 0) {
            return;
        }

        Object obj = extraInfo[0];
        if (obj.getClass() != String.class) {
            return;
        }

        String action = (String) obj;
        if (action.equals("name")) {
            if (output.equals("")) {
                return;
            }
            if (!scheme.getName().equals(output)) {
                scheme.setName(output);
                initTheList();
            }
        }
        else if (action.equals("description")) {
            if (!scheme.getDescription().equals(output)) {
                scheme.setDescription(output);
                initTheList();
            }
        }
    }
}

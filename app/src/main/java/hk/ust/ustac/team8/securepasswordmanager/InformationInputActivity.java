package hk.ust.ustac.team8.securepasswordmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import hk.ust.ustac.team8.applicationutility.AppFileUtility;
import hk.ust.ustac.team8.applicationutility.HashingSchemeProcessor;
import hk.ust.ustac.team8.generalutility.AndroidUtility;
import hk.ust.ustac.team8.hashingscheme.HashingScheme;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeCrypto;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeField;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeFieldType;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeTransform;
import hk.ust.ustac.team8.passwordutility.HashingPasswordGenerator;

public class InformationInputActivity extends Activity implements Button.OnClickListener,
        AdapterView.OnItemClickListener, AndroidUtility.PromptOneInputReceiver {

    private HashingScheme scheme;

    private ApplicationManager manager;

    private SimpleAdapter adapter;

    private ArrayList<HashMap<String,String>> fieldList = new ArrayList<HashMap<String,String>>();

    private ListView listView;

    private LinkedList<String> savedInfos;

    private String lastLoadedInfo = "";

    private Button procBtn;

    private Button loadBtn;

    private Button saveBtn;

    private EditText timeToHashED;

    private EditText resultLenED;


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
        timeToHashED = (EditText) findViewById(R.id.infoFillTimeToHash);
        resultLenED = (EditText) findViewById(R.id.infoFillLengthOfResult);

        // set listener
        listView.setOnItemClickListener(this);
        procBtn.setOnClickListener(this);
        loadBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        // get scheme
        if (manager.getSettings().lastState == ApplicationState.SHOW_HASHING_RESULT
                && manager.getSettings().currentScheme != null) {
            // coming from result page
            scheme = manager.getSettings().currentScheme;
        }
        else {
            setSchemeByState();
        }

        // load the saved information of the scheme
        savedInfos = manager.getAllSavedInfo(scheme.getName());
        if (savedInfos == null) {
            savedInfos = new LinkedList<String>();
        }

        initListView(false);

        // apply the application settings
        timeToHashED.setText(((Integer)manager.getSettings().defaultHashingTimes).toString());
        resultLenED.setText(((Integer)manager.getSettings().defaultResultLength).toString());
    }

    @Override
    public void onBackPressed() {
        manager.getSettings().currentState = ApplicationState.SELECT_SCHEME_FOR_PASSWORD_GEN;
        manager.getSettings().lastState = ApplicationState.INPUT_INFO_FOR_PASSWORD_GEN;
        finish();
    }

    private void setSchemeByState() {
        // current state shall be INPUT_INFO_FOR_PASSWORD_GEN
        // carried information shall be the name of scheme

        if (manager.getSettings().currentState != ApplicationState.INPUT_INFO_FOR_PASSWORD_GEN) {
            AndroidUtility.activityExceptionExit(this, "Invalid state for INPUT_INFO");
        }

        // validate carried information
        if (manager.getSettings().carriedInfo == null || manager.getSettings().carriedInfo.length == 0) {
            AndroidUtility.activityExceptionExit(this, "No carried info for INPUT_INFO");
        }

        Object obj = manager.getSettings().carriedInfo[0];
        if (obj.getClass() != String.class) {
            AndroidUtility.activityExceptionExit(this, "Non-string carried for INPUT_INFO");
        }

        // get the scheme from name
        String name = (String) obj;
        scheme = manager.getSchemeByName(name);
        if (scheme == null) {
            AndroidUtility.activityExceptionExit(this, "Scheme for INPUT_INFO not found");
        }

        // reset the filled value of scheme
        int fieldCount = scheme.getFieldCount();
        for (int i = 0; i < fieldCount; ++i) {
            HashingSchemeField field = scheme.getField(i);
            field.setValue("");
        }
    }

    private void initListView(boolean showValues) {
        // (re-)initialize the listView
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.infoFillLoadBtn:
                promptLoadInfo();
                break;
            case R.id.infoFillSaveBtn:
                AndroidUtility.promptForOneInput(this, getString(R.string.info_save_name), lastLoadedInfo,
                        getString(R.string.info_save_hint), this, false, "save");
                break;
            case R.id.infoFillProceedBtn:
                doProceed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        // ask the user to fill the value of the field
        HashingSchemeField field = scheme.getField(position);
        AndroidUtility.promptForOneInput(this, field.getName(), field.getValue(), getString(R.string.field_hint),
                this, true, "field", position);
    }

    @Override
    public void receivePromptOneInput(String output, Object... extraInfo) {
        /** validate extraInfo (shall have the String denoting action) **/
        if (output == null || extraInfo == null || extraInfo.length == 0) {
            return;
        }

        Object obj = extraInfo[0];
        if (obj.getClass() != String.class) {
            return;
        }

        /** move on according to action**/
        String action = (String) obj;
        if (action.equals("field") && extraInfo.length > 1) {
            // fill the field value
            obj = extraInfo[1];
            if (obj.getClass() != int.class && obj.getClass() != Integer.class) {
                return;
            }

            int index = (Integer) obj;
            HashingSchemeField field = scheme.getField(index);
            field.setValue(output);
            initListView(true);
        }
        else if (action.equals("save")) {
            /** save the info **/
            final String infoName = output;

            // validate infoName
            if (!manager.nameIsValid(infoName)) {
                manager.showToast(getString(R.string.naming_rule));
                return;
            }

            // check if info of the same name exists
            if (savedInfos.contains(infoName)) {
                // ask if overwrite
                AlertDialog dialog = AndroidUtility.createSimpleAlertDialog(this, getString(R.string.ask_overwrite),
                        getString(R.string.same_info_exists), getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                saveInfo(infoName);
                            }
                        }, getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // do nothing
                            }
                        });
                dialog.show();
            }
            else {
                // no same name, just save
                saveInfo(infoName);
            }
        }
    }

    private void saveInfo(String infoName) {
        String result = "";
        if (manager.saveOneInfo(scheme, infoName)) {
            result = getString(R.string.info_save_success);
        }
        else {
            result = getString(R.string.info_save_failed);
        }
        manager.showToast(result);
        savedInfos = manager.getAllSavedInfo(scheme.getName());
    }

    /**
     * Prompt a list and ask the user to choose the information set to load
     */
    private void promptLoadInfo() {
        // final String[] options = (String[])savedInfos.toArray(); //WRONG!
        final String[] options = savedInfos.toArray(new String[savedInfos.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.info_load_title));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String selected = options[which];
                loadInfo(selected);
            }
        });
        builder.show();
    }

    private void loadInfo(String infoName) {
        String savedInfo = manager.getOneSavedInfo(scheme.getName(), infoName);
        String result = "";

        if (savedInfo != null) {
            scheme.importFieldValues(savedInfo);
            lastLoadedInfo = infoName;
            result = getString(R.string.info_load_success);
        }
        else {
            result = getString(R.string.info_load_failed);
        }

        manager.showToast(result);

        initListView(true);
    }

    /**
     * Do the hashing, and show the result
     */
    private void doProceed() {

        /** Get how many times to hash **/
        int timeToHash = 1;

        try {
            timeToHash = Integer.valueOf(timeToHashED.getText().toString());
            if (timeToHash <= 0 || timeToHash > 100) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            timeToHash = 1;
        }

        /** Get the length of result **/
        int resultLen = 16;

        try {
            resultLen = Integer.valueOf(resultLenED.getText().toString());
            if (resultLen < 0 || resultLen > 32) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            resultLen = 16;
        }

        final int iterations = timeToHash;
        final int maxResultLen = resultLen;
        final Activity activity = this;

        /** Prompt for the secret, hash and then proceed to result page **/

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // setup the dialog
        builder.setTitle(getString(R.string.prompt_secret));
        final View view = getLayoutInflater().inflate(R.layout.dialog_one_input, null);
        final EditText edit = (EditText)view.findViewById(R.id.dialogInput);
        edit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(view);

        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HashingSchemeProcessor processor = manager.getSchemeProcessor();
                try {
                    HashingPasswordGenerator generator = processor.getHashingPasswordGeneratorFromScheme(scheme);
                    String result = generator.generatePassword(edit.getText().toString(), iterations);

                    if (result.length() > maxResultLen) {
                        result = result.substring(0, maxResultLen);
                    }

                    manager.showToast(getString(R.string.hashing_status_text)
                                        .replace("%1", ((Integer)iterations).toString()));

                    manager.getSettings().lastHashingResult = result;
                    manager.switchActivity(activity, HashingResultActivity.class, ApplicationState.SHOW_HASHING_RESULT);
                }
                catch (Exception e) {
                    manager.showToast(activity.getString(R.string.exception_exit));
                }
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing
            }
        });

        builder.show();
    }
}

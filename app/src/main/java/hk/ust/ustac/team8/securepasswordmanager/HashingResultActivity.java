package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.generalutility.AndroidUtility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class HashingResultActivity extends Activity implements SeekBar.OnSeekBarChangeListener, Button.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private ApplicationManager manager;

    private TextView hashingResultText;

    private Button doneBtn;

    private Button backBtn;

    private Button copyBtn;

    private SeekBar resultSizeSeek;

    private CheckBox hideCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashing_result);

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

        // setup reference to views
        hashingResultText = (TextView) findViewById(R.id.hashingResultTextView);
        resultSizeSeek = (SeekBar) findViewById(R.id.hashingResultSizeSeek);
        backBtn = (Button) findViewById(R.id.hashingResultBackBtn);
        doneBtn = (Button) findViewById(R.id.hashingResultDoneBtn);
        copyBtn = (Button) findViewById(R.id.hashingResultCopyBtn);
        hideCheck = (CheckBox) findViewById(R.id.hashingResultHideCheck);

        // setup listener
        resultSizeSeek.setOnSeekBarChangeListener(this);
        backBtn.setOnClickListener(this);
        doneBtn.setOnClickListener(this);
        copyBtn.setOnClickListener(this);
        hideCheck.setOnCheckedChangeListener(this);

        // set view
        hashingResultText.setTextSize(TypedValue.COMPLEX_UNIT_SP, manager.getSettings().resultFontsize);

        // apply the application settings
        resultSizeSeek.setProgress(manager.getSettings().resultFontsize);
        if (manager.getSettings().hideResult) {
            hideCheck.setChecked(true);
            hashingResultText.setText(getString(R.string.result_hidden));
        }
        else {
            hideCheck.setChecked(false);
            setHashingResultText(manager.getSettings().lastHashingResult);
        }
    }

    @Override
    public void onBackPressed() {
        manager.popState(ApplicationState.SHOW_HASHING_RESULT);
        finish();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.hashingResultSizeSeek:
                hashingResultText.setTextSize(TypedValue.COMPLEX_UNIT_SP, resultSizeSeek.getProgress());
                break;

            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // do nothing
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // do nothing
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hashingResultBackBtn:
                onBackPressed();
                break;

            case R.id.hashingResultDoneBtn:
                // all done, reset state and goto Main
                manager.getSettings().currentScheme = null;
                manager.getSettings().lastHashingResult = "";
                manager.switchToMainClear(this);
                break;

            case R.id.hashingResultCopyBtn:
                // copy the result to clipboard
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("hash", manager.getSettings().lastHashingResult);
                clipboardManager.setPrimaryClip(clipData);

                AlertDialog dialog = AndroidUtility.createSimpleAlertDialog(this, null, getString(R.string.result_copied),
                        getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // do nothing
                            }
                        }, null, null);
                dialog.show();
                break;
        }
    }

    private void setHashingResultText(String result) {
        StringBuilder toDisplay = new StringBuilder();

        int len = result.length();
        for (int i = 0; i < len; ++i) {
            toDisplay.append(result.charAt(i));
            if ((i + 1) < len && (i + 1) % 4 == 0) {
                if ((i + 1) % 8 == 0) {
                    toDisplay.append('\n');
                }
                else {
                    toDisplay.append(' '); // a whitespace
                }
            }
        }

        hashingResultText.setText(toDisplay.toString());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.isChecked()) {
            hashingResultText.setText(getString(R.string.result_hidden));
        }
        else {
            setHashingResultText(manager.getSettings().lastHashingResult);
        }
    }
}

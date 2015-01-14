package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.generalutility.AndroidUtility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingActivity extends Activity implements SeekBar.OnSeekBarChangeListener {

    private ApplicationManager manager;

    private SeekBar resultSizeSeek;

    private TextView resultSizePreview;

    private TextView resultLengthText;

    private TextView timeText;

    private CheckBox hideResultCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // set manager
        manager = ApplicationManager.getInstanceSafe(getApplicationContext());

        // set reference
        resultSizeSeek = (SeekBar) findViewById(R.id.settingSizeSeek);
        resultSizePreview = (TextView) findViewById(R.id.settingSizePreviewText);
        resultLengthText = (TextView) findViewById(R.id.settingResultLength);
        timeText = (TextView) findViewById(R.id.settingTimesToHash);
        hideResultCheck = (CheckBox) findViewById(R.id.settingHideResultCheck);

        // set listener
        resultSizeSeek.setOnSeekBarChangeListener(this);

        // load current settings
        resultSizeSeek.setProgress(manager.getSettings().resultFontsize);
        resultSizePreview.setTextSize(manager.getSettings().resultFontsize);
        resultLengthText.setText(((Integer) manager.getSettings().defaultResultLength).toString());
        timeText.setText(((Integer) manager.getSettings().defaultHashingTimes).toString());
        hideResultCheck.setChecked(manager.getSettings().hideResult);
    }

    @Override
    public void onBackPressed() {
        // prompt to save settings
        AlertDialog dialog = AndroidUtility.createSimpleAlertDialog(this, getString(R.string.ask_save_settings),
                null, getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        /*** apply, save and exit ***/

                        // get time to hash
                        int timeToHash = -1;
                        try {
                            timeToHash = Integer.valueOf(timeText.getText().toString());
                            if (timeToHash <= 0 || timeToHash > 100) {
                                throw new Exception();
                            }
                        }
                        catch (Exception e) {
                            // stop if timeToHash not valid
                            manager.showToast(getString(R.string.hash_time_invalid));
                            return;
                        }

                        // get result length
                        int resultLength;
                        try {
                            resultLength = Integer.valueOf(resultLengthText.getText().toString());
                            if (resultLength <= 0 || resultLength > 32) {
                                throw new Exception();
                            }
                        }
                        catch (Exception e) {
                            // stop if resultLength not valid
                            manager.showToast(getString(R.string.result_length_invalid));
                            return;
                        }

                        // all get, now apply
                        manager.getSettings().hideResult = hideResultCheck.isChecked();
                        manager.getSettings().resultFontsize = resultSizeSeek.getProgress();
                        manager.getSettings().defaultResultLength = resultLength;
                        manager.getSettings().defaultHashingTimes = timeToHash;

                        // now save
                        if (manager.saveAppSettings()) {
                            manager.showToast(getString(R.string.save_setting_succeed));
                        }
                        else {
                            manager.showToast(getString(R.string.save_setting_fail));
                        }

                        // now exit
                        manager.popState(null);
                        finish();
                    }
                }, getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // exit without saving
                        manager.popState(null);
                        finish();
                    }
                });
        dialog.show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.settingSizeSeek:
                resultSizePreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, resultSizeSeek.getProgress());
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
}

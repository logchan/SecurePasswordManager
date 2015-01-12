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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class HashingResultActivity extends Activity implements SeekBar.OnSeekBarChangeListener, Button.OnClickListener {

    private ApplicationManager manager;

    private TextView hashingResultText;

    private Button doneBtn;

    private Button backBtn;

    private Button copyBtn;

    private SeekBar resultSizeSeek;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashing_result);

        // set man
        manager = ApplicationManager.getInstance();

        // setup reference to views
        hashingResultText = (TextView) findViewById(R.id.hashingResultTextView);
        resultSizeSeek = (SeekBar) findViewById(R.id.hashingResultSizeSeek);
        backBtn = (Button) findViewById(R.id.hashingResultBackBtn);
        doneBtn = (Button) findViewById(R.id.hashingResultDoneBtn);
        copyBtn = (Button) findViewById(R.id.hashingResultCopyBtn);

        // setup listener
        resultSizeSeek.setOnSeekBarChangeListener(this);
        backBtn.setOnClickListener(this);
        doneBtn.setOnClickListener(this);
        copyBtn.setOnClickListener(this);

        setHashingResultText("D41D8CD98F00B204E9800998ECF8427E");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hashing_result, menu);
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
                //TODO: implement back
                break;
            case R.id.hashingResultDoneBtn:
                //TODO: implement done
                break;
            case R.id.hashingResultCopyBtn:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("hash", manager.getSettings().lastHashingResult);
                clipboardManager.setPrimaryClip(clipData);

                AlertDialog dialog = AndroidUtility.createSimpleAlertDialog(this, null, "Succefully copied result",
                        "OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // do nothing
                            }
                        }, null, null);
                dialog.show();
                break;
        }
    }
}

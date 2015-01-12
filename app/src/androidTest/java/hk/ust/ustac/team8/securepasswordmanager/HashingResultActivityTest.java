package hk.ust.ustac.team8.securepasswordmanager;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by logchan on 1/12/2015.
 */
public class HashingResultActivityTest extends ActivityInstrumentationTestCase2<HashingResultActivity> {

    private HashingResultActivity activity;

    private SeekBar resultSizeSeek;

    private TextView resultText;


    public HashingResultActivityTest() {
        super(HashingResultActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        activity = getActivity();

        resultSizeSeek = (SeekBar) activity.findViewById(R.id.hashingResultSizeSeek);
        resultText = (TextView) activity.findViewById(R.id.hashingResultTextView);
    }

    public void testPreConditions() {
        assertEquals(22, resultSizeSeek.getProgress());
    }

}

package hk.ust.ustac.team8.securepasswordmanager;

import android.test.ActivityUnitTestCase;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by logchan on 1/12/2015.
 */
public class HashingResultActivityTest extends ActivityUnitTestCase<HashingResultActivity> {

    private HashingResultActivity activity;

    private SeekBar resultSizeSeek;

    private TextView resultText1;

    private TextView resultText2;

    public HashingResultActivityTest() {
        super(HashingResultActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        activity = getActivity();

        resultSizeSeek = (SeekBar) activity.findViewById(R.id.resultSizeSeek);
        resultText1 = (TextView) activity.findViewById(R.id.resultTextView1);
        resultText2 = (TextView) activity.findViewById(R.id.resultTextView2);
    }

    public void testPreConditions() {
        assertEquals(22, resultSizeSeek.getProgress());
    }

}

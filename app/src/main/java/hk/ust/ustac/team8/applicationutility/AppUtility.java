package hk.ust.ustac.team8.applicationutility;

import hk.ust.ustac.team8.hashingscheme.HashingSchemeTransform;
import hk.ust.ustac.team8.securepasswordmanager.R;

/**
 * Created by logchan on 1/13/2015.
 */
public final class AppUtility {

    private AppUtility() {

    }

    public static int getTransformStringID(HashingSchemeTransform transform) {
        switch (transform) {
            case NO_TRANSFORM:
                return R.string.trans_no_transform;
            case MIXED_UPPER_AND_LOWER_CASE:
                return R.string.trans_to_mixed;
            default:
                return R.string.trans_no_transform;
        }
    }
}

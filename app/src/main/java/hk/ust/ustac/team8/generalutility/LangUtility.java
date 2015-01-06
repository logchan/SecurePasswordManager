package hk.ust.ustac.team8.generalutility;

/**
 * Created by logchan on 1/6/2015.
 */
public class LangUtility {

    public static void assertNonNull(Object obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }
}

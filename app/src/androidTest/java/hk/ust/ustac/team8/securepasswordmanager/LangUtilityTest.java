package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.generalutility.LangUtility;
import hk.ust.ustac.team8.passwordutility.BasicSaltAppender;
import hk.ust.ustac.team8.passwordutility.SaltingServiceProvider;

import android.test.InstrumentationTestCase;

/**
 * Created by logchan on 1/7/2015.
 */
public class LangUtilityTest extends InstrumentationTestCase {

    public void testGetObjectOfSameClass() {
        BasicSaltAppender saltAppender = new BasicSaltAppender("abc");
        SaltingServiceProvider provider = (SaltingServiceProvider)saltAppender;

        Object obj = LangUtility.getObjectOfSameClass(provider, new Object[] { "abc" });

        assertEquals(obj.getClass(), saltAppender.getClass());
        assertEquals(((BasicSaltAppender)obj).getSalt(), saltAppender.getSalt());
    }
}

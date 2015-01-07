package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.generalutility.LangUtility;
import hk.ust.ustac.team8.passwordutility.BasicSaltAppender;
import hk.ust.ustac.team8.passwordutility.NoEffectStringTransformer;
import hk.ust.ustac.team8.passwordutility.SaltingServiceProvider;

import android.test.InstrumentationTestCase;

/**
 * Created by logchan on 1/7/2015.
 */
public class LangUtilityTest extends InstrumentationTestCase {

    public void testGetObjectOfSameClass() {
        BasicSaltAppender saltAppender = new BasicSaltAppender("abc");
        SaltingServiceProvider provider = (SaltingServiceProvider)saltAppender;

        Object obj = LangUtility.getObjectOfSameClass(provider, new Object[] {"abc"});

        assertEquals(saltAppender.getClass(), obj.getClass());
        assertEquals(saltAppender.getSalt(), ((BasicSaltAppender)obj).getSalt());
    }

    public void testGetObjectOfSameClassWithDefaultConstructor() {
        NoEffectStringTransformer transformer = new NoEffectStringTransformer();
        Object obj = LangUtility.getObjectOfSameClass(transformer, new Object[] {});

        assertEquals(transformer.getClass(), obj.getClass());
    }

    public void testGetObjectOfSameClassWithWrongArgs() {
        BasicSaltAppender saltAppender = new BasicSaltAppender("abc");

        Object obj = LangUtility.getObjectOfSameClass(saltAppender, new Object[] {"abc" , "def"});

        assertEquals(null, obj);
    }
}

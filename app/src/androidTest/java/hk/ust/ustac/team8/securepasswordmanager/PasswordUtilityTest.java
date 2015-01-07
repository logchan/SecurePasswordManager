package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.passwordutility.AdditionalSaltingInformation;
import hk.ust.ustac.team8.passwordutility.BasicSaltAppender;
import hk.ust.ustac.team8.passwordutility.HashingPasswordGenerator;
import hk.ust.ustac.team8.passwordutility.MD5HashingServiceProvider;
import hk.ust.ustac.team8.passwordutility.NoEffectStringTransformer;

import android.test.InstrumentationTestCase;

/**
 * Created by logchan on 1/6/2015.
 */
public class PasswordUtilityTest extends InstrumentationTestCase {

    public void testGenerator() {
        MD5HashingServiceProvider md5Provider = new MD5HashingServiceProvider();
        NoEffectStringTransformer nonTransform = new NoEffectStringTransformer();
        BasicSaltAppender salt1 = new BasicSaltAppender("abc");
        BasicSaltAppender salt2 = new BasicSaltAppender("def");

        HashingPasswordGenerator generator = new HashingPasswordGenerator(md5Provider, nonTransform);
        generator.addSalt(salt1);
        generator.addSalt(salt2);
        
        String pwd = generator.generatePassword("somesecret", 42);

        assertEquals("87ee18deceb425c475aa334650b9f9b6", pwd);
    }

}

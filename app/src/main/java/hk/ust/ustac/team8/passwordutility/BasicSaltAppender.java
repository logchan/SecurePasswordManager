package hk.ust.ustac.team8.passwordutility;

import hk.ust.ustac.team8.generalutility.LangUtility;
/**
 * Created by logchan on 1/6/2015.
 */
public class BasicSaltAppender implements SaltingServiceProvider {

    private String salt;

    public BasicSaltAppender(String salt) {
        LangUtility.assertNonNull(salt, "Null salt provided for initialization of BasicSaltAppender");

        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String newSalt) {
        LangUtility.assertNonNull(newSalt, "Null salt provided for setter of salt of BasicSaltAppender");

        this.salt = newSalt;
    }

    public String addSalt(String input, AdditionalSaltingInformation information) {
        return input + salt;
    }
}

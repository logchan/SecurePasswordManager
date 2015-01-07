package hk.ust.ustac.team8.passwordutility;

import hk.ust.ustac.team8.generalutility.LangUtility;
/**
 * A salting service provider that simply append the salt to the input
 *
 * @author logchan
 * @see hk.ust.ustac.team8.passwordutility.SaltingServiceProvider
 */
public class BasicSaltAppender implements SaltingServiceProvider {

    private String salt;

    /**
     * The constructor of BasicSaltAppender. Setup the salt to append.
     *
     * @param salt the salt to append
     */
    public BasicSaltAppender(String salt) {
        LangUtility.assertNonNull(salt, "Null salt provided for initialization of BasicSaltAppender");

        this.salt = salt;
    }

    /**
     * Get the salt of this appender
     *
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Set the salt of this appender
     *
     * @param newSalt the new salt to be set
     */
    public void setSalt(String newSalt) {
        LangUtility.assertNonNull(newSalt, "Null salt provided for setter of salt of BasicSaltAppender");

        this.salt = newSalt;
    }

    /**
     * Add salt for the input. Simply append the salt to the input. i.e.:
     * output = input + salt
     *
     * @param input the input string to be salted
     * @param information the information of the hashing process. Ignored.
     * @return the salted input
     */
    public String addSalt(String input, HashingPasswordGenerator.AdditionalSaltingInformation information) {
        LangUtility.assertNonNull(input, "Null input provided for addSalt of BasicSaltAppender");

        return input + salt;
    }
}

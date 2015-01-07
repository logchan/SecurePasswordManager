package hk.ust.ustac.team8.passwordutility;

/**
 * A salting service provider that only append the salt at first iteration of hashing
 *
 * @author logchan
 * @see hk.ust.ustac.team8.passwordutility.BasicSaltAppender
 * @see hk.ust.ustac.team8.passwordutility.SaltingServiceProvider
 */
public class OnceSaltAppender extends BasicSaltAppender {

    /**
     * The constructor of BasicSaltAppender. Setup the salt to append.
     *
     * @param salt the salt to append
     */
    public OnceSaltAppender(String salt) {
        super(salt);
    }

    /**
     * Add salt for the input. If it is the first iteration, it appends the salt. Otherwise,
     * the input remains unchanged.
     *
     * @param input the input string to be salted
     * @param information the information of the hashing process.
     * @return the salted input
     */
    @Override
    public String addSalt(String input, HashingPasswordGenerator.AdditionalSaltingInformation information) {
        if (information.getCurrentIteration() == 0) {
            return super.addSalt(input, information);
        }
        else {
            return input;
        }
    }
}

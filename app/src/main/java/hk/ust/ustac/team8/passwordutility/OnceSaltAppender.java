package hk.ust.ustac.team8.passwordutility;

/**
 * Created by logchan on 1/7/2015.
 */
public class OnceSaltAppender extends BasicSaltAppender {

    public OnceSaltAppender(String salt) {
        super(salt);
    }

    @Override
    public String addSalt(String input, AdditionalSaltingInformation information) {
        if (information.currentIteration == 0) {
            return super.addSalt(input, information);
        }
        else {
            return input;
        }
    }
}

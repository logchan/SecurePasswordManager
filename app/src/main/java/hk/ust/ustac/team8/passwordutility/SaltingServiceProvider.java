package hk.ust.ustac.team8.passwordutility;

/**
 * An interface that can add salt to a given String
 * For example, it may simply append the salt
 *
 * @author logchan
 */
public interface SaltingServiceProvider {

    public String addSalt(String input, HashingPasswordGenerator.AdditionalSaltingInformation information);
}

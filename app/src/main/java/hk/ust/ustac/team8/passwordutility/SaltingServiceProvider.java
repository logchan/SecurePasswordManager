package hk.ust.ustac.team8.passwordutility;

/**
 * Created by logchan on 1/5/2015.
 */
public interface SaltingServiceProvider {

    public String addSalt(String input, AdditionalSaltingInformation information);
}

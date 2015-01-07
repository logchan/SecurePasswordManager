package hk.ust.ustac.team8.passwordutility;

/**
 * An interface that can produce the String-represented hashing result of a given String
 * For example, it may be doing MD5 or SHA-1
 *
 * @author logchan
 */
public interface HashingServiceProvider {

    public String hash(String input);
}

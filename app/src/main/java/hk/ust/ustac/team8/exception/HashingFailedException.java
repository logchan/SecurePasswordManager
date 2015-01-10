package hk.ust.ustac.team8.exception;

/**
 * Denotes that the hashing stage of HPG failed.
 *
 * @author logchan
 * @see hk.ust.ustac.team8.passwordutility.HashingPasswordGenerator
 */
public class HashingFailedException extends Exception {

    public HashingFailedException(String msg) {
        super(msg);
    }
}

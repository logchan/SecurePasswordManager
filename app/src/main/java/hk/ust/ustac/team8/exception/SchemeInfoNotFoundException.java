package hk.ust.ustac.team8.exception;

/**
 * Denotes that the designated saved info file of the scheme was not found
 *
 * @author logchan
 * @see hk.ust.ustac.team8.applicationutility.AppFileUtility
 */
public class SchemeInfoNotFoundException extends Exception {

    public SchemeInfoNotFoundException(String msg) {
        super(msg);
    }
}

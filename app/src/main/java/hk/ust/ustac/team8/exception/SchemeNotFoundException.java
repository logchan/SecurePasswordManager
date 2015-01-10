package hk.ust.ustac.team8.exception;

/**
 * Denotes that the designated scheme was not found (while deleting or loading)
 *
 * @author logchan
 * @see hk.ust.ustac.team8.applicationutility.AppFileUtility
 */
public class SchemeNotFoundException extends Exception {

    public SchemeNotFoundException(String msg) {
        super(msg);
    }
}

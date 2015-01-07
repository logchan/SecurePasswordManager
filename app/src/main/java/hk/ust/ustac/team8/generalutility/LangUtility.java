package hk.ust.ustac.team8.generalutility;

/**
 * Some static methods that are related to Java language
 *
 */
public class LangUtility {

    /**
     * Ensure that the object is not null. If it is, throw an NullPointerException with the provided message
     * @param obj the object to be checked
     * @param message the message of the possibly thrown NullPointerException
     */
    public static void assertNonNull(Object obj, String message) {
        if (obj == null) {
            throw new NullPointerException(message);
        }
    }
}

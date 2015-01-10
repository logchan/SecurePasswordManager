package hk.ust.ustac.team8.generalutility;

import java.lang.reflect.Constructor;

/**
 * Some static methods that are related to Java language
 *
 */
public final class LangUtility {

    /**
     * Private constructor to prevent creating instance
     */
    private LangUtility() {

    }

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

    /**
     * Get an object of the same class as the passed object, initialized with passed args
     *
     * @param object an instance of the target class
     * @param initializerArgs parameters for class constructor
     * @return a new instance, null if no constructor can be called
     */
    public static Object getObjectOfSameClass(Object object, Object[] initializerArgs) {

        Constructor[] constructors = object.getClass().getDeclaredConstructors();

        for (Constructor constructor : constructors) {
            // check if argument type matches
            Class[] paramTypes = constructor.getParameterTypes();
            boolean success = false;
            if (paramTypes.length == initializerArgs.length) {
                success = true;
                for (int i = 0; i < initializerArgs.length; ++i) {
                    if (paramTypes[i] != initializerArgs[i].getClass()) {
                        success = false;
                        break;
                    }
                }
            }
            // if matches, try to construct
            if (success) {
                try {
                    Object result = constructor.newInstance(initializerArgs);
                    return result;
                }
                catch (Exception ex) {
                    // this construction (for any reason) fails,
                    // try another one
                    continue;
                }
            }
        }

        // no constructor matches
        return null;
    }
}

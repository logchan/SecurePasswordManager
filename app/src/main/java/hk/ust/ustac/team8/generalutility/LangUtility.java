package hk.ust.ustac.team8.generalutility;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

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

    /**
     * Very simple serialization. Given the object and the fields to save, generate:
     * FIELD_NAME_1|FIELD_VALUE_1
     * FIELD_NAME_2|FIELD_VALUE_2
     * lines separated by '\n'
     * FieldNotFound & IllegalAccess will be omitted (corresponding fields are ignored).
     * Only String, int, boolean and Enum are supported.
     *
     * @param obj the object to be serialized
     * @return a string of the fields
     */
    public static String getSimpleStringRepresentation(Object obj, String ... fields) {
        if (obj == null || fields == null) {
            return "";
        }

        Class cls = obj.getClass();
        Field[] clsFields = cls.getFields();

        // make fields easier to be checked
        String clsFieldsStr = "";
        for (Field clsField : clsFields) {
            clsFieldsStr += clsField.getName();
            clsFieldsStr += "|";
        }

        StringBuilder builder = new StringBuilder();

        for (String field : fields) {
            // skip harmful inputs
            if (field.indexOf('|') >= 0) {
                continue;
            }
            // check field existance
            if (clsFieldsStr.indexOf(field) >= 0) {
                try {
                    Field targetField = cls.getField(field);
                    Object value = targetField.get(obj);
                    Class targetType = targetField.getType();

                    String line = field + "|";

                    if (targetType == String.class) {
                        line += ((String)value);
                    }
                    else if (targetType == int.class || targetType == Integer.class) {
                        line += ((Integer)value);
                    }
                    else if (targetType == boolean.class || targetType == Boolean.class) {
                        line += (((Boolean)value) ? "TRUE" : "FALSE");
                    }
                    else if (targetType.isEnum()) {
                        line += (value.toString());
                    }
                    else {
                        continue;
                    }

                    builder.append(line);
                    builder.append('\n');
                }
                catch (NoSuchFieldException e) {
                    // somehow it bypassed the check, ignore
                    continue;
                }
                catch (IllegalAccessException e) {
                    // well, we can not access it, ignore
                    continue;
                }
            }
        }

        return builder.toString();
    }

    public static <T> T parseObjectFromSimpleString(String input) {
        return null;
    }
}

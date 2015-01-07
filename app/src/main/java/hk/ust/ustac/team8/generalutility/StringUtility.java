package hk.ust.ustac.team8.generalutility;

/**
 * Some static methods that are related to Java String
 */
public class StringUtility {

    private static final char[] LOWERHEXDIGITS = { '0','1','2','3','4','5','6','7','8','9', 'a','b','c','d','e','f' };

    /**
     * Create a corresponding hex string (lower case) of a byte array
     *
     * @param byteArray the byte array to be used
     * @return the lower case hex string of the given bytearray
     */
    public static String byteArrayToLowerHexString(byte[] byteArray) {

        char[] resultCharArray = new char[byteArray.length * 2];

        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = LOWERHEXDIGITS[b >>> 4 & 0xf];
            resultCharArray[index++] = LOWERHEXDIGITS[b & 0xf];
        }

        return new String(resultCharArray);
    }
}

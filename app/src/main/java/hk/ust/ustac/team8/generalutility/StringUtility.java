package hk.ust.ustac.team8.generalutility;

/**
 * Created by logchan on 1/5/2015.
 */
public class StringUtility {

    private static final char[] HEXDIGITS = { '0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };

    public static String byteArrayToHexString(byte[] byteArray) {

        char[] resultCharArray = new char[byteArray.length * 2];

        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = HEXDIGITS[b >>> 4 & 0xf];
            resultCharArray[index++] = HEXDIGITS[b & 0xf];
        }

        return new String(resultCharArray);
    }
}

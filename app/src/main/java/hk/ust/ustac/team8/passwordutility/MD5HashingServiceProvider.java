package hk.ust.ustac.team8.passwordutility;

import hk.ust.ustac.team8.generalutility.StringUtility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * A hashing service provider that hash the string using MD5
 * The result is a lower-cased hex string
 *
 * @author logchan
 * @see hk.ust.ustac.team8.passwordutility.HashingServiceProvider
 */
public class MD5HashingServiceProvider implements HashingServiceProvider {

    /**
     * Produce the MD5 hashing of input. The result is lower-cased hex string.
     *
     * @param input the string to be hashed
     * @return lower-cased hex string representation of the hashing result, or null if exception occurred
     */
    public String hash(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] inputArray = input.getBytes();
            messageDigest.update(inputArray);
            byte[] outputArray = messageDigest.digest();

            return StringUtility.byteArrayToLowerHexString(outputArray);
        }
        catch (NoSuchAlgorithmException ex) {
            return null;
        }
        catch (Exception ex) {
            return null;
        }
    }
}

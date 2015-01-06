package hk.ust.ustac.team8.passwordutility;

import hk.ust.ustac.team8.generalutility.StringUtility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by logchan on 1/5/2015.
 */
public class MD5HashingServiceProvider implements HashingServiceProvider {

    public String hash(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] inputArray = input.getBytes();
            messageDigest.update(inputArray);
            byte[] outputArray = messageDigest.digest();

            return StringUtility.byteArrayToHexString(outputArray);
        }
        catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
}

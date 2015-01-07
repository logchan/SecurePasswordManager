package hk.ust.ustac.team8.passwordutility;

/**
 * A string transform service provider that mix the case of the input.
 * The 1st, 3rd, 5th,... letters are lower-cased,
 * and the 2nd, 4th, 6th,... letters are upper-cased.
 * For example,
 * abdef   will become aBdEf,
 * ab5de1f will become aB5dE1f. Note that the inserted numbers do not affect the result.
 *
 * @author logchan
 * @see hk.ust.ustac.team8.passwordutility.StringTransformServiceProvider
 */
public class MixedUpperAndLowerCaseTransformer implements StringTransformServiceProvider {

    /**
     * Do the transformation on the input string. Check the doc of the class to see what the
     * result looks like.
     *
     * @param input the string to be transformed
     * @return the transformed string
     */
    public String transform(String input) {

        String result = "";
        input = input.toLowerCase();

        boolean isUpper = false;
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            if ('a' <= c && 'z' >= c) {
                result += isUpper ? c + ('A' - 'a') : c;
                isUpper = !isUpper;
            }
            else {
                result += c;
            }
        }

        return result;
    }
}

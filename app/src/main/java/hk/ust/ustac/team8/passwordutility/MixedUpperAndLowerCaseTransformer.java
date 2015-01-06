package hk.ust.ustac.team8.passwordutility;

/**
 * Created by logchan on 1/6/2015.
 */
public class MixedUpperAndLowerCaseTransformer implements StringTransformServiceProvider {

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

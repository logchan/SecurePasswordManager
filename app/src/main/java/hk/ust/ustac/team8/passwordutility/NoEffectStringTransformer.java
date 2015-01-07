package hk.ust.ustac.team8.passwordutility;

/**
 * A string transform service provider that does NOTHING to the input.
 * In other words, the input will keep unchanged. This is a no-effect-transform.
 *
 * @author logchan
 * @see hk.ust.ustac.team8.passwordutility.StringTransformServiceProvider
 */
public class NoEffectStringTransformer implements StringTransformServiceProvider {

    /**
     * Do the transformation on the input string. Does nothing actually.
     *
     * @param input the string to be transformed
     * @return the transformed string
     */
    public String transform(String input) {
        return input;
    }
}

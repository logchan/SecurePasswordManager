package hk.ust.ustac.team8.passwordutility;

/**
 * Created by logchan on 1/6/2015.
 */
public class NoEffectStringTransformer implements StringTransformServiceProvider {

    public String transform(String input) {
        return input;
    }
}

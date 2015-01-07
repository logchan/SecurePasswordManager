package hk.ust.ustac.team8.passwordutility;

/**
 * An interface that can transform a String by some rule
 * For example, a transform can be turning all letters upper-cased,
 * or remove all digits.
 *
 * @author logchan
 */
public interface StringTransformServiceProvider {

    public String transform(String input);
}

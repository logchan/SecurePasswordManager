package hk.ust.ustac.team8.hashingscheme;

/**
 * An enum that denotes the final transform of a hashing scheme
 * Each item shall have a corresponding StringTransformServiceProvider
 *
 * @author logchan
 * @see hk.ust.ustac.team8.hashingscheme.HashingScheme
 */
public enum HashingSchemeTransform {
    NO_TRANSFORM,
    MIXED_UPPER_AND_LOWER_CASE
}

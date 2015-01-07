package hk.ust.ustac.team8.hashingscheme;

/**
 * An enum that denotes the corresponding salting type of a field
 * That is, how the value of this field is going to be salted
 * Each item shall have a corresponding SaltingServiceProvider
 *
 * @author logchan
 * @see hk.ust.ustac.team8.hashingscheme.HashingSchemeField
 */
public enum HashingSchemeSaltingType {
    APPEND,
    APPEND_ONCE
}

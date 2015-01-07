package hk.ust.ustac.team8.hashingscheme;

/**
 * An enum that denotes the type of value of a field
 * Not cared by the fields themselves, but by the view controller when asking the user to fill
 * in the blanks.
 *
 * @author logchan
 * @see hk.ust.ustac.team8.hashingscheme.HashingSchemeField
 */
public enum HashingSchemeFieldType {
    STRING,
    EMAIL,
    NUMBER
}

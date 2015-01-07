package hk.ust.ustac.team8.hashingscheme;

import hk.ust.ustac.team8.generalutility.LangUtility;

/**
 * Created by logchan on 1/6/2015.
 */
public class HashingSchemeField {

    private HashingSchemeFieldType type;

    private String name;

    private String description;

    private HashingSchemeSaltingType saltingType;

    public String value;

    public HashingSchemeField(HashingSchemeFieldType type, String name, String description) {
        LangUtility.assertNonNull(type, "Null type provided for initialization of HashingSchemeField");
        LangUtility.assertNonNull(name, "Null name provided for initialization of HashingSchemeField");
        LangUtility.assertNonNull(description, "Null description provided for initialization of HashingSchemeField");

        this.type = type;
        this.name = name;
        this.description = description;
        this.value = "";
        this.saltingType = HashingSchemeSaltingType.APPEND;
    }

    public HashingSchemeFieldType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public HashingSchemeSaltingType getSaltingType() {
        return saltingType;
    }

    public void setValue(String newValue) {
        LangUtility.assertNonNull(newValue, "Null new value provided for setValue of HshingSchemeField");

        this.value = newValue;
    }

    public void setSaltingType(HashingSchemeSaltingType newSaltingType) {
        LangUtility.assertNonNull(newSaltingType, "Null new salting type provided for setSaltingType of HshingSchemeField");

        this.saltingType = newSaltingType;
    }
}

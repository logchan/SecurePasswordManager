package hk.ust.ustac.team8.hashingscheme;

import hk.ust.ustac.team8.generalutility.LangUtility;

/**
 * Created by logchan on 1/6/2015.
 */
public class HashingSchemeField {

    private HashingSchemeFieldType type;

    private String name;

    private String description;

    public String value;

    public HashingSchemeField(HashingSchemeFieldType type, String name, String description) {
        LangUtility.assertNonNull(type, "Null type provided for initialization of HashingSchemeField");
        LangUtility.assertNonNull(name, "Null name provided for initialization of HashingSchemeField");
        LangUtility.assertNonNull(description, "Null description provided for initialization of HashingSchemeField");

        this.type = type;
        this.name = name;
        this.description = description;
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
}

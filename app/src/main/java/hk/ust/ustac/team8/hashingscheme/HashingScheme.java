package hk.ust.ustac.team8.hashingscheme;

import hk.ust.ustac.team8.generalutility.LangUtility;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by logchan on 1/6/2015.
 */
public class HashingScheme {

    private String name;

    private String description;

    private LinkedList<HashingSchemeField> fields;

    public HashingScheme(String name, String description) {
        LangUtility.assertNonNull(name, "Null name provided for initialization of HashingScheme");
        LangUtility.assertNonNull(description, "Null description provided for initialization of HashingScheme");

        this.name = name;
        this.description = description;
        this.fields = new LinkedList<HashingSchemeField>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getFieldCount() {
        return fields.size();
    }

    public HashingSchemeField getField(int index) {
        return fields.get(index);
    }

    public ListIterator<HashingSchemeField> getFieldIterator() {
        return fields.listIterator();
    }

    public void addField(HashingSchemeField newField) {
        LangUtility.assertNonNull(newField, "Null field provided for addField of HashingScheme");

        fields.add(newField);
    }
}

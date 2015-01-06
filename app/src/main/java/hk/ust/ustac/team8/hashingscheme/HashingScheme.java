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

    private HashingSchemeCrypto crypto;

    private HashingSchemeTransform transform;

    private LinkedList<HashingSchemeField> fields;

    public HashingScheme(String name, String description, HashingSchemeCrypto crypto) {
        LangUtility.assertNonNull(name, "Null name provided for initialization of HashingScheme");
        LangUtility.assertNonNull(description, "Null description provided for initialization of HashingScheme");
        LangUtility.assertNonNull(crypto, "Null crypto provided for initialization of HashingScheme");

        this.name = name;
        this.description = description;
        this.crypto = crypto;
        this.fields = new LinkedList<HashingSchemeField>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public HashingSchemeCrypto getCrypto() {
        return crypto;
    }

    public HashingSchemeTransform getTransform() {
        return transform;
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

    public void setName(String newName) {
        LangUtility.assertNonNull(newName, "Null name provided for setName of HashingScheme");

        this.name = newName;
    }

    public void setDescription(String newDescription) {
        LangUtility.assertNonNull(newDescription, "Null description provided for setDescription of HashingScheme");

        this.description = newDescription;
    }

    public void setCrypto(HashingSchemeCrypto newCrypto) {
        LangUtility.assertNonNull(newCrypto, "Null crypto provided for setCrypto of HashingScheme");

        this.crypto = newCrypto;
    }

    public void setTransform(HashingSchemeTransform newTransform) {
        LangUtility.assertNonNull(newTransform, "Null transform provided for setTransform of HashingScheme");

        this.transform = newTransform;
    }

    public void addField(HashingSchemeField newField) {
        LangUtility.assertNonNull(newField, "Null field provided for addField of HashingScheme");

        fields.add(newField);
    }
}

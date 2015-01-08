package hk.ust.ustac.team8.hashingscheme;

import hk.ust.ustac.team8.generalutility.LangUtility;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A class that represent a hashing scheme
 * A hashing scheme is all the information needed to create a hashing password
 * It includes the hashing crypto to be used, the salts to be added and the final transform to be done
 * Each salt is represented as a field with a name and a (optional) description.
 * The user shall fill in the fields in a scheme accordingly (info for a specified website, for example),
 * which will be used to salt.
 *
 * @author logchan
 * @see hk.ust.ustac.team8.hashingscheme.HashingSchemeCrypto
 * @see hk.ust.ustac.team8.hashingscheme.HashingSchemeField
 * @see hk.ust.ustac.team8.hashingscheme.HashingSchemeTransform
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

    public HashingSchemeField getField(Integer index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= fields.size()) {
            throw new IndexOutOfBoundsException("Getting invalid field " + index.toString());
        }

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

    public void removeField(Integer index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= fields.size()) {
            throw new IndexOutOfBoundsException("Removing invalid field " + index.toString());
        }

        fields.remove(index);
    }

    public String toStorageString() {

    }

    public static HashingScheme createInstanceFromStorageString() {

    }
}

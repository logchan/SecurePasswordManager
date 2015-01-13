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

    public HashingScheme(String name, String description, HashingSchemeCrypto crypto, HashingSchemeTransform transform) {
        LangUtility.assertNonNull(name, "Null name provided for initialization of HashingScheme");
        LangUtility.assertNonNull(description, "Null description provided for initialization of HashingScheme");
        LangUtility.assertNonNull(crypto, "Null crypto provided for initialization of HashingScheme");
        LangUtility.assertNonNull(transform, "Null transform provided for initialization of HashingScheme");

        this.name = name;
        this.description = description;
        this.crypto = crypto;
        this.transform = transform;
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

        fields.remove((int) index);
    }

    /**
     * Generate a string for storing and restoring this scheme.
     * Normally this process is done by "Serialization"
     * But this is a small project and I'm just too tired to do that (and time is tight)
     * So here's the ugly solution. Maybe it will be altered someday,
     * and this will become depreciated.
     *
     * The string has several lines. Each line is a class member, starting with its name and a '|'
     * Fields are in order.
     *
     * @return a string representation of the whole scheme (not including filled values)
     */
    public String toStorageString() {
        StringBuilder builder = new StringBuilder();

        // class member fields first

        builder.append("name|");
        builder.append(name);
        builder.append('\n');

        builder.append("description|");
        builder.append(description);
        builder.append('\n');

        builder.append("crypto|");
        builder.append(crypto.toString());
        builder.append('\n');

        builder.append("transform|");
        builder.append(transform.toString());
        builder.append('\n');

        // fields one by one

        for (HashingSchemeField field : fields) {
            builder.append("startfield");
            builder.append('\n');
            builder.append(field.toStorageString());
            builder.append("endfield");
            builder.append('\n');
        }

        return builder.toString();
    }

    /**
     * Parse a string generated by toStorageString() to a scheme.
     * Check the documentation for toStorageString() for more information.
     *
     * @param input the input to be parsed
     * @return a scheme parsed from the input string representation
     */
    public static HashingScheme fromStorageString(String input) {
        HashingScheme scheme = new HashingScheme("", "", HashingSchemeCrypto.MD5, HashingSchemeTransform.NO_TRANSFORM);
        String[] lines = input.split("[\\r\\n]+");

        for (int i = 0; i < lines.length; ++i) {
            String line = lines[i];

            if (line.startsWith("name|")) {
                scheme.setName(line.substring(5));
            }
            else if (line.startsWith("description|")) {
                scheme.setDescription(line.substring(12));
            }
            else if (line.startsWith("crypto|")) {
                HashingSchemeCrypto nCrypto = Enum.valueOf(HashingSchemeCrypto.class, line.substring(7));
                scheme.setCrypto(nCrypto);
            }
            else if (line.startsWith("transform|")) {
                HashingSchemeTransform nTransform = Enum.valueOf(HashingSchemeTransform.class, line.substring(10));
                scheme.setTransform(nTransform);
            }
            else if (line.startsWith("startfield")) {
                StringBuilder builder = new StringBuilder();

                ++i;
                while (i < lines.length) {
                    if (lines[i].equals("endfield")) {
                        break;
                    }
                    else {
                        builder.append(lines[i]);
                        builder.append('\n');
                    }
                    ++i;
                }

                scheme.addField(HashingSchemeField.fromStorageString(builder.toString()));
            }
        }

        return scheme;
    }

    /**
     * Get the values of all fields in order. Seperated by linebreak ('\n').
     *
     * @return values of all fields, one per line
     */
    public String exportFieldValues() {
        StringBuilder builder = new StringBuilder();

        for (HashingSchemeField field : fields) {
            builder.append(field.getValue());
            builder.append('\n');
        }

        return builder.toString();
    }

    /**
     * Set the values of fields in order. Values are seperated by a linebreak ('\n').
     *
     * @param input values of all fields, one per line
     */
    public void importFieldValues(String input) {
        // for mixed format
        input = input.replaceAll("\\r\\n", "\n");
        input = input.replaceAll("\\r", "\n");
        String[] lines = input.split("\\n");
        importFieldValues(lines);
    }

    /**
     * Set the values of fields in order. If too many values are provided, only the first N values
     * will be taken (where N is the number of fields); if too few (K < N) values are provided, the
     * last N-K values will remain unchanged.
     *
     * @param values the values of fields
     */
    public void importFieldValues(String[] values) {
        int bound = values.length;

        int i = 0;
        for (HashingSchemeField field : fields) {
            if (i >= bound) {
                break;
            }
            else {
                field.setValue(values[i]);
                ++i;
            }
        }
    }
}

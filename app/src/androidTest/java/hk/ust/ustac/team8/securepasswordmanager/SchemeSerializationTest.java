package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.hashingscheme.HashingScheme;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeCrypto;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeField;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeFieldType;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeTransform;

import android.test.InstrumentationTestCase;

/**
 * Created by logchan on 1/9/2015.
 */
public class SchemeSerializationTest extends InstrumentationTestCase {

    public void test1() {
        HashingScheme scheme = new HashingScheme("a scheme", "just a test scheme.", HashingSchemeCrypto.MD5, HashingSchemeTransform.NO_TRANSFORM);
        scheme.addField(new HashingSchemeField(HashingSchemeFieldType.STRING, "one field", "one field for test"));
        scheme.addField(new HashingSchemeField(HashingSchemeFieldType.EMAIL, "another field", "one different field for test"));

        String storage = scheme.toStorageString();
        HashingScheme restoredS = HashingScheme.fromStorageString(storage);

        assertEquals("a scheme", restoredS.getName());
        assertEquals("just a test scheme.", restoredS.getDescription());
        assertEquals(HashingSchemeCrypto.MD5, restoredS.getCrypto());
        assertEquals(HashingSchemeTransform.NO_TRANSFORM, restoredS.getTransform());
        assertEquals(2, restoredS.getFieldCount());

        assertEquals(HashingSchemeFieldType.STRING, restoredS.getField(0).getType());
        assertEquals("one field", restoredS.getField(0).getName());
        assertEquals("one field for test", restoredS.getField(0).getDescription());

        assertEquals(HashingSchemeFieldType.EMAIL, restoredS.getField(1).getType());
        assertEquals("another field", restoredS.getField(1).getName());
        assertEquals("one different field for test", restoredS.getField(1).getDescription());
    }

    // without any fields
    public void test2() {
        HashingScheme scheme = new HashingScheme("a scheme", "just a test scheme.", HashingSchemeCrypto.MD5, HashingSchemeTransform.NO_TRANSFORM);

        String storage = scheme.toStorageString();
        HashingScheme restoredS = HashingScheme.fromStorageString(storage);

        assertEquals(0, scheme.getFieldCount());
    }
}

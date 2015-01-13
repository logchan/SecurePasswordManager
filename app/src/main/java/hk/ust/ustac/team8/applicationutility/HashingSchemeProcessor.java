package hk.ust.ustac.team8.applicationutility;

import java.util.HashMap;

import hk.ust.ustac.team8.generalutility.HashMapUtility;
import hk.ust.ustac.team8.generalutility.LangUtility;
import hk.ust.ustac.team8.hashingscheme.HashingScheme;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeCrypto;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeField;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeSaltingType;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeTransform;
import hk.ust.ustac.team8.passwordutility.BasicSaltAppender;
import hk.ust.ustac.team8.passwordutility.HashingPasswordGenerator;
import hk.ust.ustac.team8.passwordutility.HashingServiceProvider;
import hk.ust.ustac.team8.passwordutility.MD5HashingServiceProvider;
import hk.ust.ustac.team8.passwordutility.MixedUpperAndLowerCaseTransformer;
import hk.ust.ustac.team8.passwordutility.NoEffectStringTransformer;
import hk.ust.ustac.team8.passwordutility.OnceSaltAppender;
import hk.ust.ustac.team8.passwordutility.SaltingServiceProvider;
import hk.ust.ustac.team8.passwordutility.StringTransformServiceProvider;

import java.util.ListIterator;

/**
 * A bridge between HashingScheme and PasswordUtility
 * This class is singleton.
 *
 * @author logchan
 */
public class HashingSchemeProcessor {

    private static HashingSchemeProcessor instance = null;

    private HashMap<HashingSchemeCrypto, HashingServiceProvider> hashingHashmap;

    private HashMap<HashingSchemeSaltingType, SaltingServiceProvider> saltingHashMap;

    private HashMap<HashingSchemeTransform, StringTransformServiceProvider> transformHashMap;

    private HashingSchemeProcessor() {
        hashingHashmap = new HashMap<HashingSchemeCrypto, HashingServiceProvider>();
        saltingHashMap = new HashMap<HashingSchemeSaltingType, SaltingServiceProvider>();
        transformHashMap = new HashMap<HashingSchemeTransform, StringTransformServiceProvider>();
    }

    public static HashingSchemeProcessor getInstance() {
        if (instance == null) {
            instance = new HashingSchemeProcessor();
        }
        return instance;
    }

    public void registerHashingServiceProvider(HashingSchemeCrypto crypto, HashingServiceProvider provider) {
        HashMapUtility.conditionalNonNullUpdate(hashingHashmap, crypto, provider);
    }

    public HashingServiceProvider getHashingServiceProvider(HashingSchemeCrypto crypto) throws ClassNotFoundException {
        if (hashingHashmap.containsKey(crypto)) {
            return hashingHashmap.get(crypto);
        }
        else {
            throw new ClassNotFoundException("No registered hashing service provider for crypto " + crypto.toString());
        }
    }

    public void registerSaltingServiceProvider(HashingSchemeSaltingType saltingType, SaltingServiceProvider provider) {
        HashMapUtility.conditionalNonNullUpdate(saltingHashMap, saltingType, provider);
    }

    public SaltingServiceProvider getSaltingServiceProvider(HashingSchemeSaltingType saltingType, String salt) throws ClassNotFoundException, InstantiationException {
        if (saltingHashMap.containsKey(saltingType)) {

            // warning: this step is different from get hashing/transform service provider
            // because we need a different instance of salting service provider
            // for each salt!

            SaltingServiceProvider provider = saltingHashMap.get(saltingType);

            Object newProvider = LangUtility.getObjectOfSameClass(provider, new Object[] { salt });
            if (newProvider != null) {
                return (SaltingServiceProvider)newProvider;
            }
            else {
                throw new InstantiationException("Failed initializing an instance of " + provider.getClass().getName());
            }
        }
        else {
            throw new ClassNotFoundException("No registered salting service provider for salting " + saltingType.toString());
        }
    }

    public void registerTransformServiceProvider(HashingSchemeTransform transform, StringTransformServiceProvider provider) {
        HashMapUtility.conditionalNonNullUpdate(transformHashMap, transform, provider);
    }

    public StringTransformServiceProvider getStringTransformServiceProvider(HashingSchemeTransform transform) throws ClassNotFoundException {
        if (transformHashMap.containsKey(transform)) {
            return transformHashMap.get(transform);
        }
        else {
            throw new ClassNotFoundException("No registered transform service provider for transform " + transform.toString());
        }
    }

    public HashingPasswordGenerator getHashingPasswordGeneratorFromScheme(HashingScheme scheme) throws ClassNotFoundException, InstantiationException{

        HashingServiceProvider provider = getHashingServiceProvider(scheme.getCrypto());
        StringTransformServiceProvider transform = getStringTransformServiceProvider(scheme.getTransform());

        HashingPasswordGenerator generator = new HashingPasswordGenerator(provider, transform);

        // generate salt for fields
        ListIterator<HashingSchemeField> fieldListIterator = scheme.getFieldIterator();
        while (fieldListIterator.hasNext()) {
            HashingSchemeField field = fieldListIterator.next();
            generator.addSalt(getSaltingServiceProvider(field.getSaltingType(), field.getValue()));
        }

        return generator;
    }
}

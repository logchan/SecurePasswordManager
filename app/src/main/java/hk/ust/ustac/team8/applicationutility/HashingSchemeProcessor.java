package hk.ust.ustac.team8.applicationutility;

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
 * Created by logchan on 1/6/2015.
 */
public class HashingSchemeProcessor {

    public static HashingServiceProvider getHashingServiceProviderFromSchemeCrypto(HashingSchemeCrypto crypto) throws ClassNotFoundException {

        switch (crypto) {
            case MD5:
                return new MD5HashingServiceProvider();
            default:
                throw new ClassNotFoundException("No hashing service provider for crypto " + crypto.toString());
        }
    }

    public static StringTransformServiceProvider getStringTransformServiceProviderFromSchemeTransform(HashingSchemeTransform transform) throws ClassNotFoundException {

        switch (transform) {
            case NO_TRANSFORM:
                return new NoEffectStringTransformer();
            case MIXED_UPPER_AND_LOWER_CASE:
                return new MixedUpperAndLowerCaseTransformer();
            default:
                throw new ClassNotFoundException("No transform service provider for transform " + transform.toString());
        }
    }

    public static SaltingServiceProvider getSaltingServiceProviderFromSchemeFieldSalting(HashingSchemeSaltingType salting, String salt) throws ClassNotFoundException {

        switch (salting) {
            case APPEND:
                return new BasicSaltAppender(salt);
            case APPEND_ONCE:
                return new OnceSaltAppender(salt);
            default:
                throw new ClassNotFoundException("No salting service provider for type " + salting.toString());
        }
    }

    public static HashingPasswordGenerator getHashingPasswordGeneratorFromScheme(HashingScheme scheme) throws ClassNotFoundException{

        HashingServiceProvider provider = getHashingServiceProviderFromSchemeCrypto(scheme.getCrypto());
        StringTransformServiceProvider transform = getStringTransformServiceProviderFromSchemeTransform(scheme.getTransform());

        HashingPasswordGenerator generator = new HashingPasswordGenerator(provider, transform);

        // generate salt for fields
        ListIterator<HashingSchemeField> fieldListIterator = scheme.getFieldIterator();
        while (fieldListIterator.hasNext()) {
            HashingSchemeField field = fieldListIterator.next();
            generator.addSalt(getSaltingServiceProviderFromSchemeFieldSalting(field.getSaltingType(), field.getValue()));
        }

        return generator;
    }
}

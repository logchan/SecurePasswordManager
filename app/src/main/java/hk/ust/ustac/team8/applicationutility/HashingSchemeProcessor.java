package hk.ust.ustac.team8.applicationutility;

import hk.ust.ustac.team8.hashingscheme.HashingScheme;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeCrypto;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeTransform;
import hk.ust.ustac.team8.passwordutility.HashingPasswordGenerator;
import hk.ust.ustac.team8.passwordutility.HashingServiceProvider;
import hk.ust.ustac.team8.passwordutility.MD5HashingServiceProvider;
import hk.ust.ustac.team8.passwordutility.MixedUpperAndLowerCaseTransformer;
import hk.ust.ustac.team8.passwordutility.NoEffectStringTransformer;
import hk.ust.ustac.team8.passwordutility.StringTransformServiceProvider;


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

    public static HashingPasswordGenerator getHashingPasswordGeneratorFromScheme(HashingScheme scheme) throws ClassNotFoundException{

        HashingServiceProvider provider = getHashingServiceProviderFromSchemeCrypto(scheme.getCrypto());
        StringTransformServiceProvider transform = getStringTransformServiceProviderFromSchemeTransform(scheme.getTransform());

        HashingPasswordGenerator generator = new HashingPasswordGenerator(provider, transform);

        //TODO: add salt services for generator
    }
}

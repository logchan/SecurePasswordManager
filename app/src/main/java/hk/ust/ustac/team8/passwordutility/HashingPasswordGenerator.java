package hk.ust.ustac.team8.passwordutility;

import hk.ust.ustac.team8.generalutility.LangUtility;
import java.util.LinkedList;



/**
 * Created by logchan on 1/5/2015.
 */
public class HashingPasswordGenerator {

    private HashingServiceProvider hashingServiceProvider;

    private LinkedList<SaltingServiceProvider> saltingServiceProviders;

    private StringTransformServiceProvider stringTransformServiceProvider;

    public HashingPasswordGenerator(HashingServiceProvider hashingServiceProvider, StringTransformServiceProvider stringTransformServiceProvider) {
        LangUtility.assertNonNull(hashingServiceProvider, "Null HashingServiceProvider provided for initialization of HashingPasswordGenerator");
        LangUtility.assertNonNull(stringTransformServiceProvider, "Null StringTransformServiceProvider provided for initialization of HashingPasswordGenerator");

        this.hashingServiceProvider = hashingServiceProvider;
        this.saltingServiceProviders = new LinkedList<SaltingServiceProvider>();
        this.stringTransformServiceProvider = stringTransformServiceProvider;
    }

    public String generatePassword(String secret, int iterations) {
        String result = secret;
        AdditionalSaltingInformation info = new AdditionalSaltingInformation();

        for (int i = 0; i < iterations; ++i) {
            // set information
            info.currentIteration = i;

            // append salts
            for (SaltingServiceProvider saltingServiceProvider : this.saltingServiceProviders) {
                result = saltingServiceProvider.addSalt(result, info);
            }

            // do hashing
            result = this.hashingServiceProvider.hash(result);
        }

        result = this.stringTransformServiceProvider.transform(result);

        return result;
    }
}

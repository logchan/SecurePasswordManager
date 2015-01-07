package hk.ust.ustac.team8.passwordutility;

import hk.ust.ustac.team8.generalutility.LangUtility;

import java.util.LinkedList;



/**
 * A class that help generate the hashing password that make use of
 * a HashingServiceProvider, several SaltingServiceProviders, and a StrtingTransformServiceProvider.
 *
 * @author logchan
 * @see hk.ust.ustac.team8.passwordutility.HashingServiceProvider
 * @see hk.ust.ustac.team8.passwordutility.SaltingServiceProvider
 * @see hk.ust.ustac.team8.passwordutility.StringTransformServiceProvider
 */
public class HashingPasswordGenerator {

    /**
     * A class that wraps the information provided for salting
     * The information shall be modified by the HashingPasswordGenerator,
     * and the SaltingServiceProviders may read and use them
     */
    public class AdditionalSaltingInformation {

        private int currentIteration;

        /**
         * Get the iteration of the hashing process. Starts from 0.
         *
         * @return the on-going iteration
         */
        public int getCurrentIteration() {
            return currentIteration;
        }
    }

    private HashingServiceProvider hashingServiceProvider;

    private LinkedList<SaltingServiceProvider> saltingServiceProviders;

    private StringTransformServiceProvider stringTransformServiceProvider;

    /**
     * The constructor of HashingPasswordGenerator
     * @param hashingServiceProvider the HashingServiceProvider instance that is used to do the hashing
     * @param stringTransformServiceProvider the StringTransformServiceProvider instance that is used to tranform the final hashing result
     */
    public HashingPasswordGenerator(HashingServiceProvider hashingServiceProvider, StringTransformServiceProvider stringTransformServiceProvider) {
        LangUtility.assertNonNull(hashingServiceProvider, "Null HashingServiceProvider provided for initialization of HashingPasswordGenerator");
        LangUtility.assertNonNull(stringTransformServiceProvider, "Null StringTransformServiceProvider provided for initialization of HashingPasswordGenerator");

        this.hashingServiceProvider = hashingServiceProvider;
        this.saltingServiceProviders = new LinkedList<SaltingServiceProvider>();
        this.stringTransformServiceProvider = stringTransformServiceProvider;
    }

    /**
     * Add a salting service provider to the salting process pipeline (a linked list).
     *
     * @param saltingServiceProvider a SaltingServiceProvider instance that is to be added
     */
    public void addSalt(SaltingServiceProvider saltingServiceProvider) {
        LangUtility.assertNonNull(hashingServiceProvider, "Null SaltingServiceProvider provided for addSalt in HashingPasswordGenerator");

        this.saltingServiceProviders.add(saltingServiceProvider);
    }

    /**
     * Generate the hashing password from the given secret and number of iterations.
     * The result is generated in this way:
     * 1. S = secret
     * 2. for each SaltingServiceProvider P in my providers:
     *      S = P.addSalt(S)
     * 3. S = hash(S)
     * 4. Step (2, 3) are repeated (iterations) times
     * 5. S = transform(S)
     * 6. output S
     *
     * @param secret the secret for the salting and hashing to start with
     * @param iterations the number of iterations of salting and hashing
     * @return the generated hashing password
     */
    public String generatePassword(String secret, int iterations) throws Exception {
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
            if (result == null) {
                throw new Exception("Hashing stage failed.");
            }
        }

        result = this.stringTransformServiceProvider.transform(result);

        return result;
    }
}

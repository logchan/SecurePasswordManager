package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.applicationutility.AppFileUtility;
import hk.ust.ustac.team8.generalutility.LangUtility;
import hk.ust.ustac.team8.hashingscheme.HashingScheme;

import android.content.Context;

import java.util.LinkedList;

/**
 * Singleton that store the information during the application lifetime
 *
 * @author logchan
 */
public final class ApplicationManager {

    private static ApplicationManager instance = null;

    private static Context context = null;

    private LinkedList<HashingScheme> schemes = null;

    private ApplicationSettings settings;
    /**
     * Private constructor to avoid more than one instance
     */
    private ApplicationManager() {

    }

    /**
     * Set up the class with given Context. Shall only be called once!
     *
     * @throws java.lang.InstantiationException when this method is called more than once
     */
    public static void setUp(Context context) throws InstantiationException {
        LangUtility.assertNonNull(context, "Null context provided for setUp of ApplicationManager");

        if (instance != null) {
            throw new InstantiationException("ApplicationManager already set up.");
        }

        ApplicationManager.context = context;
        instance = new ApplicationManager();
        instance.settings = new ApplicationSettings();
    }

    /**
     * Get the instance of this class. The class shall have been setup by calling setUp().
     * Otherwise, null will be returned.
     *
     * @return the instance
     */
    public static ApplicationManager getInstance() {
        return instance;
    }

    /**
     * Load all the schemes
     */
    public void reloadAllSchemes() {
        schemes = AppFileUtility.getAllSchemes(context);
    }

    /**
     * Get the linkedlist of all schemes
     *
     * @return the schemes
     */
    public LinkedList<HashingScheme> getAllSchemes() {
        return schemes;
    }

    /**
     * Get the settings of application
     *
     * @return the settings
     */
    public ApplicationSettings getSettings() {
        return settings;
    }
}

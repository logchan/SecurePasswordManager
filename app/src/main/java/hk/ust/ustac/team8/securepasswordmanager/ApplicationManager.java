package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.applicationutility.AppFileUtility;
import hk.ust.ustac.team8.applicationutility.HashingSchemeProcessor;
import hk.ust.ustac.team8.generalutility.LangUtility;
import hk.ust.ustac.team8.hashingscheme.HashingScheme;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeCrypto;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeSaltingType;
import hk.ust.ustac.team8.hashingscheme.HashingSchemeTransform;
import hk.ust.ustac.team8.passwordutility.BasicSaltAppender;
import hk.ust.ustac.team8.passwordutility.MD5HashingServiceProvider;
import hk.ust.ustac.team8.passwordutility.MixedUpperAndLowerCaseTransformer;
import hk.ust.ustac.team8.passwordutility.NoEffectStringTransformer;
import hk.ust.ustac.team8.passwordutility.OnceSaltAppender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Iterator;
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

    private HashingSchemeProcessor schemeProcessor = null;

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

        instance.schemeProcessor = HashingSchemeProcessor.getInstance();
        instance.schemeProcessor.registerHashingServiceProvider(HashingSchemeCrypto.MD5, new MD5HashingServiceProvider());
        instance.schemeProcessor.registerSaltingServiceProvider(HashingSchemeSaltingType.APPEND, new BasicSaltAppender(""));
        instance.schemeProcessor.registerSaltingServiceProvider(HashingSchemeSaltingType.APPEND_ONCE, new OnceSaltAppender(""));
        instance.schemeProcessor.registerTransformServiceProvider(HashingSchemeTransform.NO_TRANSFORM, new NoEffectStringTransformer());
        instance.schemeProcessor.registerTransformServiceProvider(HashingSchemeTransform.MIXED_UPPER_AND_LOWER_CASE, new MixedUpperAndLowerCaseTransformer());
    }

    public static boolean alreadySetUp() {
        return instance != null;
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
     * Get the instance of this class. If the class has not been set up, it will be.
     *
     * @param context the application context
     * @return the instance
     */
    public static ApplicationManager getInstanceSafe(Context context) {
        if (instance == null) {
            try {
                setUp(context);
            } catch (InstantiationException e) {
                // this is unlikely to happen
            }
        }
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

    /**
     * Get the scheme specified by the name.
     *
     * @param name the name of scheme
     * @return the scheme, null if not found
     */
    public HashingScheme getSchemeByName(String name) {
        for (HashingScheme scheme : schemes) {
            if (scheme.getName().equals(name)) {
                return scheme;
            }
        }
        return null;
    }

    public String getAllSchemesName(String separator) {
        StringBuilder builder = new StringBuilder();
        for (HashingScheme scheme : schemes) {
            builder.append(scheme.getName());
            builder.append(separator);
        }
        return builder.toString();
    }
    /**
     * Save the scheme.
     *
     * @param scheme the scheme to save
     * @return true if saved successfully, false otherwise
     */
    public boolean saveScheme(HashingScheme scheme) {
        if (AppFileUtility.saveScheme(context, scheme)) {
            reloadAllSchemes();
            return true;
        } else {
            return false;
        }
    }

    public boolean renameScheme(String oldName, String newName) {
        boolean fileSuccess = false;
        try {
            fileSuccess = AppFileUtility.renameSchemeFile(context, oldName, newName);
            return fileSuccess;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete the scheme specified by the name, then reload all schemes
     *
     * @param name the name of scheme to be deleted
     * @return true if successfully deleted, false otherwise
     */
    public boolean deleteSchemeByName(String name) {
        boolean fileDeleteSuccess = false;

        // try to delete scheme files
        try {
            fileDeleteSuccess = AppFileUtility.deleteScheme(context, name);
        } catch (Exception e) {
            return false;
        }

        if (fileDeleteSuccess) {
            reloadAllSchemes();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Load the application settings from file
     */
    public boolean loadAppSettings() {
        boolean loadingResult = true;
        settings = AppFileUtility.loadAppSettings(context);
        if (settings == null) {
            loadingResult = false;
            settings = new ApplicationSettings();
        }
        AppFileUtility.saveAppSettings(context, settings);
        return loadingResult;
    }

    /**
     * Save the current application settings to file
     */
    public boolean saveAppSettings() {
        return AppFileUtility.saveAppSettings(context, settings);
    }

    public void popState(ApplicationState last) {
        if (last == null) {
            last = settings.currentState;
        }
        settings.currentState = settings.lastState;
        settings.lastState = last;
    }

    public void switchActivity(Activity currentActivity, Class nextActivity,
                               ApplicationState newState, Object... carriedInfo) {

        settings.lastState = settings.currentState;
        settings.currentState = newState;
        settings.carriedInfo = carriedInfo == null ? new Object[]{} : carriedInfo;

        Intent intent = new Intent(currentActivity, nextActivity);
        currentActivity.startActivity(intent);
    }

    public void switchToMainClear(Activity currentActivity) {
        settings.currentState = ApplicationState.MAIN;
        settings.lastState = ApplicationState.INIT;
        settings.carriedInfo = new Object[] { };

        Intent intent = new Intent(currentActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        currentActivity.startActivity(intent);
    }

    public LinkedList<String> getAllSavedInfo(String schemeName) {
        try {
            return AppFileUtility.getAllSavedInfoOfScheme(context, schemeName);
        } catch (Exception e) {
            return null;
        }
    }

    public String getOneSavedInfo(String schemeName, String infoName) {
        try {
            return AppFileUtility.getOneSavedInfoOfScheme(context, schemeName, infoName);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean saveOneInfo(HashingScheme scheme, String infoName) {
        try {
            return AppFileUtility.saveInfoOfScheme(context, scheme, infoName);
        } catch (Exception e) {
            return false;
        }
    }

    public HashingSchemeProcessor getSchemeProcessor() {
        return schemeProcessor;
    }

    public boolean deleteschemeInfo(String schemeName, String infoName) {
        boolean fileDeleteSuccess = false;
        try {
            return fileDeleteSuccess = AppFileUtility.deleteInfoOfScheme(context, getSchemeByName(schemeName), infoName);
        } catch (Exception e) {
            return false;
        }
    }

    public void showToast(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showToastLong(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public boolean nameIsValid(String name) {
        return name.matches("[a-zA-Z0-9\\.\\-_\\(\\)\\[\\]\\s]+");
    }
}
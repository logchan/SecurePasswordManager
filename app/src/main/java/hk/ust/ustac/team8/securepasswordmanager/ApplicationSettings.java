package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.generalutility.LangUtility;
import hk.ust.ustac.team8.hashingscheme.HashingScheme;

/**
 * A wrapper class that contains all settings of the application
 *
 *  */
public class ApplicationSettings {

    private static final String[] EXPORT_FIELDS = { "hideResult", "resultFontsize" };

    public String lastHashingResult = "";

    public HashingScheme currentScheme;

    public boolean hideResult = false;

    public int resultFontsize = 22;

    public ApplicationSettings()
    {

    }

    public String exportString() {
        return LangUtility.getSimpleStringRepresentation(this, EXPORT_FIELDS);
    }

    public static ApplicationSettings importString(String input) {
        return (ApplicationSettings)LangUtility.parseObjectFromSimpleString(ApplicationSettings.class, input);
    }
}

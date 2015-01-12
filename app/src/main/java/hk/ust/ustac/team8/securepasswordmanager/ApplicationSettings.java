package hk.ust.ustac.team8.securepasswordmanager;

import hk.ust.ustac.team8.generalutility.LangUtility;

/**
 * A wrapper class that contains all settings of the application
 *
 *  */
public class ApplicationSettings {

    private static final String[] EXPORT_FIELDS = { "hideResult", "resultFontsize" };

    public String lastHashingResult = "";

    public boolean hideResult = false;

    public int resultFontsize = 22;

    public ApplicationSettings()
    {

    }

    public String exportString() {
        return LangUtility.getSimpleStringRepresentation(this, EXPORT_FIELDS);
    }

    public void importString(String input) {

    }
}

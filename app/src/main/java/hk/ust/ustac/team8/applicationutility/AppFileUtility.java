package hk.ust.ustac.team8.applicationutility;

import hk.ust.ustac.team8.exception.SchemeInfoNotFoundException;
import hk.ust.ustac.team8.exception.SchemeNotFoundException;
import hk.ust.ustac.team8.generalutility.FileUtility;
import hk.ust.ustac.team8.hashingscheme.HashingScheme;
import hk.ust.ustac.team8.securepasswordmanager.ApplicationSettings;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Some static methods involving the files of this application
 */
public final class AppFileUtility {

    public final static String SETTING_FILE = "spm.settings";
    public final static String SCHEME_FILE_EXT = ".hs";
    public final static String SCHEME_INFO_FILE_EXT = ".hsif";
    /**
     * Private constructor to prevent creating instance
     */
    private AppFileUtility() {

    }

    /**
     * Get the directory for schemes. Will create if not exists. If a file of the same name exists,
     * it will be deleted.
     *
     * @param context the app context
     * @return the File object of the schemes dir
     */
    private static File getSchemeDir(Context context) {
        File dir = context.getFilesDir();
        File sDir = new File(dir, "schemes");

        if (!sDir.exists()) {
            sDir.mkdir();
        }
        else if (!sDir.isDirectory()) {
            sDir.delete();
            sDir.mkdir();
        }

        return sDir;
    }

    /**
     * Read the file content, all lines are separated by '\n'. Return null if failed.
     * @param file an existing file
     * @return File content in a String, null if failed reading
     */
    private static String getFileContent(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }

            return builder.toString();
        }
        catch (FileNotFoundException ex) {
            return null;
        }
        catch (IOException ex) {
            return null;
        }
    }

    /**
     * Make sure that the scheme folder exists.
     * Throw an exception if it doesn't or if it is not a directory.
     *
     * @param schemeDir the File object representing the scheme dir
     * @param caller the caller of this method, used to generate exception message
     * @throws SchemeNotFoundException if the schemeDir is not found or it is not a directory.
     */
    private static void assertSchemeFolderExists(File schemeDir, String caller) throws SchemeNotFoundException {
        if (!schemeDir.exists() || !schemeDir.isDirectory()) {
            throw new SchemeNotFoundException("Scheme dir not found for " + caller + ":" + schemeDir.getAbsolutePath());
        }
    }

    /**
     * Make sure that the scheme info file exists.
     * Throw an exception if it doesn't or if it is not a file.
     *
     * @param infoFile the File representing the saved info file
     * @param caller the caller of this method, used to generate exception message
     * @throws SchemeInfoNotFoundException if the infoFile is not found or it is not a file.
     */
    private static void assertSchemeInfoFileExists(File infoFile, String caller) throws SchemeInfoNotFoundException {
        if (!infoFile.exists() || !infoFile.isFile()) {
            throw new SchemeInfoNotFoundException("Scheme info file not found for " + caller + ":" + infoFile.getAbsolutePath());
        }
    }

    /**
     * Save a scheme. If a scheme of the same name exists, it will be overwritten (saved info are
     * not affected). If a file of the same name exists in the schemes folder, it will be deleted
     * (and if it can't be, the saving will fail).
     *
     * @param context the app context
     * @param scheme the scheme to be saved
     * @return true if succeed, false otherwise
     */
    public static boolean saveScheme(Context context, HashingScheme scheme) {
        File sDir = getSchemeDir(context);
        File tsDir = new File(sDir, scheme.getName());

        if (tsDir.exists()) {
            if (!tsDir.isDirectory()) {
                if (!tsDir.delete()) {
                    return false;
                }
                if (!tsDir.mkdir()) {
                    return false;
                }
            }
        }
        else {
            if (!tsDir.mkdir()) {
                return false;
            }
        }

        try {
            File hsFile = new File(tsDir, scheme.getName() + SCHEME_FILE_EXT);
            hsFile.delete();
            hsFile.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(hsFile);
            outputStream.write(scheme.toStorageString().getBytes());
            outputStream.close();
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    /**
     * Get a scheme from its directory. If the scheme has a name different from the dir name,
     * the scheme will be renamed.
     * The scheme will be read from file (dirname).hs
     *
     * @param dir the directory for the scheme
     * @return the scheme object. null if no .hs found, or exception occurred when reading.
     */
    private static HashingScheme getOneScheme(File dir) {
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }

        // find the schemeName.hs file
        String schemeName = dir.getName();
        File hsFile = null;
        for (File file : dir.listFiles()) {
            if (file.isFile() && file.getName().equals(schemeName + SCHEME_FILE_EXT)) {
                hsFile = file;
                break;
            }
        }
        if (hsFile == null) {
            return null;
        }

        // read and parse file
        String fileContent = getFileContent(hsFile);
        if (fileContent == null) {
            return null;
        }

        HashingScheme scheme = HashingScheme.fromStorageString(fileContent);
        // correct the scheme name
        if (!scheme.getName().equals(schemeName)) {
            scheme.setName(schemeName);
        }

        return scheme;
    }

    /**
     * Get all the saved schemes. Will try to delete invalid scheme folders.
     *
     * @param context the app context
     * @return a linkedlist of all saved schemes
     */
    public static LinkedList<HashingScheme> getAllSchemes(Context context) {
        LinkedList<HashingScheme> schemes = new LinkedList<HashingScheme>();
        File sDir = getSchemeDir(context);

        // read all
        File[] subDirs = sDir.listFiles();
        for (File subDir : subDirs) {
            if (subDir.isDirectory()) {
                HashingScheme scheme = getOneScheme(subDir);
                if (scheme != null) {
                    schemes.add(scheme);
                }
                else {
                    // remove invalid
                    FileUtility.deleteRecursively(subDir);
                }
            }
        }

        return schemes;
    }

    /**
     * Delete a scheme (its folder) given its name.
     *
     * @param context the app context
     * @param name the name of the scheme to delete
     * @return true if everything works, false if it is found but something go wrong while deleting
     * @throws SchemeNotFoundException when it can not be found
     */
    public static boolean deleteScheme(Context context, String name) throws SchemeNotFoundException {
        File sDir = getSchemeDir(context);

        File dir = new File(sDir, name);

        if (dir.exists() && dir.isDirectory()) {
            return FileUtility.deleteRecursively(dir);
        }

        throw new SchemeNotFoundException("Scheme not found for deleteScheme: " + name);
    }

    /**
     * Rename a scheme (its filename and dirname).
     * The scheme name (in its file) will not be changed. Save it afterwards.
     *
     * @param context the app context
     * @param oldName the name of the scheme
     * @param newName the new name of the scheme
     * @return true if everything works, false if it is found but something go wrong while renaming
     *         or if same (new) scheme exists
     * @throws SchemeNotFoundException when it can not be found
     */
    public static boolean renameSchemeFile(Context context, String oldName, String newName) throws SchemeNotFoundException {
        File sDir = getSchemeDir(context);
        File tsDir = new File(sDir, oldName);
        File tsFile = null;

        // find old scheme
        assertSchemeFolderExists(tsDir, "renameSchemeFile");

        for (File sub : tsDir.listFiles()) {
            if (sub.isFile() && sub.getName().equals(oldName + SCHEME_FILE_EXT)) {
                tsFile = sub;
            }
        }
        if (tsFile == null) {
            throw new SchemeNotFoundException("Scheme file (." + SCHEME_FILE_EXT + ") not found for renameSchemeFile:" + oldName);
        }

        // check existance of new scheme
        File ntsDir = new File(sDir, newName);
        if (ntsDir.exists()) {
            return false;
        }
        else {
            // rename file then folder
            File ntsFile = new File(tsDir, newName + SCHEME_FILE_EXT);
            if (tsFile.renameTo(ntsFile)) {
                // rename file success
                // now rename folder
                if (tsDir.renameTo(ntsDir)) {
                    // rename folder also success
                    return true;
                }
                else {
                    // rename folder failed,
                    // try reverting process
                    tsFile = new File(tsDir, oldName + SCHEME_FILE_EXT);
                    ntsFile.renameTo(tsFile);
                    return false;
                }
            }
            else {
                // file rename failed
                return false;
            }
        }
    }

    /**
     * Get a list of the names of saved info of a scheme. That is, the filename without extension of
     * all .hsif files in the scheme folder.
     *
     * @param context the app context
     * @param schemeName the name of scheme
     * @return a linkedlist of the names of all saved info
     * @throws SchemeNotFoundException if the scheme folder does not exist
     */
    public static LinkedList<String> getAllSavedInfoOfScheme(Context context, String schemeName) throws SchemeNotFoundException {
        File sDir = getSchemeDir(context);
        File tsDir = new File(sDir, schemeName);

        assertSchemeFolderExists(tsDir, "getAllSavedInfoOfScheme");

        LinkedList<String> names = new LinkedList<String>();
        int extLength = SCHEME_INFO_FILE_EXT.length();

        for (File file : tsDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.endsWith(SCHEME_INFO_FILE_EXT);
            }
        })) {
            String fileName = file.getName();
            names.add(fileName.substring(0, fileName.length() - extLength));
        }

        return names;
    }

    /**
     * Get the file content of a saved info of a scheme, given the name of scheme and name of saved info.
     *
     * @param context the app context
     * @param schemeName the name of scheme
     * @param infoName the name of info
     * @return the content of info, null if file exists but can not be read
     * @throws SchemeNotFoundException if the scheme folder does not exist
     * @throws SchemeInfoNotFoundException if the scheme info file does not exist
     */
    public static String getOneSavedInfoOfScheme(Context context, String schemeName, String infoName) throws SchemeNotFoundException, SchemeInfoNotFoundException {
        File sDir = getSchemeDir(context);
        File tsDir = new File(sDir, schemeName);
        assertSchemeFolderExists(tsDir, "getOneSavedInfoOfScheme");

        File infoFile = new File(tsDir, infoName + SCHEME_INFO_FILE_EXT);
        assertSchemeInfoFileExists(infoFile, "getOneSavedInfoOfScheme");

        return getFileContent(infoFile);
    }

    /**
     * Save an info of one scheme. Old file will be overwritten (deleted and re-create) if info of the
     * same name already exists.
     *
     * @param context the app context
     * @param scheme the scheme whose filled info are to be saved
     * @param infoName the name of info
     * @return true if succeeded, false otherwise
     * @throws SchemeNotFoundException if the scheme folder does not exist
     */
    public static boolean saveInfoOfScheme(Context context, HashingScheme scheme, String infoName) throws SchemeNotFoundException {
        File sDir = getSchemeDir(context);
        String schemeName = scheme.getName();
        File tsDir = new File(sDir, schemeName);

        assertSchemeFolderExists(tsDir, "saveInfoOfScheme");

        try {
            File infoFile = new File(tsDir, infoName + SCHEME_INFO_FILE_EXT);
            infoFile.delete();
            infoFile.createNewFile();

            FileOutputStream outputStream = new FileOutputStream(infoFile);
            outputStream.write(scheme.exportFieldValues().getBytes());
            outputStream.close();
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }

    /**
     * Delete an info of a scheme
     *
     * @param context the app context
     * @param scheme the scheme whose saved info are to be deleted
     * @param infoName the name of info
     * @return true if succeeded, false otherwise
     * @throws SchemeNotFoundException if the scheme folder does not exist
     * @throws SchemeInfoNotFoundException if the scheme info file does not exist
     */
    public static boolean deleteInfoOfScheme(Context context, HashingScheme scheme, String infoName) throws SchemeNotFoundException, SchemeInfoNotFoundException {
        File sDir = getSchemeDir(context);
        String schemeName = scheme.getName();
        File tsDir = new File(sDir, schemeName);
        assertSchemeFolderExists(tsDir, "deleteInfoOfScheme");

        File infoFile = new File(tsDir, infoName + SCHEME_INFO_FILE_EXT);
        assertSchemeInfoFileExists(infoFile, "deleteInfoOfScheme");

        return infoFile.delete();
    }

    /**
     * Rename an info of a scheme. If a saved info of the same name exists, it fails (returns false).
     *
     * @param context the app context
     * @param scheme the scheme whose saved info are to be renamed
     * @param oldInfoName the old name of info
     * @param newInfoName the new name of info
     * @return true if succeeded, false otherwise
     * @throws SchemeNotFoundException if the scheme folder does not exist
     * @throws SchemeInfoNotFoundException if the scheme info file does not exist
     */
    public static boolean renameInfoOfScheme(Context context, HashingScheme scheme, String oldInfoName, String newInfoName) throws SchemeNotFoundException, SchemeInfoNotFoundException {
        File sDir = getSchemeDir(context);
        String schemeName = scheme.getName();
        File tsDir = new File(sDir, schemeName);
        assertSchemeFolderExists(tsDir, "renameInfoOfScheme");

        File infoFile = new File(tsDir, oldInfoName + SCHEME_INFO_FILE_EXT);
        assertSchemeInfoFileExists(infoFile, "deleteInfoOfScheme");

        File newInfoFile = new File(tsDir, newInfoName + SCHEME_INFO_FILE_EXT);
        if (newInfoFile.exists()) {
            return false;
        }

        return infoFile.renameTo(newInfoFile);
    }

    /**
     * Save the application settings
     * @param context the app context
     * @param settings the settings to be saved
     */
    public static boolean saveAppSettings(Context context, ApplicationSettings settings) {
        File sFile = new File(context.getFilesDir(), SETTING_FILE);

        if (sFile.exists()) {
            sFile.delete();
        }

        try {
            sFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(sFile);
            outputStream.write(settings.exportString().getBytes());
            outputStream.close();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Read app settings.
     * @param context the app context
     * @return The app settings, null if settings not exist
     */
    public static ApplicationSettings loadAppSettings(Context context) {
        File sFile = new File(context.getFilesDir(), SETTING_FILE);

        if (!sFile.exists() || sFile.isDirectory()) {
            return null;
        }

        String content = getFileContent(sFile);
        return ApplicationSettings.importString(content);
    }
}

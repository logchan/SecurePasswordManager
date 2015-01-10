package hk.ust.ustac.team8.applicationutility;

import hk.ust.ustac.team8.exception.SchemeNotFoundException;
import hk.ust.ustac.team8.generalutility.FileUtility;
import hk.ust.ustac.team8.hashingscheme.HashingScheme;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Some static methods involving the files of this application
 */
public final class AppFileUtility {

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
    private File getSchemeDir(Context context) {
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
     * Save a scheme. If a scheme of the same name exists, it will be overwritten (saved info are
     * not affected). If a file of the same name exists in the schemes folder, it will be deleted
     * (and if it can't be, the saving will fail).
     *
     * @param context the app context
     * @param scheme the scheme to be saved
     * @return true if succeed, false otherwise
     */
    public boolean saveScheme(Context context, HashingScheme scheme) {
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
            File hsFile = new File(tsDir, scheme.getName() + ".hs");
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
    private HashingScheme getOneScheme(File dir) {
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }

        // find the schemeName.hs file
        String schemeName = dir.getName();
        File hsFile = null;
        for (File file : dir.listFiles()) {
            if (file.isFile() && file.getName().equals(schemeName + ".hs")) {
                hsFile = file;
                break;
            }
        }
        if (hsFile == null) {
            return null;
        }

        // read and parse file
        try {
            BufferedReader reader = new BufferedReader(new FileReader(hsFile));
            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }

            HashingScheme scheme = HashingScheme.fromStorageString(builder.toString());
            // correct the scheme name
            if (!scheme.getName().equals(schemeName)) {
                scheme.setName(schemeName);
            }
            return scheme;
        }
        catch (FileNotFoundException ex) {
            // based on what we have done above, this shall never happen
            return null;
        }
        catch (IOException ex) {
            return null;
        }
    }

    /**
     * Get all the saved schemes. Will try to delete invalid scheme folders.
     *
     * @param context the app context
     * @return a linkedlist of all saved schemes
     */
    public LinkedList<HashingScheme> getAllSchemes(Context context) {
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
                    FileUtility.DeleteRecursively(subDir);
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
    public boolean deleteScheme(Context context, String name) throws SchemeNotFoundException {
        File sDir = getSchemeDir(context);

        for (File sub : sDir.listFiles()) {
            if (sub.isDirectory() && sub.getName().equals(name)) {
                return FileUtility.DeleteRecursively(sub);
            }
        }

        throw new SchemeNotFoundException("Scheme not found for deleteScheme: " + name);
    }
}

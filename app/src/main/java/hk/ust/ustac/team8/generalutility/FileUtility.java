package hk.ust.ustac.team8.generalutility;

import java.io.File;

/**
 * Some static methods that are related to Java File
 */
public final class FileUtility {

    /**
     * Private constructor to prevent creating instance
     */
    private FileUtility() {

    }

    /**
     * Recursively delete the sub file and dirs (if any) of a dir and then itself. Or delete a file.
     *
     * @param fileOrDir the root to be deleted
     * @return true if everything was done successfully, false otherwise
     */
    public static boolean deleteRecursively(File fileOrDir) {
        boolean subSuccess = true;

        if (fileOrDir.isDirectory()) {
            for (File sub : fileOrDir.listFiles()) {
                subSuccess = deleteRecursively(sub);
            }
        }

        boolean thisSuccess = fileOrDir.delete();

        return subSuccess && thisSuccess;
    }
}

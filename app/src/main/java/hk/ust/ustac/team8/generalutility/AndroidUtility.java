package hk.ust.ustac.team8.generalutility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Some static methods that are related to Android
 */
public final class AndroidUtility {

    /**
     * Private constructor to prevent creating instance
     */
    private AndroidUtility() {

    }

    /**
     * Create a simple AlertDialog. All parameters except for activity can be null.
     * This method is basically a shortcut of using AlertDialog.Builder
     * Title/message will be set if the parameter is not null
     * Positive button will be set if both its text and listener are not null (same for negative button)
     *
     * @param activity the activity for the builder. must not be null.
     * @param title the title of dialog
     * @param message the message of dialog
     * @param positiveButtonText the text of positive button
     * @param positiveListener the listener of positive button click
     * @param negativeButtonText the text of negative button
     * @param negativeListener the listener of negative button click
     * @return a AlertDialog created by the builder
     *
     * @see android.app.AlertDialog
     * @see android.app.AlertDialog.Builder
     *
     */
    public static AlertDialog createSimpleAlertDialog(Activity activity,String title, String message,
                                                      String positiveButtonText,
                                                      DialogInterface.OnClickListener positiveListener,
                                                      String negativeButtonText,
                                                      DialogInterface.OnClickListener negativeListener
                                                      ) {

        LangUtility.assertNonNull(activity, "Null activity provided for createSimpleAlertDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (message != null) {
            builder.setMessage(message);
        }

        if (title != null) {
            builder.setTitle(title);
        }

        if (positiveButtonText != null && positiveListener != null) {
            builder.setPositiveButton(positiveButtonText, positiveListener);
        }

        if (negativeButtonText != null && negativeListener != null) {
            builder.setNegativeButton(negativeButtonText, negativeListener);
        }

        return builder.create();
    }

}

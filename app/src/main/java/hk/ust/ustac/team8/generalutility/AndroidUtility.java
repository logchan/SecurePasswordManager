package hk.ust.ustac.team8.generalutility;

import hk.ust.ustac.team8.securepasswordmanager.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;

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

    public interface PromptOneInputReceiver {
        public void receivePromptOneInput(String output, Object...extraInfo);
    }

    public static void promptForOneInput(Activity activity, String title, final String original, String hint,
                                           final PromptOneInputReceiver receiver, final Object...extraInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        if (title != null) {
            builder.setTitle(title);
        }

        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_one_input, null);
        final EditText edit = (EditText)view.findViewById(R.id.dialogInput);

        if (original != null) {
            edit.setText(original);
        }

        if (hint != null) {
            edit.setHint(hint);
        }

        builder.setView(view);

        builder.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                receiver.receivePromptOneInput(edit.getText().toString(), extraInfo);
            }
        });

        builder.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                receiver.receivePromptOneInput(original, extraInfo);
            }
        });

        builder.show();
    }

    public static void activityExceptionExit(Activity activity, String extraInfo) {
        String content = "By activity " + activity.getLocalClassName() + "\n" + extraInfo;
        AlertDialog dialog = createSimpleAlertDialog(activity, activity.getString(R.string.exception_exit),
                content, activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                }, null, null);
        dialog.show();
        activity.finish();
    }
}

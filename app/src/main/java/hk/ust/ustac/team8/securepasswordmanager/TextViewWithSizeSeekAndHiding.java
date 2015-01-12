package hk.ust.ustac.team8.securepasswordmanager;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TextViewWithSizeSeekAndHiding.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TextViewWithSizeSeekAndHiding#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TextViewWithSizeSeekAndHiding extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ISHIDDEN = "ISHIDDEN";
    private static final String ARG_FONTSIZE = "FONTSIZE";
    private static final String ARG_TEXT = "TEXT";

    private boolean isHidden;
    private int fontSize;
    private String text;

    private OnFragmentInteractionListener mListener;

    public static TextViewWithSizeSeekAndHiding newInstance(boolean isHidden, int fontSize, String text) {
        TextViewWithSizeSeekAndHiding fragment = new TextViewWithSizeSeekAndHiding();
        Bundle args = new Bundle();
        args.putBoolean(ARG_ISHIDDEN, isHidden);
        args.putInt(ARG_FONTSIZE, fontSize);
        args.putString(ARG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    public TextViewWithSizeSeekAndHiding() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isHidden = getArguments().getBoolean(ARG_ISHIDDEN);
            fontSize = getArguments().getInt(ARG_FONTSIZE);
            text = getArguments().getString(ARG_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_view_with_size_seek_and_hiding, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);
    }

}

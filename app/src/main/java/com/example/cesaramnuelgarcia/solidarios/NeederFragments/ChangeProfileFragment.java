package com.example.cesaramnuelgarcia.solidarios.NeederFragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cesaramnuelgarcia.solidarios.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangeProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangeProfileFragment} factory method to
 * create an instance of this fragment.
 */
public class ChangeProfileFragment extends Fragment {


    EditText registeredFirstName;
    EditText registeredLastName;
    EditText registeredEmail;
    EditText registeredPassword;
    EditText registeredNewPassword;
    Button saveChanges;
    TextView newPasswordHint;

    private OnFragmentInteractionListener mListener;

    public ChangeProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ChangeProfileFragment.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registeredEmail = getView().findViewById(R.id.registeredEmail);
        registeredFirstName = getView().findViewById(R.id.registeredFirstName);
        registeredLastName = getView().findViewById(R.id.registeredLastName);
        registeredPassword = getView().findViewById(R.id.registeredPassword);
        registeredNewPassword = getView().findViewById(R.id.registeredNewPassword);
        saveChanges = getView().findViewById(R.id.saveProfileChanges);
        newPasswordHint = getView().findViewById(R.id.newPasswordHint);

        saveChanges.setEnabled(false);
        newPasswordHint.setTextColor(Color.RED);
        newPasswordHint.setVisibility(View.INVISIBLE);

        registeredNewPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(registeredNewPassword.getText().toString().equals(registeredPassword.getText().toString())) {
                    if(registeredNewPassword.getText().toString().length() > 8) {
                        registeredNewPassword.setTextColor(Color.GREEN);
                        saveChanges.setEnabled(true);
                        newPasswordHint.setVisibility(View.INVISIBLE);
                    } else {
                        newPasswordHint.setVisibility(View.VISIBLE);
                        registeredNewPassword.setTextColor(Color.RED);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_profile, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if(args != null) {
            registeredEmail.setText(args.getString("email"));
            registeredFirstName.setText(args.getString("firstName"));
            registeredLastName.setText(args.getString("lastName"));

            registeredEmail.setEnabled(false);
            registeredFirstName.setEnabled(false);
            registeredLastName.setEnabled(false);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

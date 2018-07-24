package com.example.cesaramnuelgarcia.solidarios.VolunteerFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cesaramnuelgarcia.solidarios.Activities.WaitingList;
import com.example.cesaramnuelgarcia.solidarios.AppSingleton;
import com.example.cesaramnuelgarcia.solidarios.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OtherOptionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OtherOptionsFragment} factory method to
 * create an instance of this fragment.
 */
public class OtherOptionsFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    public OtherOptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button closeButton = getView().findViewById(R.id.closeApp);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
                getActivity().finish();
                System.exit(0);
            }
        });

    }

    public void sendRequest() {

        String url = R.string.baseURL + "/vRequest/";
        JSONObject requestBody = new JSONObject();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String parsedPickedDate;
        String parsedPickedTime;

        parsedPickedDate = String.valueOf(new Date().getTime());
        parsedPickedTime = String.valueOf(new Date().getTime());


        try {
            JSONObject user = new JSONObject(prefs.getString("loggedUser",""));
            requestBody.accumulate("userId", user.get("_id"));
            requestBody.accumulate("creationDate", new Date().toString());
            requestBody.accumulate("title", user.get("name").toString().concat(" ")
                    .concat(user.get("surname").toString())
                    .concat(", con número de teléfono: ")
                    .concat(user.get("phone").toString())
                    .concat(" necesita que le llames!"));
            requestBody.accumulate("location", user.get("address"));
            requestBody.accumulate("requestDate", parsedPickedDate);
            requestBody.accumulate("requestTime", parsedPickedTime);
            requestBody.accumulate("requestType", "Otros");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getActivity().getApplicationContext(), "¡Tu petición se ha enviado!", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Error on connection", Toast.LENGTH_LONG).show();
            }
        });
        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_options, container, false);
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
        void onFragmentInteraction(Uri uri);
    }
}

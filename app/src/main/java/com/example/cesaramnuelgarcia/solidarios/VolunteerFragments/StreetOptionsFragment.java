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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StreetOptionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StreetOptionsFragment} factory method to
 * create an instance of this fragment.
 */
public class StreetOptionsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    Button goShopping;
    Button goWalking;
    Button goDoctor;
    Button goEvent;

    LinearLayout whenQuestion;
    Button rightNow;
    DatePicker datePicker;
    TimePicker timePicker;
    Button requestOK;

    public StreetOptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        goShopping = getView().findViewById(R.id.goShopping);
        goWalking = getView().findViewById(R.id.walk);
        goDoctor = getView().findViewById(R.id.goDoctor);
        goEvent = getView().findViewById(R.id.goEvent);

        datePicker = getView().findViewById(R.id.datePicker);
        timePicker = getView().findViewById(R.id.timePicker);
        requestOK = getView().findViewById(R.id.requestOK);

        whenQuestion = getView().findViewById(R.id.whenQuestion);
        rightNow = getView().findViewById(R.id.rightNowButton);
        datePicker.setVisibility(INVISIBLE);
        timePicker.setVisibility(INVISIBLE);
        requestOK.setVisibility(INVISIBLE);

        goShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(datePicker.getVisibility() == VISIBLE) {
                    datePicker.setVisibility(INVISIBLE);
                    timePicker.setVisibility(INVISIBLE);
                    requestOK.setVisibility(INVISIBLE);
                    whenQuestion.setVisibility(INVISIBLE);
                    goWalking.setVisibility(VISIBLE);
                    goDoctor.setVisibility(VISIBLE);
                    goEvent.setVisibility(VISIBLE);
                } else {
                    datePicker.setVisibility(VISIBLE);
                    timePicker.setVisibility(VISIBLE);
                    requestOK.setVisibility(VISIBLE);
                    whenQuestion.setVisibility(VISIBLE);
                    goWalking.setVisibility(INVISIBLE);
                    goDoctor.setVisibility(INVISIBLE);
                    goEvent.setVisibility(INVISIBLE);
                }
            }
        });

        goWalking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(datePicker.getVisibility() == VISIBLE) {
                    datePicker.setVisibility(INVISIBLE);
                    timePicker.setVisibility(INVISIBLE);
                    requestOK.setVisibility(INVISIBLE);
                    goShopping.setVisibility(VISIBLE);
                    goDoctor.setVisibility(VISIBLE);
                    goEvent.setVisibility(VISIBLE);
                    whenQuestion.setVisibility(INVISIBLE);
                } else {
                    datePicker.setVisibility(VISIBLE);
                    timePicker.setVisibility(VISIBLE);
                    requestOK.setVisibility(VISIBLE);
                    goShopping.setVisibility(INVISIBLE);
                    whenQuestion.setVisibility(VISIBLE);
                    goDoctor.setVisibility(INVISIBLE);
                    goEvent.setVisibility(INVISIBLE);
                }
            }
        });

        goDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requestOK.getVisibility() == VISIBLE) {
                    datePicker.setVisibility(INVISIBLE);
                    timePicker.setVisibility(INVISIBLE);
                    requestOK.setVisibility(INVISIBLE);
                    goShopping.setVisibility(VISIBLE);
                    goWalking.setVisibility(VISIBLE);
                    goEvent.setVisibility(VISIBLE);
                    whenQuestion.setVisibility(INVISIBLE);
                } else {
                    datePicker.setVisibility(VISIBLE);
                    timePicker.setVisibility(VISIBLE);
                    requestOK.setVisibility(VISIBLE);
                    goShopping.setVisibility(INVISIBLE);
                    whenQuestion.setVisibility(VISIBLE);
                    goWalking.setVisibility(INVISIBLE);
                    goEvent.setVisibility(INVISIBLE);
                }
            }
        });

        goEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requestOK.getVisibility() == VISIBLE) {
                    datePicker.setVisibility(INVISIBLE);
                    timePicker.setVisibility(INVISIBLE);
                    requestOK.setVisibility(INVISIBLE);
                    goShopping.setVisibility(VISIBLE);
                    goWalking.setVisibility(VISIBLE);
                    goDoctor.setVisibility(VISIBLE);
                    whenQuestion.setVisibility(INVISIBLE);
                } else {
                    datePicker.setVisibility(VISIBLE);
                    timePicker.setVisibility(VISIBLE);
                    requestOK.setVisibility(VISIBLE);
                    goShopping.setVisibility(INVISIBLE);
                    whenQuestion.setVisibility(VISIBLE);
                    goWalking.setVisibility(INVISIBLE);
                    goDoctor.setVisibility(INVISIBLE);
                }
            }
        });

        requestOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int helpCase = 0;
                if(goWalking.getVisibility() == VISIBLE)
                    helpCase = 1;
                if(goDoctor.getVisibility() == VISIBLE)
                    helpCase = 2;
                if(goEvent.getVisibility() == VISIBLE)
                    helpCase = 3;
                sendRequest(helpCase);
            }
        });

        rightNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(INVISIBLE);
                timePicker.setVisibility(INVISIBLE);
                whenQuestion.setVisibility(INVISIBLE);
            }
        });
    }


    public void sendRequest(int helpCase) {

        String url = R.string.baseURL + "/vRequest/";
        JSONObject requestBody = new JSONObject();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String parsedPickedDate;
        String parsedPickedTime;

        if(whenQuestion.getVisibility() == VISIBLE) {
            parsedPickedDate = formatPickedDate();
            parsedPickedTime = formatPickedTime();
        } else {
            parsedPickedDate = String.valueOf(new Date().getTime());
            parsedPickedTime = String.valueOf(new Date().getTime());
        }

        try {
            JSONObject user = new JSONObject(prefs.getString("loggedUser",""));
            requestBody.accumulate("userId", user.get("_id"));
            requestBody.accumulate("creationDate", new Date().toString());
            requestBody.accumulate("title", "");
            requestBody.accumulate("location", user.get("address"));
            requestBody.accumulate("requestDate", parsedPickedDate);
            requestBody.accumulate("requestTime", parsedPickedTime);

            switch (helpCase) {
                case 0: requestBody.accumulate("requestType", "Mover cosas"); break;
                case 1: requestBody.accumulate("requestType", "Limpiar"); break;
                case 2: requestBody.accumulate("requestType", "Hacer compañía"); break;
                case 3: requestBody.accumulate("requestType", "Ir a un evento de Solidarios"); break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent toWaitingList = new Intent(getActivity().getApplicationContext(), WaitingList.class);
                toWaitingList.putExtra("vRequest", response.toString());
                startActivity(toWaitingList);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Error on connection", Toast.LENGTH_LONG).show();
            }
        });
        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public String formatPickedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        int day =  datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        Date date = new Date(day, month, year);
        return dateFormat.format(date);
    }

    public String formatPickedTime() {
        int hour = timePicker.getCurrentHour();
        int minutes = timePicker.getCurrentMinute();

        return String.valueOf(hour).concat(":").concat(String.valueOf(minutes));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_street_options, container, false);
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
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

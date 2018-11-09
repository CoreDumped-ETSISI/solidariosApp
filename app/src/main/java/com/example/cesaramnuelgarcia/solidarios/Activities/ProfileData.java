package com.example.cesaramnuelgarcia.solidarios.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.example.cesaramnuelgarcia.solidarios.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileData extends AppCompatActivity {

    EditText dataName;
    EditText dataSurname;
    EditText dataAge;
    EditText dataGender;
    EditText dataDescription;
    EditText dataEmail;
    EditText dataPhoneNumber;
    EditText dataAddress;
    EditText dataDNI;

    private RequestQueue reqQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_data);

        dataName = findViewById(R.id.nameData);
        dataSurname = findViewById(R.id.surnameData);
        dataAge = findViewById(R.id.ageData);
        dataGender = findViewById(R.id.genderData);
        dataDescription = findViewById(R.id.descriptionData);
        dataEmail = findViewById(R.id.emailData);
        dataPhoneNumber = findViewById(R.id.phoneNumberData);
        dataAddress = findViewById(R.id.addressData);
        dataDNI = findViewById(R.id.dniData);

        getUserInfo();

    }

    void getUserInfo(){
        String url = "";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                                JSONObject user = response.getJSONObject("user");

                                String firstName = user.getString("name");
                                String surname = user.getString("surname");
                                Integer age = user.getInt("age");
                                String gender = user.getString("gender");
                                String description = user.getString("description");
                                String email = user.getString("email");
                                Integer phoneNumber = user.getInt("phone");
                                String address = user.getString("address");
                                String dni = user.getString("dni");


                                dataName.setText(firstName);
                                dataSurname.setText(surname);
                                dataAge.setText(age);
                                dataGender.setText(gender);
                                dataDescription.setText(description);
                                dataEmail.setText(email);
                                dataPhoneNumber.setText(phoneNumber);
                                dataAddress.setText(address);
                                dataDNI.setText(dni);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        reqQ.add(request);
    }

    void updateUser() throws JSONException {

        String url = "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "epicidad");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        reqQ.add(request);

    }
}

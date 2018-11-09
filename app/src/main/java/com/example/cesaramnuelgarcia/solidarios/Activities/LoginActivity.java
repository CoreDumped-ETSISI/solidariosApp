package com.example.cesaramnuelgarcia.solidarios.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cesaramnuelgarcia.solidarios.AppSingleton;
import com.example.cesaramnuelgarcia.solidarios.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);

        setOnClicks();

        checkCallPermission();

        try {
            String token = getOldToken();
            if(token != null)
                getUser(token);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setOnClicks() {
        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmailValid(mEmailView.getText().toString()) && isPasswordValid(mPasswordView.getText().toString())) {
                    try {
                        login(null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                    Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_LONG).show();
            }
        });

        TextView createAccount = findViewById(R.id.newAccount);
        createAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "TOUCHED!!");
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage(R.string.call_dialog_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.positive_call, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)
                                        == PackageManager.PERMISSION_GRANTED) {
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:".concat(getString(R.string.phoneNumber))));
                                    dialog.cancel();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, R.string.no_calling_permission, Toast.LENGTH_LONG).show();
                                }
                            }
                        }).setNegativeButton(R.string.negative_call, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(), R.string.call_get_account, Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
                }
            });
    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        return true;
    }

    public void login(String token) throws JSONException {


        if(token == null) {
            String url = getString(R.string.baseURL) + "/user/login/";
            JSONObject loginBody = new JSONObject();
            loginBody.accumulate("email", mEmailView.getText().toString());
            loginBody.accumulate("password", mPasswordView.getText().toString());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, loginBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Intent toMainActivity;
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("loggedUser", response.toString());
                    editor.apply();

                    try {
                        if (response.getJSONObject("user").getString("role").contentEquals("volunteer")) {
                            AppSingleton.getInstance(getApplicationContext()).token = response.getString("token");
                            toMainActivity = new Intent(getApplicationContext(), MainScreen.class);
                            toMainActivity.putExtra("role", "volunteer");
                            startActivity(toMainActivity);
                        } else if ((response.getJSONObject("user").getString("role").contentEquals("needer"))) {
                            AppSingleton.getInstance(getApplicationContext()).token = response.getString("token");
                            toMainActivity = new Intent(getApplicationContext(), MainScreen.class);
                            toMainActivity.putExtra("role", "volunteer");
                            startActivity(toMainActivity);
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.login_not_valid, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error on connection", Toast.LENGTH_LONG).show();
                }
            });
            AppSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

    private String getOldToken() throws JSONException {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String temp = prefs.getString("loggedUser", null);
        if (temp != null) {
            JSONObject loginBody = new JSONObject(temp);
            return loginBody.getString("token");
        }

        return null;
    }

    private void getRenewToken() {
        String url = getString(R.string.baseURL) + "/user/renew/";
        JSONObject loginBody = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, loginBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Intent toMainActivity;
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("loggedUser", response.toString());
                editor.apply();

                try {
                    if (response.getJSONObject("user").getString("role").contentEquals("volunteer")) {
                        AppSingleton.getInstance(getApplicationContext()).token = response.getString("token");
                        toMainActivity = new Intent(getApplicationContext(), MainScreen.class);
                        toMainActivity.putExtra("role", "volunteer");
                        startActivity(toMainActivity);
                    } else if ((response.getJSONObject("user").getString("role").contentEquals("needer"))) {
                        AppSingleton.getInstance(getApplicationContext()).token = response.getString("token");
                        toMainActivity = new Intent(getApplicationContext(), MainScreen.class);
                        toMainActivity.putExtra("role", "volunteer");
                        startActivity(toMainActivity);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.login_not_valid, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), R.string.error_json_response, Toast.LENGTH_LONG).show();
            }
        });
        AppSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void checkCallPermission() {
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.CALL_PHONE )
                != PackageManager.PERMISSION_GRANTED) {
            String[] tempPerms = {Manifest.permission.CALL_PHONE};
            ActivityCompat.requestPermissions( this, tempPerms, 123 );
        }
    }

    private void getUser(String token) {
        final String tokenFinal = token;
        String url = getString(R.string.baseURL) + "/user/renew";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Intent toMainActivity;

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                /*try {
                    response.accumulate("token", tokenFinal);
                } catch (JSONException ignored){}
                    editor.putString("loggedUser", response.toString());
                    editor.apply();
*/
                try {
                    if (response.getJSONObject("user").getString("role").contentEquals("volunteer")) {
                        AppSingleton.getInstance(getApplicationContext()).token = response.getString("token");
                        toMainActivity = new Intent(getApplicationContext(), MainScreen.class);
                        toMainActivity.putExtra("role", "volunteer");
                        startActivity(toMainActivity);
                    } else if ((response.getJSONObject("user").getString("role").contentEquals("needer"))) {
                        AppSingleton.getInstance(getApplicationContext()).token = response.getString("token");
                        toMainActivity = new Intent(getApplicationContext(), MainScreen.class);
                        toMainActivity.putExtra("role", "volunteer");
                        startActivity(toMainActivity);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.login_not_valid, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode == 401)
                    getRenewToken();
                else
                    Toast.makeText(getApplicationContext(), "Error on connection", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + tokenFinal);
                return params;
            }
        };
        AppSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);


    }

}
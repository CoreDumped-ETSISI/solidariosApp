package com.example.cesaramnuelgarcia.solidarios.Activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cesaramnuelgarcia.solidarios.AppSingleton;
import com.example.cesaramnuelgarcia.solidarios.R;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView createAccount;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        createAccount = findViewById(R.id.newAccount);
        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        checkCallPermission();


        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmailValid(mEmailView.getText().toString()) && isPasswordValid(mEmailView.getText().toString())) {
                    try {
                        login();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                    Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_LONG).show();
            }
        });

        createAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", "TOUCHED!!");
                AlertDialog builder = new AlertDialog.Builder(LoginActivity.this)
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


    private void showProgress(final boolean show) {

            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

    }


    public void login() throws JSONException {

        String url = R.string.baseURL + "/user/login/";
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
                    if(response.getString("role").equalsIgnoreCase("volunteer")){
                        toMainActivity = new Intent(getApplicationContext(), MainVolunteerActivity.class);
                        toMainActivity.putExtra("user", response.toString());
                        startActivity(toMainActivity);
                    } else {
                        toMainActivity = new Intent(getApplicationContext(), MainNeederActivity.class);
                        toMainActivity.putExtra("user", response.toString());
                        startActivity(toMainActivity);
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
}
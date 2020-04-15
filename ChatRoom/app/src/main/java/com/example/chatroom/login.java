package com.example.chatroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity {
    EditText username, password;
    TextView register;
    String user, pass;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        register = findViewById(R.id.register);
        Firebase.setAndroidContext(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this, register.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if (user.equals("")) {
                    username.setError("Username can't be blank");
                } else if (pass.equals("")) {
                    password.setError("Password can't be blank");
                }
                else {
                    final ProgressDialog p = new ProgressDialog(login.this);
                    p.setMessage("Loading...");
                    p.show();

                    String url = "https://chatroom-38a20.firebaseio.com/user.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("null")) {
                                Toast.makeText(login.this, "User not Found", Toast.LENGTH_LONG).show();
                            }
                            else {
                                JSONObject ob;
                                try {
                                    ob = new JSONObject(response);
                                    if(ob.has(user)) {
                                        if(ob.getJSONObject(user).getString("password").equals(pass)) {
                                            UserDetails.username = user;
                                            UserDetails.password = pass;
                                            startActivity(new Intent(login.this,user.class));
                                        }
                                        else {
                                            Toast.makeText(login.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(login.this, "User not Found", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            username.setText("");
                            password.setText("");
                            p.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            p.dismiss();
                        }
                    });

                    RequestQueue r = Volley.newRequestQueue(login.this);
                    r.add(request);
                }
            }
        });

    }
}

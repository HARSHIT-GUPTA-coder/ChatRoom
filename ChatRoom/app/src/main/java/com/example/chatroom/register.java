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

public class register extends AppCompatActivity {
    EditText username, password;
    Button registerButton;
    String user, pass;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        Firebase.setAndroidContext(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this, login.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if (user.equals("")) {
                    username.setError("Username can't be blank");
                } else if (pass.equals("")) {
                    password.setError("Password can't be blank");
                } else if (user.length() < 3 | user.length() > 12) {
                    username.setError("Enter a username of length between 3 to 12");
                } else if (pass.length() < 3 | pass.length() > 12) {
                    password.setError("Length of password must be between 3  to 12 characters");
                } else {
                    final ProgressDialog p = new ProgressDialog(register.this);
                    p.setMessage("Loading...");
                    p.show();

                    String url = "https://chatroom-38a20.firebaseio.com/user.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Firebase reference = new Firebase("https://chatroom-38a20.firebaseio.com/user");

                            if(response.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(register.this, "registration successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(register.this, login.class));
                            }
                            else {
                                JSONObject ob;
                                try {
                                    ob = new JSONObject(response);
                                    if(ob.has(user)) {
                                        Toast.makeText(register.this,"User already exists",Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        reference.child(user).child("password").setValue(pass);
                                        Toast.makeText(register.this, "registration successful", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(register.this, login.class));
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

                    RequestQueue r = Volley.newRequestQueue(register.this);
                    r.add(request);
                }
            }
        });

    }
}

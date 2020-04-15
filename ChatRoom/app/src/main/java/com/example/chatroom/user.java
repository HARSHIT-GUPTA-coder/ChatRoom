package com.example.chatroom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class user extends AppCompatActivity {
    ListView list;
    TextView noitem;
    ArrayList<String> us = new ArrayList<>();
    int totalusers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        list = findViewById(R.id.usersList);
        noitem = findViewById(R.id.noUsersText);
        String url = "https://chatroom-38a20.firebaseio.com/user.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                createlist(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(user.this);
        rQueue.add(request);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatwith = us.get(position);
                startActivity(new Intent(user.this, chat.class));
            }
        });


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            event.startTracking();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
                && !event.isCanceled()) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void refreshList(View view) {
        ProgressDialog p = new ProgressDialog(user.this);
        p.setMessage("Updating Users....");
        p.show();
        String url = "https://chatroom-38a20.firebaseio.com/user.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                createlist(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(user.this);
        rQueue.add(request);
        p.dismiss();
    }

    public void createlist(String s){
        if(!us.isEmpty()) {
            us.clear();
        }
        try {
            JSONObject ob = new JSONObject(s);
            Iterator i = ob.keys();
            String key;
            while(i.hasNext()) {
                key = i.next().toString();

                if(!key.equals(UserDetails.username)) {
                    us.add(key);
                    totalusers++;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalusers == 0) {
            noitem.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
        }
        else {
            list.setVisibility(View.VISIBLE);
            noitem.setVisibility(View.GONE);
            list.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,us));
        }
    }
}

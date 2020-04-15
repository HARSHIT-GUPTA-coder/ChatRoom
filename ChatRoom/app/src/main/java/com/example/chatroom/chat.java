package com.example.chatroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;



import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class chat extends AppCompatActivity {
    ImageView sendButton;
    EditText messageArea;
    DatabaseReference data;
    DatabaseReference data2;
    RecyclerView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        list = findViewById(R.id.messageRec);
        data = FirebaseDatabase.getInstance().getReference().child("messages/"+ UserDetails.username + "_" + UserDetails.chatwith);
        data2 = FirebaseDatabase.getInstance().getReference().child("messages/"+ UserDetails.chatwith + "_" + UserDetails.username);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        list.setHasFixedSize(true);
        list.setLayoutManager(linearLayoutManager);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                if(!messageText.equals("")){
                    DatabaseReference newpost = data.push();
                    newpost.child("content").setValue(messageText);
                    newpost.child("user").setValue(UserDetails.username);
                    newpost = data2.push();
                    newpost.child("content").setValue(messageText);
                    newpost.child("user").setValue(UserDetails.username);
                }
                messageArea.setText("");
                list.scrollToPosition(list.getAdapter().getItemCount());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Message,MessageViewHolder> fire = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(Message.class, R.layout.singlemessage, MessageViewHolder.class, data) {
            @Override
            protected void populateViewHolder(MessageViewHolder ViewHolder, Message message, int i) {
                ViewHolder.setContent(message.getContent(),message.getUser());
                ViewHolder.setUser(message.getUser());
            }
        };
        list.setAdapter(fire);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        View view;
        int color1R,color1B,color1G,color2R,color2B,color2G;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            color1R = new Random().nextInt(256);
            color2R = new Random().nextInt(256);
            color1G = new Random().nextInt(156);
            color2G = new Random().nextInt(156)+100;
            color1B = new Random().nextInt(156)+100 ;
            color2B = new Random().nextInt(156);
        }

        public void setContent(String content, String user) {
            TextView message = view.findViewById(R.id.message_text);
            RelativeLayout container = view.findViewById(R.id.messageframe);
            LinearLayout linear = view.findViewById(R.id.messagebound);
            if(user!=null && user.equals(UserDetails.username)) {
                container.setGravity(Gravity.RIGHT | Gravity.END);
                linear.setBackgroundResource(R.drawable.bubbleout);
            }
            else {
                container.setGravity(Gravity.LEFT | Gravity.START);
                linear.setBackgroundResource(R.drawable.bubblein);
            }
            message.setText(content);
        }
        public void setUser(String user) {
            TextView userdisplay = view.findViewById(R.id.username);
            if(user!=null && user.equals(UserDetails.username)) {
                userdisplay.setTextColor(Color.rgb(color1R,color1G,color1B));
            }
            else {
                userdisplay.setTextColor(Color.rgb(color2R,color2G,color2B));
            }
            userdisplay.setText(user);
        }

    }

}
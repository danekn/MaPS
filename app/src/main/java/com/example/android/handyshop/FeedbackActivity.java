package com.example.android.handyshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FeedbackActivity extends Activity {


    static DatabaseReference handyShopDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();





        handyShopDB = FirebaseDatabase.getInstance().getReference();
        handyShopDB.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //System.out.println(snapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError DatabaseError) {
                System.out.println("The read failed: " + DatabaseError.getMessage());
            }
        });





        setContentView(R.layout.activity_feedback);
        final String userId = (String)this.getIntent().getStringExtra("userId");
        String requestId = (String)this.getIntent().getStringExtra("requestId");
        String category = (String)this.getIntent().getStringExtra("Category");
        String subcategory = (String)this.getIntent().getStringExtra("SubCategory");
        String description = (String)this.getIntent().getStringExtra("description");

        System.out.println("userId "+userId);
        System.out.println("requestID "+requestId);
        System.out.println("category "+category);
        System.out.println("subcategory"+subcategory);
        System.out.println("description"+description);


        TextView tv_user = (TextView)findViewById(R.id.username_feedback_content);
        tv_user.setText(userId);

        TextView tv_title = (TextView)findViewById(R.id.title_feedback_content);
        tv_title.setText(requestId);

        TextView tv_des = (TextView)findViewById(R.id.description_feedback_content);
        tv_des.setText(description);

        TextView tv_sc = (TextView)findViewById(R.id.subcategory_feedback_content);
        tv_sc.setText(subcategory);

        TextView tv_c = (TextView)findViewById(R.id.category_feedback_content);
        tv_c.setText(category);


        final Button button = (Button) findViewById(R.id.leaveFeedback);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final DatabaseReference ref = handyShopDB.child("users");
                Query queryRef = ref.orderByChild("userId").equalTo(userId);
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (snapshot.getChildrenCount() == 0) {
                           // User usr = new User(id, name, email);
                           // ref.push().setValue(usr);
                        } else
                            System.out.println("Utente gia presente");
                    }
                    @Override
                    public void onCancelled(DatabaseError f) {
                    }
                });

            }
        });






    }
@Override
protected void onStart(){
    super.onStart();
    System.out.println("start feedback");

}
    @Override
    public void onNewIntent(Intent intent) {

        }
    }

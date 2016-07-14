package com.example.android.handyshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FeedbackActivity extends Activity {

    static String userId;
    static String category;
    static String subcategory;
    static String description;
    static String requestId;
    static String key;
    static DatabaseReference handyShopDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

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

        userId = (String)this.getIntent().getStringExtra("userId");
        requestId = (String)this.getIntent().getStringExtra("requestId");
        category = (String)this.getIntent().getStringExtra("Category");
        subcategory = (String)this.getIntent().getStringExtra("SubCategory");
        description = (String)this.getIntent().getStringExtra("description");
        key = (String)this.getIntent().getStringExtra("key");

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


        /*final Button button = (Button) findViewById(R.id.leaveFeedback);
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
        });*/






    }
@Override
protected void onStart(){
    super.onStart();
    System.out.println("start feedback");

}
    @Override
    public void onNewIntent(Intent intent) {

        }

    public void leaveFeedback(View v){

        final RatingBar mBar = (RatingBar) findViewById(R.id.ratingBar);
        System.out.println(mBar.getRating());

        final DatabaseReference ref = handyShopDB.child("users");

        Query queryRef = ref.orderByChild("userId").equalTo(userId);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange (DataSnapshot snapshot){
                if (snapshot.getChildrenCount() > 0)
                {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        User usr = d.getValue(User.class);

                        double fb=usr.getFeedback()*usr.getTransanctions();
                        fb=fb+mBar.getRating();
                        fb=fb/(usr.getTransanctions()+1);
                        usr.setFeedback(fb);
                        usr.setTransanctions(usr.getTransanctions()+1);

                        handyShopDB.child("users/"+d.getKey()).removeValue();
                        ref.push().setValue(usr);

                        handyShopDB.child("feedback/"+key).removeValue();

                        FeedbackActivity.super.onBackPressed();
                    }
                }
            }

            @Override
            public void onCancelled (DatabaseError f){
            }
        });

/*
        final DatabaseReference ref = handyShopDB.child("users");
        tmp=false;
        //Query queryRef = ref.orderByChild("userId").equalTo(userId);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot snapshot){
                if (snapshot.getChildrenCount() > 0)
                {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        FeedBack fdb = d.getValue(FeedBack.class);
                        Date date = new Date();
                        if(date.getTime()-fdb.getTimeCreation()>30000);
                        addNotification(fdb);
                    }
                }
            }

            @Override
            public void onCancelled (DatabaseError f){
            }
        });

*/

    }
}

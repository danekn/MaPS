package com.example.android.handyshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class FeedbackActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        getActionBar().hide();
        String quote = (String)this.getIntent().getStringExtra("dane");
        System.out.println(quote);

    }
@Override
protected void onStart(){
    super.onStart();
    System.out.println("start feedback");

}
    @Override
    public void onNewIntent(Intent intent) {
        String str=intent.getStringExtra("dane");
        System.out.println(str);
        }
    }

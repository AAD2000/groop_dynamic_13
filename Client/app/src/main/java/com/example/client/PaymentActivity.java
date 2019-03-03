package com.example.client;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {



    Integer from;
    Integer to;
    String[] tittles;
    int price=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getIntent().getExtras();
        if(arguments!=null){
            tittles = (String[]) arguments.getSerializable("tittles");
            from=(Integer) arguments.getSerializable("intFrom");
            to=(Integer) arguments.getSerializable("intTo");
        }
        setContentView(R.layout.activity_payment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        TextView fr=(TextView)findViewById(R.id.from_text);
        fr.setText(tittles[from]);

        TextView t=(TextView)findViewById(R.id.to_text);
        t.setText(tittles[to]);


    }

    @Override
    public void onBackPressed() {}


}

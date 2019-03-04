package com.example.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button logIn=(Button)findViewById(R.id.log_in_button);
        logIn.setOnClickListener((b)->{
            EditText password_edit=(EditText)findViewById(R.id.logging_password_edit);
            EditText nickname_edit=(EditText)findViewById(R.id.logging_nickname_edit);
            if(
                    !"".equals(password_edit.getText().toString()) &&
                    !"".equals(nickname_edit.getText().toString())
            ) {
                finishActivity(RESULT_OK);
            }
        });
    }

    @Override
    public void onBackPressed() { }

    @Override
    public void finishActivity(int requestCode) {
        //TODO:get information from server (need to remake)
        String[] person= new String[5];
        person[0]="Alex";
        person[1]="Danilov";
        EditText password_edit=(EditText)findViewById(R.id.logging_password_edit);
        person[2]=password_edit.getText().toString();
        person[3]="+79130105461";
        EditText nickname_edit=(EditText)findViewById(R.id.logging_nickname_edit);
        person[4]=nickname_edit.getText().toString();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        intent.putExtra("person_logged_in", person);
        super.finish();
    }
}

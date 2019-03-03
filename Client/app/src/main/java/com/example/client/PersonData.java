package com.example.client;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PersonData extends AppCompatActivity {

    Person account;
    EditText ePhone;
    EditText eSurname;
    EditText eName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_person_data);
        TextView personData=(TextView)findViewById(R.id.person_data);
        ePhone= (EditText)findViewById(R.id.phone_edit);
        eSurname=(EditText)findViewById(R.id.surname_edit);
        eName=(EditText)findViewById(R.id.name_edit);;
        Bundle bundle=getIntent().getExtras();
        account=null;

        if(bundle!=null)
            account=(Person)bundle.getSerializable("data");
        if(account!=null) {
            String s=personData.getText().toString();
            personData.setText(s + " " + account.getNickname());
            ePhone.setText(account.getTelephone());
            eSurname.setText(account.getSurname());
            eName.setText(account.getName());
        }

        Button save =(Button)findViewById(R.id.save_button);
        save.setOnClickListener(b->{

            boolean check=true;
            String number= String.valueOf(ePhone.getText());
            if(!"".equals(number) && '+'==number.toCharArray()[0] && number.toCharArray().length==12)
                check=number.substring(1).matches("\\d+");
            else
                check=false;
            if(check)
                this.finish();
            else {
                Toast toast = Toast.makeText(this, "Номер введен некорректно", Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }

    @Override
    public void finish(){
        String[] arr= new String[3];
        arr[0]=eName.getText().toString();
        arr[1]=eSurname.getText().toString();
        arr[2]=ePhone.getText().toString();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        intent.putExtra("data", arr);
        super.finish();
    }
}

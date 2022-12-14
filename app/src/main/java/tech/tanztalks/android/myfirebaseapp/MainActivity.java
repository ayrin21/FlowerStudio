package tech.tanztalks.android.myfirebaseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the title
        getSupportActionBar().setTitle("Flower Studio");

        //open login activity

//        Button buttonLogin= findViewById(R.id.button_login);
//        buttonLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });

        //open Register activity

        Button buttonRegister= findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
package com.example.silver_screen;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;  // Add this import

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.register_btn).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.login_btn) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        } else if (id == R.id.register_btn) {
            Intent i2 = new Intent(this, Register.class);
            startActivity(i2);
        }
//        else if (id == R.id.homepage_btn) {
//            Intent i3 = new Intent(this, DiscoverActivity.class);
//            startActivity(i3);
//        }

    }


}
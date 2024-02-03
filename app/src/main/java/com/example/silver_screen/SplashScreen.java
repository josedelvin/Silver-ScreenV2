package com.example.silver_screen;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Launch the layout -> splash.xml
        setContentView(R.layout.splash);
        Thread splashThread = new Thread() {


            public void run() {
                try {
                    // 3 seconds
                    sleep(3000);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
                }
            }
        };
        // To Start the thread
        splashThread.start();

    }

}

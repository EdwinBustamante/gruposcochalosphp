package com.edwinbustamante.gruposcochalos;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
      new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
              Intent i = new Intent(Splash.this,Main.class);
              startActivity(i);
              try {
                  finish();
              }catch (Exception e){

              }
          }
      },200);
    }
}

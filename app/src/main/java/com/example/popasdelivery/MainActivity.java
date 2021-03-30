package com.example.popasdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ImageView;
import android.os.Handler;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    //Variables
    Animation topAnim, bottomAnim;
    TextView fName, sName;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(Register_Login.SHARED_PREFS, MODE_PRIVATE);
        String emailLogin = sharedPreferences.getString("email", "");
        String passLogin = sharedPreferences.getString("password", "");

        TextView text = (TextView) findViewById(R.id.textView);
        String textGr = "PopasUtvin";
        SpannableString ss = new SpannableString(textGr);
        ForegroundColorSpan fRed = new ForegroundColorSpan(Color.BLACK);
        ss.setSpan(fRed, 5,10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setText(ss);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        image = findViewById(R.id.imageView);
        fName = findViewById(R.id.textView);

        image.setAnimation(bottomAnim);
        fName.setAnimation(topAnim);

        //Move to new screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(emailLogin.length() == 0 && passLogin.length() == 0)
                {
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                    finish();
                }
                else
                {
                    Intent i = new Intent(MainActivity.this, WorkPage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                    finish();
                }
                /**Intent intent = new Intent(MainActivity.this, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right, R.anim.slide_out_left);
                finish();**/
            }
        },SPLASH_SCREEN);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

}
package com.example.popasdelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CheckBox;
import android.text.SpannableString;
import android.graphics.Color;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class New_Account extends AppCompatActivity {
    Animation fade_in, fade_out;
    int greenis = 0, redis = 0;
    int isgreen = 0, isred = 0;
    int red = 0;
    int green = 0;
    int redp = 0;
    int greenp =0;
    int executed = 0;
    EditText emailCheck, passwordCheck, nameCheck, phoneCheck;
    Button btn_reg;
    ImageView imgView, imgView2, imgView3, imgView4;
    TextView txtView;
    CheckBox checkBox;
    boolean nameCheckers;
    boolean phoneChecker, emailChecker, passChecker, emailDB, checkChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__account);
        //Go back using '<' icon
        ImageButton back = (ImageButton) findViewById(R.id.imageButton2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Open terms activity
        TextView terms = (TextView) findViewById(R.id.textView12);
        String text = "Am citit și sunt de acord cu Termenele și condițiile, cu Politicile de confidențialitate și utilizare a cookie-urilor și tehnologiilor similare. Citeste";
        SpannableString ss = new SpannableString(text);
        ClickableSpan click = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(New_Account.this, Term_Page.class));
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLACK);
            }
        };
        ss.setSpan(click,145,152, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        terms.setText(ss);
        terms.setMovementMethod(LinkMovementMethod.getInstance());

        btn_reg = findViewById(R.id.button5);
        nameCheck = findViewById(R.id.editTextTextPersonName);
        phoneCheck = findViewById(R.id.editTextPhone);
        imgView = findViewById(R.id.imageView5);
        imgView2 = findViewById(R.id.imageView7);
        imgView3 = findViewById(R.id.imageView9);
        imgView4 = findViewById(R.id.imageView8);
        txtView = findViewById(R.id.textView13);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fade_in = AnimationUtils.loadAnimation(New_Account.this,R.anim.fade_in);
                fade_out = AnimationUtils.loadAnimation(New_Account.this,R.anim.fade_out);
                txtView.setTextColor(getResources().getColor(R.color.red_orange));
                emailCheck = findViewById(R.id.editTextTextEmailAddress3);
                String emailul = emailCheck.getText().toString();
                String name = nameCheck.getText().toString();
                MotionLayout motion1 = findViewById(R.id.motionLayout1);
                nameCheckers = validateName(motion1);
                if(nameCheckers){
                    emailChecker = validateEmail(motion1);
                }
                if(emailChecker) {
                    phoneChecker = validatePhone(motion1);
                }
                if(phoneChecker) {
                    passChecker = validatePassword(motion1);
                }
                if(passChecker){
                    checkBox = findViewById(R.id.checkBox);
                    if(checkBox.isChecked()){
                        checkChecker = true;
                    }
                    else{
                        checkChecker = false;
                        if(motion1.getProgress() == 0){
                            motion1.transitionToEnd();
                            txtView.setText("Ai uitat sa bifezi casuta");
                            txtView.setAnimation(fade_in);
                        }else{
                            txtView.setText("Ai uitat sa bifezi casuta");
                            txtView.setAnimation(fade_in);
                        }
                    }
                }
                if(checkChecker) {
                    String url = "http://167.99.246.91/register.php";
                    checkEmailFromDB(url, emailul,motion1);
                }
            }
        });
    }

    public boolean validateName(MotionLayout motion1){
        if(nameCheck.getText().toString().isEmpty()){
            if(red == 0) {
                if(motion1.getProgress() == 0) {
                    motion1.transitionToEnd();
                }
                txtView.setAnimation(fade_in);
                txtView.setText("Verifica numele introdus");
                imgView.setAnimation(fade_in);
                red = 1;
                green = 0;
                imgView.setImageResource(R.drawable.ic_baseline_error_outline_24);
            }
            return false;
        }
        else if(nameCheck.getText().toString().length() < 3){
            if(red == 0) {
                imgView.setAnimation(fade_in);
                if(motion1.getProgress() == 0) {
                    motion1.transitionToEnd();
                }
                txtView.setAnimation(fade_in);
                txtView.setText("Verifica numele introdus");
                red = 1;
                green = 0;
                imgView.setImageResource(R.drawable.ic_baseline_error_outline_24);
            }
            return false;
        }
        else{
            if(green == 0){
                red = 0;
                green=1;
                imgView.setAnimation(fade_in);
                imgView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                txtView.setText("");
            }
            return true;
        }
    }

    public boolean validatePhone(MotionLayout motion1){
        if(phoneCheck.length() < 10){
            if(redp == 0) {
                if(motion1.getProgress() == 0) {
                    motion1.transitionToEnd();
                }
                txtView.setAnimation(fade_in);
                txtView.setText("Numarul de telefon nu este valid");
                imgView3.setAnimation(fade_in);
                imgView3.setImageResource(R.drawable.ic_baseline_error_outline_24);
                redp = 1;
                greenp = 0;
            }
            return false;
        }
        else{
            if(greenp == 0){
                imgView3.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                imgView3.setAnimation(fade_in);
                redp=0;
                greenp=1;
                txtView.setText("");
            }
            return true;
        }
    }

    public void registerAccount(String url, String email, String pass, String name, String phone){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(New_Account.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_email", email);
                params.put("user_pass", pass);
                params.put("user_name", name);
                params.put("user_phone", phone);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(New_Account.this);
        requestQueue.add(stringRequest);
    }

    public void checkEmailFromDB(String url, String Email, MotionLayout motion1) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("trimis")) {
                    if(executed == 0){
                        executed = 1;
                        if(motion1.getProgress() == 0) {
                            motion1.transitionToEnd();
                        }
                        txtView.setText("Email-ul introdus este deja folosit");
                        if(redis != 1) {
                            imgView2.setImageResource(R.drawable.ic_baseline_error_outline_24);
                            imgView2.setAnimation(fade_in);
                        }
                    }
                } else {
                    txtView.setTextColor(getResources().getColor(R.color.green_light));
                    String arg[] = {emailCheck.getText().toString(),passwordCheck.getText().toString(), nameCheck.getText().toString(), phoneCheck.getText().toString()};
                    String url_reg = "http://167.99.246.91/reg.php";
                    registerAccount(url_reg,emailCheck.getText().toString(),passwordCheck.getText().toString(), nameCheck.getText().toString(), phoneCheck.getText().toString());
                    if(motion1.getProgress() == 0) {
                        motion1.transitionToEnd();
                    }
                    txtView.setText("Contul a fost creat cu succes!");
                    Handler deleteI = new Handler();
                    deleteI.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent newI = new Intent(New_Account.this,Dashboard.class);
                            newI.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(newI);
                        }
                    },2000);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(New_Account.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_email", Email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(New_Account.this);
        requestQueue.add(stringRequest);
    }

    public boolean validateEmail(MotionLayout motion1){
            if(emailCheck.getText().toString().isEmpty()){
                if(redis == 0){
                    if(motion1.getProgress() == 0) {
                        motion1.transitionToEnd();
                    }
                    txtView.setText("Email-ul introdus este invalid");
                    txtView.setAnimation(fade_in);
                    executed = 0;
                    imgView2.setAnimation(fade_in);
                    imgView2.setImageResource(R.drawable.ic_baseline_error_outline_24);
                    greenis = 0;
                    redis = 1;
                }
                return false;
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(emailCheck.getText().toString()).matches()){
                if(redis == 0){
                    if(motion1.getProgress() == 0) {
                        motion1.transitionToEnd();
                    }
                    txtView.setText("Email-ul introdus este invalid");
                    txtView.setAnimation(fade_in);
                    imgView2.setAnimation(fade_in);
                    imgView2.setImageResource(R.drawable.ic_baseline_error_outline_24);
                    greenis = 0;
                    executed = 0;
                    redis = 1;
                }
                return false;
            }
            else{
                if(greenis == 0){
                    imgView2.setAnimation(fade_in);
                    imgView2.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                    redis = 0;
                    executed = 0;
                    greenis = 1;
                    txtView.setText("");
                }
                return true;
            }
    }
    //Verify Password
    public boolean validatePassword(MotionLayout motion1){
        passwordCheck = findViewById(R.id.editTextTextPassword2);
        String password = passwordCheck.getText().toString();
        imgView4 = findViewById(R.id.imageView8);
        if(password.isEmpty()){
            if(isred == 0){
                if(motion1.getProgress() == 0) {
                    motion1.transitionToEnd();
                }
                txtView.setAnimation(fade_in);
                txtView.setText("Parola introdusa este invalida");
                imgView4.setAnimation(fade_in);
                imgView4.setImageResource(R.drawable.ic_baseline_error_outline_24);
                isgreen = 0;
                isred = 1;
            }
            return false;
        }
        else if(!PASSWORD_PATTERN.matcher(password).matches()){
            if(isred == 0){
                if(motion1.getProgress() == 0) {
                    motion1.transitionToEnd();
                }
                txtView.setAnimation(fade_in);
                txtView.setText("Parola introdusa este invalida");
                imgView4.setAnimation(fade_in);
                imgView4.setImageResource(R.drawable.ic_baseline_error_outline_24);
                isgreen = 0;
                isred = 1;
            }
            return false;
        }
        else{
            if(isgreen == 0){
                imgView4.setAnimation(fade_in);
                imgView4.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                isgreen = 1;
                isred = 0;
                txtView.setText("");
            }
            return true;
        }
    }

    //Close activity
    @Override
    public void finish() {
        super.finish();
    }

    public static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@"+
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9]) "+
                    //"(?=.*[a-z])" +
                    //"(?=.*[A-Z])" +
                    "(?=.*[a-zA-Z0-9])" +
                    //"(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{6,}" +
                    "$");
}
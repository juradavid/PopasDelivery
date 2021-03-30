package com.example.popasdelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.SharedPreferences;
import android.os.Handler;
import android.content.Intent;
import android.graphics.Color;
import java.sql.Connection;
import android.graphics.drawable.AnimatedImageDrawable;
import android.media.Image;
import android.text.Spanned;
import android.text.TextPaint;
import android.util.Patterns;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.text.method.PasswordTransformationMethod;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.Button;
import java.sql.PreparedStatement;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.muddzdev.styleabletoast.StyleableToast;

import org.w3c.dom.Text;
import java.sql.SQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public class Register_Login extends AppCompatActivity {

    public static String SHARED_PREFS = "com.popas.profile";

    int checkDBAcc = 0;
    private boolean isShowPass = false;
    int isEmail = 1, isPass = 1, passText = 1;
    int isRed = 0, isGreen = 0 ,redIs = 0, greenIs = 0;
    EditText checkEmail, checkPassword;
    Button authBtn;
    Button checkClickable;
    Animation fade_in, fade_out;
    boolean verifiedEmail = false, verifiedPassword = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__login);
        //Click icon eye to show or hide
        ImageButton show_hide = (ImageButton) findViewById(R.id.imageButton4);
        show_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowPass = !isShowPass;
                show_hide_pass(isShowPass);
            }
        });

        //Move to recover password
        TextView text_button1 = (TextView) findViewById(R.id.textView6);
        text_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register_Login.this, Reset_Password.class);
                startActivity(intent);
            }
        });

        //Use 'X' icon to close the activity
        ImageButton close = (ImageButton) findViewById(R.id.imageButton3);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Move to create new account
        TextView new_account = (TextView) findViewById(R.id.textView7);
        String new_accstr = "Nu ai cont? Creează cont nou";
        SpannableString ss = new SpannableString(new_accstr);
        ClickableSpan click = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(Register_Login.this,New_Account.class));
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLACK);
            }
        };
        ss.setSpan(click, 12,28,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        new_account.setText(ss);
        new_account.setMovementMethod(LinkMovementMethod.getInstance());

        //Email Validate
        Button checkBtn = findViewById(R.id.button3);
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBtn.setClickable(false);
                verifiedEmail = validateEmail();
                if(verifiedEmail){
                    verifiedPassword = validatePassword();
                }else{
                    checkBtn.setClickable(true);
                }
                if(verifiedEmail && verifiedPassword){
                    // Remove the movement
                    MotionLayout motion = findViewById(R.id.motionlayout);
                    //Check if the account is in the db
                    EditText email = findViewById(R.id.editTextTextEmailAddress);
                    EditText password = findViewById(R.id.editTextTextPassword);
                    String userEmail = email.getText().toString();
                    String userPassword = password.getText().toString();
                    String url = "http://167.99.246.91/login.php";
                    TextView error = findViewById(R.id.textView2);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("trimis")){
                                SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE).edit();
                                editor.putString("email",userEmail);
                                editor.putString("password",userPassword);
                                editor.putBoolean("verify",false);
                                editor.putString("my_orders","");
                                editor.putString("cantitate", "");
                                editor.putString("theOrders", "");
                                editor.putInt("totalCan", 0);
                                editor.putInt("totalValue", 0);
                                editor.commit();
                                error.setTextColor(getResources().getColor(R.color.green_light));
                                if(motion.getProgress() == 1){
                                    error.setText("Te-ai logat cu succes!");
                                    error.setTextSize(12);
                                }
                                else{
                                    error.setAnimation(fade_in);
                                    error.setText("Te-ai logat cu succes!");
                                    error.setTextSize(12);
                                    motion.transitionToEnd();
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(Register_Login.this, WorkPage.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                },2000);
                            }
                            else {
                                checkBtn.setClickable(true);
                                if(motion.getProgress() == 1) {
                                    if(checkDBAcc == 0) {
                                        error.setAnimation(fade_in);
                                        error.setText(R.string.accInvalid);
                                        error.setTextSize(12);
                                    }
                                    checkDBAcc = 1;
                                    redIs = 0;
                                    isRed = 0;
                                }
                                if(motion.getProgress() == 0){
                                    motion.transitionToEnd();
                                    if(checkDBAcc == 0) {
                                        error.setAnimation(fade_in);
                                        error.setText(R.string.accInvalid);
                                        error.setTextSize(12);
                                    }
                                    checkDBAcc = 1;
                                    redIs = 0;
                                    isRed = 0;
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Register_Login.this, error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("user_email", userEmail);
                            params.put("user_pass", userPassword);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(Register_Login.this);
                    requestQueue.add(stringRequest);
                }
                else{
                    checkBtn.setClickable(true);
                }
            }
        });
    }
    //Verify Email Address
    public boolean validateEmail(){
        checkEmail = findViewById(R.id.editTextTextEmailAddress);
        String email = checkEmail.getText().toString();
        ImageView checkImg = findViewById(R.id.imageView4);
        TextView errorM = findViewById(R.id.textView2);
        errorM.setTextColor(getResources().getColor(R.color.red_orange));
        fade_in = AnimationUtils.loadAnimation(Register_Login.this,R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(Register_Login.this,R.anim.fade_out);
        if(email.isEmpty()){
            if(isRed == 0){
                checkImg.setImageResource(R.drawable.ic_baseline_error_outline_24);
                checkImg.setAnimation(fade_in);
                errorM.setAnimation(fade_in);
                isGreen = 0;
                isRed = 1;
                if(errorM.getText().toString().isEmpty() || !errorM.getText().toString().isEmpty() && !errorM.getText().toString().contentEquals("Te rugăm să introduci o adresă de e-mail validă")){
                    errorM.setTextSize(13);
                    errorM.setAnimation(fade_in);
                    errorM.setText(R.string.emailError);
                }
                MotionLayout motion = findViewById(R.id.motionlayout);
                if(motion.getProgress() == 0){
                    motion.transitionToEnd();
                }
            }
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            if(isRed == 0){
                checkImg.setAnimation(fade_in);
                checkImg.setImageResource(R.drawable.ic_baseline_error_outline_24);
                errorM.setAnimation(fade_in);
                isGreen = 0;
                isRed = 1;
                if(errorM.getText().toString().isEmpty() || !errorM.getText().toString().isEmpty() && !errorM.getText().toString().contentEquals("Te rugăm să introduci o adresă de e-mail validă")){
                    errorM.setTextSize(13);
                    errorM.setAnimation(fade_in);
                    errorM.setText(R.string.emailError);
                }
                MotionLayout motion = findViewById(R.id.motionlayout);
                if(motion.getProgress() == 0){
                    motion.transitionToEnd();
                }
            }
            return false;
        }
        else{
            if(isGreen == 0){
                checkImg.setAnimation(fade_in);
                checkImg.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                errorM.setAnimation(fade_out);
                isRed = 0;
                isGreen = 1;
                redIs = 0;
                checkDBAcc = 0;
                if(!errorM.getText().toString().isEmpty()){
                    errorM.setTextSize(0);
                    errorM.setAnimation(fade_out);
                    errorM.setText("");
                }
            }
            return true;
        }
    }
    //Verify Password
    public boolean validatePassword() {
        checkPassword = findViewById(R.id.editTextTextPassword);
        fade_in = AnimationUtils.loadAnimation(Register_Login.this, R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(Register_Login.this, R.anim.fade_out);
        String pass = checkPassword.getText().toString();
        ImageView Image = findViewById(R.id.imageView6);
        TextView errorMessage = findViewById(R.id.textView2);
        errorMessage.setTextColor(getResources().getColor(R.color.red_orange));
        ImageButton checkButton = (ImageButton) findViewById(R.id.imageButton4);
        if (pass.isEmpty()) {
            if (redIs == 0) {
                Image.setAnimation(fade_in);
                Image.setImageResource(R.drawable.ic_baseline_error_outline_24);
                errorMessage.setAnimation(fade_in);
                checkButton.setTranslationX(-75);
                greenIs = 0;
                redIs = 1;
                if (errorMessage.getText().toString().isEmpty() || !errorMessage.getText().toString().isEmpty()) {
                    errorMessage.setTextSize(13);
                    errorMessage.setAnimation(fade_in);
                    errorMessage.setText(R.string.passError);
                }
                MotionLayout motion = findViewById(R.id.motionlayout);
                if (motion.getProgress() == 0) {
                    motion.transitionToEnd();
                }
            }
            return false;
        } else if (!PASSWORD_PATTERN.matcher(pass).matches()) {
            if (redIs == 0) {
                Image.setAnimation(fade_in);
                Image.setImageResource(R.drawable.ic_baseline_error_outline_24);
                errorMessage.setAnimation(fade_in);
                greenIs = 0;
                redIs = 1;
                checkButton.setTranslationX(-75);
                if (errorMessage.getText().toString().isEmpty() || !errorMessage.getText().toString().isEmpty()) {
                    errorMessage.setTextSize(13);
                    errorMessage.setAnimation(fade_in);
                    errorMessage.setText(R.string.passError);
                }
                MotionLayout motion = findViewById(R.id.motionlayout);
                if (motion.getProgress() == 0) {
                    motion.transitionToEnd();
                }
            }
            return false;
        } else {
            if (greenIs == 0) {
                Image.setAnimation(fade_in);
                Image.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                errorMessage.setAnimation(fade_out);
                checkButton.setTranslationX(-75);
                greenIs = 1;
                redIs = 0;
                isRed = 0;
                checkDBAcc = 0;
                if (!errorMessage.getText().toString().isEmpty()) {
                    errorMessage.setTextSize(0);
                    errorMessage.setAnimation(fade_out);
                    errorMessage.setText("");
                }
            }
            return true;
        }
    }
    //Show/Hide password
    public void show_hide_pass(boolean isShow){
        ImageButton password = (ImageButton) findViewById(R.id.imageButton4);
        EditText etpass = (EditText) findViewById(R.id.editTextTextPassword);
        if(isShow){
            etpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            password.setImageResource(R.drawable.ic_hide_pass_24);
        }
        else{
            etpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
            password.setImageResource(R.drawable.ic_show_pass_23);
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
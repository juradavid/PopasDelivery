package com.example.popasdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;

import android.os.Bundle;
import android.os.Handler;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muddzdev.styleabletoast.StyleableToast;
import com.mysql.cj.x.protobuf.MysqlxSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class Reset_Password extends AppCompatActivity {

    //Variables
    private ImageButton back;
    int green = 0;
    int red = 0;
    int cod_green = 0, cod_red=0;

    //Variables SendEmail**/
    EditText mail;
    Button send;
    Animation fade_in, fade_out;
    String message, subject;
    Random cod = new Random();
    String codul;
    String codul_random;
    Dialog dialog;
    EditText codEmail;
    Button btnAnulare, btnResetare;
    String cod_primit;
    ImageView cod_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset__password);
        dialog = new Dialog(Reset_Password.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        btnAnulare = dialog.findViewById(R.id.btn_anulare);
        btnResetare = dialog.findViewById(R.id.btn_resetare);
        Button codBtn = findViewById(R.id.button4);
        btnAnulare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cod_check = dialog.findViewById(R.id.imageView3);
                codEmail = dialog.findViewById(R.id.edit_code);
                cod_primit = codEmail.getText().toString();
                codul = null;
                codBtn.setClickable(true);
                dialog.dismiss();
                codEmail.setText("");
                cod_check.setImageResource(0);
                codEmail.setBackground(getDrawable(R.drawable.type_email_rounded));
            }
        });
        btnResetare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codBtn.setClickable(true);
                cod_check = dialog.findViewById(R.id.imageView3);
                codEmail = dialog.findViewById(R.id.edit_code);
                cod_primit = codEmail.getText().toString();
                fade_in = AnimationUtils.loadAnimation(Reset_Password.this, R.anim.fade_in);
                if(codul.contentEquals(cod_primit)) {
                    if(cod_green == 0) {
                        cod_check.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                        cod_check.setAnimation(fade_in);
                        codEmail.setBackground(getDrawable(R.drawable.correct_code_typed));
                        cod_green = 1;
                        cod_red = 0;
                    }
                    btnAnulare.setClickable(false);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(Reset_Password.this,SetNewPassword.class));
                            dialog.dismiss();
                            btnAnulare.setClickable(true);
                            codEmail.setText("");
                            cod_check.setImageResource(0);
                            codul = null;
                            codEmail.setBackground(getDrawable(R.drawable.type_email_rounded));
                        }
                    }, 2000);
                }else{
                    if(cod_red == 0) {
                        cod_check.setImageResource(R.drawable.ic_baseline_error_outline_24);
                        cod_check.setAnimation(fade_in);
                        codEmail.setBackground(getDrawable(R.drawable.wrong_code_typed));
                        cod_red = 1;
                        cod_green = 0;
                    }
                }
            }
        });
        //Dialog Variables
        back = findViewById(R.id.imageButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeRes_pass();
            }
        });
        mail = findViewById(R.id.editTextTextEmailAddress2);
        send = findViewById(R.id.button4);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mail.getText().toString();
                String url = "http://167.99.246.91/checkemail.php";
                if(Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if(green == 0) {
                        ImageView imageView = findViewById(R.id.imageView6);
                        fade_in = AnimationUtils.loadAnimation(Reset_Password.this, R.anim.fade_in);
                        imageView.setAnimation(fade_in);
                        imageView.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
                        green = 1;
                        red = 0;
                    }
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("trimis")){
                                codBtn.setClickable(false);
                                TextView textView = findViewById(R.id.textView7);
                                fade_out = AnimationUtils.loadAnimation(Reset_Password.this, R.anim.fade_out);
                                textView.setAnimation(fade_out);
                                textView.setTextSize(0);
                                codul = randomNumber();
                                message = "Aici ai codul de resetare a parolei: " + codul;
                                subject = "Popas Utvin - Codul De Resetare";
                                sendEmail(message, subject);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.show();
                                    }
                                }, 2000);
                            }
                            else {
                                if(green == 1){
                                    TextView textView = findViewById(R.id.textView7);
                                    textView.setAnimation(fade_in);
                                    textView.setText(R.string.emailInvalid);
                                    textView.setTextSize(13);
                                    red = 0;
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Reset_Password.this, error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("user_email", email);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(Reset_Password.this);
                    requestQueue.add(stringRequest);
                }
                else{
                    if(red == 0) {
                        ImageView imageView = findViewById(R.id.imageView6);
                        fade_in = AnimationUtils.loadAnimation(Reset_Password.this, R.anim.fade_in);
                        imageView.setAnimation(fade_in);
                        imageView.setImageResource(R.drawable.ic_baseline_error_outline_24);
                        TextView textView = findViewById(R.id.textView7);
                        textView.setAnimation(fade_in);
                        textView.setText(R.string.emailError);
                        textView.setTextSize(13);
                        green = 0;
                        red = 1;
                    }
                }
            }
        });
    }
    public void closeRes_pass(){
        super.finish();
    }

    private void sendEmail(String message, String subject){
        String email = mail.getText().toString();
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,email,subject,message);
        javaMailAPI.execute();
    }


    private String randomNumber(){
        int number = cod.nextInt(999999);
        return String.format("%06d", number);
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
}
package com.example.popasdelivery;

import androidx.annotation.NonNull;
import androidx.annotation.StyleableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;

public class Dashboard extends AppCompatActivity {

    private Button reg_log_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        reg_log_button = findViewById(R.id.button2);
        reg_log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister_Login();
            }
        });

        TextView text = (TextView) findViewById(R.id.textView3);

        String textGr = "PopasUtvin";

        SpannableString ss = new SpannableString(textGr);

        ForegroundColorSpan fRed = new ForegroundColorSpan(Color.BLACK);

        ss.setSpan(fRed, 5,10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setText(ss);

    }

    @Override
    public void onBackPressed() {
            //super.onBackPressed();
    }

    public void openRegister_Login(){
        Intent intent = new Intent(this, Register_Login.class);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
    }

}
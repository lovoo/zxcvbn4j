package com.nulabinc.zxcvbnz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.nulabinc.zxcvbn.Zxcvbn;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.textView)).setText(new Zxcvbn(this).measure(this, "Hello Wrodl").getScore() + "");
    }
}

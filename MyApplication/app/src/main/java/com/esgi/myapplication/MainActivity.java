package com.esgi.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView numText = (TextView)findViewById(R.id.num);
        TextView messageText = (TextView)findViewById(R.id.message);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String num = bundle.getString("num");
            String messages = bundle.getString("message");

            messageText.setText(messages);
            numText.setText(num);

        }
    }


}

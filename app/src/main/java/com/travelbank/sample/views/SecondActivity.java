package com.travelbank.sample.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.travelbank.knit.KnitView;
import com.travelbank.sample.R;

/**
 * Created by omerozer on 2/6/18.
 */

@KnitView
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void recMes(String message){
        ((TextView)findViewById(R.id.textView_rand)).setText(message);
    }

    public String getEditText(){
        return "test";
    }

    public Object getObj(){
        return null;
    }

    public int getInt(){
        return 8;
    }

}

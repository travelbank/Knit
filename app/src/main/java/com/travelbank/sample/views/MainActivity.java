package com.travelbank.sample.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.travelbank.knit.Knit;
import com.travelbank.knit.KnitView;
import com.travelbank.sample.R;

@KnitView
public class MainActivity extends AppCompatActivity {

    public static final String BUTTON_CLICK = "button";

    Button button;

    EditText edit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.button = (Button)findViewById(R.id.btn_next);
        this.edit = (EditText)findViewById(R.id.edit);
        Knit.getInstance().getViewEvents().onClick(BUTTON_CLICK,this,button);
    }

    public void recMes(String message){
        ((TextView)findViewById(R.id.textView_t)).setText(message);
    }

    public void showToast(String message){
        Toast.makeText(this,"YASSTOASTHEYA",Toast.LENGTH_LONG).show();
    }

    public String get(){
        return edit.getText().toString();
    }


}

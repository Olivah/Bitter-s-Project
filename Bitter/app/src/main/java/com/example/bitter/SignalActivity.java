package com.example.bitter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bitter.Class.Information;

public class SignalActivity extends AppCompatActivity {

    String input;
    EditText userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal);

        userInput = (EditText) findViewById(R.id.userInput);
    }

    public void signalHandler(View v){
        Information.pushInfo("Signal",Information.getUser().getCode().toString(), ((EditText)findViewById(R.id.userInput)).getText().toString());
        showToast("Inviato con successo\n Grazie del supporto");
        finish();
    }

    private void showToast (String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
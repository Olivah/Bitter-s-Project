package com.example.bitter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        Intent intent = new Intent(this, TabsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        input = userInput.getText().toString();
        intent.putExtra("input", input);
        startActivity(intent);
        finish();
        showToast("Inviato con successo\n Grazie del supporto");
    }

    private void showToast (String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
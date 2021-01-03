package com.example.bitter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bitter.Class.Code;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SignalActivity extends AppCompatActivity {

    private Date date;
    private SimpleDateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal);
    }

    // funzione che si occupa di aggiungere una segnalazione nel database
    public void signalHandler(View v){
        this.date = new Date();
        this.formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String input= ((EditText)findViewById(R.id.userInput)).getText().toString();
        MainActivity.info.pushInfo("Mall_List/Nave_de_Vero/Signal",new Code().toString(),formatter.format(date)+" "+input);
        showToast("Inviato con successo\n Grazie del supporto");
        finish();
    }

    // funzione che si occupa di mostrare a schermo una stringa
    private void showToast (String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
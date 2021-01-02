package com.example.bitter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bitter.Class.Code;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignalActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference rootNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal);
        firebaseDatabase= MainActivity.info.getFirebaseDatabase();
    }

    // funzione che si occupa di aggiungere una segnalazione nel database
    public void signalHandler(View v){
        String input= ((EditText)findViewById(R.id.userInput)).getText().toString();
        rootNode= firebaseDatabase.getReference("Mall_List");
        rootNode.child("Nave_de_Vero").child("Signal").child(new Code().toString()).setValue(input);
        showToast("Inviato con successo\n Grazie del supporto");
        finish();
    }

    // funzione che si occupa di mostrare a schermo una stringa
    private void showToast (String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
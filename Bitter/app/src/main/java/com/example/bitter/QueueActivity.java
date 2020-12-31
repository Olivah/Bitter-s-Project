package com.example.bitter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class QueueActivity extends AppCompatActivity {

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((TextView)findViewById(R.id.user_number)).setText("X");
        //((TextView)findViewById(R.id.tot_people)).setText(((TextView)findViewById(R.id.tot_people)).getText().toString().replace("?","100"));
        timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(QueueActivity.this, TabsActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    public void exitHandler(View v){
        this.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Sei sicuro di voler uscire? ");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
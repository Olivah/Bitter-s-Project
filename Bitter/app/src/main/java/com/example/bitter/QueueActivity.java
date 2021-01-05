package com.example.bitter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class QueueActivity extends AppCompatActivity {

    private Timer timer= new Timer();
    private String mall;
    private String shop;
    private boolean isShop=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        Intent i=getIntent();
        mall=i.getStringExtra("Mall");
        if (i.hasExtra("Shop")) {
            shop = i.getStringExtra("Shop");
            isShop=true;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!isShop) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (MainActivity.info.enqueueMall(mall)) {
                        Intent intent = new Intent(QueueActivity.this, TabsActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 200);
        }else if(isShop){
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (MainActivity.info.enqueueShop(mall, shop)) {
                        Intent intent = new Intent(QueueActivity.this, TabsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        finish();
                    }
                }
            }, 200);
        }
        showToast("Accesso eseguito");
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        timer.purge();
        super.onDestroy();
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

    private void showToast (String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
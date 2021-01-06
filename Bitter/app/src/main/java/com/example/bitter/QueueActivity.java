package com.example.bitter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class QueueActivity extends AppCompatActivity {

    private String mall;
    private String shop;
    private boolean isShop=false;
    private Thread threadMall= new Thread(new Runnable() {
        @Override
        public void run() {
            if (MainActivity.info.enqueueMall(mall, threadMall)) {
                Intent intent = new Intent(QueueActivity.this, TabsActivity.class);
                startActivity(intent);
                finish();
            }
        }
    });
    private Thread threadShop= new Thread(new Runnable() {
        @Override
        public void run() {
            if (MainActivity.info.enqueueShop(mall, shop, threadShop)) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        }
    });

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
            threadMall.start();
        }else if(isShop){
            threadShop.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!isShop) {
            threadMall.interrupt();
        }else if(isShop){
            threadShop.interrupt();
        }
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
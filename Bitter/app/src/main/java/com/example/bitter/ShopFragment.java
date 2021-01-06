package com.example.bitter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ShopFragment extends Fragment {

    private ListView listView;
    private FragmentActivity context;
    public static boolean isInside;
    private View disabledElement;
    private Button exitButton;
    private String shopName;
    private Timer exit= new Timer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_shop, container, false);

        listView= v.findViewById(R.id.ListView);

        // mi prendo la lista di negozi dal database
        final ArrayList<String> list= new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        Query shops= MainActivity.info.getInfo("Mall_List/Nave_de_Vero/Shop_List_Name");
        shops.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        list.add(snapshot.getValue().toString());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!isInside) {
                    shopName = list.get(position);
                    disabledElement = view;

                    Intent i= new Intent(context, QueueActivity.class);
                    i.putExtra("Mall", "Nave_de_Vero");
                    i.putExtra("Shop", shopName);
                    startActivityForResult(i,1);
                }else{
                    showToast("Sei già dentro un negozio");
                }
            }
        });

        exitButton= v.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("Sei sicuro di voler uscire? ");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    exit.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            MainActivity.info.dequeueShop("Nave_de_Vero", shopName);
                            disabledElement.setEnabled(true);
                            isInside=false;
                            exitButton.setVisibility(View.INVISIBLE);
                        }
                    }, 100);
                    showToast("Sei uscito dal negozio");
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        return v;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        exit.cancel();
        exit.purge();
        exit=null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context= getActivity();
    }

    public void enterSuccess(){
        showToast("Sei entrato in "+shopName);
        disabledElement.setEnabled(false);
        exitButton.setVisibility(View.VISIBLE);
        isInside = true;
    }

    private void showToast (String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
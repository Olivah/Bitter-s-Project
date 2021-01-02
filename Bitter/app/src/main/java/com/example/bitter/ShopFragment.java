package com.example.bitter;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
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

import static java.lang.Double.parseDouble;

public class ShopFragment extends Fragment {

    private ListView listView;
    private FragmentActivity context;
    private boolean isInside;
    private View disabledElement;
    private Button exitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_shop, container, false);

        listView= (ListView) v.findViewById(R.id.ListView);

        final ArrayList<String> list= new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        Query shops= MainActivity.info.getInfo().child("Mall_List").child("Nave_de_Vero").child("Shop_List_Name");
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
                    disabledElement=view;
                    view.setEnabled(false);
                    showToast("Sei entrato in: "+list.get(position));
                    isInside = true;
                    exitButton.setVisibility(View.VISIBLE);
                }else{
                    showToast("Sei già dentro un negozio");
                }
            }
        });

        exitButton= ((Button) v.findViewById(R.id.exitButton));
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disabledElement.setEnabled(true);
                isInside=false;
                v.setVisibility(View.INVISIBLE);
            }
        });

        return v;
    }

    private void showToast (String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context= getActivity();
    }
}
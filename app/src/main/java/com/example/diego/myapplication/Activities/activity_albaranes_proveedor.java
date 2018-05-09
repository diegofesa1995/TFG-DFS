package com.example.diego.myapplication.Activities;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.diego.myapplication.AdapterAlbaranes;
import com.example.diego.myapplication.Objects.Cliente;
import com.example.diego.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class activity_albaranes_proveedor extends AppCompatActivity {

    private String claveproveedor = "";
    private AdapterAlbaranes adapteralbaranes;
    private FirebaseStorage storage;
    private StorageReference storageref;
    private ListView listviewalbaranes;
    private StorageReference albaranesRef;
    private DatabaseReference dbalbaranes;
    private final long ONEGB = 1024 * 1024*1024;
    private ArrayList<Bitmap> listaimagenes = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albaranes_proveedor);

        claveproveedor = getIntent().getExtras().getString("claveproveedor");

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbaralbaranesproveedor);
        toolbar.setTitle("Albaranes - " + claveproveedor);*/
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storage = FirebaseStorage.getInstance();
        storageref = storage.getReferenceFromUrl("gs://tfgr-f044d.appspot.com");
        Log.d("CLAVEEEEEE", claveproveedor);

        dbalbaranes = FirebaseDatabase.getInstance().getReference("AlbaranesProveedores");

        listviewalbaranes = (ListView) findViewById(R.id.listviewalbaranesproveedor);
        adapteralbaranes = new AdapterAlbaranes(activity_albaranes_proveedor.this, listaimagenes);
        listviewalbaranes.setAdapter(adapteralbaranes);


        dbalbaranes.child(claveproveedor).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int cont=0;
                adapteralbaranes.clear();
                for (DataSnapshot image : dataSnapshot.getChildren()) {
                    storageref.child("albaranes").child(claveproveedor).child(image.getKey()).getBytes(ONEGB).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            try {
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                adapteralbaranes.add(bmp);
                                adapteralbaranes.notifyDataSetChanged();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Error: Descarga de imagen 1", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("EXCEPCIOOONNN", e.toString());
                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}

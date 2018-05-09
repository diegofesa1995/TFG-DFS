package com.example.diego.myapplication.FragmentsPedidos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.diego.myapplication.AdapterPedidos;
import com.example.diego.myapplication.Objects.Pedido;
import com.example.diego.myapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class pedidos_pendientes extends Fragment {
    private FirebaseRecyclerAdapter adapter;
    private View rootView;
    private ListView listview;
    private String name;
    private Query dbPedidos;
    private static ArrayList<Pedido> pedidos = new ArrayList<>();
    private AdapterPedidos adapterpedidos;
    private Pedido pedidoseleccionado;
    private Dialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.listviewpedidos, container, false);

        dbPedidos = FirebaseDatabase.getInstance().getReference("Pedidos").orderByChild("fecha");

        listview = (ListView) rootView.findViewById(R.id.listviewpedidos);
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Cargando datos..");
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        dbPedidos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    almacenarpedidospendientes(dataSnapshot);
                    adapterpedidos = new AdapterPedidos(getActivity(), pedidos);
                    adapterpedidos.notifyDataSetChanged();
                    listview.setAdapter(adapterpedidos);
                    progress.dismiss();
                }catch (Exception e){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pedidoseleccionado = (Pedido) parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View vista = getActivity().getLayoutInflater().inflate(R.layout.pop_info_pedido, null);
                builder.setView(vista);
                dialog = builder.create();
                dialog.show();
            }
        });

        return rootView;
    }

    public void almacenarpedidospendientes(DataSnapshot dataSnapshot){
        pedidos.clear();
        for(DataSnapshot data: dataSnapshot.getChildren()){
            Pedido p = new Pedido();
            if(data.getValue(Pedido.class).getEstado().equalsIgnoreCase("Pendiente")){
                p.setRazonSocial(data.getValue(Pedido.class).getRazonSocial());
                p.setFecha(data.getValue(Pedido.class).getFecha());
                p.setDireccion(data.getValue(Pedido.class).getDireccion());
                pedidos.add(p);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }



}

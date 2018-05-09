package com.example.diego.myapplication.FragmentsPedidos;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diego.myapplication.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class pedidos_cobrados extends Fragment {
    private FirebaseRecyclerAdapter adapter;
    private View rootView;
    private RecyclerView recycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.listviewpedidos, container, false);
        Query dbPedidos = FirebaseDatabase.getInstance().getReference().child("Pedidos").orderByChild("Estado").equalTo("Cobrado");
      /*  recycler = (RecyclerView) rootView.findViewById(R.id.recviewpedpend);
        recycler.setHasFixedSize(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FirebaseRecyclerAdapter<Pedido,Holder>(Pedido.class,R.layout.lista_pedidos,Holder.class,dbPedidos){

            @Override
            protected void populateViewHolder(Holder viewHolder, Pedido model, int position) {
                    viewHolder.setCliente(model.getCodigoCliente());

            }
        };

        recycler.setAdapter(adapter);
*/
        return rootView;



    }

    public void onDestroy(){
        super.onDestroy();
    }

}

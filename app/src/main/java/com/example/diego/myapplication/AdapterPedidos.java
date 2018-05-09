package com.example.diego.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.diego.myapplication.Objects.Pedido;
import com.example.diego.myapplication.Objects.ProductoStock;

import java.util.ArrayList;
import java.util.List;

import static com.example.diego.myapplication.R.layout.lista_pedidos;
import static com.example.diego.myapplication.R.layout.lista_productos_stock;

/**
 * Created by diego on 16/03/2018.
 */

public class AdapterPedidos extends ArrayAdapter<Pedido> implements Filterable {

    protected ArrayList<Pedido> items;

    public AdapterPedidos(@NonNull Context context, @NonNull ArrayList<Pedido> datos) {
        super(context, R.layout.lista_pedidos);
        for (Pedido p : datos) {
            add(p);
        }
        items = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inf = LayoutInflater.from(getContext());
        v = inf.inflate(lista_pedidos, null);

        Pedido pedido = getItem(position);
        TextView razonsocial = (TextView) v.findViewById(R.id.nombreclientepedidolista);
        razonsocial.setText(pedido.getRazonSocial());
        TextView fecha = (TextView) v.findViewById(R.id.fechapedidolista);
        fecha.setText(pedido.getFecha());
        TextView direccion = (TextView) v.findViewById(R.id.direccionpedidolista);
        direccion.setText(pedido.getDireccion());
        return v;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    results.values = items;
                    results.count = items.size();

                } else {
                    ArrayList<ProductoStock> resultadosfiltrados = new ArrayList<>();
                    for (Pedido p : items) {
                     /*   if (ps.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultadosfiltrados.add(ps);

                        }*/
                    }
                    results.values = resultadosfiltrados;
                    results.count = resultadosfiltrados.size();

                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                for (Pedido p : (List<Pedido>)results.values) {
                    add(p);
                }
                notifyDataSetChanged();
            }
        };
    }

}

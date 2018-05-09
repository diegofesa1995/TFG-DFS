package com.example.diego.myapplication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diego.myapplication.Objects.ProductoStock;
import com.example.diego.myapplication.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.example.diego.myapplication.R.layout.design_bottom_navigation_item;
import static com.example.diego.myapplication.R.layout.lista_productos_stock;


/**
 * Created by diego on 13/03/2018.
 */

public class AdapterStock extends ArrayAdapter<ProductoStock> implements Filterable {

    protected ArrayList<ProductoStock> items;

    public AdapterStock(@NonNull Context context, @NonNull ArrayList<ProductoStock> datos) {
        super(context, R.layout.lista_productos_stock);
        for (ProductoStock p : datos) {
            add(p);
        }
        items = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        LayoutInflater inf = LayoutInflater.from(getContext());
        v = inf.inflate(lista_productos_stock, null);

        ProductoStock ps = getItem(position);

        TextView nombre = (TextView) v.findViewById(R.id.nombreproductolista);
        nombre.setText(ps.getNombre());
        TextView cantidad = (TextView) v.findViewById(R.id.cantidadproductolista);
        cantidad.setText(String.valueOf(ps.getCantidadTotal()));

        TextView precio = (TextView) v.findViewById(R.id.precioporunidadlista);
        try{
            precio.setText(String.valueOf(ps.getLotes().get(ps.getLotes().keySet().toArray()[ps.getLotes().size()-1]).getPrecioVenta()));
        }catch(Exception e){

        }

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
                    for (ProductoStock ps : items) {
                        if (ps.getNombre().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultadosfiltrados.add(ps);

                        }
                    }
                    results.values = resultadosfiltrados;
                    results.count = resultadosfiltrados.size();

                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                for (ProductoStock p : (List<ProductoStock>) results.values) {
                    add(p);
                }
                notifyDataSetChanged();
            }
        };
    }

}

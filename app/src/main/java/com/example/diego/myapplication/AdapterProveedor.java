package com.example.diego.myapplication;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.example.diego.myapplication.Objects.ProductoStock;
import com.example.diego.myapplication.Objects.Proveedor;
import com.example.diego.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.diego.myapplication.R.layout.lista_productos_stock;
import static com.example.diego.myapplication.R.layout.lista_proveedores;

/**
 * Created by diego on 14/03/2018.
 */

public class AdapterProveedor extends ArrayAdapter<Proveedor> implements Filterable {

    protected ArrayList<Proveedor> items;

    public AdapterProveedor(@NonNull Context context, @NonNull ArrayList<Proveedor> datos) {
        super(context, R.layout.lista_proveedores);
        for (Proveedor p : datos) {
            add(p);
        }
        items = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inf = LayoutInflater.from(getContext());
        v = inf.inflate(lista_proveedores, null);

        Proveedor p = getItem(position);
        TextView razonsocial = (TextView) v.findViewById(R.id.razonsocialproveedorlista);
        razonsocial.setText(p.getRazon_Social());
        TextView nombre = (TextView) v.findViewById(R.id.nombreproveedorlista);
        nombre.setText(p.getNombre());
        TextView telefono = (TextView) v.findViewById(R.id.telefonoproveedorlista);
        telefono.setText(String.valueOf(p.getTelefono()));
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
                    ArrayList<Proveedor> resultadosfiltrados = new ArrayList<>();
                    for (Proveedor ps : items) {
                        if (ps.getRazon_Social().toLowerCase().contains(constraint.toString().toLowerCase())) {
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
                for (Proveedor p : (List<Proveedor>)results.values) {
                    add(p);
                }
                notifyDataSetChanged();
            }
        };
    }

}

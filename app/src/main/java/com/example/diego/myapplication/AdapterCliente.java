package com.example.diego.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.diego.myapplication.Objects.Cliente;
import com.example.diego.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diego on 20/03/2018.
 */

public class AdapterCliente extends ArrayAdapter<Cliente> implements Filterable {

    public ArrayList<Cliente> items;
    private View v;
    private TextView razonsocial;
    private TextView nombre;
    private TextView telefono;

    public AdapterCliente(@NonNull Context context, @NonNull ArrayList<Cliente> datos) {
        super(context, R.layout.lista_clientes);
        for (Cliente p : datos) {
        add(p);
    }

    items = datos;
}

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inf = LayoutInflater.from(getContext());
        v = inf.inflate(R.layout.lista_clientes, null);
        Cliente ps = getItem(position);
        razonsocial = (TextView) v.findViewById(R.id.razonsociallista);
        nombre = (TextView) v.findViewById(R.id.nombreclientelista);
        telefono = (TextView) v.findViewById(R.id.telefonoclientelista);

        if (ps.getFormalidad().equals("Mala")) {
            int aux = Color.RED;
            telefono.setText(ps.getTelefono());
            razonsocial.setText(ps.getRazon_Social());
            nombre.setText(ps.getNombre());
            telefono.setTextColor(aux);
            razonsocial.setTextColor(aux);
            nombre.setTextColor(aux);
        } else {

            telefono.setText(ps.getTelefono());
            razonsocial.setText(ps.getRazon_Social());
            nombre.setText(ps.getNombre());

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
                    ArrayList<Cliente> resultadosfiltrados = new ArrayList<>();
                    for (Cliente c : items) {
                        if (c.getRazon_Social().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultadosfiltrados.add(c);

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

                for (Cliente c : (List<Cliente>) results.values) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        };
    }

}

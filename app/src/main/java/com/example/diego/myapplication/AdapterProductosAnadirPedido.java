package com.example.diego.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diego.myapplication.Objects.ProductoStock;
import com.example.diego.myapplication.Objects.ProductoVendido;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import static com.example.diego.myapplication.R.layout.item_lista_productos_anadirpedido;

public class AdapterProductosAnadirPedido  extends ArrayAdapter<ProductoVendido> implements Filterable{

    protected ArrayList<ProductoVendido> items;
    private View v;


    public AdapterProductosAnadirPedido(@NonNull Context context, @NonNull ArrayList<ProductoVendido> datos) {
        super(context, R.layout.item_lista_productos_anadirpedido);
        for (ProductoVendido p : datos) {
            add(p);
        }
        items = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inf = LayoutInflater.from(getContext());
        v = inf.inflate(item_lista_productos_anadirpedido, null);

        ProductoVendido pv = getItem(position);

        TextView nombre = (TextView) v.findViewById(R.id.nombreproductolistanadirpedido);
        nombre.setText(pv.getNombre());
        TextView cantidad = (TextView) v.findViewById(R.id.cantidadproductolistanadirpedido);
        cantidad.setText(String.valueOf(pv.getCantidad()));

        TextView preciounidad = (TextView) v.findViewById(R.id.precioporunidadconivalistanadirpedido);
        preciounidad.setText(String.valueOf(pv.getPreciounidad()));

        TextView preciototal = (TextView) v.findViewById(R.id.preciototalconivalistanadirpedido);
        preciototal.setText(String.valueOf(pv.getPreciototal()));

        return v;
    }
}

package com.example.diego.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diego.myapplication.Activities.activity_albaranes_proveedor;
import com.example.diego.myapplication.Objects.Proveedor;
import com.google.firebase.storage.FileDownloadTask;

import java.util.ArrayList;
import java.util.List;

import static com.example.diego.myapplication.R.layout.lista_proveedores;

public class AdapterAlbaranes extends ArrayAdapter<Bitmap> {
    protected List<Bitmap> items;

    public AdapterAlbaranes(@NonNull Context context, @NonNull List<Bitmap> datos) {
        super(context, R.layout.lista_proveedores);
        items = datos;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inf = LayoutInflater.from(getContext());
        v = inf.inflate(R.layout.items_listview_albaranesproveedor, null);
        Bitmap bitmapalbaran = (Bitmap) getItem(position);
        ImageView viewimageview = (ImageView) v.findViewById(R.id.imageviewalbaran);

        viewimageview.setImageBitmap(bitmapalbaran);

        return v;
    }
}


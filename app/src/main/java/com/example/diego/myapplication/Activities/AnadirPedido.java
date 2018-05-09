package com.example.diego.myapplication.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diego.myapplication.AdapterProductosAnadirPedido;
import com.example.diego.myapplication.Objects.Cliente;
import com.example.diego.myapplication.Objects.Lote;
import com.example.diego.myapplication.Objects.Pedido;
import com.example.diego.myapplication.Objects.ProductoStock;
import com.example.diego.myapplication.Objects.ProductoVendido;
import com.example.diego.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * Created by diego on 15/03/2018.
 */

public class AnadirPedido extends Fragment {

    private View rootView;
    private DatabaseReference dbclientes;
    private DatabaseReference dbpedidos;
    private DatabaseReference dbstock;
    private ArrayList<String> listaclientes = new ArrayList<>();
    private AutoCompleteTextView autocompletecliente;
    private EditText edittextfecha;
    private Calendar calendar;
    private Button botonanadirproducto;
    private ListView listaproductosanadirpedido;
    private AdapterProductosAnadirPedido adapterproductosvendidos;
    private AlertDialog dialoganadirproducto;
    private EditText cantidadunidadedittext;
    private EditText subcantidadedittext;
    private EditText pesovariableedittext;
    private EditText precioventaedittext;
    private Double baseimponible4=0.0;
    private Double baseimponible10=0.0;
    private Double baseimponible21=0.0;
    private Double importeiva4=0.0;
    private Double importeiva10=0.0;
    private Double importeiva21=0.0;
    private Double totalfacturapedido=0.0;


    private Button aceptarproducto;
    private Button cancelarproducto;
    AutoCompleteTextView autocompleteproducto;
    private ImageButton btnclear;
    private ArrayList<String> listaproductosstring = new ArrayList<>();
    private ArrayList<ProductoVendido> listaproductosvendidos = new ArrayList<>();
    private ProductoStock productoseleccionado;
    private String claveproductoseleccionado;
    private ArrayList<String> listalotesstring = new ArrayList<>();
    private Lote loteseleccionado;
    private String claveloteseleccionado;
    private String nuevaclavepedido;
    private FirebaseAuth Authlogin = FirebaseAuth.getInstance();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_anadir_pedido, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbaranadirpedido);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        dbclientes = FirebaseDatabase.getInstance().getReference("Clientes");
        dbpedidos = FirebaseDatabase.getInstance().getReference("Pedidos");
        dbclientes.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                almacenarclientes(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listaproductosanadirpedido = (ListView) rootView.findViewById(R.id.listviewproductosanadirpedido);
        adapterproductosvendidos = new AdapterProductosAnadirPedido(getActivity(), listaproductosvendidos);
        listaproductosanadirpedido.setAdapter(adapterproductosvendidos);

        listaproductosanadirpedido.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ProductoVendido productovendidolongclick = (ProductoVendido) parent.getItemAtPosition(position);
                AlertDialog.Builder buildereliminar =
                        new AlertDialog.Builder(getActivity());

                buildereliminar.setMessage("¿Eliminar  " + productovendidolongclick.getNombre() + " de la lista del pedido?")
                        .setTitle("Confirmacion")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (productovendidolongclick.getUnidadPeso().equalsIgnoreCase("cajas/uds")) {
                                    dbstock.child(productovendidolongclick.getClaveproducto()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            dbstock.child(productovendidolongclick.getClaveproducto()).child("cantidadTotal").setValue(Double.parseDouble(dataSnapshot.child("cantidadTotal").getValue().toString()) + productovendidolongclick.getCantidad());
                                            dbstock.child(productovendidolongclick.getClaveproducto()).child("lotes").child(productovendidolongclick.getLote().substring(0, productovendidolongclick.getLote().indexOf(":"))).child("cantidad").setValue(Double.parseDouble(dataSnapshot.child("lotes").child(productovendidolongclick.getLote().substring(0, productovendidolongclick.getLote().indexOf(":"))).child("cantidad").getValue().toString()) + productovendidolongclick.getCantidadcajas());
                                            dbstock.child(productovendidolongclick.getClaveproducto()).child("lotes").child(productovendidolongclick.getLote().substring(0, productovendidolongclick.getLote().indexOf(":"))).child("cantidadTotal").setValue(Double.parseDouble(dataSnapshot.child("lotes").child(productovendidolongclick.getLote().substring(0, productovendidolongclick.getLote().indexOf(":"))).child("cantidadTotal").getValue().toString()) + productovendidolongclick.getCantidad());
                                            adapterproductosvendidos.remove(productovendidolongclick);
                                            listaproductosvendidos.remove(productovendidolongclick);
                                            adapterproductosvendidos.notifyDataSetChanged();

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                } else {
                                    dbstock.child(productovendidolongclick.getClaveproducto()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            dbstock.child(productovendidolongclick.getClaveproducto()).child("cantidadTotal").setValue(Double.parseDouble(dataSnapshot.child("cantidadTotal").getValue().toString()) + productovendidolongclick.getCantidad());
                                            dbstock.child(productovendidolongclick.getClaveproducto()).child("lotes").child(productovendidolongclick.getLote().substring(0, productovendidolongclick.getLote().indexOf(":"))).child("cantidad").setValue(Double.parseDouble(dataSnapshot.child("lotes").child(productovendidolongclick.getLote().substring(0, productovendidolongclick.getLote().indexOf(":"))).child("cantidad").getValue().toString()) + productovendidolongclick.getCantidad());
                                            dbstock.child(productovendidolongclick.getClaveproducto()).child("lotes").child(productovendidolongclick.getLote().substring(0, productovendidolongclick.getLote().indexOf(":"))).child("cantidadTotal").setValue(Double.parseDouble(dataSnapshot.child("lotes").child(productovendidolongclick.getLote().substring(0, productovendidolongclick.getLote().indexOf(":"))).child("cantidadTotal").getValue().toString()) + productovendidolongclick.getCantidad());
                                            adapterproductosvendidos.remove(productovendidolongclick);
                                            listaproductosvendidos.remove(productovendidolongclick);
                                            adapterproductosvendidos.notifyDataSetChanged();

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                Toast.makeText(getActivity(), "Producto borrado", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).show();
                return true;
            }
        });

        final ArrayAdapter<String> adapterclientes = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listaclientes);
        adapterclientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autocompletecliente = (AutoCompleteTextView) rootView.findViewById(R.id.autocompleteclienteanadirpedido);
        autocompletecliente.setThreshold(1);
        autocompletecliente.setAdapter(adapterclientes);

        autocompletecliente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        edittextfecha = (EditText) rootView.findViewById(R.id.fechaanadirpedido);

        edittextfecha.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mdatepicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        edittextfecha.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, year, month, day);
                mdatepicker.show();
            }
        });


        botonanadirproducto = (Button) rootView.findViewById(R.id.botonanadirproductoanadirpedido);
        botonanadirproducto.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View vista = getActivity().getLayoutInflater().inflate(R.layout.pop_anadirproducto_anadirpedido, null);
                builder.setView(vista);
                dialoganadirproducto = builder.create();
                dialoganadirproducto.show();

                dbstock = FirebaseDatabase.getInstance().getReference("Stock");
                dbstock.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        almacenarproductosstock(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                final Spinner spinnerloteproducto = (Spinner) vista.findViewById(R.id.spinnerloteproductoanadirpedido);
                cantidadunidadedittext = (EditText) vista.findViewById(R.id.cantidadunidadproductoanadirpedido);
                subcantidadedittext = (EditText) vista.findViewById(R.id.cantidadsubunidadproductoanadirpedido);
                pesovariableedittext = (EditText) vista.findViewById(R.id.pesovariableproductoanadirpedido);
                precioventaedittext = (EditText) vista.findViewById(R.id.precioventaproductoanadirpedido);

                btnclear = (ImageButton) vista.findViewById(R.id.btnclear);
                btnclear.setVisibility(View.GONE);
                aceptarproducto = (Button) vista.findViewById(R.id.botonaceptarproductonuevonuevopedido);
                cancelarproducto = (Button) vista.findViewById(R.id.botoncancelarproductonuevonuevoepedido);


                final ArrayAdapter<String> adapterproductos = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listaproductosstring);
                adapterproductos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                autocompleteproducto = (AutoCompleteTextView) vista.findViewById(R.id.autocompleteproductoanadirpedido);
                autocompleteproducto.setThreshold(1);
                autocompleteproducto.setAdapter(adapterproductos);

                btnclear.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        btnclear.setVisibility(View.GONE);
                        autocompleteproducto.setText("");
                    }
                });


                autocompleteproducto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        btnclear.setVisibility(View.VISIBLE);
                        final String stringproductoseleccionado = String.valueOf(parent.getItemAtPosition(position));
                        listalotesstring.clear();


                        dbstock.child(stringproductoseleccionado.substring(0, stringproductoseleccionado.indexOf("-")).replace(" ", "")).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                productoseleccionado = dataSnapshot.getValue(ProductoStock.class);
                                if (productoseleccionado.getCantidadTotal() == 0.0 || productoseleccionado.getCantidadTotal() == 0) {
                                    Toast.makeText(getActivity(), "Producto sin existencias", Toast.LENGTH_SHORT).show();
                                } else {
                                    claveproductoseleccionado = dataSnapshot.getKey();
                                    subcantidadedittext.setEnabled(false);

                                    for (int i = 0; i < productoseleccionado.getLotes().size(); i++) {
                                        listalotesstring.add("L" + (i + 1) + ": " + productoseleccionado.getLotes().get("L" + (i + 1)).getProveedor().substring(0, productoseleccionado.getLotes().get("L" + (i + 1)).getProveedor().indexOf("-")) + "-" + productoseleccionado.getLotes().get("L" + (i + 1)).getFecha());
                                    }

                                    ArrayAdapter<String> adaptadorlotes = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listalotesstring);
                                    adaptadorlotes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spinnerloteproducto.setAdapter(adaptadorlotes);
                                    spinnerloteproducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            claveloteseleccionado = String.valueOf(parent.getItemAtPosition(position)).substring(0, String.valueOf(parent.getItemAtPosition(position)).indexOf(":"));

                                            dbstock.child(stringproductoseleccionado.substring(0, stringproductoseleccionado.indexOf("-")).replace(" ", "")).child("lotes").child(claveloteseleccionado).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    loteseleccionado = dataSnapshot.getValue(Lote.class);
                                                    precioventaedittext.setText(String.valueOf(loteseleccionado.getPrecioVenta()));


                                                    if (productoseleccionado.getUnidadPeso().equalsIgnoreCase("Cajas/uds")) {
                                                        cantidadunidadedittext.setVisibility(View.VISIBLE);
                                                        cantidadunidadedittext.setText("");
                                                        cantidadunidadedittext.setHint(String.valueOf(loteseleccionado.getCantidad()) + " cajas");
                                                        subcantidadedittext.setVisibility(View.VISIBLE);
                                                        subcantidadedittext.setText("");
                                                        precioventaedittext.setVisibility(View.VISIBLE);
                                                        pesovariableedittext.setVisibility(View.GONE);
                                                        subcantidadedittext.setHint(loteseleccionado.getUnidadesenCaja() + " " + loteseleccionado.getSubUnidadPeso() + "/caja");

                                                    } else {
                                                        if (loteseleccionado.getPesoUnidad().equals(0.0) && loteseleccionado.getPesoSubUnidad().equals(0.0) && loteseleccionado.getSubUnidadPeso().equalsIgnoreCase("") && loteseleccionado.getUnidadesenCaja().equals(0)) {
                                                            subcantidadedittext.setVisibility(View.GONE);
                                                            pesovariableedittext.setVisibility(View.GONE);
                                                            precioventaedittext.setVisibility(View.VISIBLE);
                                                            cantidadunidadedittext.setVisibility(View.VISIBLE);
                                                            cantidadunidadedittext.setText("");
                                                            cantidadunidadedittext.setHint(String.valueOf(loteseleccionado.getCantidadTotal()) + " kilos");

                                                        } else if (loteseleccionado.getPesoUnidad().equals(0.0) && loteseleccionado.getPesoSubUnidad().equals(0.0) && !loteseleccionado.getSubUnidadPeso().equalsIgnoreCase("") && !loteseleccionado.getUnidadesenCaja().equals(0)) {
                                                            cantidadunidadedittext.setHint("Cajas");
                                                            cantidadunidadedittext.setText("");
                                                            cantidadunidadedittext.setVisibility(View.VISIBLE);
                                                            precioventaedittext.setVisibility(View.VISIBLE);
                                                            subcantidadedittext.setVisibility(View.VISIBLE);
                                                            subcantidadedittext.setText("");
                                                            subcantidadedittext.setHint(loteseleccionado.getUnidadesenCaja() + " ud/caja");
                                                            pesovariableedittext.setVisibility(View.VISIBLE);
                                                            pesovariableedittext.setHint("MAX: " + String.valueOf(loteseleccionado.getCantidadTotal()) + " kilos");
                                                            pesovariableedittext.setEnabled(true);


                                                        } else if (!loteseleccionado.getPesoUnidad().equals(0.0) && !loteseleccionado.getPesoSubUnidad().equals(0.0) && !loteseleccionado.getSubUnidadPeso().equalsIgnoreCase("") && !loteseleccionado.getUnidadesenCaja().equals(0)) {
                                                            cantidadunidadedittext.setHint(String.valueOf(Math.floor(loteseleccionado.getCantidadTotal() / loteseleccionado.getPesoUnidad())) + " cajas");
                                                            cantidadunidadedittext.setText("");
                                                            cantidadunidadedittext.setVisibility(View.VISIBLE);
                                                            precioventaedittext.setVisibility(View.VISIBLE);
                                                            subcantidadedittext.setVisibility(View.VISIBLE);
                                                            subcantidadedittext.setText("");
                                                            subcantidadedittext.setHint(loteseleccionado.getUnidadesenCaja() + " " + loteseleccionado.getSubUnidadPeso() + "/caja");
                                                            subcantidadedittext.setEnabled(false);
                                                            pesovariableedittext.setText("");
                                                            pesovariableedittext.setHint(loteseleccionado.getPesoSubUnidad() + " kilos");
                                                            pesovariableedittext.setVisibility(View.VISIBLE);
                                                            pesovariableedittext.setEnabled(false);

                                                        } else {
                                                            cantidadunidadedittext.setHint(String.valueOf(Math.floor(loteseleccionado.getCantidadTotal() / loteseleccionado.getPesoUnidad())) + " cajas");
                                                            cantidadunidadedittext.setText("");
                                                            cantidadunidadedittext.setVisibility(View.VISIBLE);
                                                            precioventaedittext.setVisibility(View.VISIBLE);
                                                            subcantidadedittext.setVisibility(View.GONE);
                                                            pesovariableedittext.setText("");
                                                            pesovariableedittext.setHint(loteseleccionado.getPesoUnidad() + " kilos");
                                                            pesovariableedittext.setVisibility(View.VISIBLE);
                                                            pesovariableedittext.setEnabled(false);

                                                        }

                                                    }

                                                    cantidadunidadedittext.addTextChangedListener(new TextWatcher() {
                                                        @Override
                                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                        }

                                                        @Override
                                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                            subcantidadedittext.setText("");
                                                            pesovariableedittext.setText("");
                                                            if (productoseleccionado.getUnidadPeso().equalsIgnoreCase("Cajas/uds")) {
                                                                if (s.toString().equalsIgnoreCase("")) {
                                                                    subcantidadedittext.setEnabled(false);
                                                                    cantidadunidadedittext.setHint(String.valueOf(loteseleccionado.getCantidad()) + " cajas");
                                                                    subcantidadedittext.setText("");
                                                                    subcantidadedittext.setHint(loteseleccionado.getUnidadesenCaja() + " " + loteseleccionado.getSubUnidadPeso() + "/caja");
                                                                } else if (Integer.parseInt(s.toString()) == (0)) {
                                                                    subcantidadedittext.setEnabled(true);
                                                                    subcantidadedittext.setHint(Math.floor(loteseleccionado.getCantidadTotal()) + " " + loteseleccionado.getSubUnidadPeso());
                                                                } else {
                                                                    try {
                                                                        if (Integer.parseInt(s.toString()) > loteseleccionado.getCantidad()) {
                                                                            cantidadunidadedittext.setTextColor(Color.RED);
                                                                            subcantidadedittext.setTextColor(Color.RED);
                                                                            subcantidadedittext.setText(String.valueOf(Integer.parseInt(s.toString()) * loteseleccionado.getUnidadesenCaja()));
                                                                        } else {
                                                                            subcantidadedittext.setTextColor(Color.BLACK);
                                                                            cantidadunidadedittext.setTextColor(Color.BLACK);
                                                                            subcantidadedittext.setText(String.valueOf(Integer.parseInt(s.toString()) * loteseleccionado.getUnidadesenCaja()));
                                                                        }


                                                                    } catch (Exception e) {
                                                                        Toast.makeText(getActivity(), "Error: Cantidad", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            } else {
                                                                // ------------------------------------------------------  AL CONTADO / SIN UNIDADES EN CAJA PESO VARIABLE-----------------------------------------------
                                                                if (loteseleccionado.getPesoUnidad().equals(0.0) && loteseleccionado.getPesoSubUnidad().equals(0.0) && loteseleccionado.getSubUnidadPeso().equalsIgnoreCase("") && loteseleccionado.getUnidadesenCaja().equals(0)) {
                                                                    if (s.toString().equalsIgnoreCase("")) {
                                                                        cantidadunidadedittext.setHint(String.valueOf(loteseleccionado.getCantidadTotal()) + " kilos");
                                                                    } else {
                                                                        try {
                                                                            if (Double.parseDouble(s.toString()) > loteseleccionado.getCantidadTotal()) {
                                                                                cantidadunidadedittext.setTextColor(Color.RED);
                                                                            } else {
                                                                                cantidadunidadedittext.setTextColor(Color.BLACK);

                                                                            }
                                                                        } catch (Exception e) {
                                                                            Toast.makeText(getActivity(), "Error: Cantidad", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                    //----------------------------------------------------- CON UNIDADES EN CAJA PESO VARIABLEEE -----------------------------------------------------
                                                                } else if (loteseleccionado.getPesoUnidad().equals(0.0) && loteseleccionado.getPesoSubUnidad().equals(0.0) && !loteseleccionado.getSubUnidadPeso().equalsIgnoreCase("") && !loteseleccionado.getUnidadesenCaja().equals(0)) {
                                                                    if (s.toString().equalsIgnoreCase("")) {
                                                                        cantidadunidadedittext.setHint("cajas");
                                                                        subcantidadedittext.setEnabled(false);
                                                                        subcantidadedittext.setHint(loteseleccionado.getUnidadesenCaja() + " " + loteseleccionado.getSubUnidadPeso() + "/caja");
                                                                    } else if (s.toString().equalsIgnoreCase("0")) {
                                                                        subcantidadedittext.setEnabled(true);
                                                                    } else {
                                                                        try {
                                                                            subcantidadedittext.setText(String.valueOf(Integer.parseInt(s.toString()) * loteseleccionado.getUnidadesenCaja()));

                                                                        } catch (Exception e) {
                                                                            Toast.makeText(getActivity(), "Error: Cantidad de cajas", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                    //----------------------------------------------------- CON UNIDADES EN CAJA PESO FIJOOO -----------------------------------------------------
                                                                } else if (!loteseleccionado.getPesoUnidad().equals(0.0) && !loteseleccionado.getPesoSubUnidad().equals(0.0) && !loteseleccionado.getSubUnidadPeso().equalsIgnoreCase("") && !loteseleccionado.getUnidadesenCaja().equals(0)) {
                                                                    if (s.toString().equalsIgnoreCase("")) {
                                                                        cantidadunidadedittext.setHint(String.valueOf(Math.floor(loteseleccionado.getCantidadTotal() / loteseleccionado.getPesoUnidad())) + " cajas");
                                                                        subcantidadedittext.setHint(loteseleccionado.getUnidadesenCaja() + " " + loteseleccionado.getSubUnidadPeso() + "/caja");
                                                                        pesovariableedittext.setHint(loteseleccionado.getPesoSubUnidad() + " kilos");
                                                                        subcantidadedittext.setEnabled(false);
                                                                        subcantidadedittext.setText("");
                                                                    } else if (s.toString().equalsIgnoreCase("0")) {
                                                                        subcantidadedittext.setEnabled(true);
                                                                        subcantidadedittext.setText("");
                                                                        subcantidadedittext.setHint(String.valueOf(Math.floor(((loteseleccionado.getCantidadTotal() / loteseleccionado.getPesoUnidad()) * loteseleccionado.getUnidadesenCaja()))) + " " + loteseleccionado.getSubUnidadPeso());
                                                                    } else {
                                                                        if (Integer.parseInt(s.toString()) > Math.floor(loteseleccionado.getCantidadTotal() / loteseleccionado.getPesoUnidad())) {
                                                                            cantidadunidadedittext.setTextColor(Color.RED);
                                                                            pesovariableedittext.setTextColor(Color.RED);
                                                                            subcantidadedittext.setEnabled(false);
                                                                            subcantidadedittext.setText(String.valueOf(Integer.parseInt(s.toString()) * loteseleccionado.getUnidadesenCaja()));
                                                                            pesovariableedittext.setText(String.valueOf(Integer.parseInt(s.toString()) * loteseleccionado.getPesoUnidad()));
                                                                        } else {
                                                                            try {
                                                                                cantidadunidadedittext.setTextColor(Color.BLACK);
                                                                                pesovariableedittext.setTextColor(Color.BLACK);
                                                                                subcantidadedittext.setEnabled(false);
                                                                                subcantidadedittext.setText(String.valueOf(Integer.parseInt(s.toString()) * loteseleccionado.getUnidadesenCaja()));
                                                                                pesovariableedittext.setText(String.valueOf(Integer.parseInt(s.toString()) * loteseleccionado.getPesoUnidad()));
                                                                            } catch (Exception e) {
                                                                                Toast.makeText(getActivity(), "Error: Cantidad de cajas", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    }
                                                                    //----------------------------------------------------- SIN UNIDADES EN CAJA PESO FIJOO -----------------------------------------------------
                                                                } else {

                                                                    if (s.toString().equalsIgnoreCase("")) {
                                                                        cantidadunidadedittext.setHint(String.valueOf(Math.floor(loteseleccionado.getCantidadTotal() / loteseleccionado.getPesoUnidad())) + " cajas");
                                                                        pesovariableedittext.setHint(loteseleccionado.getPesoUnidad() + " kilos");
                                                                    } else if (s.toString().equalsIgnoreCase("0")) {
                                                                        cantidadunidadedittext.setHint(String.valueOf(Math.floor(loteseleccionado.getCantidadTotal() / loteseleccionado.getPesoUnidad())) + " cajas");
                                                                        cantidadunidadedittext.setText("");
                                                                        pesovariableedittext.setHint(loteseleccionado.getPesoUnidad() + " kilos");
                                                                        Toast.makeText(getActivity(), "No puede ser 0", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        if (Integer.parseInt(s.toString()) > Math.floor(loteseleccionado.getCantidadTotal() / loteseleccionado.getPesoUnidad())) {
                                                                            subcantidadedittext.setTextColor(Color.RED);
                                                                            pesovariableedittext.setTextColor(Color.RED);
                                                                            pesovariableedittext.setText(String.valueOf(Integer.parseInt(s.toString()) * loteseleccionado.getPesoUnidad()));
                                                                        } else {
                                                                            subcantidadedittext.setTextColor(Color.BLACK);
                                                                            pesovariableedittext.setTextColor(Color.BLACK);
                                                                            pesovariableedittext.setText(String.valueOf(Integer.parseInt(s.toString()) * loteseleccionado.getPesoUnidad()));
                                                                        }

                                                                    }

                                                                }

                                                            }
                                                        }


                                                        @Override
                                                        public void afterTextChanged(Editable s) {

                                                        }
                                                    });

                                                    subcantidadedittext.addTextChangedListener(new TextWatcher() {
                                                        @Override
                                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                        }

                                                        @Override
                                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                            if (productoseleccionado.getUnidadPeso().equalsIgnoreCase("Cajas/uds")) {
                                                                if (s.toString().equalsIgnoreCase("")) {
                                                                    if (cantidadunidadedittext.getText().toString().equalsIgnoreCase("0")) {
                                                                        subcantidadedittext.setHint(Math.floor(loteseleccionado.getCantidadTotal()) + " " + loteseleccionado.getSubUnidadPeso());
                                                                    } else {
                                                                        subcantidadedittext.setHint(loteseleccionado.getUnidadesenCaja() + " " + loteseleccionado.getSubUnidadPeso() + "/caja");
                                                                    }

                                                                } else {
                                                                    try {
                                                                        if (Integer.parseInt(s.toString()) > loteseleccionado.getCantidadTotal()) {
                                                                            subcantidadedittext.setTextColor(Color.RED);

                                                                        } else {
                                                                            subcantidadedittext.setTextColor(Color.BLACK);
                                                                        }
                                                                    } catch (Exception e) {
                                                                        Toast.makeText(getActivity(), "Error: Subcantidad", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            } else {

                                                                if (loteseleccionado.getPesoUnidad().equals(0.0) && loteseleccionado.getPesoSubUnidad().equals(0.0) && !loteseleccionado.getSubUnidadPeso().equalsIgnoreCase("") && !loteseleccionado.getUnidadesenCaja().equals(0)) {

                                                                } else if (!loteseleccionado.getPesoUnidad().equals(0.0) && !loteseleccionado.getPesoSubUnidad().equals(0.0) && !loteseleccionado.getSubUnidadPeso().equalsIgnoreCase("") && !loteseleccionado.getUnidadesenCaja().equals(0)) {
                                                                    if (s.toString().equalsIgnoreCase("")) {
                                                                        subcantidadedittext.setHint(String.valueOf(Math.floor(((loteseleccionado.getCantidadTotal() / loteseleccionado.getPesoUnidad()) * loteseleccionado.getUnidadesenCaja()))) + " " + loteseleccionado.getSubUnidadPeso());
                                                                        pesovariableedittext.setText("");
                                                                        pesovariableedittext.setHint(loteseleccionado.getPesoSubUnidad() + " kilos");
                                                                    } else {
                                                                        try {
                                                                            if (Integer.parseInt(s.toString()) > Math.floor(((loteseleccionado.getCantidadTotal() / loteseleccionado.getPesoUnidad()) * loteseleccionado.getUnidadesenCaja()))) {
                                                                                subcantidadedittext.setTextColor(Color.RED);
                                                                                pesovariableedittext.setTextColor(Color.RED);
                                                                                pesovariableedittext.setText(String.valueOf(Integer.parseInt(s.toString()) * loteseleccionado.getPesoSubUnidad()));
                                                                            } else {
                                                                                subcantidadedittext.setTextColor(Color.BLACK);
                                                                                pesovariableedittext.setTextColor(Color.BLACK);
                                                                                pesovariableedittext.setText(String.valueOf(Integer.parseInt(s.toString()) * loteseleccionado.getPesoSubUnidad()));
                                                                            }

                                                                        } catch (Exception e) {

                                                                        }


                                                                    }

                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void afterTextChanged(Editable s) {

                                                        }
                                                    });

                                                    precioventaedittext.addTextChangedListener(new TextWatcher() {
                                                        @Override
                                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                        }

                                                        @Override
                                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                            if (s.toString().equalsIgnoreCase("")) {
                                                                precioventaedittext.setHint(String.valueOf(loteseleccionado.getPrecioVenta()));
                                                            } else {
                                                                try {
                                                                    if (Double.parseDouble(s.toString()) < loteseleccionado.getPrecioVenta()) {
                                                                        precioventaedittext.setTextColor(Color.RED);
                                                                    } else if (Double.parseDouble(s.toString()) > loteseleccionado.getPrecioVenta()) {
                                                                        precioventaedittext.setTextColor(Color.GREEN);
                                                                    } else {
                                                                        precioventaedittext.setTextColor(Color.BLACK);
                                                                    }
                                                                } catch (Exception e) {
                                                                    Toast.makeText(getActivity(), "Error: Precio Venta", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                        }

                                                        @Override
                                                        public void afterTextChanged(Editable s) {

                                                        }
                                                    });

                                                    pesovariableedittext.addTextChangedListener(new TextWatcher() {
                                                        @Override
                                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                        }

                                                        @Override
                                                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                            if (s.toString().equalsIgnoreCase("")) {
                                                                pesovariableedittext.setHint("MAX: " + String.valueOf(loteseleccionado.getCantidadTotal()) + " kilos");
                                                            } else {
                                                                if (Double.parseDouble(s.toString()) > loteseleccionado.getCantidadTotal()) {
                                                                    pesovariableedittext.setTextColor(Color.RED);
                                                                } else {
                                                                    pesovariableedittext.setTextColor(Color.BLACK);
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void afterTextChanged(Editable s) {

                                                        }
                                                    });

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });
                                }
                            }

                                @Override
                                public void onCancelled (DatabaseError databaseError){

                                }

                        });


                    }
                });


                aceptarproducto.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!listaproductosstring.contains(autocompleteproducto.getText().toString())) {
                            Toast.makeText(getActivity(), "Error: Producto no registrado", Toast.LENGTH_SHORT).show();
                        } else {
                            if (productoseleccionado.getUnidadPeso().equalsIgnoreCase("Cajas/uds")) {
                                Integer nuevacantidadcajas;
                                Integer nuevaunidades;
                                Integer nuevacantidadtotalproducto;
                                Integer cantidadcajasproducto;
                                if (cantidadunidadedittext.getText().toString().equalsIgnoreCase("") || subcantidadedittext.getText().toString().equalsIgnoreCase("") || precioventaedittext.getText().toString().equalsIgnoreCase("")) {
                                    Toast.makeText(getActivity(), "Error: Campo vacío", Toast.LENGTH_SHORT).show();
                                } else {

                                    if (cantidadunidadedittext.getText().toString().equalsIgnoreCase("0") && Integer.parseInt(subcantidadedittext.getText().toString()) >= loteseleccionado.getUnidadesenCaja()) {
                                        cantidadcajasproducto = Integer.parseInt(subcantidadedittext.getText().toString()) / loteseleccionado.getUnidadesenCaja();
                                        nuevacantidadcajas = (int) (Math.floor(loteseleccionado.getCantidad())) - (Integer.parseInt(subcantidadedittext.getText().toString()) / loteseleccionado.getUnidadesenCaja());
                                        nuevaunidades = (int) (Math.floor(loteseleccionado.getCantidadTotal())) - (Integer.parseInt(subcantidadedittext.getText().toString()) % loteseleccionado.getUnidadesenCaja());
                                        nuevacantidadtotalproducto = (int) (Math.floor(productoseleccionado.getCantidadTotal()) - Math.floor(loteseleccionado.getCantidadTotal())) + nuevaunidades;
                                    } else if (!cantidadunidadedittext.getText().toString().equalsIgnoreCase("0")) {
                                        cantidadcajasproducto = Integer.parseInt(cantidadunidadedittext.getText().toString());
                                        nuevacantidadcajas = (int) (Math.floor(loteseleccionado.getCantidad())) - (Integer.parseInt(cantidadunidadedittext.getText().toString()));
                                        nuevaunidades = (int) (Math.floor(loteseleccionado.getCantidadTotal())) - (Integer.parseInt(subcantidadedittext.getText().toString()));
                                        nuevacantidadtotalproducto = (int) (Math.floor(productoseleccionado.getCantidadTotal()) - Math.floor(loteseleccionado.getCantidadTotal())) + nuevaunidades;
                                    } else {
                                        cantidadcajasproducto = 1;
                                        nuevacantidadcajas = (int) (Math.floor(loteseleccionado.getCantidad())) - 1;
                                        nuevaunidades = (int) (Math.floor(loteseleccionado.getCantidadTotal())) - (Integer.parseInt(subcantidadedittext.getText().toString()));
                                        nuevacantidadtotalproducto = (int) (Math.floor(productoseleccionado.getCantidadTotal()) - Math.floor(loteseleccionado.getCantidadTotal())) + nuevaunidades;
                                    }

                                    ProductoVendido productovendido = new ProductoVendido(claveproductoseleccionado, productoseleccionado.getNombre(), Math.floor(Double.parseDouble(subcantidadedittext.getText().toString()) * 100) / 100, cantidadcajasproducto, productoseleccionado.getIVA(), claveloteseleccionado + ": " + loteseleccionado.getFecha() + "-" + loteseleccionado.getProveedor().substring(0, loteseleccionado.getProveedor().indexOf("-")), Math.floor(Double.parseDouble(precioventaedittext.getText().toString()) * 100) / 100, Math.floor(Integer.parseInt(subcantidadedittext.getText().toString()) * Double.parseDouble(precioventaedittext.getText().toString()) * 100) / 100, productoseleccionado.getUnidadPeso(), loteseleccionado.getSubUnidadPeso(), "Producto vendido en cajas de " + loteseleccionado.getUnidadesenCaja() + loteseleccionado.getSubUnidadPeso());

                                    Double aux = 0.0;
                                    if(productoseleccionado.getIVA()==4){
                                        if(baseimponible4==0.0){
                                            baseimponible4 = productovendido.getPreciototal();
                                        }else{
                                            aux = baseimponible4;
                                            baseimponible4 = Math.floor((baseimponible4 + productovendido.getPreciototal())*1000)/1000;
                                        }

                                        if(importeiva4!=0.0){
                                            aux += importeiva4;
                                        }
                                        importeiva4 = Math.floor(((baseimponible4*4)/100)*1000)/1000;


                                        if(totalfacturapedido==0.0){
                                            totalfacturapedido = Math.floor((baseimponible4 + importeiva4)*1000)/1000;
                                        }else{
                                            totalfacturapedido = Math.floor((totalfacturapedido - aux + baseimponible4 + importeiva4) *1000)/1000;
                                        }

                                    } else if(productoseleccionado.getIVA()==10){
                                        if(baseimponible10==0.0){
                                            baseimponible10 = productovendido.getPreciototal();
                                        }else{
                                            aux = baseimponible10;
                                            baseimponible10 = Math.floor((baseimponible10 + productovendido.getPreciototal())*1000)/1000;
                                        }

                                        if(importeiva10!=0.0){
                                            aux += importeiva10;
                                        }
                                        importeiva10 = Math.floor(((baseimponible10*10)/100)*1000)/1000;


                                        if(totalfacturapedido==0.0){
                                            totalfacturapedido = Math.floor((baseimponible10 + importeiva10)*1000)/1000;
                                        }else{
                                            totalfacturapedido = Math.floor((totalfacturapedido - aux + baseimponible10 + importeiva10) *1000)/1000;
                                        }

                                    }else if(productoseleccionado.getIVA()==21){
                                        if(baseimponible21==0.0){
                                            baseimponible21 = productovendido.getPreciototal();
                                        }else{
                                            aux = baseimponible21;
                                            baseimponible21 = Math.floor((baseimponible21 + productovendido.getPreciototal())*1000)/1000;
                                        }

                                        if(importeiva21!=0.0){
                                            aux += importeiva21;
                                        }
                                        importeiva21 = Math.floor(((baseimponible21*21)/100)*1000)/1000;


                                        if(totalfacturapedido==0.0){
                                            totalfacturapedido = Math.floor((baseimponible21 + importeiva21)*1000)/1000;
                                        }else{
                                            totalfacturapedido = Math.floor((totalfacturapedido - aux + baseimponible21 + importeiva21) *1000)/1000;
                                        }

                                    }

                                    dbstock.child(claveproductoseleccionado).child("cantidadTotal").setValue(Math.floor(nuevacantidadtotalproducto * 100) / 100);
                                    dbstock.child(claveproductoseleccionado).child("lotes").child(claveloteseleccionado).child("cantidad").setValue(Math.floor(nuevacantidadcajas * 100) / 100);
                                    dbstock.child(claveproductoseleccionado).child("lotes").child(claveloteseleccionado).child("cantidadTotal").setValue(Math.floor(nuevaunidades * 100) / 100);


                                    listaproductosvendidos.add(productovendido);
                                    adapterproductosvendidos.add(productovendido);
                                    adapterproductosvendidos.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "Producto Añadido", Toast.LENGTH_SHORT).show();
                                    dialoganadirproducto.dismiss();
                                }

                            } else {
                                ProductoVendido productovendido=null;
                                Double nuevacantidadlote;
                                Double nuevacantidadproducto;
                                //--------------------------------------------------------------- AL CONTADO / SIN UNIDADES EN CAJA PESO VARIABLEEE ----------------------------------------------------------------------------------
                                if (loteseleccionado.getPesoUnidad().equals(0.0) && loteseleccionado.getPesoSubUnidad().equals(0.0) && loteseleccionado.getSubUnidadPeso().equalsIgnoreCase("") && loteseleccionado.getUnidadesenCaja().equals(0)) {
                                    if (cantidadunidadedittext.getText().toString().equalsIgnoreCase("") || precioventaedittext.getText().toString().equalsIgnoreCase("")) {
                                        Toast.makeText(getActivity(), "Error: Campo vacío", Toast.LENGTH_SHORT).show();
                                    } else {
                                        nuevacantidadlote = loteseleccionado.getCantidad() - (Double.parseDouble(cantidadunidadedittext.getText().toString()));
                                        nuevacantidadproducto = productoseleccionado.getCantidadTotal() - (Double.parseDouble(cantidadunidadedittext.getText().toString()));

                                        dbstock.child(claveproductoseleccionado).child("cantidadTotal").setValue(Math.floor(nuevacantidadproducto * 100) / 100);
                                        dbstock.child(claveproductoseleccionado).child("lotes").child(claveloteseleccionado).child("cantidad").setValue(Math.floor(nuevacantidadlote * 100) / 100);
                                        dbstock.child(claveproductoseleccionado).child("lotes").child(claveloteseleccionado).child("cantidadTotal").setValue(Math.floor(nuevacantidadlote * 100) / 100);

                                        productovendido = new ProductoVendido(claveproductoseleccionado, productoseleccionado.getNombre(), Math.floor(Double.parseDouble(cantidadunidadedittext.getText().toString()) * 100) / 100, 0, productoseleccionado.getIVA(), claveloteseleccionado + ": " + loteseleccionado.getFecha() + "-" + loteseleccionado.getProveedor().substring(0, loteseleccionado.getProveedor().indexOf("-")), Math.floor(Double.parseDouble(precioventaedittext.getText().toString()) * 100) / 100, Math.floor(Double.parseDouble(cantidadunidadedittext.getText().toString()) * Double.parseDouble(precioventaedittext.getText().toString()) * 100) / 100, productoseleccionado.getUnidadPeso(), "", " Producto vendido en kilos");
                                        listaproductosvendidos.add(productovendido);
                                        adapterproductosvendidos.add(productovendido);
                                        adapterproductosvendidos.notifyDataSetChanged();

                                        Toast.makeText(getActivity(), "Producto Añadido", Toast.LENGTH_SHORT).show();
                                        dialoganadirproducto.dismiss();
                                    }
                                    //----------------------------------------------------- CON UNIDADES EN CAJA PESO VARIABLEEE -----------------------------------------------------
                                } else if (loteseleccionado.getPesoUnidad().equals(0.0) && loteseleccionado.getPesoSubUnidad().equals(0.0) && !loteseleccionado.getSubUnidadPeso().equalsIgnoreCase("") && !loteseleccionado.getUnidadesenCaja().equals(0)) {
                                    if (pesovariableedittext.getText().toString().equalsIgnoreCase("")) {
                                        Toast.makeText(getActivity(), "Error: Campo vacío", Toast.LENGTH_SHORT).show();
                                    } else {
                                        nuevacantidadlote = loteseleccionado.getCantidad() - (Double.parseDouble(pesovariableedittext.getText().toString()));
                                        nuevacantidadproducto = productoseleccionado.getCantidadTotal() - (Double.parseDouble(pesovariableedittext.getText().toString()));

                                        dbstock.child(claveproductoseleccionado).child("cantidadTotal").setValue(Math.floor(nuevacantidadproducto * 100) / 100);
                                        dbstock.child(claveproductoseleccionado).child("lotes").child(claveloteseleccionado).child("cantidad").setValue(Math.floor(nuevacantidadlote * 100) / 100);
                                        dbstock.child(claveproductoseleccionado).child("lotes").child(claveloteseleccionado).child("cantidadTotal").setValue(Math.floor(nuevacantidadlote * 100) / 100);
                                        productovendido = new ProductoVendido(claveproductoseleccionado, productoseleccionado.getNombre(), Math.floor(Double.parseDouble(pesovariableedittext.getText().toString()) * 100) / 100, 0, productoseleccionado.getIVA(), claveloteseleccionado + ": " + loteseleccionado.getFecha() + "-" + loteseleccionado.getProveedor().substring(0, loteseleccionado.getProveedor().indexOf("-")), Math.floor(Double.parseDouble(precioventaedittext.getText().toString()) * 100) / 100, Math.floor(Double.parseDouble(pesovariableedittext.getText().toString()) * Double.parseDouble(precioventaedittext.getText().toString()) * 100) / 100, productoseleccionado.getUnidadPeso(), loteseleccionado.getSubUnidadPeso(), "Producto vendido en cajas de " + loteseleccionado.getUnidadesenCaja() + loteseleccionado.getSubUnidadPeso() + " al peso");
                                        listaproductosvendidos.add(productovendido);
                                        adapterproductosvendidos.add(productovendido);
                                        adapterproductosvendidos.notifyDataSetChanged();

                                        Toast.makeText(getActivity(), "Producto Añadido", Toast.LENGTH_SHORT).show();
                                        dialoganadirproducto.dismiss();
                                    }

                                    //----------------------------------------------------- CON UNIDADES EN CAJA PESO FIJOOO -----------------------------------------------------
                                } else if (!loteseleccionado.getPesoUnidad().equals(0.0) && !loteseleccionado.getPesoSubUnidad().equals(0.0) && !loteseleccionado.getSubUnidadPeso().equalsIgnoreCase("") && !loteseleccionado.getUnidadesenCaja().equals(0)) {
                                    if (cantidadunidadedittext.getText().toString().equalsIgnoreCase("") || subcantidadedittext.getText().toString().equalsIgnoreCase("")) {
                                        Toast.makeText(getActivity(), "Error: Campo vacío", Toast.LENGTH_SHORT).show();
                                    } else {
                                        nuevacantidadlote = loteseleccionado.getCantidad() - (Double.parseDouble(pesovariableedittext.getText().toString()));
                                        nuevacantidadproducto = productoseleccionado.getCantidadTotal() - (Double.parseDouble(pesovariableedittext.getText().toString()));

                                        dbstock.child(claveproductoseleccionado).child("cantidadTotal").setValue(Math.floor(nuevacantidadproducto * 100) / 100);
                                        dbstock.child(claveproductoseleccionado).child("lotes").child(claveloteseleccionado).child("cantidad").setValue(Math.floor(nuevacantidadlote * 100) / 100);
                                        dbstock.child(claveproductoseleccionado).child("lotes").child(claveloteseleccionado).child("cantidadTotal").setValue(Math.floor(nuevacantidadlote * 100) / 100);
                                        productovendido = new ProductoVendido(claveproductoseleccionado, productoseleccionado.getNombre(), Math.floor(Double.parseDouble(pesovariableedittext.getText().toString()) * 100) / 100, 0, productoseleccionado.getIVA(), claveloteseleccionado + ": " + loteseleccionado.getFecha() + "-" + loteseleccionado.getProveedor().substring(0, loteseleccionado.getProveedor().indexOf("-")), Math.floor(Double.parseDouble(precioventaedittext.getText().toString()) * 100) / 100, Math.floor(Double.parseDouble(pesovariableedittext.getText().toString()) * Double.parseDouble(precioventaedittext.getText().toString()) * 100) / 100, productoseleccionado.getUnidadPeso(), loteseleccionado.getSubUnidadPeso(), "Producto vendido en cajas de " + loteseleccionado.getUnidadesenCaja() + loteseleccionado.getSubUnidadPeso() + " con un peso de " + loteseleccionado.getPesoUnidad());
                                        listaproductosvendidos.add(productovendido);
                                        adapterproductosvendidos.add(productovendido);
                                        adapterproductosvendidos.notifyDataSetChanged();

                                        Toast.makeText(getActivity(), "Producto Añadido", Toast.LENGTH_SHORT).show();
                                        dialoganadirproducto.dismiss();
                                    }
                                    //----------------------------------------------------- SIN UNIDADES EN CAJA PESO FIJOO -----------------------------------------------------
                                } else {
                                    if (cantidadunidadedittext.getText().toString().equalsIgnoreCase("")) {
                                        Toast.makeText(getActivity(), "Error: Campo vacío", Toast.LENGTH_SHORT).show();
                                    } else {
                                        nuevacantidadlote = loteseleccionado.getCantidad() - (Double.parseDouble(pesovariableedittext.getText().toString()));
                                        nuevacantidadproducto = productoseleccionado.getCantidadTotal() - (Double.parseDouble(pesovariableedittext.getText().toString()));

                                        dbstock.child(claveproductoseleccionado).child("cantidadTotal").setValue(Math.floor(nuevacantidadproducto * 100) / 100);
                                        dbstock.child(claveproductoseleccionado).child("lotes").child(claveloteseleccionado).child("cantidad").setValue(Math.floor(nuevacantidadlote * 100) / 100);
                                        dbstock.child(claveproductoseleccionado).child("lotes").child(claveloteseleccionado).child("cantidadTotal").setValue(Math.floor(nuevacantidadlote * 100) / 100);

                                        productovendido = new ProductoVendido(claveproductoseleccionado, productoseleccionado.getNombre(), Math.floor(Double.parseDouble(pesovariableedittext.getText().toString()) * 100) / 100, 0, productoseleccionado.getIVA(), claveloteseleccionado + ": " + loteseleccionado.getFecha() + "-" + loteseleccionado.getProveedor().substring(0, loteseleccionado.getProveedor().indexOf("-")), Math.floor(Double.parseDouble(precioventaedittext.getText().toString()) * 100) / 100, Math.floor(Double.parseDouble(pesovariableedittext.getText().toString()) * Double.parseDouble(precioventaedittext.getText().toString()) * 100) / 100, productoseleccionado.getUnidadPeso(), loteseleccionado.getSubUnidadPeso(), "Producto vendido en cajas con un peso fijo de " + loteseleccionado.getPesoUnidad());
                                        listaproductosvendidos.add(productovendido);
                                        adapterproductosvendidos.add(productovendido);
                                        adapterproductosvendidos.notifyDataSetChanged();

                                        Toast.makeText(getActivity(), "Producto Añadido", Toast.LENGTH_SHORT).show();
                                        dialoganadirproducto.dismiss();
                                    }


                                }

                                Double aux = 0.0;
                                if(productoseleccionado.getIVA()==4){
                                    if(baseimponible4==0.0){
                                        baseimponible4 = productovendido.getPreciototal();
                                    }else{
                                        aux = baseimponible4;
                                        baseimponible4 = Math.floor((baseimponible4 + productovendido.getPreciototal())*1000)/1000;
                                    }

                                    if(importeiva4!=0.0){
                                        aux += importeiva4;
                                    }
                                    importeiva4 = Math.floor(((baseimponible4*4)/100)*1000)/1000;


                                    if(totalfacturapedido==0.0){
                                        totalfacturapedido = Math.floor((baseimponible4 + importeiva4)*1000)/1000;
                                    }else{
                                        totalfacturapedido = Math.floor((totalfacturapedido - aux + baseimponible4 + importeiva4) *1000)/1000;
                                    }

                                } else if(productoseleccionado.getIVA()==10){
                                    if(baseimponible10==0.0){
                                        baseimponible10 = productovendido.getPreciototal();
                                    }else{
                                        aux = baseimponible10;
                                        baseimponible10 = Math.floor((baseimponible10 + productovendido.getPreciototal())*1000)/1000;
                                    }

                                    if(importeiva10!=0.0){
                                        aux += importeiva10;
                                    }
                                    importeiva10 = Math.floor(((baseimponible10*10)/100)*1000)/1000;


                                    if(totalfacturapedido==0.0){
                                        totalfacturapedido = Math.floor((baseimponible10 + importeiva10)*1000)/1000;
                                    }else{
                                        totalfacturapedido = Math.floor((totalfacturapedido - aux + baseimponible10 + importeiva10) *1000)/1000;
                                    }

                                }else if(productoseleccionado.getIVA()==21){
                                    if(baseimponible21==0.0){
                                        baseimponible21 = productovendido.getPreciototal();
                                    }else{
                                        aux = baseimponible21;
                                        baseimponible21 = Math.floor((baseimponible21 + productovendido.getPreciototal())*1000)/1000;
                                    }

                                    if(importeiva21!=0.0){
                                        aux += importeiva21;
                                    }
                                    importeiva21 = Math.floor(((baseimponible21*21)/100)*1000)/1000;


                                    if(totalfacturapedido==0.0){
                                        totalfacturapedido = Math.floor((baseimponible21 + importeiva21)*1000)/1000;
                                    }else{
                                        totalfacturapedido = Math.floor((totalfacturapedido - aux + baseimponible21 + importeiva21) *1000)/1000;
                                    }

                                }


                            }
                        }
                    }
                });

                cancelarproducto.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialoganadirproducto.dismiss();
                    }
                });


            }
        });

        Button botonaceptarpedido = (Button) rootView.findViewById(R.id.botonaceptarpedidonuevo);

        botonaceptarpedido.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(autocompletecliente.getText().toString().equalsIgnoreCase("") || edittextfecha.getText().toString().equalsIgnoreCase("") || listaproductosvendidos.size()==0){
                    Toast.makeText(getActivity(), "Error: Campo Vacío", Toast.LENGTH_SHORT).show();
                }else if(!listaclientes.contains(autocompletecliente.getText().toString())){
                    Toast.makeText(getActivity(), "Error: Cliente no registrado", Toast.LENGTH_SHORT).show();
                }else{
                    dbpedidos.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            nuevaclavepedido = nuevaKeyPedido(dataSnapshot1);
                            dbclientes.child(autocompletecliente.getText().toString().substring(0,autocompletecliente.getText().toString().indexOf("-")).replace(" ", "")).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot2) {
                                    HashMap<String, ProductoVendido> productos = new HashMap<>();
                                    HashMap<String, Double> datosbaseimponible4 = new HashMap<>();
                                    HashMap<String, Double> datosbaseimponible10 = new HashMap<>();
                                    HashMap<String, Double> datosbaseimponible21 = new HashMap<>();
                                    HashMap<String, HashMap<String, Double>> baseImponible = new HashMap<>();
                                    int i = 0;
                                    for(ProductoVendido pv : listaproductosvendidos){
                                        productos.put("PV"+i++, pv);
                                    }
                                    datosbaseimponible4.put("baseimponible", baseimponible4);
                                    datosbaseimponible4.put("importeiva", importeiva4);
                                    baseImponible.put("4",datosbaseimponible4);
                                    datosbaseimponible10.put("baseimponible", baseimponible10);
                                    datosbaseimponible10.put("importeiva", importeiva10);
                                    baseImponible.put("10",datosbaseimponible10);
                                    datosbaseimponible21.put("baseimponible", baseimponible21);
                                    datosbaseimponible21.put("importeiva", importeiva21);
                                    baseImponible.put("21",datosbaseimponible21);


                                    Pedido nuevopedido = new Pedido(autocompletecliente.getText().toString().substring(0,autocompletecliente.getText().toString().indexOf("-")).replace(" ", ""), autocompletecliente.getText().toString().substring(autocompletecliente.getText().toString().indexOf("-")+2), dataSnapshot2.child("direccion").getValue().toString(), "Pendiente", edittextfecha.getText().toString(), Authlogin.getCurrentUser().getEmail(), productos, baseImponible, totalfacturapedido);

                                    try {
                                        dbpedidos.child(nuevaclavepedido).setValue(nuevopedido);
                                        Toast.makeText(getActivity(), "Pedido Creado", Toast.LENGTH_SHORT).show();
                                        listaproductosvendidos.clear();
                                        adapterproductosvendidos.clear();
                                        autocompletecliente.setText("");
                                        adapterproductosvendidos.notifyDataSetChanged();
                                        edittextfecha.setText("");
                                    }catch (Exception e ){

                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
        return rootView;
    }

    private void almacenarclientes(DataSnapshot dataSnapshot) {
        listaclientes.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (!listaclientes.contains(ds.getKey() + " - " + ds.getValue(Cliente.class).getRazon_Social())) {
                listaclientes.add(ds.getKey() + " - " + ds.getValue(Cliente.class).getRazon_Social());
            }
        }
    }

    private void almacenarproductosstock(DataSnapshot dataSnapshot) {
        listaproductosstring.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (!listaproductosstring.contains(ds.getKey() + " - " + ds.getValue(ProductoStock.class).getNombre())) {
                listaproductosstring.add(ds.getKey() + " - " + ds.getValue(ProductoStock.class).getNombre());
            }
        }

    }

    private String nuevaKeyPedido(DataSnapshot dataSnapshot) {
        Integer maximo = 0;
        Integer key = 0;
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            key = Integer.parseInt(ds.getKey().substring(2));
            if (key > maximo) {
                maximo = key;
            }
        }
        maximo++;
        return "PE" + maximo;

    }
}

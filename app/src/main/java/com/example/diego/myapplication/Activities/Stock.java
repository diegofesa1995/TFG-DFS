package com.example.diego.myapplication.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.support.v7.widget.SearchView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.diego.myapplication.AdapterStock;
import com.example.diego.myapplication.Objects.Lote;
import com.example.diego.myapplication.Objects.ProductoStock;
import com.example.diego.myapplication.Objects.Proveedor;
import com.example.diego.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Fragment;

public class Stock extends Fragment implements SearchView.OnQueryTextListener {

    private DatabaseReference dbstock;
    private DatabaseReference dbproveedores;
    private DatabaseReference dbproveedoresedit;
    private FloatingActionButton anadirarticulostock;

    private EditText nombrenuevoproducto;
    private Spinner spinnerunidadpesonuevoproduto;
    private LinearLayout layoutcheckboxesnuevoproducto;
    private CheckBox checkBoxpesoalcontadonuevoproducto;
    private CheckBox checkBoxpesoencajasnuevoproducto;
    private EditText cantidadtotalnuevoproducto;
    private LinearLayout layoutunidadesysubunidadcajanuevoproducto;
    private EditText unidadesencajanuevoproducto;
    private EditText subunidadpesonuevoproducto;
    private LinearLayout layoutcheckboxespesonuevoproducto;
    private CheckBox checkBoxpesofijonuevoproducto;
    private CheckBox checkBoxpesovariablenuevoproducto;
    private EditText pesounidadencajanuevoproducto;
    private AutoCompleteTextView proveedornuevoproducto;
    private EditText margenbeneficionuevoproducto;
    private EditText preciocompranuevoproducto;
    private Spinner spinnerivanuevoproducto;
    private Button botonaceptarnuevoproducto;
    private Button botoncancelarnuevoproducto;

    private Double pesoSubUnidaddouble = 0.0;
    private Integer unidadesencajainteger = 0;
    private Double cantidadunidaddouble = 0.0;
    private Double margendouble = 0.0;
    private Double preciocompradouble = 0.0;
    private Double pesounidadencajadouble=0.0;
    private Double precioVentadouble = 0.0;
    private Double subcantidaddouble = 0.0;
    private Lote nuevolote;
    private ProductoStock productoeditado;
    private Button botoncancelarnuevoproduto;
    private Button botonaceptarnuevoproduto;
    private String ivanuevoproducto;
    private String unidadpesonuevoproducto;
    private String unidadpesonuevolote;
    private String lotenuevoproducto;
    private String Keynuevoproducto;
    private ArrayAdapter<String> adaptadorunidadpeso;

    private Lote loteaeditar;
    private Double nuevacantidadeditlote;

    private AlertDialog dialog;
    private AlertDialog dialog2;
    private final String[] ivas = new String[]{"IVA", "4", "10", "21"};
    private final String[] unidadespeso = new String[]{"Elegir Ud. peso", "Cajas/uds", "Al peso"};
    private static List<String> proveedores = new ArrayList<>();
    private static ArrayList<ProductoStock> arrayproductos = new ArrayList<>();
    private AdapterStock adapterstock;
    private ListView listaproductos;
    private View rootView;
    private ProductoStock productoseleccionado;
    private String claveproductoedit = "";
    private String claveloteedit = "";
    private Integer ivaeditinteger;
    private Double subcantidadlotedouble = 0.0;
    private Double subcantincrementadadouble;
    private String[] arraylotesmostrar;

    DecimalFormat decimales = new DecimalFormat("0.000");


    private HashMap<String, Lote> mapa = new HashMap<>();

    private ArrayAdapter<String> adapterproveedores;

    private AlertDialog dialogbotones;

    private String loteeditseleccionado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_stock, container, false);

//---------------------------TOOLBAR-------------------------------------------------------------------
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.appbaralmacen);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Cargando datos..");
        progress.setCanceledOnTouchOutside(false);
        progress.show();


//------------------------------------LISTVIEW DEL STOCK DE LA BASE DE DATOS--------------------------------------------------------------------------------


        dbstock = FirebaseDatabase.getInstance().getReference("Stock");
        dbstock.keepSynced(true);

        listaproductos = (ListView) rootView.findViewById(R.id.listviewstock);


        dbstock.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    almacenarproductosstock(dataSnapshot);
                    adapterstock = new AdapterStock(getActivity(), arrayproductos);
                    adapterstock.notifyDataSetChanged();
                    listaproductos.setAdapter(adapterstock);
                    progress.dismiss();
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//------------------------------------------------------------------ LONG CLICK ITEM LISTVIEW ----------------------------------------------------------------------------
        listaproductos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                productoseleccionado = (ProductoStock) listaproductos.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View vista = getActivity().getLayoutInflater().inflate(R.layout.editar_eliminar_anadir_stock, null);
                builder.setView(vista);
                dialogbotones = builder.create();
                dialogbotones.show();


                dbstock.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            claveproductoedit = claveproductoedit(dataSnapshot, productoseleccionado.getNombre());

                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                Button btnanadir = (Button) vista.findViewById(R.id.btnanadirlote);
                Button btneditar = (Button) vista.findViewById(R.id.btneditarproducto);
                Button btneliminar = (Button) vista.findViewById(R.id.btndeleteproducto);

//----------------------------------------------------------------- AÑADIR LOTEEEEE -----------------------------------------------------------------------------------
                btnanadir.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder buildercase0 = new AlertDialog.Builder(getActivity());
                        final View viewcase0 = getActivity().getLayoutInflater().inflate(R.layout.pop_anadir_lote, null);
                        buildercase0.setView(viewcase0);
                        dialog = buildercase0.create();
                        dialog.show();

                        dbproveedoresedit = FirebaseDatabase.getInstance().getReference("Proveedores");
                        dbproveedoresedit.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    almacenarproveedores(dataSnapshot);
                                } catch (Exception e) {
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        final TextView textviewnombrelote = (TextView) viewcase0.findViewById(R.id.nombreproductoanadirlote);
                        final TextView edittextunidadpesolote = (TextView) viewcase0.findViewById(R.id.unidadpesoanadirlote);
                        final LinearLayout layoutcheckboxesanadirlote = (LinearLayout) viewcase0.findViewById(R.id.layoutcheckboxesanadirlote);
                        final CheckBox checkBoxpesoalcontadoanadirlote = (CheckBox) viewcase0.findViewById(R.id.checkboxpesoalcontadoanadirlote);
                        final CheckBox checkBoxpesoencajasanadirlote = (CheckBox) viewcase0.findViewById(R.id.checkboxpesoencajasanadirlote);
                        final EditText subunidadpesoanadirlote = (EditText) viewcase0.findViewById(R.id.subunidadpesoanadirlote);
                        final EditText cantidadtotalanadirlote = (EditText) viewcase0.findViewById(R.id.cantidadtotalanadirlote);
                        final LinearLayout layoutunidadesysubunidadcajaanadirlote = (LinearLayout) viewcase0.findViewById(R.id.layoutunidadesysubunidadcajaanadirlote);
                        final EditText unidadesencajaanadirlote = (EditText) viewcase0.findViewById(R.id.unidadesencajaanadirlote);
                        final LinearLayout layoutcheckboxespesoanadirlote = (LinearLayout) viewcase0.findViewById(R.id.layoutcheckboxespesoanadirlote);
                        final CheckBox checkBoxpesofijoanadirlote = (CheckBox) viewcase0.findViewById(R.id.checkboxpesofijocajasanadirlote);
                        final CheckBox checkBoxpesovariableanadirlote = (CheckBox) viewcase0.findViewById(R.id.checkboxpesovariablecajasanadirlote);
                        final EditText pesounidadencajaanadirlote = (EditText) viewcase0.findViewById(R.id.pesosubunidadesanadirlote);
                        final AutoCompleteTextView proveedoranadirlote = (AutoCompleteTextView) viewcase0.findViewById(R.id.proveedoranadirlote);
                        final EditText margenbeneficioanadirlote = (EditText) viewcase0.findViewById(R.id.margenbeneficioanadirlote);
                        final EditText preciocompraanadirlote = (EditText) viewcase0.findViewById(R.id.preciocompraanadirlote);
                        final Button botonaceptaranadirlote = (Button) viewcase0.findViewById(R.id.botonaceptaranadirlote);
                        final Button botoncancelaranadirlote = (Button) viewcase0.findViewById(R.id.botoncancelaranadirlote);
                        textviewnombrelote.setText(productoseleccionado.getNombre());
                        edittextunidadpesolote.setText(productoseleccionado.getUnidadPeso());

                        if (productoseleccionado.getUnidadPeso().equalsIgnoreCase("Cajas/uds")) {
                            cantidadtotalanadirlote.setVisibility(View.VISIBLE);
                            cantidadtotalanadirlote.setHint("Cantidad de cajas");
                            layoutcheckboxesanadirlote.setVisibility(View.GONE);
                            layoutunidadesysubunidadcajaanadirlote.setVisibility(View.VISIBLE);
                            layoutcheckboxespesoanadirlote.setVisibility(View.GONE);
                            pesounidadencajaanadirlote.setVisibility(View.GONE);
                            preciocompraanadirlote.setHint("Precio de compra por unidad");
                            checkBoxpesoalcontadoanadirlote.setChecked(false);
                            checkBoxpesoencajasanadirlote.setChecked(false);
                            checkBoxpesofijoanadirlote.setChecked(false);
                            checkBoxpesovariableanadirlote.setChecked(false);
                        } else if (productoseleccionado.getUnidadPeso().equalsIgnoreCase("Al peso")) {
                            cantidadtotalanadirlote.setHint("Cantidad de kilos");
                            cantidadtotalanadirlote.setVisibility(View.VISIBLE);
                            layoutcheckboxesanadirlote.setVisibility(View.VISIBLE);
                            layoutunidadesysubunidadcajaanadirlote.setVisibility(View.GONE);
                            layoutcheckboxespesoanadirlote.setVisibility(View.GONE);
                            pesounidadencajaanadirlote.setVisibility(View.GONE);
                            preciocompraanadirlote.setHint("Precio de compra por kilo");
                            checkBoxpesoalcontadoanadirlote.setChecked(false);
                            checkBoxpesoencajasanadirlote.setChecked(false);
                            checkBoxpesofijoanadirlote.setChecked(false);
                            checkBoxpesovariableanadirlote.setChecked(false);

                        }

                        adapterproveedores = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, proveedores);
                        adapterproveedores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        proveedoranadirlote.setThreshold(1);
                        proveedoranadirlote.setAdapter(adapterproveedores);

                        checkBoxpesoalcontadoanadirlote.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    checkBoxpesoalcontadoanadirlote.setClickable(false);
                                    checkBoxpesoencajasanadirlote.setClickable(true);
                                    checkBoxpesoencajasanadirlote.setChecked(false);
                                    checkBoxpesofijoanadirlote.setChecked(false);
                                    checkBoxpesovariableanadirlote.setChecked(false);
                                    layoutunidadesysubunidadcajaanadirlote.setVisibility(View.GONE);
                                    layoutcheckboxespesoanadirlote.setVisibility(View.GONE);
                                    pesounidadencajaanadirlote.setVisibility(View.GONE);

                                }
                            }
                        });

                        checkBoxpesoencajasanadirlote.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    checkBoxpesoalcontadoanadirlote.setClickable(true);
                                    checkBoxpesoencajasanadirlote.setClickable(false);
                                    checkBoxpesoalcontadoanadirlote.setChecked(false);
                                    layoutunidadesysubunidadcajaanadirlote.setVisibility(View.VISIBLE);
                                    layoutcheckboxespesoanadirlote.setVisibility(View.VISIBLE);
                                    pesounidadencajaanadirlote.setHint("Peso fijo de caja");
                                    checkBoxpesofijoanadirlote.setClickable(true);
                                    checkBoxpesovariableanadirlote.setClickable(true);

                                }
                            }
                        });

                        checkBoxpesofijoanadirlote.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    checkBoxpesofijoanadirlote.setClickable(false);
                                    checkBoxpesovariableanadirlote.setClickable(true);
                                    checkBoxpesovariableanadirlote.setChecked(false);
                                    pesounidadencajaanadirlote.setVisibility(View.VISIBLE);
                                }

                            }
                        });

                        checkBoxpesovariableanadirlote.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    checkBoxpesofijoanadirlote.setClickable(true);
                                    checkBoxpesovariableanadirlote.setClickable(false);
                                    checkBoxpesofijoanadirlote.setChecked(false);
                                    pesounidadencajaanadirlote.setVisibility(View.GONE);
                                }

                            }
                        });

                        unidadesencajaanadirlote.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                try {
                                    if (!s.toString().equalsIgnoreCase("") && Integer.parseInt(s.toString()) != 0) {
                                        subunidadpesoanadirlote.setEnabled(true);
                                        pesounidadencajaanadirlote.setHint("Peso fijo unidad en caja");
                                    } else {
                                        pesounidadencajaanadirlote.setHint("Peso fijo de caja");
                                        subunidadpesoanadirlote.setEnabled(false);
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Error: numero incorrecto", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });


                        botonaceptaranadirlote.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Boolean error = false;
                                SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
                                Date datenuevo = new Date();
                                final String fechaanadirlote = dateFormat.format(datenuevo);
                                if (proveedoranadirlote.getText().toString().equalsIgnoreCase("") || cantidadtotalanadirlote.getText().toString().equalsIgnoreCase("")
                                        || cantidadtotalanadirlote.getText().toString().equalsIgnoreCase("0") || cantidadtotalanadirlote.getText().toString().equalsIgnoreCase("0.0")
                                        || preciocompraanadirlote.getText().toString().equalsIgnoreCase("") || margenbeneficioanadirlote.getText().toString().equalsIgnoreCase("") || margenbeneficioanadirlote.getText().toString().equalsIgnoreCase("0")
                                        || margenbeneficioanadirlote.getText().toString().equalsIgnoreCase("0.0")) {
                                    Toast.makeText(getActivity(), "Error: Campo Vacio o nulo", Toast.LENGTH_SHORT).show();
                                } else if (!proveedores.contains(proveedoranadirlote.getText().toString())) {
                                    Toast.makeText(getActivity(), "Error: proveedor no registrado", Toast.LENGTH_SHORT).show();
                                    error = true;

                                } else {
                                    try {
                                        cantidadunidaddouble = Math.floor(Double.parseDouble(cantidadtotalanadirlote.getText().toString()) * 10000) / 10000;

                                        try {
                                            preciocompradouble = Math.floor(Double.parseDouble(preciocompraanadirlote.getText().toString()) * 10000) / 10000;

                                            try {
                                                margendouble = Math.floor(Double.parseDouble(margenbeneficioanadirlote.getText().toString()) * 10000) / 10000;

                                            } catch (Exception e) {
                                                Toast.makeText(getActivity(), "Error: Margen erróneo", Toast.LENGTH_SHORT).show();
                                                error = true;
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(getActivity(), "Error: Precio erróneo", Toast.LENGTH_SHORT).show();
                                            error = true;
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), "Error: Cantidad errónea", Toast.LENGTH_SHORT).show();
                                        error = true;
                                    }
                                    if (productoseleccionado.getUnidadPeso().equalsIgnoreCase("Cajas/uds")) {
                                        if (unidadesencajaanadirlote.getText().toString().equalsIgnoreCase("") || subunidadpesoanadirlote.getText().toString().equalsIgnoreCase("")) {
                                            Toast.makeText(getActivity(), "Error: campo vacío", Toast.LENGTH_SHORT).show();
                                            error = true;
                                        } else {
                                            try {
                                                unidadesencajainteger = Integer.parseInt(unidadesencajaanadirlote.getText().toString());
                                                nuevolote = new Lote(cantidadunidaddouble, unidadesencajainteger, subunidadpesoanadirlote.getText().toString(), cantidadunidaddouble * unidadesencajainteger, fechaanadirlote, proveedoranadirlote.getText().toString(), margendouble, preciocompradouble, Math.floor((((margendouble * preciocompradouble) / 100) + preciocompradouble) * 10000) / 10000, 0.0, 0.0);
                                            } catch (Exception e) {
                                                Toast.makeText(getActivity(), "Error: Unidades en caja errónea", Toast.LENGTH_SHORT).show();
                                                error = true;
                                            }

                                        }
                                    } else if (productoseleccionado.getUnidadPeso().equalsIgnoreCase("Al peso")) {
                                        if (checkBoxpesoalcontadoanadirlote.isChecked()) {
                                            nuevolote = new Lote(cantidadunidaddouble, 0, "", cantidadunidaddouble, fechaanadirlote, proveedoranadirlote.getText().toString(), margendouble, preciocompradouble, Math.floor((((margendouble * preciocompradouble) / 100) + preciocompradouble) * 10000) / 10000, 0.0, 0.0);
                                        } else if (checkBoxpesoencajasanadirlote.isChecked()) {

                                            if (unidadesencajaanadirlote.getText().toString().equalsIgnoreCase("") || unidadesencajaanadirlote.getText().toString().equalsIgnoreCase("0") || unidadesencajaanadirlote.getText().toString().equalsIgnoreCase("0.0")) {
                                                if (checkBoxpesofijoanadirlote.isChecked()) {
                                                    if (pesounidadencajaanadirlote.getText().toString().equalsIgnoreCase("") || pesounidadencajaanadirlote.getText().toString().equalsIgnoreCase("0") || pesounidadencajaanadirlote.getText().toString().equalsIgnoreCase("0.0")) {
                                                        Toast.makeText(getActivity(), "Error: peso vacío o nulo", Toast.LENGTH_SHORT).show();
                                                        error = true;
                                                    } else {
                                                        try {
                                                            pesoSubUnidaddouble = Math.floor(Double.parseDouble(pesounidadencajaanadirlote.getText().toString()) * 10000) / 10000;
                                                            nuevolote = new Lote(cantidadunidaddouble, 0, "", cantidadunidaddouble, fechaanadirlote, proveedoranadirlote.getText().toString(), margendouble, preciocompradouble, Math.floor((((margendouble * preciocompradouble) / 100) + preciocompradouble) * 10000) / 10000, 0.0, pesoSubUnidaddouble);
                                                        } catch (Exception e) {
                                                            Toast.makeText(getActivity(), "Error: Unidades en caja errónea", Toast.LENGTH_SHORT).show();
                                                            error = true;
                                                        }

                                                    }
                                                } else if (checkBoxpesovariableanadirlote.isChecked()) {
                                                    nuevolote = new Lote(cantidadunidaddouble, 0, "", cantidadunidaddouble, fechaanadirlote, proveedoranadirlote.getText().toString(), margendouble, preciocompradouble, Math.floor((((margendouble * preciocompradouble) / 100) + preciocompradouble) * 10000) / 10000, 0.0, 0.0);

                                                }
                                            } else {
                                                if (checkBoxpesofijoanadirlote.isChecked()) {
                                                    if (pesounidadencajaanadirlote.getText().toString().equalsIgnoreCase("") || pesounidadencajaanadirlote.getText().toString().equalsIgnoreCase("0") || pesounidadencajaanadirlote.getText().toString().equalsIgnoreCase("0.0")) {
                                                        Toast.makeText(getActivity(), "Error: peso vacío o nulo", Toast.LENGTH_SHORT).show();
                                                        error = true;
                                                    } else {
                                                        if (subunidadpesoanadirlote.getText().toString().equalsIgnoreCase("")) {
                                                            Toast.makeText(getActivity(), "Error: Subunidad de peso vacia", Toast.LENGTH_SHORT).show();
                                                            error = true;
                                                        } else {
                                                            try {
                                                                pesoSubUnidaddouble = Double.parseDouble(pesounidadencajaanadirlote.getText().toString());
                                                                unidadesencajainteger = Integer.parseInt(unidadesencajaanadirlote.getText().toString());
                                                                nuevolote = new Lote(cantidadunidaddouble, unidadesencajainteger, subunidadpesoanadirlote.getText().toString(), cantidadunidaddouble, fechaanadirlote, proveedoranadirlote.getText().toString(), margendouble, preciocompradouble, Math.floor((((margendouble * preciocompradouble) / 100) + preciocompradouble) * 10000) / 10000, pesoSubUnidaddouble, pesoSubUnidaddouble * unidadesencajainteger);
                                                            } catch (Exception e) {
                                                                Toast.makeText(getActivity(), "Error: Unidades en caja errónea", Toast.LENGTH_SHORT).show();
                                                                error = true;
                                                            }
                                                        }

                                                    }
                                                } else if (checkBoxpesovariableanadirlote.isChecked()) {
                                                    try {
                                                        unidadesencajainteger = Integer.parseInt(unidadesencajaanadirlote.getText().toString());
                                                        nuevolote = new Lote(cantidadunidaddouble, unidadesencajainteger, subunidadpesoanadirlote.getText().toString(), cantidadunidaddouble, fechaanadirlote, proveedoranadirlote.getText().toString(), margendouble, preciocompradouble, Math.floor((((margendouble * preciocompradouble) / 100) + preciocompradouble) * 10000) / 10000, 0.0, 0.0);
                                                    } catch (Exception e) {
                                                        Toast.makeText(getActivity(), "Error: Unidades en caja errónea", Toast.LENGTH_SHORT).show();
                                                        error = true;
                                                    }

                                                }

                                            }


                                        }
                                    }


                                    if (!error) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle("Confirmación");
                                        builder.setMessage("¿Añadir " + cantidadunidaddouble + " de " + productoseleccionado.getNombre() + "?")
                                                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogaux, int which) {
                                                        mapa.clear();
                                                        dbstock.child(claveproductoedit).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.getValue(ProductoStock.class).getLotes() != null) {
                                                                    mapa = dataSnapshot.getValue(ProductoStock.class).getLotes();
                                                                    int max = 0;
                                                                    for (String key : productoseleccionado.getLotes().keySet()) {
                                                                        if (Integer.parseInt(key.substring(1)) > max) {
                                                                            max = Integer.parseInt(key.substring(1));
                                                                        }
                                                                    }
                                                                    max++;
                                                                    mapa.put("L" + max, nuevolote);
                                                                } else {
                                                                    mapa.put("L1", nuevolote);
                                                                }
                                                                productoeditado = new ProductoStock(productoseleccionado.getNombre(), Math.floor(((productoseleccionado.getCantidadTotal() + nuevolote.getCantidadTotal()) * 10000)) / 10000, productoseleccionado.getIVA(), mapa, productoseleccionado.getPrecioMaximo(), productoseleccionado.getPrecioMinimo(), productoseleccionado.getUnidadPeso());
                                                                if (preciocompradouble > productoseleccionado.getPrecioMaximo()) {
                                                                    productoeditado.setPrecioMaximo(preciocompradouble);
                                                                } else if (preciocompradouble < productoseleccionado.getPrecioMinimo()) {
                                                                    productoeditado.setPrecioMinimo(preciocompradouble);
                                                                }

                                                                dbstock.child(claveproductoedit).setValue(productoeditado);
                                                                dialog.dismiss();
                                                                dialogbotones.dismiss();
                                                                Toast.makeText(getActivity(), "Lote añadido", Toast.LENGTH_SHORT).show();
                                                            }


                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });


                                                        dialogaux.dismiss();


                                                    }
                                                })
                                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogaux, int which) {
                                                        dialogaux.dismiss();
                                                    }
                                                }).show();
                                    }

                                }
                            }
                        });


                        botoncancelaranadirlote.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });


                //-------------------------------------------------------------- EDITAR PRODUCTO Y LOTEEEE -------------------------------------------------------------------------------
                btneditar.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        final View view = getActivity().getLayoutInflater().inflate(R.layout.pop_edit_producto_lote, null);
                        builder.setView(view);
                        dialog = builder.create();
                        dialog.show();


                        final TextView titulocabecera = (TextView) view.findViewById(R.id.titulocabeceraeditarproductolote);
                        final EditText nombreedit = (EditText) view.findViewById(R.id.nombreeditproducto);
                        final EditText ivaedit = (EditText) view.findViewById(R.id.ivaeditproducto);
                        final TextInputLayout layoutiva = (TextInputLayout) view.findViewById(R.id.layoutivaedit);

                        final Spinner spinnereditlote = (Spinner) view.findViewById(R.id.spinnereditlote);
                        final EditText subunidadpesoeditlote = (EditText) view.findViewById(R.id.subunidadpesoeditlote);
                        final EditText cantidadeditlote = (EditText) view.findViewById(R.id.cantidadtotaleditlote);
                        final EditText margeneditlote = (EditText) view.findViewById(R.id.margenbeneficioeditlote);
                        final EditText preciocompraeditlote = (EditText) view.findViewById(R.id.preciocompraeditlote);

                        final LinearLayout layoutcheckboxeseditlote = (LinearLayout) view.findViewById(R.id.layoutcheckboxeseditlote);
                        final CheckBox checkBoxpesoalcontadoeditlote = (CheckBox) view.findViewById(R.id.checkboxpesoalcontadoeditlote);
                        final CheckBox checkBoxpesoencajaseditlote = (CheckBox) view.findViewById(R.id.checkboxpesoencajaseditlote);
                        final LinearLayout layoutunidadesysubunidadcajaeditlote = (LinearLayout) view.findViewById(R.id.layoutunidadesysubunidadcajaeditlote);
                        final EditText unidadesencajaeditlote = (EditText) view.findViewById(R.id.unidadesencajaeditlote);
                        final LinearLayout layoutcheckboxespesoeditlote = (LinearLayout) view.findViewById(R.id.layoutcheckboxespesoeditlote);
                        final CheckBox checkBoxpesofijoeditlote = (CheckBox) view.findViewById(R.id.checkboxpesofijocajaseditlote);
                        final CheckBox checkBoxpesovariableeditlote = (CheckBox) view.findViewById(R.id.checkboxpesovariablecajaseditlote);
                        final EditText pesounidadencajaeditlote = (EditText) view.findViewById(R.id.pesosubunidadeseditlote);

                        final CheckBox checkboxproducto = (CheckBox) view.findViewById(R.id.checkboxeditproducto);
                        final CheckBox checkboxlote = (CheckBox) view.findViewById(R.id.checkboxeditlote);

                        checkBoxpesoalcontadoeditlote.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    checkBoxpesoalcontadoeditlote.setClickable(false);
                                    checkBoxpesoencajaseditlote.setClickable(true);
                                    checkBoxpesoencajaseditlote.setChecked(false);
                                    checkBoxpesofijoeditlote.setChecked(false);
                                    checkBoxpesovariableeditlote.setChecked(false);
                                    layoutunidadesysubunidadcajaeditlote.setVisibility(View.GONE);
                                    layoutcheckboxespesoeditlote.setVisibility(View.GONE);
                                    pesounidadencajaeditlote.setVisibility(View.GONE);

                                }
                            }
                        });

                        checkBoxpesoencajaseditlote.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    checkBoxpesoalcontadoeditlote.setClickable(true);
                                    checkBoxpesoencajaseditlote.setClickable(false);
                                    checkBoxpesoalcontadoeditlote.setChecked(false);
                                    layoutunidadesysubunidadcajaeditlote.setVisibility(View.VISIBLE);
                                    layoutcheckboxespesoeditlote.setVisibility(View.VISIBLE);
                                    pesounidadencajaeditlote.setHint("Peso fijo de caja");
                                    checkBoxpesofijoeditlote.setClickable(true);
                                    checkBoxpesovariableeditlote.setClickable(true);

                                }
                            }
                        });

                        checkBoxpesofijoeditlote.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    checkBoxpesofijoeditlote.setClickable(false);
                                    checkBoxpesovariableeditlote.setClickable(true);
                                    checkBoxpesovariableeditlote.setChecked(false);
                                    pesounidadencajaeditlote.setVisibility(View.VISIBLE);
                                }

                            }
                        });

                        checkBoxpesovariableeditlote.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    checkBoxpesofijoeditlote.setClickable(true);
                                    checkBoxpesovariableeditlote.setClickable(false);
                                    checkBoxpesofijoeditlote.setChecked(false);
                                    pesounidadencajaeditlote.setVisibility(View.GONE);
                                }

                            }
                        });

                        unidadesencajaeditlote.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                try {
                                    if (!s.toString().equalsIgnoreCase("") && Integer.parseInt(s.toString()) != 0) {
                                        subunidadpesoeditlote.setEnabled(true);
                                        pesounidadencajaeditlote.setHint("Peso fijo unidad en caja");
                                    } else {
                                        pesounidadencajaeditlote.setHint("Peso fijo de caja");
                                        subunidadpesoeditlote.setEnabled(false);
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Error: numero incorrecto", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });

                        dbstock.child(claveproductoedit).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                try {
                                    HashMap<String, Lote> lotes = dataSnapshot.getValue(ProductoStock.class).getLotes();
                                    arraylotesmostrar = new String[lotes.size()];
                                    int i = 0;
                                    for (String keys : lotes.keySet()) {
                                        arraylotesmostrar[i] = keys;
                                        i++;
                                    }
                                    i = 0;
                                    for (Lote l : lotes.values()) {
                                        arraylotesmostrar[i] += ": " + l.getFecha() + "-" + l.getProveedor().substring(0, l.getProveedor().indexOf("-"));
                                        i++;
                                    }


                                } catch (Exception e) {
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        if (checkboxproducto.isChecked()) {
                            checkboxlote.setClickable(true);
                            checkboxproducto.setClickable(false);
                            checkboxlote.setChecked(false);

                            titulocabecera.setText("Editar Producto");
                            layoutiva.setVisibility(View.VISIBLE);
                            spinnereditlote.setVisibility(View.GONE);
                            cantidadeditlote.setVisibility(View.GONE);
                            margeneditlote.setVisibility(View.GONE);
                            preciocompraeditlote.setVisibility(View.GONE);
                            layoutcheckboxeseditlote.setVisibility(View.GONE);
                            layoutunidadesysubunidadcajaeditlote.setVisibility(View.GONE);
                            layoutcheckboxespesoeditlote.setVisibility(View.GONE);
                            pesounidadencajaeditlote.setVisibility(View.GONE);

                            nombreedit.setText(productoseleccionado.getNombre());
                            ivaedit.setText(String.valueOf(productoseleccionado.getIVA()));

                            final Button botoncancelar = (Button) view.findViewById(R.id.botoncancelareditproductolote);
                            final Button botonaceptar = (Button) view.findViewById(R.id.botonaceptareditproductolote);

                            botonaceptar.setOnClickListener(new Button.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Boolean encontrado = false;
                                    if (ivaedit.getText().toString().equals("") || nombreedit.getText().toString().equalsIgnoreCase("")) {
                                        Toast.makeText(getActivity(), "Error: Campo Vacío", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (!nombreedit.getText().toString().equalsIgnoreCase(productoseleccionado.getNombre())) {
                                            for (ProductoStock producto : arrayproductos) {
                                                if (producto.getNombre().equalsIgnoreCase(nombreedit.getText().toString())) {
                                                    encontrado = true;
                                                }
                                            }
                                        }
                                        if (encontrado) {
                                            Toast.makeText(getActivity(), "El producto ya existe", Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (ivaedit.getText().toString().equalsIgnoreCase("4") || ivaedit.getText().toString().equalsIgnoreCase("10")
                                                    || ivaedit.getText().toString().equalsIgnoreCase("21")) {

                                                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                                                builder2.setTitle("Comfirmación");
                                                builder2.setMessage("¿Desea editar el producto " + productoseleccionado.getNombre() + "?")
                                                        .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogaux, int which) {
                                                                productoeditado = new ProductoStock(nombreedit.getText().toString(), Math.floor(productoseleccionado.getCantidadTotal() * 10000) / 10000, Integer.parseInt(ivaedit.getText().toString()), productoseleccionado.getLotes(), productoseleccionado.getPrecioMaximo(), productoseleccionado.getPrecioMinimo(), productoseleccionado.getUnidadPeso());
                                                                dbstock.child(claveproductoedit).setValue(productoeditado);
                                                                dialogaux.dismiss();
                                                                dialog.dismiss();
                                                                dialogbotones.dismiss();
                                                                Toast.makeText(getActivity(), "Producto Editado", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })

                                                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogaux, int which) {
                                                                dialogaux.dismiss();
                                                            }
                                                        }).show();


                                            } else {
                                                Toast.makeText(getActivity(), "IVA erróneo", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    }
                                }
                            });


                            botoncancelar.setOnClickListener(new Button.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }

                        checkboxproducto.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()

                        {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    checkboxlote.setClickable(true);
                                    checkboxproducto.setClickable(false);
                                    checkboxlote.setChecked(false);

                                    titulocabecera.setText("Editar Producto");

                                    nombreedit.setText(productoseleccionado.getNombre());

                                    layoutiva.setVisibility(View.VISIBLE);
                                    ivaedit.setVisibility(View.VISIBLE);
                                    spinnereditlote.setVisibility(View.GONE);
                                    cantidadeditlote.setVisibility(View.GONE);
                                    margeneditlote.setVisibility(View.GONE);
                                    preciocompraeditlote.setVisibility(View.GONE);
                                    layoutcheckboxeseditlote.setVisibility(View.GONE);
                                    layoutunidadesysubunidadcajaeditlote.setVisibility(View.GONE);
                                    layoutcheckboxespesoeditlote.setVisibility(View.GONE);
                                    pesounidadencajaeditlote.setVisibility(View.GONE);

                                    nombreedit.setEnabled(true);
                                    nombreedit.setText(productoseleccionado.getNombre());

                                    ivaedit.setText(String.valueOf(productoseleccionado.getIVA()));

                                    final Button botoncancelar = (Button) view.findViewById(R.id.botoncancelareditproductolote);
                                    final Button botonaceptar = (Button) view.findViewById(R.id.botonaceptareditproductolote);

                                    botonaceptar.setOnClickListener(new Button.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Boolean encontrado = false;
                                            if (ivaedit.getText().toString().equals("") || nombreedit.getText().toString().equalsIgnoreCase("")) {
                                                Toast.makeText(getActivity(), "Error: Campo Vacío", Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (!nombreedit.getText().toString().equalsIgnoreCase(productoseleccionado.getNombre())) {
                                                    for (ProductoStock producto : arrayproductos) {
                                                        if (producto.getNombre().equalsIgnoreCase(nombreedit.getText().toString())) {
                                                            encontrado = true;
                                                        }
                                                    }
                                                }
                                                if (encontrado) {
                                                    Toast.makeText(getActivity(), "El producto ya existe", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (ivaedit.getText().toString().equalsIgnoreCase("4") || ivaedit.getText().toString().equalsIgnoreCase("10")
                                                            || ivaedit.getText().toString().equalsIgnoreCase("21")) {

                                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                                                        builder2.setTitle("Comfirmación");
                                                        builder2.setMessage("¿Desea editar el producto " + productoseleccionado.getNombre() + "?")
                                                                .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogaux, int which) {
                                                                        productoeditado = new ProductoStock(nombreedit.getText().toString(), Math.floor(productoseleccionado.getCantidadTotal() * 10000) / 10000, Integer.parseInt(ivaedit.getText().toString()), productoseleccionado.getLotes(), productoseleccionado.getPrecioMaximo(), productoseleccionado.getPrecioMinimo(), productoseleccionado.getUnidadPeso());
                                                                        dbstock.child(claveproductoedit).setValue(productoeditado);
                                                                        dialogaux.dismiss();
                                                                        dialog.dismiss();
                                                                        dialogbotones.dismiss();
                                                                        Toast.makeText(getActivity(), "Producto Editado", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                })

                                                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogaux, int which) {
                                                                        dialogaux.dismiss();
                                                                    }
                                                                }).show();


                                                    } else {
                                                        Toast.makeText(getActivity(), "IVA erróneo", Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                            }
                                        }
                                    });


                                    botoncancelar.setOnClickListener(new Button.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                }

                            }
                        });

                        checkboxlote.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    checkboxlote.setClickable(false);
                                    checkboxproducto.setClickable(true);
                                    checkboxproducto.setChecked(false);
                                    titulocabecera.setText("Editar Lote");

                                    nombreedit.setText(productoseleccionado.getNombre());
                                    nombreedit.setEnabled(false);
                                    layoutiva.setVisibility(View.GONE);
                                    ivaedit.setVisibility(View.GONE);
                                    spinnereditlote.setVisibility(View.VISIBLE);
                                    cantidadeditlote.setVisibility(View.VISIBLE);
                                    margeneditlote.setVisibility(View.VISIBLE);
                                    preciocompraeditlote.setVisibility(View.VISIBLE);
                                    layoutcheckboxeseditlote.setVisibility(View.VISIBLE);
                                    layoutunidadesysubunidadcajaeditlote.setVisibility(View.VISIBLE);
                                    layoutcheckboxespesoeditlote.setVisibility(View.VISIBLE);
                                    pesounidadencajaeditlote.setVisibility(View.VISIBLE);

                                    final Button botoncancelar = (Button) view.findViewById(R.id.botoncancelareditproductolote);
                                    final Button botonaceptar = (Button) view.findViewById(R.id.botonaceptareditproductolote);

                                    if (productoseleccionado.getLotes() != null) {
                                        ArrayAdapter<String> adaptadorlotes = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arraylotesmostrar);
                                        adaptadorlotes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        spinnereditlote.setAdapter(adaptadorlotes);
                                        spinnereditlote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                loteeditseleccionado = String.valueOf(parent.getItemAtPosition(position)).substring(3);
                                                claveloteedit = String.valueOf(parent.getItemAtPosition(position)).substring(0, String.valueOf(parent.getItemAtPosition(position)).indexOf(":")).replace(" ", "");

                                                dbstock.child(claveproductoedit).child("lotes").child(claveloteedit).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        final Lote loteobjectseleccionado = dataSnapshot.getValue(Lote.class);
                                                        cantidadeditlote.setText(String.valueOf(loteobjectseleccionado.getCantidad()));
                                                        margeneditlote.setText(String.valueOf(loteobjectseleccionado.getMargenBeneficio()));
                                                        preciocompraeditlote.setText(String.valueOf(loteobjectseleccionado.getPrecioCompra()));
                                                        if (productoseleccionado.getUnidadPeso().equalsIgnoreCase("Al peso")) {
                                                            if (loteobjectseleccionado.getPesoSubUnidad().equals(0.0) && loteobjectseleccionado.getPesoUnidad().equals(0.0)
                                                                    && loteobjectseleccionado.getSubUnidadPeso().equalsIgnoreCase("") && loteobjectseleccionado.getUnidadesenCaja().equals(0)) {
                                                                checkBoxpesoalcontadoeditlote.setChecked(true);
                                                                checkBoxpesoencajaseditlote.setChecked(false);
                                                            } else {
                                                                checkBoxpesoencajaseditlote.setChecked(true);
                                                                checkBoxpesoalcontadoeditlote.setChecked(false);
                                                                if (!loteobjectseleccionado.getUnidadesenCaja().equals(0)) {
                                                                    unidadesencajaeditlote.setText(String.valueOf(loteobjectseleccionado.getUnidadesenCaja()));
                                                                }
                                                                if (!loteobjectseleccionado.getSubUnidadPeso().equalsIgnoreCase("")) {
                                                                    subunidadpesoeditlote.setText(loteobjectseleccionado.getSubUnidadPeso());
                                                                }
                                                                if (!loteobjectseleccionado.getPesoSubUnidad().equals(0.0)) {
                                                                    checkBoxpesofijoeditlote.setChecked(true);
                                                                    checkBoxpesovariableeditlote.setChecked(false);
                                                                    pesounidadencajaeditlote.setText(String.valueOf(loteobjectseleccionado.getPesoSubUnidad()));

                                                                }else if(!loteobjectseleccionado.getPesoUnidad().equals(0.0) && loteobjectseleccionado.getPesoSubUnidad().equals(0.0)){
                                                                    checkBoxpesofijoeditlote.setChecked(true);
                                                                    checkBoxpesovariableeditlote.setChecked(false);
                                                                    pesounidadencajaeditlote.setText(String.valueOf(loteobjectseleccionado.getPesoUnidad()));

                                                                } else {
                                                                    checkBoxpesofijoeditlote.setChecked(false);
                                                                    checkBoxpesovariableeditlote.setChecked(true);

                                                                }

                                                            }
                                                        } else if (productoseleccionado.getUnidadPeso().equalsIgnoreCase("Cajas/uds")) {
                                                            layoutcheckboxeseditlote.setVisibility(View.GONE);
                                                            layoutcheckboxespesoeditlote.setVisibility(View.GONE);
                                                            pesounidadencajaeditlote.setVisibility(View.GONE);
                                                            unidadesencajaeditlote.setText(String.valueOf(loteobjectseleccionado.getUnidadesenCaja()));
                                                            subunidadpesoeditlote.setText(loteobjectseleccionado.getSubUnidadPeso());
                                                        }


                                                        botonaceptar.setOnClickListener(new Button.OnClickListener() {

                                                            @Override
                                                            public void onClick(View v) {
                                                                Boolean error = false;
                                                                if (cantidadeditlote.getText().toString().equalsIgnoreCase("") || preciocompraeditlote.getText().toString().equalsIgnoreCase("") || margeneditlote.getText().toString().equalsIgnoreCase("") ||
                                                                        preciocompraeditlote.getText().toString().equalsIgnoreCase("0") || margeneditlote.getText().toString().equalsIgnoreCase("0")
                                                                        || preciocompraeditlote.getText().toString().equalsIgnoreCase("0.0") || margeneditlote.getText().toString().equalsIgnoreCase("0.0")) {
                                                                    Toast.makeText(getActivity(), "Error: Campos Vacíos", Toast.LENGTH_SHORT).show();
                                                                } else if (cantidadeditlote.getText().toString().equalsIgnoreCase("0") || cantidadeditlote.getText().toString().equalsIgnoreCase("0.0")) {
                                                                    AlertDialog.Builder buildereliminar = new AlertDialog.Builder(getActivity());
                                                                    buildereliminar.setMessage("¿Confirma borrar el lote " + loteeditseleccionado + "?").setTitle("Confirmacion").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialogaux, int id) {

                                                                            dbstock.child(claveproductoedit).child("lotes").child(claveloteedit).removeValue();
                                                                            dbstock.child(claveproductoedit).child("cantidadTotal").setValue(productoseleccionado.getCantidadTotal() - loteobjectseleccionado.getCantidadTotal());

                                                                            Toast.makeText(getActivity(), "Lote " + loteeditseleccionado + " borrado", Toast.LENGTH_SHORT).show();
                                                                            dialog2.dismiss();
                                                                            dialog.dismiss();
                                                                            dialogaux.dismiss();
                                                                            dialogbotones.dismiss();

                                                                        }
                                                                    })
                                                                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int id) {
                                                                                    dialog.cancel();
                                                                                }
                                                                            }).show();
                                                                }

                                                                if (productoseleccionado.getUnidadPeso().equalsIgnoreCase("Cajas/uds")) {
                                                                    if (subunidadpesoeditlote.getText().toString().equalsIgnoreCase("") || unidadesencajaeditlote.getText().toString().equalsIgnoreCase("")) {
                                                                        Toast.makeText(getActivity(), "Error: SubUd o SubCant vacíos", Toast.LENGTH_SHORT).show();
                                                                        error = true;
                                                                    } else if (unidadesencajaeditlote.getText().toString().equalsIgnoreCase("0") || unidadesencajaeditlote.getText().toString().equalsIgnoreCase("0.0")) {
                                                                        Toast.makeText(getActivity(), "Error: SubCant no puede ser 0", Toast.LENGTH_SHORT).show();
                                                                        error = true;
                                                                    } else {
                                                                        try {
                                                                            final Double cantidaddouble = Double.parseDouble(cantidadeditlote.getText().toString());

                                                                            try {
                                                                                final Integer subcantidadinteger = Integer.parseInt(unidadesencajaeditlote.getText().toString());

                                                                                try {
                                                                                    final Double margeninteger = Double.parseDouble(margeneditlote.getText().toString());

                                                                                    try {
                                                                                        final Double preciocompradouble = Double.parseDouble(preciocompraeditlote.getText().toString());

                                                                                        if (!error) {

                                                                                            AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                                                                                            View view2 = getActivity().getLayoutInflater().inflate(R.layout.pop_info_edit_lote, null);
                                                                                            builder2.setView(view2);
                                                                                            dialog2 = builder2.create();
                                                                                            dialog2.show();

                                                                                            TextView nombreproductolote = (TextView) view2.findViewById(R.id.infonombreproducto);
                                                                                            TextView infolote = (TextView) view2.findViewById(R.id.infolote);
                                                                                            TextView infounidaddepeso = (TextView) view2.findViewById(R.id.infounidaddepeso);
                                                                                            TextView infosubunidaddepeso = (TextView) view2.findViewById(R.id.infosubunidaddepeso);
                                                                                            TextView infocantidadlote = (TextView) view2.findViewById(R.id.infocantidadlote);
                                                                                            TextView infosubcantidadlote = (TextView) view2.findViewById(R.id.infosubcantidadlote);
                                                                                            TextView infopesounidad = (TextView) view2.findViewById(R.id.infopesounidad);
                                                                                            TextView infopesosubunidad = (TextView) view2.findViewById(R.id.infopesosubunidad);
                                                                                            TextView infomargenlote = (TextView) view2.findViewById(R.id.infomargenlote);
                                                                                            TextView infopreciolote = (TextView) view2.findViewById(R.id.infopreciolote);

                                                                                            nombreproductolote.setText(productoseleccionado.getNombre());
                                                                                            infolote.setText(loteeditseleccionado);
                                                                                            infocantidadlote.setText(cantidadeditlote.getText().toString());
                                                                                            infosubcantidadlote.setText(unidadesencajaeditlote.getText().toString());
                                                                                            infounidaddepeso.setText(productoseleccionado.getUnidadPeso());
                                                                                            infosubunidaddepeso.setText(subunidadpesoeditlote.getText().toString());
                                                                                            infopesounidad.setText("");
                                                                                            infopesosubunidad.setText("");
                                                                                            infomargenlote.setText(margeneditlote.getText().toString());
                                                                                            infopreciolote.setText(preciocompraeditlote.getText().toString());

                                                                                            Button botoncancelar = (Button) view2.findViewById(R.id.botoncancelarinfolote);
                                                                                            Button botonaceptar = (Button) view2.findViewById(R.id.botonconfirmarinfolote);

                                                                                            botonaceptar.setOnClickListener(new Button.OnClickListener() {

                                                                                                @Override
                                                                                                public void onClick(View v) {

                                                                                                    loteaeditar = new Lote(Math.floor(cantidaddouble * 10000) / 10000, subcantidadinteger, subunidadpesoeditlote.getText().toString(), Math.floor(cantidaddouble * subcantidadinteger * 10000) / 10000, loteobjectseleccionado.getFecha(), loteobjectseleccionado.getProveedor(), Math.floor(margeninteger * 10000) / 10000, Math.floor(preciocompradouble * 10000) / 10000, Math.floor((((preciocompradouble * margeninteger) / 100) + preciocompradouble) * 10000) / 10000, 0.0,0.0);
                                                                                                    nuevacantidadeditlote = (productoseleccionado.getCantidadTotal() - loteobjectseleccionado.getCantidadTotal()) + (Math.floor(cantidaddouble * subcantidadinteger * 10000) / 10000);
                                                                                                    dbstock.child(claveproductoedit).child("lotes").child(claveloteedit).setValue(loteaeditar);
                                                                                                    dbstock.child(claveproductoedit).child("cantidadTotal").setValue(nuevacantidadeditlote);

                                                                                                    dialog2.dismiss();
                                                                                                    dialog.dismiss();
                                                                                                    dialogbotones.dismiss();
                                                                                                    Toast.makeText(getActivity(), "Lote editado", Toast.LENGTH_SHORT).show();

                                                                                                }
                                                                                            });

                                                                                            botoncancelar.setOnClickListener(new Button.OnClickListener() {

                                                                                                @Override
                                                                                                public void onClick(View v) {
                                                                                                    dialog2.dismiss();
                                                                                                }
                                                                                            });
                                                                                        }


                                                                                    } catch (Exception e) {
                                                                                        Toast.makeText(getActivity(), "Error: Precio de compra", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                } catch (Exception e) {
                                                                                    Toast.makeText(getActivity(), "Error: Margen de beneficio", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            } catch (Exception e) {
                                                                                Toast.makeText(getActivity(), "Error: Unidades en caja", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                        } catch (Exception e) {
                                                                            Toast.makeText(getActivity(), "Error: Cantidad", Toast.LENGTH_SHORT).show();
                                                                        }


                                                                    }
                                                                } else {
                                                                    try {
                                                                        final Double cantidaddouble = Double.parseDouble(cantidadeditlote.getText().toString());

                                                                        try {
                                                                            final Double margeninteger = Double.parseDouble(margeneditlote.getText().toString());

                                                                            try {
                                                                                final Double preciocompradouble = Double.parseDouble(preciocompraeditlote.getText().toString());


                                                                                if (checkBoxpesoalcontadoeditlote.isChecked()) {
                                                                                    if (!error) {

                                                                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                                                                                        View view2 = getActivity().getLayoutInflater().inflate(R.layout.pop_info_edit_lote, null);
                                                                                        builder2.setView(view2);
                                                                                        dialog2 = builder2.create();
                                                                                        dialog2.show();

                                                                                        TextView nombreproductolote = (TextView) view2.findViewById(R.id.infonombreproducto);
                                                                                        TextView infolote = (TextView) view2.findViewById(R.id.infolote);
                                                                                        TextView infounidaddepeso = (TextView) view2.findViewById(R.id.infounidaddepeso);
                                                                                        TextView infosubunidaddepeso = (TextView) view2.findViewById(R.id.infosubunidaddepeso);
                                                                                        TextView infocantidadlote = (TextView) view2.findViewById(R.id.infocantidadlote);
                                                                                        TextView infosubcantidadlote = (TextView) view2.findViewById(R.id.infosubcantidadlote);
                                                                                        TextView infopesounidad = (TextView) view2.findViewById(R.id.infopesounidad);
                                                                                        TextView infopesosubunidad = (TextView) view2.findViewById(R.id.infopesosubunidad);
                                                                                        TextView infomargenlote = (TextView) view2.findViewById(R.id.infomargenlote);
                                                                                        TextView infopreciolote = (TextView) view2.findViewById(R.id.infopreciolote);


                                                                                        nombreproductolote.setText(productoseleccionado.getNombre());
                                                                                        infolote.setText(loteeditseleccionado);
                                                                                        infocantidadlote.setText(cantidadeditlote.getText().toString());
                                                                                        infosubcantidadlote.setText("");
                                                                                        infounidaddepeso.setText(productoseleccionado.getUnidadPeso());
                                                                                        infosubunidaddepeso.setText("");
                                                                                        infopesounidad.setText("");
                                                                                        infopesosubunidad.setText("");
                                                                                        infomargenlote.setText(margeneditlote.getText().toString());
                                                                                        infopreciolote.setText(preciocompraeditlote.getText().toString());

                                                                                        Button botoncancelar = (Button) view2.findViewById(R.id.botoncancelarinfolote);
                                                                                        Button botonaceptar = (Button) view2.findViewById(R.id.botonconfirmarinfolote);

                                                                                        botonaceptar.setOnClickListener(new Button.OnClickListener() {

                                                                                            @Override
                                                                                            public void onClick(View v) {

                                                                                                loteaeditar = new Lote(Math.floor(cantidaddouble * 10000) / 10000, 0,"", Math.floor(cantidaddouble * 10000) / 10000, loteobjectseleccionado.getFecha(), loteobjectseleccionado.getProveedor(), Math.floor(margeninteger * 10000) / 10000, Math.floor(preciocompradouble * 10000) / 10000, Math.floor((((preciocompradouble * margeninteger) / 100) + preciocompradouble) * 10000) / 10000, 0.0,0.0);
                                                                                                nuevacantidadeditlote = (productoseleccionado.getCantidadTotal() - loteobjectseleccionado.getCantidadTotal()) + cantidaddouble;
                                                                                                dbstock.child(claveproductoedit).child("lotes").child(claveloteedit).setValue(loteaeditar);
                                                                                                dbstock.child(claveproductoedit).child("cantidadTotal").setValue(nuevacantidadeditlote);

                                                                                                dialog2.dismiss();
                                                                                                dialog.dismiss();
                                                                                                dialogbotones.dismiss();
                                                                                                Toast.makeText(getActivity(), "Lote editado", Toast.LENGTH_SHORT).show();

                                                                                            }
                                                                                        });

                                                                                        botoncancelar.setOnClickListener(new Button.OnClickListener() {

                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                dialog2.dismiss();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                } else {

                                                                                    if (!error) {

                                                                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                                                                                        View view2 = getActivity().getLayoutInflater().inflate(R.layout.pop_info_edit_lote, null);
                                                                                        builder2.setView(view2);
                                                                                        dialog2 = builder2.create();
                                                                                        dialog2.show();

                                                                                        TextView nombreproductolote = (TextView) view2.findViewById(R.id.infonombreproducto);
                                                                                        TextView infolote = (TextView) view2.findViewById(R.id.infolote);
                                                                                        TextView infounidaddepeso = (TextView) view2.findViewById(R.id.infounidaddepeso);
                                                                                        TextView infosubunidaddepeso = (TextView) view2.findViewById(R.id.infosubunidaddepeso);
                                                                                        TextView infocantidadlote = (TextView) view2.findViewById(R.id.infocantidadlote);
                                                                                        TextView infosubcantidadlote = (TextView) view2.findViewById(R.id.infosubcantidadlote);
                                                                                        TextView infopesounidad = (TextView) view2.findViewById(R.id.infopesounidad);
                                                                                        TextView infopesosubunidad = (TextView) view2.findViewById(R.id.infopesosubunidad);
                                                                                        TextView infomargenlote = (TextView) view2.findViewById(R.id.infomargenlote);
                                                                                        TextView infopreciolote = (TextView) view2.findViewById(R.id.infopreciolote);


                                                                                        nombreproductolote.setText(productoseleccionado.getNombre());
                                                                                        infolote.setText(loteeditseleccionado);
                                                                                        infocantidadlote.setText(cantidadeditlote.getText().toString());
                                                                                        infosubcantidadlote.setText(unidadesencajaeditlote.getText().toString());
                                                                                        infounidaddepeso.setText(productoseleccionado.getUnidadPeso());
                                                                                        infosubunidaddepeso.setText(subunidadpesoeditlote.getText().toString());
                                                                                        infomargenlote.setText(margeneditlote.getText().toString());
                                                                                        infopreciolote.setText(preciocompraeditlote.getText().toString());

                                                                                        if(!unidadesencajaeditlote.getText().toString().equalsIgnoreCase("") && !unidadesencajaeditlote.getText().toString().equalsIgnoreCase("0") && !unidadesencajaeditlote.getText().toString().equalsIgnoreCase("0.0")) {
                                                                                            try {
                                                                                                unidadesencajainteger = Integer.parseInt(unidadesencajaeditlote.getText().toString());
                                                                                            } catch (Exception e) {
                                                                                                Toast.makeText(getActivity(), "Error: Unidades en caja", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }else{
                                                                                            unidadesencajainteger = 0;
                                                                                        }

                                                                                            if (checkBoxpesofijoeditlote.isChecked()) {
                                                                                                try {
                                                                                                    pesounidadencajadouble = Math.floor(Double.parseDouble(pesounidadencajaeditlote.getText().toString())*10000)/10000;
                                                                                                } catch(Exception e ) {
                                                                                                    Toast.makeText(getActivity(), "Error: Peso Unidad en caja", Toast.LENGTH_SHORT).show();
                                                                                                    error = true;
                                                                                                }
                                                                                                if (unidadesencajainteger != 0 && !unidadesencajaeditlote.getText().toString().equalsIgnoreCase("")) {
                                                                                                    infopesounidad.setText(String.valueOf(unidadesencajainteger*pesounidadencajadouble));
                                                                                                    infopesosubunidad.setText(String.valueOf(pesounidadencajadouble));
                                                                                                }else{
                                                                                                    infopesounidad.setText(String.valueOf(pesounidadencajadouble));
                                                                                                    infopesosubunidad.setText("");
                                                                                                }
                                                                                            }else{
                                                                                                infopesounidad.setText("");
                                                                                                infopesosubunidad.setText("");
                                                                                                pesounidadencajadouble=0.0;
                                                                                            }


                                                                                        Button botoncancelar = (Button) view2.findViewById(R.id.botoncancelarinfolote);
                                                                                        Button botonaceptar = (Button) view2.findViewById(R.id.botonconfirmarinfolote);

                                                                                        botonaceptar.setOnClickListener(new Button.OnClickListener() {

                                                                                            @Override
                                                                                            public void onClick(View v) {

                                                                                                if(unidadesencajainteger!=0){
                                                                                                    loteaeditar = new Lote(Math.floor(cantidaddouble * 10000) / 10000, unidadesencajainteger, subunidadpesoeditlote.getText().toString(), Math.floor(cantidaddouble * 10000) / 10000, loteobjectseleccionado.getFecha(), loteobjectseleccionado.getProveedor(), Math.floor(margeninteger * 10000) / 10000, Math.floor(preciocompradouble * 10000) / 10000, Math.floor((((preciocompradouble * margeninteger) / 100) + preciocompradouble) * 10000) / 10000,  pesounidadencajadouble,   pesounidadencajadouble*unidadesencajainteger);
                                                                                                }else{
                                                                                                    loteaeditar = new Lote(Math.floor(cantidaddouble * 10000) / 10000, unidadesencajainteger, subunidadpesoeditlote.getText().toString(), Math.floor(cantidaddouble * 10000) / 10000, loteobjectseleccionado.getFecha(), loteobjectseleccionado.getProveedor(), Math.floor(margeninteger * 10000) / 10000, Math.floor(preciocompradouble * 10000) / 10000, Math.floor((((preciocompradouble * margeninteger) / 100) + preciocompradouble) * 10000) / 10000,  0.0,   pesounidadencajadouble);
                                                                                                }

                                                                                                nuevacantidadeditlote = (productoseleccionado.getCantidadTotal() - loteobjectseleccionado.getCantidadTotal()) + cantidaddouble;
                                                                                                dbstock.child(claveproductoedit).child("lotes").child(claveloteedit).setValue(loteaeditar);
                                                                                                dbstock.child(claveproductoedit).child("cantidadTotal").setValue(nuevacantidadeditlote);


                                                                                                dialog2.dismiss();
                                                                                                dialog.dismiss();
                                                                                                dialogbotones.dismiss();
                                                                                                Toast.makeText(getActivity(), "Lote editado", Toast.LENGTH_SHORT).show();

                                                                                            }
                                                                                        });

                                                                                        botoncancelar.setOnClickListener(new Button.OnClickListener() {

                                                                                            @Override
                                                                                            public void onClick(View v) {
                                                                                                dialog2.dismiss();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }


                                                                            } catch (Exception e) {
                                                                                Toast.makeText(getActivity(), "Error: Precio de compra", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                        } catch (Exception e) {
                                                                            Toast.makeText(getActivity(), "Error: Margen de beneficio", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    } catch (Exception e) {
                                                                        Toast.makeText(getActivity(), "Error: Cantidad", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }

                                                        });

                                                        botoncancelar.setOnClickListener(new Button.OnClickListener() {

                                                            @Override
                                                            public void onClick(View v) {
                                                                dialog.dismiss();
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
                                    } else {
                                        Toast.makeText(getActivity(), "Este producto no tiene lotes", Toast.LENGTH_SHORT).show();
                                        botonaceptar.setEnabled(false);
                                    }


                                }
                            }
                        });


                    }
                });


                btneliminar.setOnClickListener(new Button.OnClickListener()

                {

                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder buildereliminar =
                                new AlertDialog.Builder(getActivity());

                        buildereliminar.setMessage("¿Confirma borrar el producto " + productoseleccionado.getNombre() + "?")
                                .setTitle("Confirmacion")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dbstock.child(claveproductoedit).removeValue();
                                        adapterstock.notifyDataSetChanged();

                                        Toast.makeText(getActivity(), "Producto " + claveproductoedit + " borrado", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        dialogbotones.dismiss();

                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                });

                return true;
            }
        });


        listaproductos.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductoStock seleccionado = (ProductoStock) parent.getItemAtPosition(position);
                Toast.makeText(getActivity(), seleccionado.getNombre(), Toast.LENGTH_SHORT).show();
            }
        });

        listaproductos.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_TOUCH_SCROLL){
                    anadirarticulostock.hide();
                }else{
                    anadirarticulostock.show();


                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
//-----------------------------------------------AÑADIR NUEVO PRODUCTO-----------------------------------------------------------------------
        anadirarticulostock = (FloatingActionButton) rootView.findViewById(R.id.anadirarticulostock);
        anadirarticulostock.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View view = getActivity().getLayoutInflater().inflate(R.layout.pop_anadir_producto_stock, null);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();


//---------------------------------------------------------------------KEY DEL PRODUCTO AÑADIDO AL STOCK------------------------------------------------------------------------------------------------------

                dbstock.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Keynuevoproducto = nuevoIdProducto(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                nombrenuevoproducto = (EditText) view.findViewById(R.id.nombrenuevoproducto);
                spinnerunidadpesonuevoproduto = (Spinner) view.findViewById(R.id.spinnerunidadPesonuevoproducto);
                layoutcheckboxesnuevoproducto = (LinearLayout) view.findViewById(R.id.layoutcheckboxesnuevoproducto);
                checkBoxpesoalcontadonuevoproducto = (CheckBox) view.findViewById(R.id.checkboxpesoalcontadonuevoproducto);
                checkBoxpesoencajasnuevoproducto = (CheckBox) view.findViewById(R.id.checkboxpesoencajasnuevoproducto);
                subunidadpesonuevoproducto = (EditText) view.findViewById(R.id.subunidadpesonuevoproducto);
                cantidadtotalnuevoproducto = (EditText) view.findViewById(R.id.cantidadtotalnuevoproducto);
                layoutunidadesysubunidadcajanuevoproducto = (LinearLayout) view.findViewById(R.id.layoutunidadesysubunidadcajanuevoproducto);
                unidadesencajanuevoproducto = (EditText) view.findViewById(R.id.unidadesencajanuevoproducto);
                layoutcheckboxespesonuevoproducto = (LinearLayout) view.findViewById(R.id.layoutcheckboxespesonuevoproducto);
                checkBoxpesofijonuevoproducto = (CheckBox) view.findViewById(R.id.checkboxpesofijocajasnuevoproducto);
                checkBoxpesovariablenuevoproducto = (CheckBox) view.findViewById(R.id.checkboxpesovariablecajasnuevoproducto);
                pesounidadencajanuevoproducto = (EditText) view.findViewById(R.id.pesosubunidadesnuevoproducto);
                proveedornuevoproducto = (AutoCompleteTextView) view.findViewById(R.id.proveedornuevoproducto);
                margenbeneficionuevoproducto = (EditText) view.findViewById(R.id.margenbeneficionuevoproducto);
                preciocompranuevoproducto = (EditText) view.findViewById(R.id.preciocompranuevoproducto);
                botonaceptarnuevoproducto = (Button) view.findViewById(R.id.botonaceptarnuevoproducto);
                botoncancelarnuevoproducto = (Button) view.findViewById(R.id.botoncancelarnuevoproducto);


//-----------------------------------------------AutocompleteTExtView proveedores-----------------------------------------------------------------------

                dbproveedores = FirebaseDatabase.getInstance().getReference("Proveedores");
                dbproveedores.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        almacenarproveedores(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                ArrayAdapter<String> adapterproveedores = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, proveedores);
                adapterproveedores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                proveedornuevoproducto.setThreshold(1);
                proveedornuevoproducto.setAdapter(adapterproveedores);


//-----------------------------------------------Spinner IVA---------------------------------------------------------------------------

                ArrayAdapter<String> adaptadorivas = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ivas);
                adaptadorivas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerivanuevoproducto = (Spinner) view.findViewById(R.id.spinnerivanuevoproducto);
                spinnerivanuevoproducto.setAdapter(adaptadorivas);
                spinnerivanuevoproducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ivanuevoproducto = parent.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


//---------------------------------------------------------------------Spinner UnidadPESO------------------------------------------------------------------------------------------------------
                adaptadorunidadpeso = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, unidadespeso);
                adaptadorunidadpeso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerunidadpesonuevoproduto.setAdapter(adaptadorunidadpeso);
                spinnerunidadpesonuevoproduto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                        unidadpesonuevoproducto = parent.getItemAtPosition(position).toString();
                        if (unidadpesonuevoproducto.equalsIgnoreCase("Cajas/uds")) {
                            cantidadtotalnuevoproducto.setVisibility(View.VISIBLE);
                            cantidadtotalnuevoproducto.setHint("Cantidad de cajas");
                            layoutcheckboxesnuevoproducto.setVisibility(View.GONE);
                            layoutunidadesysubunidadcajanuevoproducto.setVisibility(View.VISIBLE);
                            layoutcheckboxespesonuevoproducto.setVisibility(View.GONE);
                            pesounidadencajanuevoproducto.setVisibility(View.GONE);
                            preciocompranuevoproducto.setHint("Precio de compra por unidad");
                            checkBoxpesoalcontadonuevoproducto.setChecked(false);
                            checkBoxpesoencajasnuevoproducto.setChecked(false);
                            checkBoxpesofijonuevoproducto.setChecked(false);
                            checkBoxpesovariablenuevoproducto.setChecked(false);

                        } else if (unidadpesonuevoproducto.equalsIgnoreCase("Al peso")) {
                            cantidadtotalnuevoproducto.setHint("Cantidad de kilos");
                            cantidadtotalnuevoproducto.setVisibility(View.VISIBLE);
                            layoutcheckboxesnuevoproducto.setVisibility(View.VISIBLE);
                            layoutunidadesysubunidadcajanuevoproducto.setVisibility(View.GONE);
                            layoutcheckboxespesonuevoproducto.setVisibility(View.GONE);
                            pesounidadencajanuevoproducto.setVisibility(View.GONE);
                            preciocompranuevoproducto.setHint("Precio de compra por kilo");
                            checkBoxpesoalcontadonuevoproducto.setChecked(false);
                            checkBoxpesoencajasnuevoproducto.setChecked(false);
                            checkBoxpesofijonuevoproducto.setChecked(false);
                            checkBoxpesovariablenuevoproducto.setChecked(false);

                        } else {
                            cantidadtotalnuevoproducto.setVisibility(View.GONE);
                            layoutcheckboxesnuevoproducto.setVisibility(View.GONE);
                            layoutunidadesysubunidadcajanuevoproducto.setVisibility(View.GONE);
                            layoutcheckboxespesonuevoproducto.setVisibility(View.GONE);
                            pesounidadencajanuevoproducto.setVisibility(View.GONE);
                            checkBoxpesoalcontadonuevoproducto.setChecked(false);
                            checkBoxpesoencajasnuevoproducto.setChecked(false);
                            checkBoxpesofijonuevoproducto.setChecked(false);
                            checkBoxpesovariablenuevoproducto.setChecked(false);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                checkBoxpesoalcontadonuevoproducto.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            checkBoxpesoalcontadonuevoproducto.setClickable(false);
                            checkBoxpesoencajasnuevoproducto.setClickable(true);
                            checkBoxpesoencajasnuevoproducto.setChecked(false);
                            checkBoxpesofijonuevoproducto.setChecked(false);
                            checkBoxpesovariablenuevoproducto.setChecked(false);
                            layoutunidadesysubunidadcajanuevoproducto.setVisibility(View.GONE);
                            layoutcheckboxespesonuevoproducto.setVisibility(View.GONE);
                            pesounidadencajanuevoproducto.setVisibility(View.GONE);

                        }
                    }
                });

                checkBoxpesoencajasnuevoproducto.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            checkBoxpesoalcontadonuevoproducto.setClickable(true);
                            checkBoxpesoencajasnuevoproducto.setClickable(false);
                            checkBoxpesoalcontadonuevoproducto.setChecked(false);
                            layoutunidadesysubunidadcajanuevoproducto.setVisibility(View.VISIBLE);
                            layoutcheckboxespesonuevoproducto.setVisibility(View.VISIBLE);
                            pesounidadencajanuevoproducto.setHint("Peso fijo de caja");
                            checkBoxpesofijonuevoproducto.setClickable(true);
                            checkBoxpesovariablenuevoproducto.setClickable(true);

                        }
                    }
                });

                checkBoxpesofijonuevoproducto.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            checkBoxpesofijonuevoproducto.setClickable(false);
                            checkBoxpesovariablenuevoproducto.setClickable(true);
                            checkBoxpesovariablenuevoproducto.setChecked(false);
                            pesounidadencajanuevoproducto.setVisibility(View.VISIBLE);
                        }

                    }
                });

                checkBoxpesovariablenuevoproducto.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            checkBoxpesofijonuevoproducto.setClickable(true);
                            checkBoxpesovariablenuevoproducto.setClickable(false);
                            checkBoxpesofijonuevoproducto.setChecked(false);
                            pesounidadencajanuevoproducto.setVisibility(View.GONE);
                        }

                    }
                });

                unidadesencajanuevoproducto.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            if (!s.toString().equalsIgnoreCase("") && Integer.parseInt(s.toString()) != 0) {
                                subunidadpesonuevoproducto.setEnabled(true);
                                pesounidadencajanuevoproducto.setHint("Peso fijo unidad en caja");
                            } else {
                                pesounidadencajanuevoproducto.setHint("Peso fijo de caja");
                                subunidadpesonuevoproducto.setEnabled(false);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Error: numero incorrecto", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                botonaceptarnuevoproducto.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Boolean error = false;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
                        Date datenuevo = new Date();
                        final String fechanuevoproducto = dateFormat.format(datenuevo);
                        if (nombrenuevoproducto.getText().toString().equalsIgnoreCase("") || proveedornuevoproducto.getText().toString().equalsIgnoreCase("") || cantidadtotalnuevoproducto.getText().toString().equalsIgnoreCase("")
                                || cantidadtotalnuevoproducto.getText().toString().equalsIgnoreCase("0") || cantidadtotalnuevoproducto.getText().toString().equalsIgnoreCase("0.0")
                                || preciocompranuevoproducto.getText().toString().equalsIgnoreCase("") || margenbeneficionuevoproducto.getText().toString().equalsIgnoreCase("") || margenbeneficionuevoproducto.getText().toString().equalsIgnoreCase("0")
                                || margenbeneficionuevoproducto.getText().toString().equalsIgnoreCase("0.0") || ivanuevoproducto.equalsIgnoreCase("IVA") || unidadpesonuevoproducto.equalsIgnoreCase("Elegir Ud. peso")) {
                            Toast.makeText(getActivity(), "Error: Campo Vacio o nulo", Toast.LENGTH_SHORT).show();
                        } else if (!proveedores.contains(proveedornuevoproducto.getText().toString())) {
                            Toast.makeText(getActivity(), "Error: proveedor no registrado", Toast.LENGTH_SHORT).show();
                            error = true;

                        } else {
                            try {
                                cantidadunidaddouble = Math.floor(Double.parseDouble(cantidadtotalnuevoproducto.getText().toString()) * 10000) / 10000;

                                try {
                                    preciocompradouble = Math.floor(Double.parseDouble(preciocompranuevoproducto.getText().toString()) * 10000) / 10000;

                                    try {
                                        margendouble = Math.floor(Double.parseDouble(margenbeneficionuevoproducto.getText().toString()) * 10000) / 10000;

                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), "Error: Margen erróneo", Toast.LENGTH_SHORT).show();
                                        error = true;
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Error: Precio erróneo", Toast.LENGTH_SHORT).show();
                                    error = true;
                                }

                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "Error: Cantidad errónea", Toast.LENGTH_SHORT).show();
                                error = true;
                            }
                            if (unidadpesonuevoproducto.equalsIgnoreCase("Cajas/uds")) {
                                if (unidadesencajanuevoproducto.getText().toString().equalsIgnoreCase("") || subunidadpesonuevoproducto.getText().toString().equalsIgnoreCase("")) {
                                    Toast.makeText(getActivity(), "Error: campo vacío", Toast.LENGTH_SHORT).show();
                                    error = true;
                                } else {
                                    try {
                                        unidadesencajainteger = Integer.parseInt(unidadesencajanuevoproducto.getText().toString());
                                        nuevolote = new Lote(cantidadunidaddouble, unidadesencajainteger, subunidadpesonuevoproducto.getText().toString(), cantidadunidaddouble * unidadesencajainteger, fechanuevoproducto, proveedornuevoproducto.getText().toString(), margendouble, preciocompradouble, Math.floor((((margendouble * preciocompradouble) / 100) + preciocompradouble) * 10000) / 10000, 0.0, 0.0);
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), "Error: Unidades en caja errónea", Toast.LENGTH_SHORT).show();
                                        error = true;
                                    }

                                }
                            } else if (unidadpesonuevoproducto.equalsIgnoreCase("Al peso")) {
                                if (checkBoxpesoalcontadonuevoproducto.isChecked()) {
                                    nuevolote = new Lote(cantidadunidaddouble, 0, "", cantidadunidaddouble, fechanuevoproducto, proveedornuevoproducto.getText().toString(), margendouble, preciocompradouble, Math.floor((((margendouble * preciocompradouble) / 100) + preciocompradouble) * 10000) / 10000, 0.0, 0.0);
                                } else if (checkBoxpesoencajasnuevoproducto.isChecked()) {

                                    if (unidadesencajanuevoproducto.getText().toString().equalsIgnoreCase("") || unidadesencajanuevoproducto.getText().toString().equalsIgnoreCase("0") || unidadesencajanuevoproducto.getText().toString().equalsIgnoreCase("0.0")) {
                                        if (checkBoxpesofijonuevoproducto.isChecked()) {
                                            if (pesounidadencajanuevoproducto.getText().toString().equalsIgnoreCase("") || pesounidadencajanuevoproducto.getText().toString().equalsIgnoreCase("0") || pesounidadencajanuevoproducto.getText().toString().equalsIgnoreCase("0.0")) {
                                                Toast.makeText(getActivity(), "Error: peso vacío o nulo", Toast.LENGTH_SHORT).show();
                                                error = true;
                                            } else {
                                                try {
                                                    pesoSubUnidaddouble = Math.floor(Double.parseDouble(pesounidadencajanuevoproducto.getText().toString()) * 10000) / 10000;
                                                    nuevolote = new Lote(cantidadunidaddouble, 0, "", cantidadunidaddouble, fechanuevoproducto, proveedornuevoproducto.getText().toString(), margendouble, preciocompradouble, Math.floor((((margendouble * preciocompradouble) / 100) + preciocompradouble) * 10000) / 10000, 0.0, pesoSubUnidaddouble);
                                                } catch (Exception e) {
                                                    Toast.makeText(getActivity(), "Error: Unidades en caja errónea", Toast.LENGTH_SHORT).show();
                                                    error = true;
                                                }

                                            }
                                        } else if (checkBoxpesovariablenuevoproducto.isChecked()) {
                                            nuevolote = new Lote(cantidadunidaddouble, 0, "", cantidadunidaddouble, fechanuevoproducto, proveedornuevoproducto.getText().toString(), margendouble, preciocompradouble, Math.floor((((margendouble * preciocompradouble) / 100) + preciocompradouble) * 10000) / 10000, 0.0, 0.0);

                                        }
                                    } else {
                                        if(subunidadpesonuevoproducto.getText().toString().equalsIgnoreCase("")){
                                            Toast.makeText(getActivity(), "Error: Subunidad de peso", Toast.LENGTH_SHORT).show();
                                            error = true;
                                        }else {
                                            if (checkBoxpesofijonuevoproducto.isChecked()) {
                                                if (pesounidadencajanuevoproducto.getText().toString().equalsIgnoreCase("") || pesounidadencajanuevoproducto.getText().toString().equalsIgnoreCase("0") || pesounidadencajanuevoproducto.getText().toString().equalsIgnoreCase("0.0")) {
                                                    Toast.makeText(getActivity(), "Error: peso vacío o nulo", Toast.LENGTH_SHORT).show();
                                                    error = true;
                                                } else {
                                                    if (subunidadpesonuevoproducto.getText().toString().equalsIgnoreCase("")) {
                                                        Toast.makeText(getActivity(), "Error: Subunidad de peso vacia", Toast.LENGTH_SHORT).show();
                                                        error = true;
                                                    } else {
                                                        try {
                                                            pesoSubUnidaddouble = Double.parseDouble(pesounidadencajanuevoproducto.getText().toString());
                                                            unidadesencajainteger = Integer.parseInt(unidadesencajanuevoproducto.getText().toString());
                                                            nuevolote = new Lote(cantidadunidaddouble, unidadesencajainteger, subunidadpesonuevoproducto.getText().toString(), cantidadunidaddouble, fechanuevoproducto, proveedornuevoproducto.getText().toString(), margendouble, preciocompradouble, Math.floor((((margendouble * preciocompradouble) / 100) + preciocompradouble) * 10000) / 10000, pesoSubUnidaddouble, pesoSubUnidaddouble * unidadesencajainteger);
                                                        } catch (Exception e) {
                                                            Toast.makeText(getActivity(), "Error: Unidades en caja errónea", Toast.LENGTH_SHORT).show();
                                                            error = true;
                                                        }
                                                    }

                                                }
                                            } else if (checkBoxpesovariablenuevoproducto.isChecked()) {
                                                try {
                                                    unidadesencajainteger = Integer.parseInt(unidadesencajanuevoproducto.getText().toString());
                                                    nuevolote = new Lote(cantidadunidaddouble, unidadesencajainteger, subunidadpesonuevoproducto.getText().toString(), cantidadunidaddouble, fechanuevoproducto, proveedornuevoproducto.getText().toString(), margendouble, preciocompradouble, Math.floor((((margendouble * preciocompradouble) / 100) + preciocompradouble) * 10000) / 10000, 0.0, 0.0);
                                                } catch (Exception e) {
                                                    Toast.makeText(getActivity(), "Error: Unidades en caja errónea", Toast.LENGTH_SHORT).show();
                                                    error = true;
                                                }

                                            }
                                        }

                                    }


                                }
                            }

                            if (!error) {
                                Boolean encontrado = false;
                                for (ProductoStock product : arrayproductos) {
                                    if (product.getNombre().equalsIgnoreCase(nombrenuevoproducto.getText().toString())) {
                                        encontrado = true;
                                    }
                                }
                                if (encontrado) {
                                    Toast.makeText(getActivity(), "El producto ya existe", Toast.LENGTH_SHORT).show();
                                } else {
                                    AlertDialog.Builder builder =
                                            new AlertDialog.Builder(getActivity());

                                    builder.setMessage("¿Confirmar?").setTitle("Confirmacion")
                                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog2, int id) {

                                                    ProductoStock nuevoproducto;
                                                    HashMap<String, Lote> mapa = new HashMap<>();
                                                    mapa.put("L1", nuevolote);
                                                    nuevoproducto = new ProductoStock(nombrenuevoproducto.getText().toString(), nuevolote.getCantidadTotal(), Integer.parseInt(ivanuevoproducto), mapa, preciocompradouble, preciocompradouble, unidadpesonuevoproducto);


                                                    dbstock.child(Keynuevoproducto).setValue(nuevoproducto);
                                                    Toast.makeText(getActivity(), "Producto creado", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                    dialog2.dismiss();


                                                }

                                            })
                                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog2, int id) {
                                                    dialog2.cancel();
                                                }
                                            }).show();
                                }
                            }
                        }
                    }
                });

                botoncancelarnuevoproducto.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
        return rootView;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuinflater) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.search_toolbar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView sv = (SearchView) item.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapterstock.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void almacenarproveedores(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (!proveedores.contains(ds.getKey() + "-" + ds.getValue(Proveedor.class).getRazon_Social())) {
                proveedores.add(ds.getKey() + "-" + ds.getValue(Proveedor.class).getRazon_Social());
            }
        }
    }

    private void almacenarproductosstock(DataSnapshot dataSnapshot) {
        arrayproductos.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            ProductoStock p = new ProductoStock();
            p.setNombre(ds.getValue(ProductoStock.class).getNombre());
            p.setIVA(ds.getValue(ProductoStock.class).getIVA());
            p.setLotes(ds.getValue(ProductoStock.class).getLotes());
            p.setPrecioMaximo(ds.getValue(ProductoStock.class).getPrecioMaximo());
            p.setPrecioMinimo(ds.getValue(ProductoStock.class).getPrecioMinimo());
            p.setCantidadTotal(ds.getValue(ProductoStock.class).getCantidadTotal());
            p.setUnidadPeso(ds.getValue(ProductoStock.class).getUnidadPeso());
            arrayproductos.add(p);
        }
        Collections.sort(arrayproductos, new Comparator<ProductoStock>() {
            @Override
            public int compare(ProductoStock o1, ProductoStock o2) {
                return o1.getNombre().compareToIgnoreCase(o2.getNombre());
            }
        });
    }

    private String nuevoIdProducto(DataSnapshot dataSnapshot) {

        Integer maximo = 0;
        Integer ultimo = 0;
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            ultimo = Integer.parseInt(ds.getKey().substring(1));
            if (ultimo > maximo) {
                maximo = ultimo;
            }

        }

        maximo++;
        String res = "S" + String.valueOf(maximo);
        return res;
    }

    private String claveproductoedit(DataSnapshot dataSnapshot, String nombre) {
        String res = "";
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (ds.getValue(ProductoStock.class).getNombre().equalsIgnoreCase(nombre)) {
                res = ds.getKey();
            }
        }
        return res;
    }


    private String[] lotesproductoedit(DataSnapshot dataSnapshot, String nombre) {
        String[] res = null;
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (ds.getValue(ProductoStock.class).getNombre().equalsIgnoreCase(nombre)) {
                res = new String[ds.getValue(ProductoStock.class).getLotes().size()];
                for (int i = 0; i < ds.getValue(ProductoStock.class).getLotes().size(); i++) {
                    res[i] = ("L" + (i + 1) + ": " + String.valueOf(ds.getValue(ProductoStock.class).getLotes().get("L" + (i + 1)).getFecha()) + "-" + String.valueOf(ds.getValue(ProductoStock.class).getLotes().get("L" + (i + 1)).getProveedor().substring(0, ds.getValue(ProductoStock.class).getLotes().get("L" + (i + 1)).getProveedor().indexOf("-"))));
                }
            }
        }
        return res;

    }

    //CONTROL BACK BUTTON
    public void onBackPressed() {

    }


    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }
}

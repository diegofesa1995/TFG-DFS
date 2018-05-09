package com.example.diego.myapplication.Activities;

import android.app.Fragment;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.diego.myapplication.AdapterCliente;
import com.example.diego.myapplication.Objects.Cliente;
import com.example.diego.myapplication.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class Clientes extends Fragment {

    private Menu menu;
    private EditText nombrecliente;
    private EditText razonsocial;
    private EditText nifcliente;
    private EditText emailcliente;
    private EditText telefonocliente;
    private EditText direccioncliente;
    private EditText codigopostalcliente;
    private EditText provinciacliente;
    private EditText poblacioncliente;

    private EditText edittextdireccionedit;
    private EditText edittextcodigopostaledit;
    private EditText edittextprovinciaedit;
    private EditText edittextpoblacionedit;

    private Cliente clientenuevo;
    private Button aceptarcliente;
    private Button cancelarcliente;
    private AlertDialog dialog;
    private AlertDialog dialogbotones;
    private DatabaseReference dbclientes;
    private String keynuevocliente;
    private String clavecliente;
    private AlertDialog dialog2;
    private AdapterCliente adapterCliente;
    private static ArrayList<Cliente> arrayclientes = new ArrayList<>();
    private String opciondireccion;
    private View rootView;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private ListView listviewclientes;
    private Cliente clienteseleccionado;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_clientes, container, false);


        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbarclientes);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Cargando datos..");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        dbclientes = FirebaseDatabase.getInstance().getReference("Clientes");
        listviewclientes = (ListView) rootView.findViewById(R.id.listviewclientes);

        dbclientes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    almacenarclientes(dataSnapshot);
                    adapterCliente = new AdapterCliente(getActivity(), arrayclientes);
                    adapterCliente.notifyDataSetChanged();
                    listviewclientes.setAdapter(adapterCliente);
                    progress.dismiss();
                } catch (Exception e) {
                    Log.d("CATCHHHH", "ETRAA");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listviewclientes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View vista = getActivity().getLayoutInflater().inflate(R.layout.opciones_clientes, null);
                builder.setView(vista);
                dialogbotones = builder.create();
                dialogbotones.show();

                Button btneditar = (Button) vista.findViewById(R.id.btneditarclienteproveedor);
                Button btneliminar = (Button) vista.findViewById(R.id.btndeleteclienteproveedor);

                clienteseleccionado = (Cliente) parent.getItemAtPosition(position);


                dbclientes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        clavecliente = clavecliente(dataSnapshot, clienteseleccionado.getNif());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                btneliminar.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder buildereliminar =
                                new AlertDialog.Builder(getActivity());

                        buildereliminar.setMessage("¿Confirma borra al cliente " + clienteseleccionado.getRazon_Social() + "?")
                                .setTitle("Confirmacion")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dbclientes.child(clavecliente).removeValue();
                                        adapterCliente.notifyDataSetChanged();

                                        Toast.makeText(getActivity(), "Cliente " + clavecliente + " borrado", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                        dialogbotones.cancel();

                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                });

                btneditar.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        final View view = getActivity().getLayoutInflater().inflate(R.layout.pop_edit_cliente_proveedor, null);
                        builder.setView(view);
                        dialog = builder.create();
                        dialog.show();

                        final EditText edittextnombreedit = (EditText) view.findViewById(R.id.nombreedit);
                        final EditText edittextrazonsocialedit = (EditText) view.findViewById(R.id.razonsocialedit);
                        final EditText edittextnifedit = (EditText) view.findViewById(R.id.nifedit);
                        final EditText edittextemailedit = (EditText) view.findViewById(R.id.emailedit);
                        final EditText edittexttelefonoedit = (EditText) view.findViewById(R.id.telefonoedit);
                        edittextdireccionedit = (EditText) view.findViewById(R.id.direccionedit);
                        edittextcodigopostaledit = (EditText) view.findViewById(R.id.codigopostaledit);
                        edittextprovinciaedit = (EditText) view.findViewById(R.id.provinciaedit);
                        edittextpoblacionedit = (EditText) view.findViewById(R.id.poblacionedit);
                        edittextnombreedit.setText(clienteseleccionado.getNombre());
                        edittextrazonsocialedit.setText(clienteseleccionado.getRazon_Social());
                        edittextnifedit.setText(clienteseleccionado.getNif());
                        edittextnifedit.setEnabled(false);
                        edittextemailedit.setText(clienteseleccionado.getEmail());
                        edittexttelefonoedit.setText(clienteseleccionado.getTelefono());

                        edittextdireccionedit.setText(clienteseleccionado.getDireccion());
                        edittextdireccionedit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    opciondireccion = "editar";
                                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                                } catch (GooglePlayServicesRepairableException e) {
                                    // TODO: Handle the error.
                                } catch (GooglePlayServicesNotAvailableException e) {
                                    // TODO: Handle the error.
                                }
                            }
                        });

                        edittextcodigopostaledit.setText(clienteseleccionado.getCP());
                        edittextcodigopostaledit.setEnabled(false);
                        edittextprovinciaedit.setText(clienteseleccionado.getProvincia());
                        edittextprovinciaedit.setEnabled(false);
                        edittextpoblacionedit.setText(clienteseleccionado.getPoblacion());
                        edittextpoblacionedit.setEnabled(false);


                        Button botonconfirmareditcliente = (Button) view.findViewById(R.id.botonaceptaredit);
                        botonconfirmareditcliente.setOnClickListener(new Button.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                final String nombreeditado = edittextnombreedit.getText().toString();
                                final String razonsocialeditado = edittextrazonsocialedit.getText().toString();
                                String nifeditado = edittextnifedit.getText().toString();
                                final String emaileditado = edittextemailedit.getText().toString();
                                final String telefonoeditado = edittexttelefonoedit.getText().toString();
                                final String direccioneditada = edittextdireccionedit.getText().toString();
                                final String codigopostaleditado = edittextcodigopostaledit.getText().toString();
                                final String provinciaeditada = edittextprovinciaedit.getText().toString();
                                final String poblacioneditada = edittextpoblacionedit.getText().toString();

                                if(nombreeditado.equalsIgnoreCase("") || razonsocialeditado.equalsIgnoreCase("") || emaileditado.equalsIgnoreCase("")
                                        || telefonoeditado.equalsIgnoreCase("") || direccioneditada.equalsIgnoreCase("")){
                                    Toast.makeText(getActivity(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                                }else{
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                                    final View view2 = getActivity().getLayoutInflater().inflate(R.layout.pop_info_cliente_proveedor, null);
                                    builder2.setView(view2);
                                    dialog2 = builder2.create();
                                    dialog2.show();

                                    TextView infonombre = (TextView) view2.findViewById(R.id.infonombre);
                                    TextView inforazonsocial = (TextView) view2.findViewById(R.id.inforazonsocial);
                                    TextView infonif = (TextView) view2.findViewById(R.id.infonif);
                                    TextView infoemail = (TextView) view2.findViewById(R.id.infoemail);
                                    TextView infotelefono = (TextView) view2.findViewById(R.id.infotelefono);
                                    TextView infodireccion = (TextView) view2.findViewById(R.id.infodireccion);
                                    infonombre.setText(nombreeditado);
                                    inforazonsocial.setText(razonsocialeditado);
                                    infonif.setText(nifeditado);
                                    infoemail.setText(emaileditado);
                                    infotelefono.setText(telefonoeditado);
                                    infodireccion.setText(direccioneditada + " " + poblacioneditada);

                                    Button btnconfirmardatos = (Button) view2.findViewById(R.id.botonconfirmarinfo);

                                    btnconfirmardatos.setOnClickListener(new Button.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {
                                            Cliente clienteditado = new Cliente(codigopostaleditado,nombreeditado,telefonoeditado,direccioneditada,clienteseleccionado.getFormalidad(),emaileditado,clienteseleccionado.getFecha_Alta(),clienteseleccionado.getNif(), razonsocialeditado,provinciaeditada,poblacioneditada);
                                            dbclientes.child(clavecliente).setValue(clienteditado);
                                            adapterCliente.notifyDataSetChanged();
                                            dialog2.dismiss();
                                            dialog.dismiss();
                                            dialogbotones.dismiss();
                                            Toast.makeText(getActivity(),"Cliente " + clavecliente + " editado", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                    Button btncancelardatos = (Button) view2.findViewById(R.id.botoncancelarinfo);
                                    btncancelardatos.setOnClickListener(new Button.OnClickListener(){
                                        @Override
                                        public void onClick(View v) {
                                            dialog2.dismiss();
                                        }
                                    });


                                }
                            }
                        });

                        Button botoncancelareditcliente = (Button) view.findViewById(R.id.botoncancelaredit);
                        botoncancelareditcliente.setOnClickListener(new Button.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                });

                return true;
            }
        });

        listviewclientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clienteseleccionado = (Cliente) parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View vista = getActivity().getLayoutInflater().inflate(R.layout.pop_cliente_proveedor_seleccionado, null);
                builder.setView(vista);
                dialog = builder.create();
                dialog.show();

                TextView razonsocialclienteseleccionado = (TextView) vista.findViewById(R.id.inforazonsocialseleccionado);
                TextView nombreclienteseleccionado = (TextView) vista.findViewById(R.id.nombreseleccionado);
                TextView nifclienteseleccionado = (TextView) vista.findViewById(R.id.infonifseleccionado);
                final TextView emailclienteseleccionado = (TextView) vista.findViewById(R.id.infoemailseleccionado);
                emailclienteseleccionado.setClickable(true);
                final TextView telefonoclienteseleccionado = (TextView) vista.findViewById(R.id.infotelefonoseleccionado);
                telefonoclienteseleccionado.setClickable(true);
                final TextView direccionclienteseleccioando = (TextView) vista.findViewById(R.id.infodireccionseleccionado);
                final TextView fechadealtaclienteseleccionado = (TextView) vista.findViewById(R.id.infofechadealta);
                direccionclienteseleccioando.setClickable(true);
                Button cerrarclienteseleccionado = (Button) vista.findViewById(R.id.botoncerrarinfo);
                razonsocialclienteseleccionado.setText(clienteseleccionado.getRazon_Social());
                nombreclienteseleccionado.setText(clienteseleccionado.getNombre());
                nifclienteseleccionado.setText(clienteseleccionado.getNif());
                emailclienteseleccionado.setText(clienteseleccionado.getEmail());
                telefonoclienteseleccionado.setText(clienteseleccionado.getTelefono());
                direccionclienteseleccioando.setText(clienteseleccionado.getDireccion() + " " + clienteseleccionado.getPoblacion());
                fechadealtaclienteseleccionado.setText(clienteseleccionado.getFecha_Alta());

                emailclienteseleccionado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enviarEmail(emailclienteseleccionado.getText().toString());
                    }
                });

                telefonoclienteseleccionado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llamartelefono(telefonoclienteseleccionado.getText().toString());
                    }
                });

                direccionclienteseleccioando.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        direccionmaps(direccionclienteseleccioando.getText().toString());
                    }
                });

                cerrarclienteseleccionado.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

//-------------------------------------------------------- VENTANA AÑADIR CLIENTE -----------------------------------------------------------------------------------------------
        FloatingActionButton anadircliente = (FloatingActionButton) rootView.findViewById(R.id.btnanadircliente);
        anadircliente.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                //CREAMOS LA INTERFAZ DE AÑADIR CLIENTE
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = getActivity().getLayoutInflater().inflate(R.layout.pop_anadir_cliente, null);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();

                //TOOLBAR
                Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbaranadircliente);
                toolbar.setTitle("Nuevo Cliente");

                //RECOJEMOS LOS CAMPOS DE LA INTERFAZ
                nombrecliente = (EditText) view.findViewById(R.id.textnombrecliente);
                razonsocial = (EditText) view.findViewById(R.id.textrazonsocial);
                nifcliente = (EditText) view.findViewById(R.id.textnifcliente);
                emailcliente = (EditText) view.findViewById(R.id.textemailcliente);
                telefonocliente = (EditText) view.findViewById(R.id.texttelefonocliente);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date date = new Date();
                final String fecha = dateFormat.format(date);

                direccioncliente = (EditText) view.findViewById(R.id.textdireccioncliente);
                direccioncliente.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            opciondireccion = "crear";
                            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                        } catch (GooglePlayServicesRepairableException e) {
                            // TODO: Handle the error.
                        } catch (GooglePlayServicesNotAvailableException e) {
                            // TODO: Handle the error.
                        }
                    }
                });

                codigopostalcliente = (EditText) view.findViewById(R.id.textcodigopostalcliente);
                provinciacliente = (EditText) view.findViewById(R.id.textprovinciacliente);
                poblacioncliente = (EditText) view.findViewById(R.id.textpoblacioncliente);

                //---------------------------------------------------------------------KEY DEL PRODUCTO AÑADIDO AL STOCK------------------------------------------------------------------------------------------------------

                dbclientes.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        keynuevocliente = nuevaKeyCliente(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                aceptarcliente = (Button) view.findViewById(R.id.botonaceptarcliente);
                aceptarcliente.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                        View view = getActivity().getLayoutInflater().inflate(R.layout.pop_info_cliente_proveedor, null);
                        builder2.setView(view);
                        dialog2 = builder2.create();
                        dialog2.show();
                        TextView infonombre = (TextView) view.findViewById(R.id.infonombre);
                        TextView inforazonsocial = (TextView) view.findViewById(R.id.inforazonsocial);
                        TextView infonif = (TextView) view.findViewById(R.id.infonif);
                        TextView infoemail = (TextView) view.findViewById(R.id.infoemail);
                        TextView infotelefono = (TextView) view.findViewById(R.id.infotelefono);
                        TextView infodireccion = (TextView) view.findViewById(R.id.infodireccion);
                        infonombre.setText(nombrecliente.getText().toString());
                        inforazonsocial.setText(razonsocial.getText().toString());
                        infonif.setText(nifcliente.getText().toString());
                        infoemail.setText(emailcliente.getText().toString());
                        infotelefono.setText(telefonocliente.getText().toString());
                        infodireccion.setText(direccioncliente.getText().toString() + " " + poblacioncliente.getText().toString());

                        Button confirmar = (Button) view.findViewById(R.id.botonconfirmarinfo);
                        confirmar.setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (nombrecliente.getText().toString().equals("") || (telefonocliente.getText().toString().equals("") && emailcliente.getText().toString().equals("")) || direccioncliente.getText().toString().equals("")
                                        || nifcliente.getText().toString().equals("")) {
                                    Toast.makeText(getActivity(), "Datos Imcompletos", Toast.LENGTH_SHORT).show();
                                    dialog2.dismiss();
                                } else {
                                    clientenuevo = new Cliente(codigopostalcliente.getText().toString(), nombrecliente.getText().toString(), telefonocliente.getText().toString(), direccioncliente.getText().toString()
                                            , "Buena", emailcliente.getText().toString(), fecha, nifcliente.getText().toString()
                                            , razonsocial.getText().toString(), provinciacliente.getText().toString(), poblacioncliente.getText().toString());
                                    dbclientes.child(keynuevocliente).setValue(clientenuevo);
                                    adapterCliente.notifyDataSetChanged();
                                    dialog2.dismiss();
                                    dialog.dismiss();

                                    Toast.makeText(getActivity(), "Cliente añadido", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        Button cancelar = (Button) view.findViewById(R.id.botoncancelarinfo);
                        cancelar.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                            }
                        });
                    }
                });

                cancelarcliente = (Button) view.findViewById(R.id.botoncancelarcliente);
                cancelarcliente.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
        return rootView;
    }

    //CONTROL BACK BUTTON
   /* public void onBackPressed() {
        Intent i = new Intent(Clientes.this, MainActivity.class);
        startActivity(i);
    }
*/
    private void almacenarclientes(DataSnapshot dataSnapshot) {
        arrayclientes.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Cliente p = new Cliente();
            p.setNombre(ds.getValue(Cliente.class).getNombre());
            p.setEmail(ds.getValue(Cliente.class).getEmail());
            p.setTelefono(ds.getValue(Cliente.class).getTelefono());
            p.setRazon_Social(ds.getValue(Cliente.class).getRazon_Social());
            p.setNIF(ds.getValue(Cliente.class).getNif());
            p.setCP(ds.getValue(Cliente.class).getCP());
            p.setDireccion(ds.getValue(Cliente.class).getDireccion());
            p.setEmail(ds.getValue(Cliente.class).getEmail());
            p.setFecha_Alta(ds.getValue(Cliente.class).getFecha_Alta());
            p.setProvincia(ds.getValue(Cliente.class).getProvincia());
            p.setPoblacion(ds.getValue(Cliente.class).getPoblacion());
            p.setFormalidad(ds.getValue(Cliente.class).getFormalidad());
            arrayclientes.add(p);
        }
        Collections.sort(arrayclientes, new Comparator<Cliente>() {
            @Override
            public int compare(Cliente o1, Cliente o2) {
                return o1.getRazon_Social().compareToIgnoreCase(o2.getRazon_Social());
            }
        });
    }


    private String clavecliente(DataSnapshot ds, String nif) {
        String res = "";
        for (DataSnapshot data : ds.getChildren()) {
            if (data.getValue(Cliente.class).getNif().equalsIgnoreCase(nif)) {
                res = data.getKey();
            }
        }
        return res;

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuinflater) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.search_toolbar, menu);
        this.menu = menu;
        MenuItem itemsearch = menu.findItem(R.id.action_search);

        SearchView sv = (SearchView) itemsearch.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterCliente.getFilter().filter(newText);
                return false;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);

                String[] aux = String.valueOf(place.getAddress()).split(",");
                List<String> aux2 = Arrays.asList(aux);
                String auxcodigopostal = "";
                String auxpoblacion = "";
                String auxprovincia = "";

                if (aux2.size() < 4) {
                    Toast.makeText(getActivity(), "Introduzca calle y numero", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Integer numero = Integer.parseInt(String.valueOf(aux2.get(1).substring(1)));
                        auxcodigopostal = aux2.get(2).substring(1, 6);
                        if (aux2.size() == 4) {
                            auxpoblacion = aux2.get(2).substring(7);
                            auxprovincia = aux2.get(2).substring(7);
                        } else if (aux2.size() == 5) {
                            auxpoblacion = aux2.get(2).substring(7);
                            auxprovincia = aux2.get(3);
                        }
                        if (opciondireccion.equalsIgnoreCase("crear")) {
                            direccioncliente.setText(aux2.get(0) + " " + String.valueOf(numero));
                            codigopostalcliente.setText(auxcodigopostal);
                            poblacioncliente.setText(auxpoblacion);
                            provinciacliente.setText(auxprovincia);
                        } else {
                            edittextdireccionedit.setText(aux2.get(0) + " " + String.valueOf(numero));
                            edittextcodigopostaledit.setText(auxcodigopostal);
                            edittextpoblacionedit.setText(auxpoblacion);
                            edittextprovinciaedit.setText(auxprovincia);
                        }


                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Introduzca calle y numero", Toast.LENGTH_SHORT).show();
                    }
                }


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private String nuevaKeyCliente(DataSnapshot dataSnapshot) {
        Integer maximo = 0;
        Integer key = 0;
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            key = Integer.parseInt(ds.getKey().substring(1));
            if (key > maximo) {
                maximo = key;
            }
        }
        maximo++;
        return "C" + maximo;

    }

    private void enviarEmail(String correo) {
        String[] to = {correo};
        String[] cc = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Enviar email..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(),
                    "No tienes clientes de email instalados.", Toast.LENGTH_SHORT).show();
        }
    }

    private void llamartelefono(String telefono) {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + telefono));
        try {
            startActivity(i);

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(),
                    "No tienes la aplicación telefono instalada", Toast.LENGTH_SHORT).show();
        }

    }

    private void direccionmaps(String direccion) {
        Intent intentMap = new Intent(Intent.ACTION_VIEW);
        Uri intentUri = Uri.parse("google.navigation:q="+direccion);
        intentMap.setData(intentUri);
        try {
            startActivity(intentMap);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(),
                    "no tienes maps", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}

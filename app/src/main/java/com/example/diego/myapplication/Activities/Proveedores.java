package com.example.diego.myapplication.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.diego.myapplication.AdapterProveedor;
import com.example.diego.myapplication.Objects.Proveedor;
import com.example.diego.myapplication.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.app.Fragment;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
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

public class Proveedores extends Fragment {

    private DatabaseReference dbproveedores;
    private DatabaseReference dbalbaranes;
    private ListView listaproveedores;
    private AdapterProveedor adapterproveedor;
    private static ArrayList<Proveedor> arrayproveedores = new ArrayList<>();
    private View rootView;
    private FloatingActionButton btnanadirproveedor;
    private AlertDialog dialog;
    private AlertDialog dialog2;
    private AlertDialog dialogbotones;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private EditText nombre;
    private EditText razonsocial;
    private EditText nif;
    private EditText email;
    private EditText telefono;
    private EditText direccion;
    private EditText codigopostal;
    private EditText provincia;
    private EditText poblacion;
    private String keynuevoproveedor;
    private Button aceptarproveedor;
    private Button cancelarproveedor;
    private String claveproveedorseleccionado;
    private Proveedor proveedorseleccionado;

    private EditText edittextdireccionedit;
    private EditText edittextcodigopostaledit;
    private EditText edittextprovinciaedit;
    private EditText edittextpoblacionedit;

    private String opciondireccion;
    private String clavenuevoalbaran;
    private Uri urialbarannuevo;

    private static final int GALLERY_INTENT = 5;

    private FirebaseStorage storage;
    private StorageReference storageref;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_proveedores, container, false);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargando datos..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbarproveedores);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        storage = FirebaseStorage.getInstance();
        storageref = storage.getReferenceFromUrl("gs://tfgr-f044d.appspot.com");
        dbalbaranes = FirebaseDatabase.getInstance().getReference("AlbaranesProveedores");

//------------------------------------LISTVIEW DE LOS PROVEEDORES DE LA BASE DE DATOS-------------------------------------------------------------------*    dbstock = FirebaseDatabase.getInstance().getReference().child("Stock");

        dbproveedores = FirebaseDatabase.getInstance().getReference("Proveedores");
        listaproveedores = (ListView) rootView.findViewById(R.id.listviewproveedores);


        dbproveedores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    almacenarproveedores(dataSnapshot);
                    adapterproveedor = new AdapterProveedor(getActivity(), arrayproveedores);
                    adapterproveedor.notifyDataSetChanged();
                    listaproveedores.setAdapter(adapterproveedor);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    Log.d("CATCH", "CATCH");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listaproveedores.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                proveedorseleccionado = (Proveedor) parent.getItemAtPosition(position);

                dbproveedores.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        claveproveedorseleccionado = claveproveedorseleccionado(dataSnapshot, proveedorseleccionado.getNIF());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View vista = getActivity().getLayoutInflater().inflate(R.layout.opciones_proveedores, null);
                builder.setView(vista);
                dialogbotones = builder.create();
                dialogbotones.show();

                Button btnveralbaranes = (Button) vista.findViewById(R.id.btnveralbaranesproveedores);
                Button btnanadiralbaran = (Button) vista.findViewById(R.id.btnanadiralbaranproveedor);
                Button btneditar = (Button) vista.findViewById(R.id.btneditarclienteproveedor);
                Button btneliminar = (Button) vista.findViewById(R.id.btndeleteclienteproveedor);

                btnveralbaranes.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), activity_albaranes_proveedor.class);
                        i.putExtra("claveproveedor", claveproveedorseleccionado);
                        startActivity(i);
                    }
                });

                btnanadiralbaran.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, GALLERY_INTENT);
                    }
                });

                btneliminar.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder buildereliminar =
                                new AlertDialog.Builder(getActivity());

                        buildereliminar.setMessage("¿Confirma borra al cliente " + proveedorseleccionado.getRazon_Social() + "?")
                                .setTitle("Confirmacion")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        dbproveedores.child(claveproveedorseleccionado).removeValue();
                                        adapterproveedor.notifyDataSetChanged();

                                        Toast.makeText(getActivity(), "Cliente " + claveproveedorseleccionado + " borrado", Toast.LENGTH_SHORT).show();
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

                btneditar.setOnClickListener(new Button.OnClickListener() {

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
                        edittextnombreedit.setText(proveedorseleccionado.getNombre());
                        edittextrazonsocialedit.setText(proveedorseleccionado.getRazon_Social());
                        edittextnifedit.setText(proveedorseleccionado.getNIF());
                        edittextnifedit.setEnabled(false);
                        edittextemailedit.setText(proveedorseleccionado.getEmail());
                        edittexttelefonoedit.setText(String.valueOf(proveedorseleccionado.getTelefono()));

                        edittextdireccionedit.setText(proveedorseleccionado.getDireccion());
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

                        edittextcodigopostaledit.setText(String.valueOf(proveedorseleccionado.getCP()));
                        edittextcodigopostaledit.setEnabled(false);
                        edittextprovinciaedit.setText(proveedorseleccionado.getProvincia());
                        edittextprovinciaedit.setEnabled(false);
                        edittextpoblacionedit.setText(proveedorseleccionado.getPoblacion());
                        edittextpoblacionedit.setEnabled(false);


                        Button botonconfirmaredit = (Button) view.findViewById(R.id.botonaceptaredit);
                        botonconfirmaredit.setOnClickListener(new Button.OnClickListener() {

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

                                if (nombreeditado.equalsIgnoreCase("") || razonsocialeditado.equalsIgnoreCase("") || emaileditado.equalsIgnoreCase("")
                                        || telefonoeditado.equalsIgnoreCase("") || direccioneditada.equalsIgnoreCase("")) {
                                    Toast.makeText(getActivity(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                                } else {
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

                                    btnconfirmardatos.setOnClickListener(new Button.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Proveedor proveedoreditado = new Proveedor(codigopostaleditado, nombreeditado, telefonoeditado, direccioneditada, emaileditado, proveedorseleccionado.getFecha_Alta(), proveedorseleccionado.getNIF(), razonsocialeditado, provinciaeditada, poblacioneditada);
                                            dbproveedores.child(claveproveedorseleccionado).setValue(proveedoreditado);
                                            adapterproveedor.notifyDataSetChanged();
                                            dialog2.dismiss();
                                            dialog.dismiss();
                                            dialogbotones.dismiss();
                                            Toast.makeText(getActivity(), "Proveedor " + claveproveedorseleccionado + " editado", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                    Button btncancelardatos = (Button) view2.findViewById(R.id.botoncancelarinfo);
                                    btncancelardatos.setOnClickListener(new Button.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog2.dismiss();
                                        }
                                    });


                                }
                            }
                        });


                        Button botoncancelareditproveedor = (Button) view.findViewById(R.id.botoncancelaredit);
                        botoncancelareditproveedor.setOnClickListener(new Button.OnClickListener() {
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


        listaproveedores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Proveedor proveedorseleccionado = (Proveedor) parent.getItemAtPosition(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final View vista = getActivity().getLayoutInflater().inflate(R.layout.pop_cliente_proveedor_seleccionado, null);
                builder.setView(vista);
                dialog = builder.create();
                dialog.show();

                TextView razonsocialseleccionado = (TextView) vista.findViewById(R.id.inforazonsocialseleccionado);
                TextView nombreseleccionado = (TextView) vista.findViewById(R.id.nombreseleccionado);
                TextView nifseleccionado = (TextView) vista.findViewById(R.id.infonifseleccionado);
                final TextView emailseleccionado = (TextView) vista.findViewById(R.id.infoemailseleccionado);
                emailseleccionado.setClickable(true);
                final TextView telefonoseleccionado = (TextView) vista.findViewById(R.id.infotelefonoseleccionado);
                telefonoseleccionado.setClickable(true);
                final TextView direccionseleccioando = (TextView) vista.findViewById(R.id.infodireccionseleccionado);
                final TextView fechadealtaseleccioando = (TextView) vista.findViewById(R.id.infofechadealta);
                direccionseleccioando.setClickable(true);
                Button cerrarseleccionado = (Button) vista.findViewById(R.id.botoncerrarinfo);
                razonsocialseleccionado.setText(proveedorseleccionado.getRazon_Social());
                nombreseleccionado.setText(proveedorseleccionado.getNombre());
                nifseleccionado.setText(proveedorseleccionado.getNIF());
                emailseleccionado.setText(proveedorseleccionado.getEmail());
                telefonoseleccionado.setText(proveedorseleccionado.getTelefono());
                direccionseleccioando.setText(proveedorseleccionado.getDireccion() + " " + proveedorseleccionado.getPoblacion());
                fechadealtaseleccioando.setText(proveedorseleccionado.getFecha_Alta());
                emailseleccionado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enviarEmail(emailseleccionado.getText().toString());
                    }
                });

                telefonoseleccionado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        llamartelefono(telefonoseleccionado.getText().toString());
                    }
                });

                direccionseleccioando.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        direccionmaps(direccionseleccioando.getText().toString());
                    }
                });

                cerrarseleccionado.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });


        btnanadirproveedor = (FloatingActionButton) rootView.findViewById(R.id.btnanadirproveedor);
        btnanadirproveedor.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = getActivity().getLayoutInflater().inflate(R.layout.pop_anadir_proveedor, null);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();

                //TOOLBAR
                Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbaranadirproveedor);
                toolbar.setTitle("Nuevo Proveedor");

                //RECOJEMOS LOS CAMPOS DE LA INTERFAZ
                nombre = (EditText) view.findViewById(R.id.nombrenuevoproveedor);
                razonsocial = (EditText) view.findViewById(R.id.razonsocialnuevoproveedor);
                nif = (EditText) view.findViewById(R.id.nifnuevoproveedor);
                email = (EditText) view.findViewById(R.id.emailnuevoproveedor);
                telefono = (EditText) view.findViewById(R.id.telefononuevoproveedor);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                Date date = new Date();
                final String fechaalta = dateFormat.format(date);

                direccion = (EditText) view.findViewById(R.id.direccionnuevoproveedor);
                direccion.setOnClickListener(new View.OnClickListener() {
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

                codigopostal = (EditText) view.findViewById(R.id.codigopostalnuevoproveedor);
                provincia = (EditText) view.findViewById(R.id.provincianuevoproveedor);
                poblacion = (EditText) view.findViewById(R.id.poblacionnuevoproveedor);

                //---------------------------------------------------------------------KEY DEL PRODUCTO AÑADIDO AL STOCK------------------------------------------------------------------------------------------------------

                dbproveedores.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        keynuevoproveedor = nuevaKeyProveedor(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                aceptarproveedor = (Button) view.findViewById(R.id.btnaceptarproveedor);
                aceptarproveedor.setOnClickListener(new Button.OnClickListener() {
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
                        infonombre.setText(nombre.getText().toString());
                        inforazonsocial.setText(razonsocial.getText().toString());
                        infonif.setText(nif.getText().toString());
                        infoemail.setText(email.getText().toString());
                        infotelefono.setText(telefono.getText().toString());
                        infodireccion.setText(direccion.getText().toString() + " " + poblacion.getText().toString());

                        Button confirmar = (Button) view.findViewById(R.id.botonconfirmarinfo);
                        confirmar.setOnClickListener(new Button.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (nombre.getText().toString().equals("") || (telefono.getText().toString().equals("") && email.getText().toString().equals("")) || direccion.getText().toString().equals("")
                                        || nif.getText().toString().equals("")) {
                                    Toast.makeText(getActivity(), "Datos Imcompletos", Toast.LENGTH_SHORT).show();
                                    dialog2.dismiss();
                                } else {
                                    Proveedor p = new Proveedor(codigopostal.getText().toString(), nombre.getText().toString(), telefono.getText().toString(), direccion.getText().toString()
                                            , email.getText().toString(), fechaalta, nif.getText().toString()
                                            , razonsocial.getText().toString(), provincia.getText().toString(), poblacion.getText().toString());
                                    dbproveedores.child(keynuevoproveedor).setValue(p);
                                    adapterproveedor.notifyDataSetChanged();
                                    dialog2.dismiss();
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Proveedor añadido", Toast.LENGTH_SHORT).show();
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

                cancelarproveedor = (Button) view.findViewById(R.id.btncancelarproveedor);
                cancelarproveedor.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });


        return rootView;
    }

    private void almacenarproveedores(DataSnapshot dataSnapshot) {
        arrayproveedores.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Proveedor p = new Proveedor();
            p.setNombre(ds.getValue(Proveedor.class).getNombre());
            p.setEmail(ds.getValue(Proveedor.class).getEmail());
            p.setTelefono(ds.getValue(Proveedor.class).getTelefono());
            p.setRazon_Social(ds.getValue(Proveedor.class).getRazon_Social());
            p.setCP(ds.getValue(Proveedor.class).getCP());
            p.setDireccion(ds.getValue(Proveedor.class).getDireccion());
            p.setEmail(ds.getValue(Proveedor.class).getEmail());
            p.setFecha_Alta(ds.getValue(Proveedor.class).getFecha_Alta());
            p.setProvincia(ds.getValue(Proveedor.class).getProvincia());
            p.setPoblacion(ds.getValue(Proveedor.class).getPoblacion());
            p.setNIF(ds.getValue(Proveedor.class).getNIF());
            arrayproveedores.add(p);
        }
        Collections.sort(arrayproveedores, new Comparator<Proveedor>() {
            @Override
            public int compare(Proveedor o1, Proveedor o2) {
                return o1.getRazon_Social().compareToIgnoreCase(o2.getRazon_Social());
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ON ACTIVITY RESULT", String.valueOf(requestCode));
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
                            direccion.setText(aux2.get(0) + " " + String.valueOf(numero));
                            codigopostal.setText(auxcodigopostal);
                            poblacion.setText(auxpoblacion);
                            provincia.setText(auxprovincia);
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
        }else if (requestCode == GALLERY_INTENT) {
            if(resultCode == RESULT_OK) {
                urialbarannuevo = data.getData();
                dbalbaranes.child(claveproveedorseleccionado).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        clavenuevoalbaran = clavenuevafoto(dataSnapshot);
                        dbalbaranes.child(claveproveedorseleccionado).child(clavenuevoalbaran).setValue(clavenuevoalbaran);
                        StorageReference imagesRef = storageref.child("albaranes").child(claveproveedorseleccionado).child(clavenuevoalbaran);
                        UploadTask ut = imagesRef.putFile(urialbarannuevo);

                        ut.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getActivity(), "Imagen subida", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Imagen fallada", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                dialogbotones.dismiss();
            }
        }


    }

    public String clavenuevafoto(DataSnapshot data){
        int maximo = 0;
        for(DataSnapshot ds:data.getChildren()){
            if(Integer.parseInt(ds.getKey().substring(5))>maximo){
                maximo = Integer.parseInt(ds.getKey().substring(5));
            }
        }
        maximo++;
        return "image"+maximo;
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

                adapterproveedor.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private String claveproveedorseleccionado(DataSnapshot ds, String nif) {
        String res = "";
        for (DataSnapshot data : ds.getChildren()) {
            if (data.getValue(Proveedor.class).getNIF().equalsIgnoreCase(nif)) {
                res = data.getKey();
            }
        }
        return res;

    }

    private String nuevaKeyProveedor(DataSnapshot dataSnapshot) {
        Integer maximo = 0;
        Integer ultimo = 0;
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            ultimo = Integer.parseInt(ds.getKey().substring(1));
            if (ultimo > maximo) {
                maximo = ultimo;
            }

        }
        maximo++;
        String res = "P" + String.valueOf(maximo);
        return res;
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
        Uri intentUri = Uri.parse("google.navigation:q=" + direccion);
        intentMap.setData(intentUri);
        try {
            startActivity(intentMap);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(),
                    "no tienes maps", Toast.LENGTH_SHORT).show();
        }
    }

    //CONTROL BACK BUTTON
    /*public void onBackPressed() {
        Intent i = new Intent(Proveedores.this, MainActivity.class);
        startActivity(i);
    }*/
}
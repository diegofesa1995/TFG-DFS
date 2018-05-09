package com.example.diego.myapplication.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.diego.myapplication.R;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {
    int backpress = 0;
    private TextView cuentalogueada;
    private ImageButton botonpedidos;
    private ImageButton botonclientes;
    private ImageButton botonproveedores;
    private ImageButton botonalmacen;
    private Fragment ventana = null;
    private Fragment aux = null;
    public Boolean pedidoCompleto = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarmain);


        BottomNavigationView barrabotones = (BottomNavigationView) findViewById(R.id.barrabotones);
        ventana = new Pedidos();
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,ventana).commit();
        barrabotones.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.botonpedidosbar:
                     ventana = new Pedidos();
                        break;
                    case R.id.botonclientesbar:
                        ventana = new Clientes();
                        break;
                    case R.id.botonproveedoresbar:
                        ventana = new Proveedores();
                        break;
                    case R.id.botonstockbar:
                        ventana = new Stock();
                        break;
                    case R.id.botonanadirpedidobar:
                        if(pedidoCompleto){
                            pedidoCompleto = false;
                            ventana = new AnadirPedido();
                            aux = ventana;
                        }else{
                            ventana = aux;
                        }

                        break;

                }

                getFragmentManager().beginTransaction().replace(R.id.fragmentContainer,ventana).commit();
                return true;
            }

        });

    }

    //CONTROL BACK BUTTON
    public void onBackPressed() {
      /*  if (backpress == 1) {

            FirebaseAuth.getInstance().signOut();
            finish();
            Intent i = new Intent(this, Registro.class);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "Pulsa otra vez para salir", Toast.LENGTH_SHORT).show();
            backpress++;

        }*/

    }
}

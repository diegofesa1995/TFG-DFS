package com.example.diego.myapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diego.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registro extends Activity {

    private EditText usuario;
    private EditText contrasenia;
    private Button botonlogin;
    private FirebaseAuth Authlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        usuario = (EditText) findViewById(R.id.usuario);
        contrasenia = (EditText) findViewById(R.id.contrasenia);
        botonlogin = (Button) findViewById(R.id.botonlogin);
        Authlogin = FirebaseAuth.getInstance();



        botonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                botonlogin.setEnabled(false);
                usuario.setEnabled(false);
                contrasenia.setEnabled(false);
                final String email = usuario.getText().toString();
                String pass = contrasenia.getText().toString();


                if (email.equals("") || pass.equals("")) {
                    Toast.makeText(Registro.this, "Campos vacíos", Toast.LENGTH_SHORT).show();
                    botonlogin.setEnabled(true);
                    usuario.setEnabled(true);
                    contrasenia.setEnabled(true);
                } else {
                    Authlogin.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Registro.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                                botonlogin.setEnabled(true);
                                usuario.setEnabled(true);
                                contrasenia.setEnabled(true);

                            } else {
                                Intent i = new Intent(v.getContext(), MainActivity.class);
                                startActivity(i);
                            }

                        }
                    });
                }
            }
        });

//OCULTAR EL TECLADO CUANDO SE PULSA FUERA
        findViewById(R.id.vistalogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
            }
        });

    }
    //OCULTAR EL TECLADO CUANDO SE PULSA FUERA
    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}

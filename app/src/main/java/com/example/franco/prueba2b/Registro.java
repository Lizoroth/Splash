package com.example.franco.prueba2b;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class Registro extends AppCompatActivity {

    Button btnG;
    EditText txtNombre,txtApellido,txtEmail,txtPassword,txtRepPass;


    //************activa la funcionalidad para regresar*****************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //*******************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        btnG =  findViewById(R.id.btnRegistro);
        txtNombre = findViewById(R.id.txtNombre);
        txtApellido = findViewById(R.id.txtApellido);
        txtEmail = findViewById(R.id.txtCorreo);
        txtPassword = findViewById(R.id.txtPass);
        txtRepPass = findViewById(R.id.txtRepitePass);


        btnG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( validaNombre()){
                    if (validaApellido()) {
                        if (validaCorreo()){
                            if (validaContrasena()){
                                if (validaExistenciaUsuario(txtEmail.getText().toString())){
                                    Toast.makeText(Registro.this,"Correo ya esta registrado", Toast.LENGTH_SHORT).show();
                                }else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Registro.this);
                                    builder.setMessage("¿Esta seguro de registrar el usuario?");
                                    builder.setTitle("Registro");
                                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            guarda(txtNombre.getText().toString().toUpperCase(), txtApellido.getText().toString().toUpperCase(), txtEmail.getText().toString().toLowerCase(), txtPassword.getText().toString());
                                            limpiaCampos();
                                            Toast.makeText(Registro.this, "Registro Éxitoso", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    AlertDialog dialog=builder.create();
                                    dialog.show();
                                }
                            }
                        }
                    }
                }

            }
        });

        //*******visualiza la flecha para volver*********************
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //***********************************************************

    }
    private boolean validaNombre() {
        boolean flg = false;
        txtNombre.setError(null);
        if (TextUtils.isEmpty(txtNombre.getText().toString())) {
            txtNombre.setError("Campo Obligatorio");
            txtNombre.requestFocus();

        } else {
            if (validaNumero(txtNombre.getText().toString())) {
                txtNombre.setError(null);
                txtNombre.setError("Hay números en el nombre");
                txtNombre.requestFocus();

            } else {


                flg = true;

            }

        }
        return flg;
    }

    private boolean validaNumero(String txt){
        boolean flg=false;
        String patronNumero = "[0-9]";
        Pattern pat = Pattern.compile(patronNumero);
        Matcher mat = pat.matcher(txt);
        if (mat.find()) {
            flg = true;
        }
        return flg;
    }

    private boolean validaApellido(){
        boolean flg=false;
        txtApellido.setError(null);
        if (TextUtils.isEmpty(txtApellido.getText().toString())){
            txtApellido.setError("Campo Obligatorio");
            txtApellido.requestFocus();
        } else {

            if (validaNumero(txtApellido.getText().toString())) {
                txtApellido.setError(null);
                txtApellido.setError("Hay numeros en el apellido");
                txtApellido.requestFocus();
                // no puede haber numero
            } else {


                flg = true;

            }
        }
        return flg;
    }

    private boolean validaCorreo() {
        boolean flg=false;
        txtEmail.setError(null);
        if (TextUtils.isEmpty(txtEmail.getText().toString())){
            txtEmail.setError("Campo Obligatorio");
            txtEmail.requestFocus();
        } else {

            String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            Pattern pattern = Pattern.compile(emailPattern);
            Matcher matcher = pattern.matcher(txtEmail.getText().toString());
            flg = matcher.matches();
            if (flg==false){
                txtEmail.setError(null);
                txtEmail.setError("Email invalido");
                txtEmail.requestFocus();

            }
        }


        return flg;
    }

    private boolean validaContrasena() {
        boolean flg=false;

        if (!(TextUtils.isEmpty(txtPassword.getText().toString()))){

            if (txtPassword.getText().toString().length()<6 || txtPassword.getText().toString().length()>15){
                Crouton.makeText(Registro.this, "Contraseña debe tener entre 6 y 15 caracteres",Style.INFO).show();
            } else {

                if (!(TextUtils.isEmpty(txtRepPass.getText().toString()))){
                    if (txtRepPass.getText().toString().length()<6 || txtRepPass.getText().toString().length()>15){
                        Crouton.makeText(Registro.this, "Repetir Contraseña debe tener entre 6 y 15 caracteres",Style.INFO).show();
                    } else {
                        if (txtPassword.getText().toString().equals(txtRepPass.getText().toString())){
                            System.out.println("formulario ready");
                            flg=true;
                        } else {
                            Crouton.makeText(Registro.this, "Contraseñas deben ser iguales",Style.INFO).show();
                        }
                    }
                } else {
                    Crouton.makeText(Registro.this, "Repetir Contraseña no puede estar vacio",Style.INFO).show();
                }

            }

        } else {
            Crouton.makeText(Registro.this, "Contraseña no puede estar vacio",Style.INFO).show();
        }


        return flg;
    }

    public void guarda(String n, String a, String co, String p){
        ConexionBD conn = new ConexionBD(this,Tabla.nombre_bd,null,1);
        SQLiteDatabase db = conn.getWritableDatabase();

        ContentValues c= new ContentValues();
        c.put(Tabla.campo_nombre_persona,n.toUpperCase());
        c.put(Tabla.campo_apellido_persona,a.toUpperCase());
        c.put(Tabla.campo_email_persona,co.toLowerCase());
        c.put(Tabla.campo_pass_persona,p);
        db.insert(Tabla.tabla_persona,null,c);
        db.close();

    }

    public boolean validaExistenciaUsuario(String mail){
        boolean flg=false;
        mail=mail.toLowerCase();
        ConexionBD conn= new ConexionBD(this,Tabla.nombre_bd,null,1);
        SQLiteDatabase db = conn.getReadableDatabase();
        String sql = "SELECT "+
                Tabla.campo_email_persona+" FROM "+
                Tabla.tabla_persona+" WHERE "+
                Tabla.campo_email_persona+" = '"+
                mail+"'";
        Cursor c=db.rawQuery(sql,null);
        if (c.moveToFirst()){
            do {
                flg=true;
            } while (c.moveToNext());
        }
        db.close();
        return flg;
    }

    public void Ingresa(){
        Intent inte = new Intent();
        inte.setClass(Registro.this,Principal.class);
        startActivity(inte);
    }

    public void limpiaCampos(){
        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtRepPass.setText("");;
    }
}

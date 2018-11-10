package com.example.franco.prueba2b;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class Principal extends AppCompatActivity {
    Button btnInicio;
    EditText txtCorreo,txtPassword;
    String password;
    static String nAdmin="admin",aAdmin="admin",coAdmin="111@111.cl",pAdmin="111111";
    SessionManager sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        btnInicio = findViewById(R.id.btnIngresar);
        txtCorreo = findViewById(R.id.editText);
        txtPassword = findViewById(R.id.editText2);
        if (!(validaExistenciaUsuario(coAdmin))){
            creaAdmin();
        }



        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaCorreo()){
                    if (validaContrasena()){
                        if (validaUsuario(txtCorreo.getText().toString(),txtPassword.getText().toString())){
                                limpiaCampos();
                                Intent inte = new Intent();
                                inte.setClass(Principal.this, Menu.class);
                                startActivity(inte);
                        } else {
                            Toast.makeText(Principal.this,"Usuario No válido",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }

    public boolean validaUsuario(String co,String pass){
        boolean flg=false;
        co=co.toLowerCase();
        ConexionBD conn= new ConexionBD(this,Tabla.nombre_bd,null,1);
        SQLiteDatabase db = conn.getReadableDatabase();
        String sql = "SELECT "+
                Tabla.campo_pass_persona+" FROM "+
                Tabla.tabla_persona+" WHERE "+
                Tabla.campo_email_persona+" = '"+
                co+"' AND "+
                Tabla.campo_pass_persona+" = '"
                +pass+"'";
        Cursor c=db.rawQuery(sql,null);
        if (c.moveToFirst()){
            do {
                password=c.getString(0);
            } while (c.moveToNext());
        } else {
            System.out.println("Bd sin información");
        }
        db.close();
        if (pass.equals(password)){
            sesion = new SessionManager(this);
            sesion.createSession(txtCorreo.getText().toString());
            flg=true;
        }
        return flg;
    }


    private boolean validaCorreo() {
        boolean flg=false;
        txtCorreo.setError(null);
        if (TextUtils.isEmpty(txtCorreo.getText().toString())){
            txtCorreo.setError("Campo Obligatorio");
            txtCorreo.requestFocus();
        } else {

            String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            Pattern pattern = Pattern.compile(emailPattern);
            Matcher matcher = pattern.matcher(txtCorreo.getText().toString());
            flg = matcher.matches();
            if (flg==false){
                txtCorreo.setError(null);
                txtCorreo.setError("Email invalido");
                txtCorreo.requestFocus();
            }
        }

        return flg;
    }

    private boolean validaContrasena() {
        boolean flg=false;

        if (!(TextUtils.isEmpty(txtPassword.getText().toString()))){

            if (txtPassword.getText().toString().length()<6 || txtPassword.getText().toString().length()>15){
                Crouton.makeText(Principal.this, "Contraseña debe tener entre 6 y 15 caracteres",Style.INFO).show();
            } else {
                flg=true;
            }

        } else {
            Crouton.makeText(Principal.this, "Contraseña no puede estar vacio",Style.INFO).show();
        }
        return flg;
    }

    private void creaAdmin (){
            ConexionBD conn = new ConexionBD(this,Tabla.nombre_bd,null,1);
            SQLiteDatabase db = conn.getWritableDatabase();

            ContentValues c= new ContentValues();
            c.put(Tabla.campo_nombre_persona,nAdmin.toUpperCase());
            c.put(Tabla.campo_apellido_persona,aAdmin.toUpperCase());
            c.put(Tabla.campo_email_persona,coAdmin.toLowerCase());
            c.put(Tabla.campo_pass_persona,pAdmin);
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


    public void limpiaCampos(){
        txtCorreo.setText("");
        txtPassword.setText("");

    }


}

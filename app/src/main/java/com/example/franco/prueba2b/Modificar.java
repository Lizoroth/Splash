package com.example.franco.prueba2b;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Modificar extends AppCompatActivity {
    EditText txtNombre,txtApellido,txtCorreo;
    int codigo,tot;
    String nombre,apellido,correo;
    Button btnModifica,btnElimina;


    //************activa la funcionalidad para regresar*****************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    //*******************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);

        txtNombre=findViewById(R.id.txtmNombre);
        txtApellido=findViewById(R.id.txtMApellido);
        txtCorreo=findViewById(R.id.txtMemail);
        btnElimina=findViewById(R.id.btnMElimina);
        btnModifica=findViewById(R.id.btnMmodifica);

        codigo   = getIntent().getIntExtra("codigo",0);
        nombre   = getIntent().getStringExtra("nombre")+" "+getIntent().getStringExtra("nombre2");
        apellido = getIntent().getStringExtra("apellido")+" "+getIntent().getStringExtra("apellido2");
        correo   = getIntent().getStringExtra("correo");

        txtNombre.setText(nombre);
        txtApellido.setText(apellido);
        txtCorreo.setText(correo);

        //*******visualiza la flecha para volver*********************
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //***********************************************************

        btnModifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre= txtNombre.getText().toString();
                apellido=txtApellido.getText().toString();
                correo=txtCorreo.getText().toString();
                if (validaExistenciaUsuario(correo)){
                    Toast.makeText(Modificar.this,"Correo ya esta registrado", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Modificar.this);
                    builder.setMessage("¿Esta seguro de Modificar?");
                    builder.setTitle("Modificación");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            modificar(codigo,nombre,apellido,correo);
                            Toast.makeText(Modificar.this,"Registro Modificado exitosamente",Toast.LENGTH_SHORT).show();
                            cambiaActivity();
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
        });

        btnElimina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Modificar.this);
                builder.setMessage("¿Esta seguro de Eliminar?");
                builder.setTitle("Eliminación");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eliminar(codigo);
                        Toast.makeText(Modificar.this,"Registro Eliminado exitosamente",Toast.LENGTH_SHORT).show();
                        cambiaActivity();
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
        });
    }

    private void modificar (int cod, String n, String a, String co ){

                ConexionBD conn= new ConexionBD(Modificar.this,Tabla.nombre_bd,null,1);
                SQLiteDatabase db = conn.getWritableDatabase();

                String clausulaWhere = Tabla.campo_id_persona+"=?";
                String[] argumentosWhere= {String.valueOf(codigo)};

                ContentValues c = new ContentValues();
                c.put(Tabla.campo_nombre_persona,nombre.toUpperCase());
                c.put(Tabla.campo_apellido_persona,apellido.toUpperCase());
                c.put(Tabla.campo_email_persona,correo.toLowerCase());
                db.update(Tabla.tabla_persona,c,clausulaWhere,argumentosWhere);
                db.close();
      //  System.out.println("modificar ("+cod+","+n+","+a+","+co+" ");


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
                mail+"' AND "+
                Tabla.campo_id_persona+" != "+codigo;
        System.out.println(sql);
        Cursor c=db.rawQuery(sql,null);
        if (c.moveToFirst()){
            do {
                flg=true;
            } while (c.moveToNext());
        }
        db.close();
        System.out.println(flg);
        return flg;
    }


    private void eliminar(int codigo){
        ConexionBD conn= new ConexionBD(this,Tabla.nombre_bd,null,1);
        SQLiteDatabase db = conn.getWritableDatabase();
        ContentValues c = new ContentValues();
        db.delete(Tabla.tabla_persona,Tabla.campo_id_persona+" = ?",new String[]{String.valueOf(codigo)});
        db.close();

    }

    public void cambiaActivity(){
        Intent intent= new Intent(Modificar.this,Listado.class);
        startActivity(intent);
    }
}

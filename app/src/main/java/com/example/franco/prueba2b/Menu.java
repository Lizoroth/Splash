package com.example.franco.prueba2b;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {
    Button btnListar,btnClima,btnAcerca,btnSalir,btnRegistro;
    SessionManager sesion;


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
        setContentView(R.layout.activity_menu);
        sesion = new SessionManager(this);
        sesion.checkLogin();

        btnListar   = findViewById(R.id.btnListar);
        btnClima    = findViewById(R.id.btnClima);
        btnAcerca   = findViewById(R.id.btnAcerca);
        btnSalir    = findViewById(R.id.btnSalir);
        btnRegistro = findViewById(R.id.btnRegistro);

        //*******visualiza la flecha para volver*********************
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //***********************************************************


        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent();
                inte.setClass(Menu.this,Listado.class);
                startActivity(inte);
            }
        });

        btnClima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent();
                inte.setClass(Menu.this,Clima.class);
                startActivity(inte);
            }
        });

        btnAcerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent();
                inte.setClass(Menu.this,Acerca.class);
                startActivity(inte);
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sesion.logout();
                Intent inte = new Intent();
                inte.setClass(Menu.this,Principal.class);
                startActivity(inte);
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inte = new Intent();
                inte.setClass(Menu.this,Registro.class);
                startActivity(inte);
            }
        });
    }
}

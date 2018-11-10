package com.example.franco.prueba2b;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class Listado extends AppCompatActivity {
    ListView lst;
    SearchView busca;
    ArrayList<String> listaResultado;
    int tot;


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
        setContentView(R.layout.activity_listado);

        lst = findViewById(R.id.lstLista);
        busca =  findViewById(R.id.srcBusca);
        cargarlistado();

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        //*********************************
       ListaUsuario();
        //*********************************

        //*********************************


       busca.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String txt) {
                filtroLista(txt,cargarlistado());
               return false;
           }
       });

       lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //System.out.println("total de palabras >>"+listaResultado.size());
               //StringTokenizer tk = new StringTokenizer(listaResultado.get(position));
               //tot=tk.countTokens();
               //System.out.println("total >"+tk.countTokens());

                   int    clave     =Integer.parseInt(listaResultado.get(position).split(" ")[0]);
                   String nombre    =listaResultado.get(position).split(" ")[1];
                   String nombre2    =listaResultado.get(position).split(" ")[2];
                   String apellido   =listaResultado.get(position).split(" ")[3];
                   String apellido2   =listaResultado.get(position).split(" ")[4];
                   String correo  =listaResultado.get(position).split(" ")[5];

                   Intent intent= new Intent(Listado.this,Modificar.class);

                   intent.putExtra("codigo",clave);
                   intent.putExtra("nombre",nombre);
                   intent.putExtra("nombre2",nombre2);
                   intent.putExtra("apellido",apellido);
                   intent.putExtra("apellido2",apellido2);
                   intent.putExtra("correo",correo);
                   intent.putExtra("tot",tot);
                   startActivity(intent);


           }
       });

    }

    private ArrayList<String> ListaUsuario (){
        ArrayList<String> datos = new ArrayList<String>();
        ConexionBD helper = new ConexionBD(this,Tabla.nombre_bd,null,1);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT "+ Tabla.campo_id_persona+
                ","+Tabla.campo_nombre_persona +
                ","+Tabla.campo_apellido_persona +
                ","+Tabla.campo_email_persona+
                " FROM "+Tabla.tabla_persona+" WHERE "+Tabla.campo_id_persona+" != 1";
        Cursor c=db.rawQuery(sql,null);

        if (c.moveToFirst()){
            do {
                String linea=c.getString(0)+" "+c.getString(1)+" "+c.getString(2)+" "+c.getString(3);
                datos.add(linea);
            } while (c.moveToNext());
        } else {
            System.out.println("Bd sin información");
        }
        db.close();

       return datos;
    }

    public ArrayAdapter<String> cargarlistado(){
        listaResultado=ListaUsuario();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,  listaResultado);
        lst.setAdapter(adapter);

        return adapter;
    }

    public void filtroLista(String txt, ArrayAdapter<String> adapter){
        adapter.getFilter().filter(txt);
    }



}

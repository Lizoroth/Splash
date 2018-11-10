package com.example.franco.prueba2b;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Clima extends AppCompatActivity {
    TextView lblTemp,lblHum,fechaHora;
    private RequestQueue aux;
    String valTemp,valHum;

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
        setContentView(R.layout.activity_clima);

        //*******visualiza la flecha para volver*********************
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //***********************************************************

        lblTemp=findViewById(R.id.lblTempVal);
        lblHum=findViewById(R.id.lblHumVal);
        fechaHora= findViewById(R.id.txtfechaHora);
        aux=Volley.newRequestQueue(this);

        Date date = new Date(); date.setHours(date.getHours() );
        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat  sdfHora = new SimpleDateFormat("kk:mm:ss");
        fechaHora.setText(sdfFecha.format(date)+" - "+sdfHora.format(date));
        obtenerDatos();
    }

    private void obtenerDatos(){
        String url="http://iot.cyberls.net/iotmoviles/muestra_datos.php";
        JsonObjectRequest obtiene= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    valTemp = response.getString("temperatura");
                    valHum = response.getString("humedad");
                    if (valTemp.isEmpty() || valHum.isEmpty()){
                        lblTemp.setText("0" + " °C");
                        lblHum.setText("0" + " %");
                        Toast.makeText(Clima.this,"Se han recibidos valores vacios",Toast.LENGTH_SHORT).show();
                    } else {
                        lblTemp.setText(valTemp + " °C");
                        lblHum.setText(valHum + " %");
                    }
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        aux.add(obtiene);
    }
}

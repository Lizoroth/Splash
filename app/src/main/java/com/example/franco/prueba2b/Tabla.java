package com.example.franco.prueba2b;

public class Tabla {

    //**/**************TABLA PERSONA*********************************************************
    public static final String tabla_persona="USUARIO";
    public static final String campo_id_persona="CODIGO";
    public static final String campo_nombre_persona="NOMBRE";
    public static final String campo_apellido_persona="APELLIDO";
    public static final String campo_email_persona="MAIL";
    public static final String campo_pass_persona="PASSWORD";
    public static final String nombre_bd="BD_USUARIIOS_P2";


    public static final String create_tbl_persona="CREATE TABLE "+tabla_persona+" ("+
            campo_id_persona+" INTEGER PRIMARY KEY, "+
            campo_nombre_persona+" TEXT, "+
            campo_apellido_persona+" TEXT, " +
            campo_email_persona+" TEXT, "+
            campo_pass_persona+" TEXT)";


//*********************FIN TABLA PERSONA************************************************
}

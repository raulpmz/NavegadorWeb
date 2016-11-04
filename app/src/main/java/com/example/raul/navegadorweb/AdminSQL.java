package com.example.raul.navegadorweb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 18/10/2016.
 */

public class AdminSQL extends SQLiteOpenHelper{

    private static Historial historial;
    private static String ins = "CREATE TABLE Historial (texto VARCHAR(100), url VARCHAR(200))";
    private static final int VERSION_BASEDATOS = 1;
    private static final String NOMBRE_BASEDATOS = "Historial.db";

    public AdminSQL(Context context){

        super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ins);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public long insertarPagina(Historial historial){
        long nreg_afectados = -1;

        SQLiteDatabase db = getWritableDatabase();

        if(db != null){

            ContentValues valores = new ContentValues();
            valores.put("texto", historial.getTexto());
            valores.put("url",  historial.getUrl());

            nreg_afectados = db.insert("Historial", null, valores);

        }

        db.close();
        return nreg_afectados;
    }

    public ArrayList<Historial> obtenerHistorial(){

        Historial historial;
        ArrayList<Historial> listaHistorial = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String campos[] = {"texto", "url"};
        Cursor c;
        if(db != null){
            c = db.query("Historial", campos, null, null, null, null, null);
            c.moveToFirst();
            do {
                historial = new Historial(c.getString(0),c.getString(1));
                listaHistorial.add(historial);
            }while (c.moveToNext());

            c.close();
        }
        db.close();
        return listaHistorial;
    }


    public int borrarHistorial(){
        int n = -1;
        SQLiteDatabase db = getWritableDatabase();
        if(db != null){
            n = db.delete("Historial", null, null);
        }
        db.close();
        return n;
    }


}

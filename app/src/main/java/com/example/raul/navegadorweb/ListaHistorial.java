package com.example.raul.navegadorweb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class ListaHistorial extends AppCompatActivity {

    private AdminSQL bd;
    private ArrayList ListaUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_historial);

        bd = new AdminSQL(this);
        ListaUrl = new ArrayList<String>();
        ListView lv = (ListView) findViewById(R.id.lv);

        try{
            ArrayList<Historial> listaHistorial = null;
            listaHistorial = bd.obtenerHistorial();
            for(int i = 0; i < listaHistorial.size(); i ++){
                String direccion = listaHistorial.get(i).getUrl();
                if(direccion != null){
                    ListaUrl.add(direccion);
                }
            }

            Collections.reverse(ListaUrl);


            lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ListaUrl));
            if(ListaUrl.size() < 1){
                Toast.makeText(this, "No hay datos en el historial.", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){

            Toast.makeText(this, "No hay datos en el historial.", Toast.LENGTH_SHORT).show();

        }



    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    public void borrar(View v){
        ArrayList<String> texto = new ArrayList();
        ArrayList<Historial> listaSugerencias = null;

        try {
            listaSugerencias = bd.obtenerHistorial();

            for (int i = 0; i < listaSugerencias.size(); i++) {
                String direccion = listaSugerencias.get(i).getTexto();
                if (direccion != null) {
                    texto.add(direccion);
                }
            }
            HashSet<String> hashSet = new HashSet<String>(texto);
            texto.clear();
            texto.addAll(hashSet);

            int nreg_afectados = bd.borrarHistorial();

            if (nreg_afectados >= 0) {
                Toast.makeText(this, "se ha borrado el historial.", Toast.LENGTH_SHORT).show();
            }

            for(int i = 0 ; i < texto.size();i++) {
                Historial h = new Historial(texto.get(i));
                bd.insertarPagina(h);
            }

        }catch (Exception e){
            Toast.makeText(this, "Error al borrar historial.", Toast.LENGTH_SHORT).show();
        }


        finish();
    }
}

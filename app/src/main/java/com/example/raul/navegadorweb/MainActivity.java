package com.example.raul.navegadorweb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity{

    private WebView web;
    private ProgressBar pb;
    private AutoCompleteTextView et;
    private Historial historial;
    private AdminSQL bd;
    long nreg_afectados = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bd = new AdminSQL(this);

        web = (WebView)this.findViewById(R.id.web);

        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String url = "http://www.google.es";
        web.loadUrl(url);

        et = (AutoCompleteTextView) this.findViewById(R.id.et);

        //Cargamos direcciones web.
        web.setWebViewClient(new WebViewClient(){

            public boolean shouldOverriderUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });

        et.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey (View v, int KeyCode, KeyEvent event){
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (KeyCode == KeyEvent.KEYCODE_ENTER)){
                    ir();
                    return true;
                }
                return false;
            }
        });

        web.setWebViewClient(new WebViewClient());

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(web.getWindowToken(),0);

        //Parametros para configurar la barra de proceso
        pb =(ProgressBar)findViewById(R.id.pb);

        web.setWebChromeClient(new WebChromeClient(){
            private int progress;

            public void setProgress(int progress) {
                this.progress = progress;
            }

            public void onProgressChanged(WebView view, int progress){

                pb.setProgress(0);
                pb.setVisibility(View.VISIBLE);
                this.setProgress(progress*1000);

                pb.incrementProgressBy(progress);

                if(progress == 100){
                    pb.setVisibility(View.GONE);
                }
            }
        });

        autoCompletado();
    }


    public void buscar(View v) {
        ir();
    }

    public void ir(){
        try{
            String header = "http://www.";
            String fin = ".es";
            String url = et.getText().toString();

            if(URLUtil.isValidUrl(url)){
                historial = new Historial(url, url);
                nreg_afectados = bd.insertarPagina(historial);
                web.loadUrl(url);
                et.setText(null);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

            }else{
                historial = new Historial(url, header + url + fin);
                nreg_afectados = bd.insertarPagina(historial);
                web.loadUrl(header + url + fin);
                et.setText(null);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
            }
        }catch(Exception e){
            Toast.makeText(this,"Error al insertar historial", Toast.LENGTH_SHORT).show();
        }

        autoCompletado();

    }

    public void autoCompletado(){
        ArrayList<String> texto = new ArrayList();
        ArrayList<Historial> listaHistorial = null;

        try{

            listaHistorial = bd.obtenerHistorial();

            for(int i = 0; i < listaHistorial.size(); i ++){
                String direccion = listaHistorial.get(i).getTexto();
                if(direccion != null){
                    texto.add(direccion);
                }
            }
            HashSet<String> hashSet = new HashSet<String>(texto);
            texto.clear();
            texto.addAll(hashSet);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item,texto);
            et.setThreshold(1);
            et.setAdapter(adapter);
            et.setTextColor(Color.RED);

        }catch (Exception e){

        }

    }

    public void home(View v){
        web.loadUrl("http://www.google.es");
    }

    public void lanzarHistorial(View v){
        Intent i = new Intent(this, ListaHistorial.class);
        startActivity(i);
    }

    public void anterior(View v){
        if(web.canGoBack()) {
            web.goBack();
        }
    }

    public void siguiente(View v)
    {
        if(web.canGoForward()){
            web.goForward();
        }

    }

    public void recargar(View v){
        web.loadUrl(web.getOriginalUrl());
    }

    @Override
    public void onBackPressed()
    {
        if (web.canGoBack())
        {
            web.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }


}

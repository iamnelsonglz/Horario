package com.nelsonglz.horario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.nelsonglz.horario.adaptadores.ListaHorasAdapter;
import com.nelsonglz.horario.database.DbHoras;
import com.nelsonglz.horario.entidades.Horas;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ArrayList<Horas> listaArrayHoras;

    // Referencias a las vistas
    private RecyclerView vLista;

    // Referencias a los botones
    private Button vBtnEntrada, vBtnSalida,vBtnManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Colocar icono a ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // Se instancian las vistas
        vLista = findViewById(R.id.listaHoras);

        // Se instancian los botones
        vBtnEntrada = findViewById(R.id.btn_entrada);
        vBtnSalida = findViewById(R.id.btn_salida);
        vBtnManual = findViewById(R.id.btn_manual);

        vLista.setLayoutManager(new LinearLayoutManager(this));

        // Se muestran las horas disponibles

        funMostrar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_main);

        // Colocar icono a ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // Se instancian las vistas
        vLista = findViewById(R.id.listaHoras);

        // Se instancian los botones
        vBtnEntrada = findViewById(R.id.btn_entrada);
        vBtnSalida = findViewById(R.id.btn_salida);

        vLista.setLayoutManager(new LinearLayoutManager(this));

        // Se muestran las horas disponibles
        funMostrar();
    }

    public void funEntrada(View v){

        // Se extrae fecha y hora
        String fecha = funObtenerFecha().toString();
        String hentrada = funObtenerHora().toString();

        //Se comprueba si existe el registro
        DbHoras db = new DbHoras(MainActivity.this);
        long c = db.existenciaEntrada(fecha);
        if(c>0){
            Toast.makeText(this, "La hora de entrada ya existe para el día "+fecha, Toast.LENGTH_SHORT).show();
        }else{
            // Se llama el método para insertar hora
            long id = db.registroEntrada(fecha,hentrada);
            if(id>0){
                Toast.makeText(this, "Hora de entrada registrada", Toast.LENGTH_SHORT).show();
                funMostrar();
            }else{
                Toast.makeText(this, "No se pudo registrar la hora de entrada", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void funSalida(View v){
        // Se extrae fecha y hora
        String fecha = funObtenerFecha().toString();
        String hsalida = funObtenerHora().toString();

        //Se comprueba si el registro esta vacio
        DbHoras db = new DbHoras(MainActivity.this);
        long ev = db.existenciaSalidaVacio(fecha);
        if(ev>0){
            long e = db.existenciaSalida(fecha);
            if(e>0){
                Toast.makeText(this, "La hora de salida ya existe para el día "+fecha, Toast.LENGTH_SHORT).show();
            }
            else{
                // Se llama el método para insertar hora
                long id = db.registroSalida(fecha,hsalida);
                if(id>0){
                    funHoras(fecha);
                }else{
                    Toast.makeText(this, "No se pudo registrar la hora de salida", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(this, "Ya existe la hora de salida del día o no existe la hora de entrada"+fecha, Toast.LENGTH_LONG).show();
        }
    }

    public void  funHoras(String fecha){
        DbHoras db = new DbHoras(MainActivity.this);
        boolean v = db.registroTotal(fecha);
        if (v == true) {
            Toast.makeText(this, "Horas registradas", Toast.LENGTH_SHORT).show();
            funMostrar();
        }else{
            Toast.makeText(this, "No se pudo registrar las horas", Toast.LENGTH_SHORT).show();
        }
    }

    public void funAdd(View v){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    public void funAcumHoras(){
        DbHoras db = new DbHoras(MainActivity.this);
        Horas hora;
        hora = db.acumHora();
        String h = hora+"";

        if(hora.getAhoras().isEmpty() || hora.getAhoras().equals("")){
            Toast.makeText(this,"Horas acumuladas: 0",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Horas acumuladas: "+hora.getAhoras(),Toast.LENGTH_LONG).show();
        }
    }

    public void funMostrar(){
        DbHoras db = new DbHoras(MainActivity.this);
        listaArrayHoras = new ArrayList<>();

        ListaHorasAdapter adapter = new ListaHorasAdapter(db.mostrarHoras());
        vLista.setAdapter(adapter);
        funAcumHoras();
    }

    private String funObtenerHora(){

        Calendar calendario = Calendar.getInstance();
        int hour = calendario.get(Calendar.HOUR_OF_DAY);
        int minute = calendario.get(Calendar.MINUTE);
        String shour,sminute;
        // Hora a String
        if (hour<10){
            shour = "0"+hour;
        }else{
            shour = ""+hour;
        }
        // Minuto a String
        if (minute<10){
            sminute = "0"+minute;
        }else{
            sminute = ""+minute;
        }
        String time = (shour+":"+sminute);
        return time;
    }

    private String funObtenerFecha(){
        Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);
        int month = calendario.get(Calendar.MONTH);
        int day = calendario.get(Calendar.DAY_OF_MONTH);
        String smonth;
        month+=1;
        // Mes a String
        if (month<10){
            smonth = "0"+month;
        }else{
            smonth = ""+month;
        }
        String date = year+"-"+smonth+"-"+day;
        return date;
    }
}

// Obtener horas de servicio
// SELECT round((julianday(salida)-julianday(entrada))*24*60 ,0) FROM horas where fecha = '2022-01-08';
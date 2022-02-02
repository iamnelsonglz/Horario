package com.nelsonglz.horario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.nelsonglz.horario.adaptadores.ListaHorasAdapter;
import com.nelsonglz.horario.database.DbHoras;
import com.nelsonglz.horario.entidades.Horas;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ArrayList<Horas> listaArrayHoras;

    // Referencias a las vistas
    private RecyclerView vLista;
    private TextView vTxt;

    // Referencias a los botones
    private Button vBtnEntrada, vBtnSalida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Colocar icono a ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // Se instancian las vistas
        vLista = findViewById(R.id.listaHoras);
        vTxt = findViewById(R.id.txtAcumHoras);

        // Se instancian los botones
        vBtnEntrada = findViewById(R.id.btn_entrada);
        vBtnSalida = findViewById(R.id.btn_salida);

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

        if(hora.getAhoras()==null){
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
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return time;
    }

    private String funObtenerFecha(){
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        return date;
    }
}

// Obtener horas de servicio
// SELECT round((julianday(salida)-julianday(entrada))*24*60 ,0) FROM horas where fecha = '2022-01-08';
package com.nelsonglz.horario;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nelsonglz.horario.database.DbHoras;
import com.nelsonglz.horario.entidades.Horas;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    TextView txtFecha, txtEntrada, txtSalida;
    Button btnAdd;
    Horas hora;
    String fecha = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Colocar icono a ActionBar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        txtFecha = findViewById(R.id.txtAddFecha);
        txtEntrada = findViewById(R.id.txtAddEntrada);
        txtSalida = findViewById(R.id.txtAddSalida);
        btnAdd = findViewById(R.id.btnAdd);
    }
    public void funAddRegistro(View v){
        String fecha=null,entrada=null,salida=null;
        fecha = txtFecha.getText().toString();
        entrada = txtEntrada.getText().toString();
        salida = txtSalida.getText().toString();
        // Se comprueba que el registro no este vacio
        if(fecha.isEmpty() || entrada.isEmpty() || salida.isEmpty()){
            Toast.makeText(this, "No se pueden guardar registros vacios", Toast.LENGTH_SHORT).show();
        }else{
            //Se comprueba si existe el registro
            DbHoras db = new DbHoras(AddActivity.this);
            long c = db.existenciaEntrada(fecha);
            if(c>0){
                Toast.makeText(this, "La fecha "+fecha+" ya existe", Toast.LENGTH_SHORT).show();
            }else{
                // Se llama el método para insertar hora
                long id = db.addHora(fecha,entrada,salida);
                if(id>0){
                    boolean d = db.registroTotal(fecha);
                    if(d==true){
                        txtFecha.setText("");
                        txtEntrada.setText("");
                        txtSalida.setText("");
                        Toast.makeText(this, "Registro de hora agregado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getBaseContext(), MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        finish();
                    }else{
                        Toast.makeText(this, "No se pudo registrar la hora de entrada", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }
    }
    public void funAddFecha(View view){
        Calendar calendario = Calendar.getInstance();
        int vyear = calendario.get(Calendar.YEAR);
        int vmonth = calendario.get(Calendar.MONTH);
        int vday = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String smonth;
                month+=1;
                // Mes a String
                if (month<10){
                    smonth = "0"+month;
                }else{
                    smonth = ""+month;
                }
                String fecha = year+"-"+smonth+"-"+dayOfMonth;
                txtFecha.setText(fecha);
            }
        },vyear,vmonth,vday);
        datePickerDialog.show();
    }
    public void funAddEntrada(View view){
        Calendar calendario = Calendar.getInstance();
        int vhour = calendario.get(Calendar.HOUR_OF_DAY);
        int vminute = calendario.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String shour,sminute;
                // Hora a String
                if (hourOfDay<10){
                    shour = "0"+hourOfDay;
                }else{
                    shour = ""+hourOfDay;
                }
                // Minuto a String
                if (minute<10){
                    sminute = "0"+minute;
                }else{
                    sminute = ""+minute;
                }
                txtEntrada.setText(shour+":"+sminute);
            }
        },vhour,vminute,true);
        timePickerDialog.show();
    }
    public void funAddSalida(View view){
        Calendar calendario = Calendar.getInstance();
        int vhour = calendario.get(Calendar.HOUR_OF_DAY);
        int vminute = calendario.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String shour,sminute;
                // Hora a String
                if (hourOfDay<10){
                    shour = "0"+hourOfDay;
                }else{
                    shour = ""+hourOfDay;
                }
                // Minuto a String
                if (minute<10){
                    sminute = "0"+minute;
                }else{
                    sminute = ""+minute;
                }
                txtSalida.setText(shour+":"+sminute);
            }
        },vhour,vminute,true);
        timePickerDialog.show();
    }
}
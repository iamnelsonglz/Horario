package com.nelsonglz.horario;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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

public class EditarActivity extends AppCompatActivity {

    TextView txtFecha, txtEntrada, txtSalida;
    Button btnEditar, btnBorrar;
    Horas hora;
    String fecha = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        txtFecha = findViewById(R.id.txtModFecha);
        txtEntrada = findViewById(R.id.txtModEntrada);
        txtSalida = findViewById(R.id.txtModSalida);
        btnEditar = findViewById(R.id.btnMod);
        btnBorrar = findViewById(R.id.btnDel);

        if(savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                fecha = null;
            }else{
                fecha = extras.getString("FECHA");
            }
        }else{
            fecha = (String) savedInstanceState.getSerializable("FECHA");
        }
        DbHoras db = new DbHoras(EditarActivity.this);
        hora = db.verHora(fecha);

        if(hora != null){
            txtFecha.setText(hora.getFecha());
            txtEntrada.setText(hora.getHentrada());
            txtSalida.setText(hora.getHsalida());
        }
    }
    public void funEditar(View v){
        DbHoras db = new DbHoras(EditarActivity.this);
        String hentrada = txtEntrada.getText().toString();
        String hsalida = txtSalida.getText().toString();
        if(hentrada.isEmpty() || hsalida.isEmpty()){
            Toast.makeText(this, "No se pueden dejar campos vacios", Toast.LENGTH_SHORT).show();
        }else {
            long c = db.editarHora(fecha, hentrada, hsalida);
            if (c > 0) {
                boolean d = db.registroTotal(fecha);
                if (d == true) {
                    Toast.makeText(this, "Registro de hora modificado", Toast.LENGTH_SHORT).show();
                    txtFecha.setText("");
                    txtEntrada.setText("");
                    txtSalida.setText("");
                    //Regresar al main
                    //Intent intent = new Intent(this,MainActivity.class);
                    startActivity(new Intent(getBaseContext(), MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                    finish();
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //startActivity(intent);
                } else {

                }
            } else {
                Toast.makeText(this, "No se pudo modificar el registro de la hora", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void funEliminar(View v){
        DbHoras db = new DbHoras(EditarActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(EditarActivity.this);
        builder.setMessage("¿Desea eliminar este registro de hora?").setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txtFecha.setText("");
                txtEntrada.setText("");
                txtSalida.setText("");
                boolean v = db.eliminarHora(fecha);
                    if (v == true){
                        startActivity(new Intent(getBaseContext(), MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        finish();
                    }else{

                    }
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).show();
    }
    public void funModEntrada(View view){
        Calendar calendario = Calendar.getInstance();
        int vhour = calendario.get(Calendar.HOUR_OF_DAY);
        int vminute = calendario.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(EditarActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
    public void funModSalida(View view){
        Calendar calendario = Calendar.getInstance();
        int vhour = calendario.get(Calendar.HOUR_OF_DAY);
        int vminute = calendario.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(EditarActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
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
package com.nelsonglz.horario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nelsonglz.horario.database.DbHoras;
import com.nelsonglz.horario.entidades.Horas;

public class AddActivity extends AppCompatActivity {

    TextView txtFecha, txtEntrada, txtSalida;
    Button btnAdd;
    Horas hora;
    String fecha = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

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
        //Se comprueba si existe el registro
        DbHoras db = new DbHoras(AddActivity.this);
        long c = db.existenciaEntrada(fecha);
        if(c>0){
            Toast.makeText(this, "La hora de entrada ya existe para el día "+fecha, Toast.LENGTH_SHORT).show();
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
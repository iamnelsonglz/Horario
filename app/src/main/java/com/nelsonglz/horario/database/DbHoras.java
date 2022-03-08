package com.nelsonglz.horario.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.nelsonglz.horario.entidades.Horas;

import java.util.ArrayList;

public class DbHoras extends DbHelper {

    Context context;

    public DbHoras(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long existenciaEntrada(String fecha) {
        long v = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            // Accedemos la tabla
            String query = "Select entrada From " + NOMBRE_TABLA + " WHERE fecha = '" + fecha + "'";
            db.rawQuery(query, null);
            Cursor cursor = db.rawQuery(query, null);
            v = cursor.getCount();
        } catch (Exception e) {

        }
        return v;
    }

    public long registroEntrada(String fecha, String hentrada) {
        long v = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Se prepara el Content Values
            ContentValues registro = new ContentValues();
            registro.put(CAMPO1, fecha);
            registro.put(CAMPO2, hentrada);

            // Se crea el registro y se insertan los valores
            v = db.insert(NOMBRE_TABLA, null, registro);
        } catch (Exception e) {

        }
        return v;
    }

    public long existenciaSalidaVacio(String fecha) {
        long v = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            // Accedemos la tabla
            String query = "Select salida From " + NOMBRE_TABLA + " WHERE fecha = '" + fecha + "' AND salida = '00:00'";
            db.rawQuery(query, null);
            Cursor cursor = db.rawQuery(query, null);
            v = cursor.getCount();
        } catch (Exception e) {

        }
        return v;
    }

    public long existenciaSalida(String fecha) {
        long v = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            // Accedemos la tabla
            String query = "Select salida From " + NOMBRE_TABLA + " WHERE fecha = '" + fecha + "' AND salida != '00:00'";
            db.rawQuery(query, null);
            Cursor cursor = db.rawQuery(query, null);
            v = cursor.getCount();
        } catch (Exception e) {

        }
        return v;
    }

    public long registroSalida(String fecha, String hsalida) {
        long v = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Se colocan los valores en un ContentValues
            ContentValues valores = new ContentValues();
            valores.put(CAMPO3, hsalida);

            // Consulta
            v = db.update(NOMBRE_TABLA, valores, CAMPO1 + " = '" + fecha + "'", null);
            db.close();
        } catch (Exception e) {

        }
        return v;
    }

    public boolean registroTotal(String fecha) {
        boolean v = false;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Se registra el calculo de las horas trabajadas
        try {
            db.execSQL("UPDATE " + NOMBRE_TABLA + " SET horas  = " +
                    "(SELECT round((julianday(salida)-julianday(entrada))*24*60,0) " +
                    "FROM " + NOMBRE_TABLA + " WHERE fecha = '" + fecha + "') WHERE fecha = '" + fecha + "';");
            v = true;
        } catch (Exception e) {
            v = false;
        }
        return v;
    }

    public ArrayList<Horas> mostrarHoras() {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Horas> listaHoras = new ArrayList<>();
        Horas hora = null;
        Cursor cursor = null;

        cursor = db.rawQuery("SELECT fecha,entrada,salida,round(horas/60) FROM " + NOMBRE_TABLA+" ORDER BY fecha DESC", null);
        if (cursor.moveToFirst()) {
            do {
                hora = new Horas();
                hora.setFecha(cursor.getString(0));
                hora.setHentrada(cursor.getString(1));
                hora.setHsalida(cursor.getString(2));
                hora.setThoras(cursor.getString(3));
                listaHoras.add(hora);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listaHoras;
    }

    public Horas verHora(String fecha) {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Horas hora = null;
        Cursor cursor = null;

        cursor = db.rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE fecha ='" + fecha + "' LIMIT 1", null);
        if (cursor.moveToFirst()) {
            hora = new Horas();
            hora.setFecha(cursor.getString(0));
            hora.setHentrada(cursor.getString(1));
            hora.setHsalida(cursor.getString(2));
            hora.setThoras(cursor.getString(3));
        }
        cursor.close();
        return hora;
    }

    public long editarHora(String fecha, String hentrada, String hsalida) {
        long v = 0;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            // Se colocan los valores en un ContentValues
            ContentValues valores = new ContentValues();
            valores.put(CAMPO2, hentrada);
            valores.put(CAMPO3, hsalida);
            // Consulta
            v = db.update(NOMBRE_TABLA, valores, CAMPO1 + " = '" + fecha + "'", null);
            db.close();
        } catch (Exception e) {

        }
        return v;
    }

    public boolean eliminarHora(String fecha) {
        boolean v = false;
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM " + NOMBRE_TABLA + " WHERE fecha = '" + fecha + "'");
            v = true;
        } catch (Exception e) {
            v = false;
        }
        db.close();
        return v;
    }

    public long addHora(String fecha, String entrada, String salida) {
        long v = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Se prepara el Content Values
            ContentValues registro = new ContentValues();
            registro.put(CAMPO1, fecha);
            registro.put(CAMPO2, entrada);
            registro.put(CAMPO3, salida);

            // Se crea el registro y se insertan los valores
            v = db.insert(NOMBRE_TABLA, null, registro);
        } catch (Exception e) {

        }
        return v;
    }

    public Horas acumHora() {
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Horas hora = null;
        Cursor cursor = null;
        try {
            String query = "SELECT sum("+CAMPO4+")/60 AS acum from "+NOMBRE_TABLA+" LIMIT 1";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                hora = new Horas();
                hora.setAhoras(cursor.getString(0));
            }
        } catch (Exception e) {
            Log.e("Alerta", "Error con: " + hora);
        }
        cursor.close();
        return hora;
    }
}


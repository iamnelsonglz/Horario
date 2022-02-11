package com.nelsonglz.horario.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    //Idendentificadores para la BDD y la version
    public static final String NOMBRE_BD="bdServicio";
    public static final int VERSION_BD=2;

    //Identificadores para la tabla y sus campos
    public static final String NOMBRE_TABLA="horas";
    public static final String CAMPO1="fecha";
    public static final String CAMPO2="entrada";
    public static final String CAMPO3="salida";
    public static final String CAMPO4="horas";

    public DbHelper(@Nullable Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+NOMBRE_TABLA+" (" +
                "fecha DATE PRIMARY KEY, " +
                "entrada TIME NOT NULL, " +
                "salida TIME DEFAULT '00:00'," +
                "horas TIME DEFAULT '00:00')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+NOMBRE_TABLA);
        onCreate(sqLiteDatabase);
    }
}

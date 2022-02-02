package com.nelsonglz.horario.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.nelsonglz.horario.EditarActivity;
import com.nelsonglz.horario.R;
import com.nelsonglz.horario.entidades.Horas;

import java.util.ArrayList;

public class ListaHorasAdapter extends RecyclerView.Adapter<ListaHorasAdapter.HorasViewHolder> {

    ArrayList<Horas> listaHoras;
    public ListaHorasAdapter(ArrayList<Horas> listaHoras){
        this.listaHoras = listaHoras;
    }

    @NonNull
    @Override
    public HorasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item,null,false);
        return new HorasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorasViewHolder holder, int position) {
        holder.txt_fecha.setText(listaHoras.get(position).getFecha());
        holder.txt_entrada.setText(listaHoras.get(position).getHentrada());
        holder.txt_salida.setText(listaHoras.get(position).getHsalida());
        holder.txt_horas.setText(listaHoras.get(position).getThoras());
    }

    @Override
    public int getItemCount() {
        return listaHoras.size();
    }

    public class HorasViewHolder extends RecyclerView.ViewHolder {

        TextView txt_fecha, txt_entrada, txt_salida, txt_horas;

        public HorasViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_fecha = itemView.findViewById(R.id.txt_fecha);
            txt_entrada = itemView.findViewById(R.id.txt_entrada);
            txt_salida = itemView.findViewById(R.id.txt_salida);
            txt_horas = itemView.findViewById(R.id.txt_total);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, EditarActivity.class);
                    intent.putExtra("FECHA",listaHoras.get(getAdapterPosition()).getFecha());
                    context.startActivity(intent);
                }
            });
        }
    }
}

package com.example.lab3_20190212;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder>{
    private List<Lista> mdata;
    private LayoutInflater minflater;
    private Context context;

    public Adaptador(List<Lista> itemlist, Context context){
        this.minflater = LayoutInflater.from(context);
        this.context =context;
        this.mdata = itemlist;
    }

    @Override
    public  Adaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = minflater.inflate(R.layout.lista, null);
        return new Adaptador.ViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return mdata.size();
    }
    @Override
    public void onBindViewHolder(final Adaptador.ViewHolder holder,final int position){
        holder.bindData(mdata.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imagen;
        TextView nombre, genero, ciudad, email, phone, pais;
        ViewHolder(View itemView){
            super(itemView);
            imagen = itemView.findViewById(R.id.imagen);
            nombre = itemView.findViewById(R.id.nombre);
            genero = itemView.findViewById(R.id.genero);
            ciudad = itemView.findViewById(R.id.ciudad);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            pais = itemView.findViewById(R.id.pais);
        }

        void bindData(final Lista item){
            nombre.setText(item.getNombre());
            genero.setText(item.getGenero());
            ciudad.setText(item.getCiudad());
            email.setText(item.getCorreo());
            phone.setText(item.getPhone());
            pais.setText(item.getPais());
            Picasso.get().load(item.getFoto()).into(imagen);
        }
    }

}

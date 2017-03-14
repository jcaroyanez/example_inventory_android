package com.example.dell.retrofirestapp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DELL on 14/03/2017.
 */

public class IventarioAdapter  extends RecyclerView.Adapter<IventarioAdapter.IventarioViewHoldder>{

    private ArrayList<Inventario> datos = new ArrayList<>();
    static private View.OnClickListener clickListener;

    public IventarioAdapter(){
    }


    public  void  set(ArrayList<Inventario> datosE){
            this.datos.clear();
            this.datos.addAll(datosE);
            notifyDataSetChanged();
    }

    public Inventario getItem(int pos){
        return datos.get(pos);
    }

    @Override
    public IventarioViewHoldder onCreateViewHolder(ViewGroup parent, int viewType) {

        View ItemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_inventario,parent,false);
        IventarioViewHoldder iventarioViewHoldder = new IventarioViewHoldder(ItemView);
        return iventarioViewHoldder;
    }

    @Override
    public void onBindViewHolder(IventarioViewHoldder holder, int position) {
        Inventario inventario = datos.get(position);
        holder.bindIventario(inventario);
    }


    @Override
    public int getItemCount() {
        return datos.size();
    }

    public static class IventarioViewHoldder extends RecyclerView.ViewHolder{
        TextView nombre,cantidad,precio_u,total,fecha;
        public IventarioViewHoldder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(clickListener);
            nombre = (TextView)itemView.findViewById(R.id.nombre);
            cantidad = (TextView)itemView.findViewById(R.id.cantidad);
            precio_u = (TextView)itemView.findViewById(R.id.precio);
            total = (TextView)itemView.findViewById(R.id.total);
            fecha = (TextView)itemView.findViewById(R.id.fecha);
        }

        public void bindIventario(Inventario i){
            nombre.setText(i.getNombre());
            cantidad.setText(i.getCantidad()+"");
            precio_u.setText(i.getPrecio()+"");
            total.setText(i.getTotal()+"");
            fecha.setText(i.getFecha_i()+" / "+i.getFecha_f());
        }
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }
}

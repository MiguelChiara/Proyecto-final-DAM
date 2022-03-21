package com.miguelchiara.pimiguel.Spinner;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miguelchiara.pimiguel.R;

import java.util.List;
//https://www.youtube.com/watch?v=UUGipy7h2l8&ab_channel=AndroChunk

public class CustomSpinnerAdapter extends ArrayAdapter<CustomItem> {

    public CustomSpinnerAdapter(@NonNull Context context, @NonNull List objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.spinner_layout,parent,false);
        }
        CustomItem item = (CustomItem) getItem(position);
        TextView texto = convertView.findViewById(R.id.spinner_textView);
        if(item!=null){
            texto.setText(item.getSpinnerItemName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.spinner_down_layout,parent,false);
        }
        CustomItem item = (CustomItem) getItem(position);
        TextView texto = convertView.findViewById(R.id.spinner_textViewdown);
        if(item!=null){
            texto.setText(item.getSpinnerItemName());
        }
        return convertView;
    }
}

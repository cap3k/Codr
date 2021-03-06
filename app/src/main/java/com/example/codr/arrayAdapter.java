package com.example.codr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards> {
    Context context;

    public arrayAdapter(Context context, int ressourceId, List<cards> items){
        super(context,ressourceId,items);
    }

    public View getView(int position, View convertView, ViewGroup parent){
       cards card_item=getItem(position);
       if(convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
       }
        TextView name = (TextView) convertView.findViewById(R.id.helloText);
        ImageView image = (ImageView) convertView.findViewById(R.id.projectImage);

        name.setText(card_item.getProjectName());
        image.setImageResource(R.mipmap.ic_launcher);

        return convertView;
    }

}

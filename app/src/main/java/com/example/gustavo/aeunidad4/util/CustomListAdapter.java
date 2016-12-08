package com.example.gustavo.aeunidad4.util;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gustavo.aeunidad4.R;
import com.example.gustavo.aeunidad4.Shelter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gustavo on 5/12/16.
 */

public class CustomListAdapter extends ArrayAdapter<String> {
    private JSONArray json;
    private Activity context;
    public CustomListAdapter(Activity context, JSONArray json) {
        super(context, R.layout.list_model,new String[json.length()]);
        this.context=context;
        this.json=json;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_model, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textViewDesc);
        TextView textViewPrize = (TextView) listViewItem.findViewById(R.id.textViewPrize);
        TextView textViewStock = (TextView) listViewItem.findViewById(R.id.textViewStock);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imageView);
        try {
            JSONObject item=json.getJSONObject(position);
            textViewName.setText(item.getString("productname"));
            textViewDesc.setText("Marca: "+item.getString("brand"));
            textViewPrize.setText("Precio: "+item.getString("salepricemin")+" "+item.getString("currency"));
            textViewStock.setText("Stock: "+item.getString("stock"));
            Picasso.with(context).load(Shelter.DEFAULT_DOMAIN+"/AEEcommerce/ProductImage?image="+item.getString("image")).into(image);
            image.setTag(item.getString("image"));
            textViewName.setTag(item.getString("productid"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listViewItem;
    }
}

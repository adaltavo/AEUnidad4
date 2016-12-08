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

/**
 * Created by gustavo on 7/12/16.
 */

public class CustomListAdapterCart extends ArrayAdapter {
    private JSONArray json;
    private Activity context;
    private Double total;

    public CustomListAdapterCart(Activity context, JSONArray json) {
        super(context, R.layout.list_model_cart, new String[json.length()]);
        this.context=context;
        this.json=json;
        total=0.0;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_model_cart, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewNameCart);
        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textViewBrand);
        TextView textViewPrize = (TextView) listViewItem.findViewById(R.id.textViewPrizeCart);
        TextView textViewQuantity = (TextView) listViewItem.findViewById(R.id.textViewQuantity);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imageViewCart);
        try {
            JSONObject item=json.getJSONObject(position);
            textViewName.setText(item.getString("product"));
            textViewDesc.setText("Marca: "+item.getString("brand"));
            textViewPrize.setText("Precio unitario: "+item.getString("priceperitem")+" "+item.getString("currency"));
            textViewQuantity.setText("Cantidad: "+item.getString("quantity"));
            Picasso.with(context).load(Shelter.DEFAULT_DOMAIN+"/AEEcommerce/ProductImage?image="+item.getString("image")).into(image);
            image.setTag(item.getString("image"));
            //textViewPrize.setTag(item.getString("priceperitem"));
            //textViewName.setTag(item.getString("productid"));
            //parent.setTag((Double) parent.getTag()+ Float.parseFloat(item.getString("priceperitem")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listViewItem;
    }

    public Double getTotal() {
        return total;
    }
}

package com.example.gustavo.aeunidad4.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gustavo.aeunidad4.R;
import com.example.gustavo.aeunidad4.Shelter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by gustavo on 6/12/16.
 */

public class ProductDetailAlertDialog extends Dialog implements android.view.View.OnClickListener{
    private String brand,prize,name,imagename,stock,productid;
    private ImageView image;
    private TextView brandV,prizeV,nameV,stockV;
    private Activity context;
    private Button yes,no;
    public ProductDetailAlertDialog(Activity context,String name,String prize,String brand,String stock, String imagename, String productid) {
        super(context);
        this.name=name;
        this.prize=prize;
        this.brand=brand;
        this.stock=stock;
        this.imagename=imagename;
        this.context=context;
        this.productid=productid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_dialog);
        init();
    }

    private void init() {
        //setTitle();
        //setTitle("¿Agregar al carrito?");
        yes=(Button)findViewById(R.id.btn_yes);
        no=(Button)findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        image=(ImageView) findViewById(R.id.imageViewDialog);
        brandV=(TextView) findViewById(R.id.textViewDescDialog);
        prizeV=(TextView) findViewById(R.id.textViewPrizeDialog);
        nameV=(TextView) findViewById(R.id.textViewNameDialog);
        stockV=(TextView) findViewById(R.id.textViewStockDialog);
        Picasso.with(context).load(Shelter.DEFAULT_DOMAIN+"/AEEcommerce/ProductImage?image="+imagename).into(image);
        brandV.setText(brand);
        prizeV.setText(prize);
        nameV.setText(name);
        stockV.setText(stock);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                HttpRequest request=new HttpRequest("post",Shelter.DEFAULT_DOMAIN+"/AEEcommerce/webresources/user/addProduct/"+Shelter.USER_KEY){
                    @Override
                    protected void onPostExecute(String s) {
                        try {
                            ((Shelter) context).updateCart(new JSONArray(s));
                            Toast.makeText(context,"Agregado",Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                request.execute("userid:"+Shelter.USER_ID,"productid:"+productid);

                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}

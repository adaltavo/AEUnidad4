package com.example.gustavo.aeunidad4.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gustavo.aeunidad4.MainActivity;
import com.example.gustavo.aeunidad4.R;
import com.example.gustavo.aeunidad4.Shelter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by gustavo on 6/12/16.
 */

public class ProductDetailAlertDialogCart extends Dialog implements View.OnClickListener{
    private String brand,prize,name,imagename,stock,productid;
    private ImageView image;
    private TextView brandV,prizeV,nameV,stockV;
    private Activity context;
    private Button yes,no;
    public ProductDetailAlertDialogCart(Activity context, String name, String prize, String brand, String stock, String imagename, String productid) {
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
        setContentView(R.layout.product_dialog_cart);
        init();
    }

    private void init() {
        //setTitle();
        //setTitle("¿Agregar al carrito?");
        yes=(Button)findViewById(R.id.btn_yesCart);
        no=(Button)findViewById(R.id.btn_noCart);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        image=(ImageView) findViewById(R.id.imageViewDialogCart);
        brandV=(TextView) findViewById(R.id.textViewDescDialogCart);
        prizeV=(TextView) findViewById(R.id.textViewPrizeDialogCart);
        nameV=(TextView) findViewById(R.id.textViewNameDialogCart);
        stockV=(TextView) findViewById(R.id.textViewStockDialogCart);
        Picasso.with(context).load(Shelter.DEFAULT_DOMAIN+"/AEEcommerce/ProductImage?image="+imagename).into(image);
        brandV.setText(brand);
        prizeV.setText(prize);
        nameV.setText(name);
        stockV.setText(stock);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yesCart:
                HttpRequest request=new HttpRequest("put",Shelter.DEFAULT_DOMAIN+"/AEEcommerce/webresources/user/deleteProduct/"+Shelter.USER_KEY){
                    @Override
                    protected void onPostExecute(String s) {
                        try {
                            ((Shelter) context).updateCart(new JSONArray(s));
                            Toast.makeText(context,"Borrado!",Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                                new AlertDialog.Builder(context).setMessage(s).setTitle("Error").show();
                        }
                    }
                };
                request.execute("userid:"+Shelter.USER_ID,"productid:"+productid);
                Toast.makeText(context,productid,Toast.LENGTH_SHORT).show();

                break;
            case R.id.btn_noCart:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}

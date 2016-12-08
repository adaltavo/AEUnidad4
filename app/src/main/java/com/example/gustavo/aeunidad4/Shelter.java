package com.example.gustavo.aeunidad4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gustavo.aeunidad4.util.CustomListAdapter;
import com.example.gustavo.aeunidad4.util.CustomListAdapterCart;
import com.example.gustavo.aeunidad4.util.HttpRequest;
import com.example.gustavo.aeunidad4.util.ProductDetailAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Shelter extends AppCompatActivity {
    public final static String DEFAULT_DOMAIN = "http://126d5ffb.ngrok.io";
    public final static String USER_ID = "34";//33 es de grecia
    public final static String USER_KEY = "4c96f8324e3ba54a99e78249b95daa30";//"10bab2c711bca9ace3036044b0efcc8a";//
    ListView items;
    ListView itemsCart;
    TabHost tabhost;
    TextView total;
    Button empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter);
        init();
        events();
    }

    private void init() {
        tabhost = (TabHost) findViewById(R.id.tabhost);
        tabhost.setup();
        total = (TextView) findViewById(R.id.textViewTotal);
        empty = (Button) findViewById(R.id.ButtonVaciarCarrito);

        TabHost.TabSpec tab1 = tabhost.newTabSpec("First Tab");
        tab1.setIndicator("", ResourcesCompat.getDrawable(getResources(), R.drawable.goal, null));
        tab1.setContent(R.id.tab1);
        tabhost.addTab(tab1);

        TabHost.TabSpec tab2 = tabhost.newTabSpec("Second Tab");
        tab2.setIndicator("", ContextCompat.getDrawable(this, R.drawable.cart));
        tab2.setContent(R.id.tab2);
        tabhost.addTab(tab2);
        /////////////////////Obtener la lista de productos
        HttpRequest fillList = new HttpRequest("get", DEFAULT_DOMAIN + "/AEEcommerce/webresources/product/getProducts?userid=34&apikey=4c96f8324e3ba54a99e78249b95daa30") {
            @Override
            protected void onPostExecute(String s) {
                items = (ListView) findViewById(R.id.listview1);
                CustomListAdapter adapter = null;
                try {
                    new AlertDialog.Builder(Shelter.this).setMessage(s).show();
                    adapter = new CustomListAdapter(Shelter.this, new JSONArray(s));
                } catch (JSONException e) {
                    new AlertDialog.Builder(Shelter.this).setMessage(e.toString()).show();
                    e.printStackTrace();
                }
                ArrayAdapter as = new ArrayAdapter<String>(Shelter.this, android.R.layout.simple_spinner_dropdown_item);
                as.add("asdsaddsadsa");
                items.setAdapter(adapter);
                items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name = ((TextView) view.findViewById(R.id.textViewName)).getText().toString();
                        String prize = ((TextView) view.findViewById(R.id.textViewPrize)).getText().toString();
                        String stock = ((TextView) view.findViewById(R.id.textViewStock)).getText().toString();
                        String brand = ((TextView) view.findViewById(R.id.textViewDesc)).getText().toString();
                        String imagename = (String) ((ImageView) view.findViewById(R.id.imageView)).getTag();
                        //new AlertDialog.Builder(Shelter.this).setMessage(imagename+">>>>>>dsadd").show();
                        ProductDetailAlertDialog dialog = new ProductDetailAlertDialog(Shelter.this, name, prize, brand, stock,
                                imagename,(String)((TextView) view.findViewById(R.id.textViewName)).getTag());

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                });
            }
        };
        fillList.execute();
        /////////////////////Obtener la lista del carrito
        fillList = new HttpRequest("get", Shelter.DEFAULT_DOMAIN + "/AEEcommerce/webresources/user/getCart/" + Shelter.USER_ID + "/" + Shelter.USER_KEY) {

            @Override
            protected void onPostExecute(String s) {
                itemsCart = (ListView) findViewById(R.id.listview2);
                try {
                    updateCart(new JSONArray(s));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        fillList.execute();

    }

    public void updateCart(JSONArray json){
        CustomListAdapterCart adapter = null;
        try{
            adapter = new CustomListAdapterCart(Shelter.this, json);
            Double temp=0.0;///Obtener el total (Puede ser mejor definitivamente)
            new AlertDialog.Builder(Shelter.this).setMessage(json.toString()).show();
            if (!json.isNull(0)) {
                for (int i = 0; i < json.length(); i++) {
                    temp+=json.getJSONObject(i).getDouble("priceperitem")*json.getJSONObject(i).getInt("quantity");
                }
            } else{
                try {
                    adapter = new CustomListAdapterCart(Shelter.this, new JSONArray("[{\"brand\":\"\",\"image\":\"product.png\",\"currency\":" +
                            "\"\",\"product\":\"No hay productos\",\"user\":\"gustavo\",\"quantity\":0,\"priceperitem\":0.0}]"));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            total.setText("Total: "+ temp + "");
        } catch (JSONException e) {
            new AlertDialog.Builder(Shelter.this).setMessage(e.toString()).show();
            e.printStackTrace();
            try {
                adapter = new CustomListAdapterCart(Shelter.this, new JSONArray("[{\"brand\":\"\",\"image\":\"product.png\",\"currency\":" +
                        "\"\",\"product\":\"No hay productos\",\"user\":\"gustavo\",\"quantity\":0,\"priceperitem\":0.0}]"));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        itemsCart.setAdapter(adapter);
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private void events() {
        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Shelter.this).setTitle("Confirm")
                        .setMessage("Seguro que desea vaciar el carrito")
                        .setPositiveButton("Si awebo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HttpRequest delete = new HttpRequest("put", DEFAULT_DOMAIN+"/AEEcommerce/webresources/user/deleteCart/"+USER_KEY){
                                    @Override
                                    protected void onPostExecute(String s) {
                                        updateCart(new JSONArray());
                                    }
                                };
                                delete.execute("userid:"+USER_ID);
                            }
                        }).show();
            }
        });
    }


}

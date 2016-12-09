package com.example.gustavo.aeunidad4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import com.example.gustavo.aeunidad4.util.ProductDetailAlertDialogCart;
import com.example.gustavo.aeunidad4.util.User;
import com.stripe.android.*;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

public class Shelter extends AppCompatActivity {
    public final static String DEFAULT_DOMAIN = "http://126d5ffb.ngrok.io";
    public final static boolean IS_DEBUG = false;
    public static String USER_ID = "34";//33 es de grecia
    public static String USER_KEY = "4c96f8324e3ba54a99e78249b95daa30";//"10bab2c711bca9ace3036044b0efcc8a";//
    public static String USER_NAME = "34";//33 es de grecia
    ListView items;
    ListView itemsCart;
    TabHost tabhost;
    TextView total;
    Button empty, checkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter);
        init();
        events();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.session_close:
                User user = new User(Shelter.this);
                SQLiteDatabase db = user.getWritableDatabase();
                db.execSQL("delete from user;");
                startActivity(new Intent(Shelter.this, MainActivity.class));
                finish();
                return true;

        }
        return false;
    }

    private void init() {
        //Inicializo elementos views
        tabhost = (TabHost) findViewById(R.id.tabhost);
        tabhost.setup();
        total = (TextView) findViewById(R.id.textViewTotal);
        empty = (Button) findViewById(R.id.ButtonVaciarCarrito);
        checkout = (Button) findViewById(R.id.ButtonComprar);

        //Configuro la primera pestaña
        TabHost.TabSpec tab1 = tabhost.newTabSpec("First Tab");
        tab1.setIndicator("", ResourcesCompat.getDrawable(getResources(), R.drawable.goal, null));
        tab1.setContent(R.id.tab1);
        tabhost.addTab(tab1);
        //Configuro la segunda pestaña
        TabHost.TabSpec tab2 = tabhost.newTabSpec("Second Tab");
        tab2.setIndicator("", ContextCompat.getDrawable(this, R.drawable.cart));
        tab2.setContent(R.id.tab2);
        tabhost.addTab(tab2);

        setTitle(getTitle() + ". Bienvenido, " + USER_NAME);
        /////////////////////Obtener la lista de productos
        HttpRequest fillList = new HttpRequest("get", DEFAULT_DOMAIN + "/AEEcommerce/webresources/product/getProducts/" + USER_ID + "/" + USER_KEY) {
            @Override
            protected void onPostExecute(String s) {
                items = (ListView) findViewById(R.id.listview1);
                CustomListAdapter adapter = null;
                try {
                    updateProducts(new JSONArray(s));
                } catch (JSONException e) {
                    new AlertDialog.Builder(Shelter.this).setMessage(e.toString()).show();
                    e.printStackTrace();
                }
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
                                imagename, (String) ((TextView) view.findViewById(R.id.textViewName)).getTag());

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
                itemsCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name = ((TextView) view.findViewById(R.id.textViewNameCart)).getText().toString();
                        String prize = ((TextView) view.findViewById(R.id.textViewPrizeCart)).getText().toString();
                        String stock = ((TextView) view.findViewById(R.id.textViewQuantity)).getText().toString();
                        String brand = ((TextView) view.findViewById(R.id.textViewBrand)).getText().toString();
                        String imagename = (String) ((ImageView) view.findViewById(R.id.imageViewCart)).getTag();
                        //new AlertDialog.Builder(Shelter.this).setMessage(imagename+">>>>>>dsadd").show();
                        ProductDetailAlertDialogCart dialog = new ProductDetailAlertDialogCart(Shelter.this, name, prize, brand, stock,
                                imagename, (String) ((TextView) view.findViewById(R.id.textViewNameCart)).getTag());

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                });
            }

        };
        fillList.execute();

    }

    public void updateProducts(JSONArray json) {
        CustomListAdapter adapter = null;
        if (IS_DEBUG)
            new AlertDialog.Builder(Shelter.this).setMessage(json.toString()).show();
        adapter = new CustomListAdapter(Shelter.this, json);
        items.setAdapter(adapter);
    }

    public void updateCart(JSONArray json) {
        CustomListAdapterCart adapter = null;
        try {
            adapter = new CustomListAdapterCart(Shelter.this, json);
            Double temp = 0.0;///Obtener el total (Puede ser mejor definitivamente)
            if (IS_DEBUG)
                new AlertDialog.Builder(Shelter.this).setMessage(json.toString()).show();
            if (!json.isNull(0)) {
                itemsCart.setEnabled(true);
                empty.setEnabled(true);
                checkout.setEnabled(true);
                for (int i = 0; i < json.length(); i++) {
                    temp += json.getJSONObject(i).getDouble("priceperitem") * json.getJSONObject(i).getInt("quantity");
                }
            } else {
                try {
                    adapter = new CustomListAdapterCart(Shelter.this, new JSONArray("[{\"brand\":\"\",\"image\":\"product.png\",\"currency\":" +
                            "\"\",\"product\":\"No hay productos\",\"user\":\"gustavo\",\"quantity\":0,\"priceperitem\":0.0}]"));
                    itemsCart.setEnabled(false);
                    empty.setEnabled(false);
                    checkout.setEnabled(false);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
            total.setText("Total: " + temp + "");
        } catch (JSONException e) {
            new AlertDialog.Builder(Shelter.this).setMessage(e.toString()).show();
            e.printStackTrace();
            try {
                itemsCart.setEnabled(false);
                empty.setEnabled(false);
                checkout.setEnabled(false);
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
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HttpRequest delete = new HttpRequest("put", DEFAULT_DOMAIN + "/AEEcommerce/webresources/user/deleteCart/" + USER_KEY) {
                                    @Override
                                    protected void onPostExecute(String s) {
                                        updateCart(new JSONArray());
                                    }
                                };
                                delete.execute("userid:" + USER_ID);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        //////////////////////////////////////////////////////Hacer la compra al presionar el boton comprar
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//evento onclick
                final EditText tarjeta = new EditText(Shelter.this);
                new AlertDialog.Builder(Shelter.this).setTitle("Confirm")//Mostramos un alert dialog de confirmación con un número de tarjeta
                        .setMessage("¿Seguro que desea concretar su compra?")
                        .setView(tarjeta)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {     //Si el usuario acepta (da clic en aceptar)
                                Card card = new Card(tarjeta.getText().toString(), 12, 2017, "123");
                                Stripe stripe = null;
                                try {
                                    stripe = new Stripe("pk_test_9PEolhrHd4neqTqmO4awlxNw");
                                } catch (AuthenticationException e) {
                                    e.printStackTrace();
                                }
                                stripe.createToken(//se manda la peticion a Stripe
                                        card,
                                        new TokenCallback() {
                                            public void onSuccess(Token token) {//Si todo sale bien se manda la peticion al webservice de checkout
                                                HttpRequest buy = new HttpRequest("post", DEFAULT_DOMAIN + "/AEEcommerce/webresources/user/checkout") {
                                                    @Override
                                                    protected void onPostExecute(String s) {
                                                        try {
                                                            if (s.contains("\"code\":404")) {
                                                                new AlertDialog.Builder(Shelter.this).setMessage("Servicio no disponible").show();
                                                            } else if (s.contains("\"code\":401")) {
                                                                new AlertDialog.Builder(Shelter.this).setMessage("Error de autenticación").show();
                                                            }
                                                            else if (s.contains("\"code\":400")) {
                                                                new AlertDialog.Builder(Shelter.this).setMessage("Datos no válidos, revise el stock disponible").show();
                                                            }
                                                            else if (s.contains("\"code\":500")) {
                                                                new AlertDialog.Builder(Shelter.this).setMessage("Error del servidor, intentelo de nuevo").show();
                                                            } else {
                                                                new AlertDialog.Builder(Shelter.this).setMessage("venta realizada con éxito")
                                                                        .setTitle("Aviso!")
                                                                        .show();
                                                                updateCart(new JSONArray());
                                                                HttpRequest fillList = new HttpRequest("get", DEFAULT_DOMAIN + "/AEEcommerce/webresources/product/getProducts/" + USER_ID + "/" + USER_KEY) {
                                                                    @Override
                                                                    protected void onPostExecute(String s) {
                                                                        try {
                                                                            updateProducts(new JSONArray(s));
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                };
                                                                fillList.execute();

                                                            }

                                                            //updateCart(new JSONArray(""));
                                                        } catch (Exception e) {

                                                            e.printStackTrace();
                                                            new AlertDialog.Builder(Shelter.this).setMessage(s).show();
                                                        }
                                                    }
                                                };
                                                buy.execute("userid:" + USER_ID, "apikey:" + USER_KEY, "token:" + token.getId());
                                            }

                                            public void onError(Exception error) {
                                                // Show localized error message
                                                Toast.makeText(Shelter.this,
                                                        error.toString(),
                                                        Toast.LENGTH_LONG
                                                ).show();
                                            }
                                        }
                                );

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }


}

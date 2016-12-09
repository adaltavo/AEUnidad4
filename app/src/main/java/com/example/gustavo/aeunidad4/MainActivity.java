package com.example.gustavo.aeunidad4;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.gustavo.aeunidad4.util.HttpRequest;
import com.example.gustavo.aeunidad4.util.User;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private String usuario;
    private String password;
    ImageView imagen;
    EditText user, pass;
    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startActivity(new Intent(this,Shelter.class));

        imagen = (ImageView) findViewById(R.id.imageView);
        user = (EditText) findViewById(R.id.editText);
        pass = (EditText) findViewById(R.id.editText2);
        boton = (Button) findViewById(R.id.button);


        final User basededatos = new User(this);//



        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 usuario = user.getText().toString();
                 password = pass.getText().toString();
                /* El metodo getText() obtiene el dato escrito con el metodo toString()
                    se convierte a String para poder manipularlo como tal, por ultimo
                    se muestra en el textView con el metodo .setText()
                  */

                HttpRequest request = new HttpRequest("get",Shelter.DEFAULT_DOMAIN + "/AEEcommerce/webresources/user/login/" + usuario +"/"+ password ){
                    @Override
                    protected void onPostExecute(String s) {

                        try {

                            JSONObject json = new JSONObject(s);
                            Shelter.USER_ID = json.getString("userid");
                            Shelter.USER_KEY = json.getString("apikey");

                            SQLiteDatabase db = basededatos.getWritableDatabase();
                            int useri= json.getInt("userid");
                            String username = json.getString("username");
                            String pass = json.getString("password");
                            String phone = json.getString("phone");
                            String neigborhood = json.getString("neigborhood");
                            String zipcode = json.getString("zipcode");
                            String city = json.getString("city");
                            String country = json.getString("country");
                            String state = json.getString("state");
                            String region = json.getString("region");
                            String street = json.getString("street");
                            String email = json.getString("email");
                            String streetnumber = json.getString("streetnumber");
                            String photo = json.getString("photo");
                            String cellphone = json.getString("cellphone");
                            String companyid = json.getString("companyid");
                            String roleid = json.getString("roleid");
                            String gender = json.getString("gender");
                            String apikey = json.getString("apikey");

                            ContentValues values = new ContentValues();
                            values.put("userid",useri);
                            values.put("username",username);
                            values.put("password",pass);
                            values.put("phone",phone);
                            values.put("neigborhood",neigborhood);
                            values.put("zipcode",zipcode);
                            values.put("city",city);
                            values.put("country",country);
                            values.put("state",state);
                            values.put("region",region);
                            values.put("street",street);
                            values.put("email",email);
                            values.put("streetnumber",streetnumber);
                            values.put("photo",photo);
                            values.put("cellphone",cellphone);
                            values.put("companyid",companyid);
                            values.put("roleid", roleid);
                            values.put("gende",gender);
                            values.put("apikey",apikey);

                            db.insert("user", null, values);
                            db.close();

                            startActivity(new Intent(MainActivity.this,Shelter.class));
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            new AlertDialog.Builder(MainActivity.this).setMessage(s).show();

                        }


                    }
                } ;
                request.execute();

            }
        });



    }






}

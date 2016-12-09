package com.example.gustavo.aeunidad4;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.gustavo.aeunidad4.util.HttpRequest;

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

package com.example.lab3_20190212;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppActivity extends AppCompatActivity{
    private SensorManager sensorManager;
    private Sensor ace;
    private Sensor mang;
    private float[] floatsgra = new float[3];
    private float[] floatmag = new float[3];
    private float[] orient = new float[3];
    private float[] rota = new float[9];
    private long lastTime = 0;
    private float lastAccX = 0;
    private float lastAccY = 0;
    private float lastAccZ = 0;
    private float velX = 0;
    private float velY = 0;
    private float velZ = 0;

    private double velocidad;
    List<Lista> acele, mag;
    int estado = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Button cambio = findViewById(R.id.button);
        ImageView ojo = findViewById(R.id.imageView);
        ojo.setOnClickListener(v -> {
            if(estado==1){
                AlertDialog1();
            }else{
                AlertDialog2();
            }
        });
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ace = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mang = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        SensorEventListener sensorEventListenerAce = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                floatsgra = event.values;
                SensorManager.getRotationMatrix(rota,null,floatsgra,floatmag);
                SensorManager.getOrientation(rota,orient);
                if(estado==0){
                    RecyclerView recyclerView = findViewById(R.id.vistab);
                    recyclerView.setAlpha((float) ((orient[0]+3.14)/6.28));
                }
                float accX = event.values[0];
                float accY = event.values[1];
                float accZ = event.values[2];
                long currentTime = System.currentTimeMillis();
                long deltaTime = currentTime - lastTime;
                velX = (accX - lastAccX) * (deltaTime);
                velY = (accY - lastAccY) * (deltaTime);
                velZ = (accZ - lastAccZ) * (deltaTime);
                velocidad = Math.sqrt(Math.pow(velX,2)+Math.pow(velY,2)+Math.pow(velZ,2))/1000;
                if(velocidad>4 && estado ==1 && velocidad<30){
                    Toast.makeText(getApplicationContext(), "Velocidad:" + String.format("%.2f",velocidad) + "m/s2", Toast.LENGTH_SHORT).show();
                }
                Log.d("orientacion", String.valueOf(velocidad));
                lastAccX = accX;
                lastAccY = accY;
                lastAccZ = accZ;
                lastTime = currentTime;

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        SensorEventListener sensorEventListenerMag = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                floatmag = event.values;
                SensorManager.getRotationMatrix(rota,null,floatsgra,floatmag);
                SensorManager.getOrientation(rota,orient);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(sensorEventListenerAce,ace,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListenerMag,mang,SensorManager.SENSOR_DELAY_GAME);
        acele = new ArrayList<>();
        mag = new ArrayList<>();
        Button agregar = findViewById(R.id.button1);
        agregar.setOnClickListener(v -> {
            agregar.setEnabled(false);
            agregar.setAlpha(0.1F);
            cambio.setEnabled(false);
            String url = "https://randomuser.me/api/";
            StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray resultsArray = jsonObject.getJSONArray("results");
                            JSONObject firstResult = resultsArray.getJSONObject(0);

                            String firstName = firstResult.getJSONObject("name").getString("first");
                            String lastName = firstResult.getJSONObject("name").getString("last");
                            String fullName = firstName + " " + lastName;

                            String email = "Correo:"+ firstResult.getString("email");

                            String country ="Pais:" + firstResult.getJSONObject("location").getString("country");
                            String city = "Ciudad:"+ firstResult.getJSONObject("location").getString("city");

                            String phone ="Celular:"+ firstResult.getString("phone");

                            String gender = "Genero:"+ firstResult.getString("gender");

                            String photoUrl = firstResult.getJSONObject("picture").getString("large");
                            if(estado==1){
                                acele.add(new Lista(fullName,gender,city,country,email,phone,photoUrl));
                            }else {
                                mag.add(new Lista(fullName,gender,city,country,email,phone,photoUrl));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        error.printStackTrace();
                    }
            );

            Volley.newRequestQueue(this).add(getRequest);
            if(estado == 1){
                vistaace();
            }else {
                vistamag();
            }
            agregar.setEnabled(true);
            agregar.setAlpha(1F);
            cambio.setEnabled(true);
        });
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Fragment magnetometro = new Magnetometro();
        Fragment ace = new Acelerometro();
        cambio.setOnClickListener(v -> {
            if(estado==1){
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainerView, magnetometro);
                transaction.commit();
                estado = 0;
                cambio.setText("Ir a Acelerometro");
                vistamag();
            }else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainerView, ace);
                transaction.commit();
                estado = 1;
                cambio.setText("Ir a Magnetometro");
                vistaace();
            }
        });
    }

    private void AlertDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Detalle - Acelerometro")
                .setMessage("Jex no seas malito, mucho texto estas obligadnos a colocar como descripcion pipipipi");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void AlertDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Detalle - Magnetometro")
                .setMessage("Jex no seas malito, mucho texto estas obligadnos a colocar como descripcion pipipipi");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

     public void vistaace(){
        Adaptador adaptador = new Adaptador(acele,this);
         RecyclerView recyclerView = findViewById(R.id.vistaa);
         if (recyclerView != null) {
             recyclerView.setHasFixedSize(true);
             recyclerView.setLayoutManager(new LinearLayoutManager(this));
             recyclerView.setAdapter(adaptador);
         }
     }

    public void vistamag(){
        Adaptador adaptador = new Adaptador(mag,this);
        RecyclerView recyclerView = findViewById(R.id.vistab);
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adaptador);
        }
    }

}
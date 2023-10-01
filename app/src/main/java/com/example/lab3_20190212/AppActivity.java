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

public class AppActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor acelerometro;
    private long lastUpdate = 0;
    private float lastX, lastY, lastZ;
    private static final int SHAKE_THRESHOLD = 1000;
    private static final long TIME_INTERVAL = 500; // Intervalo de tiempo deseado en milisegundos (0.5 segundos)

    private boolean isShaking = false;
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
            }else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContainerView, ace);
                transaction.commit();
                estado = 1;
                cambio.setText("Ir a Magnetometro");
                acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastUpdate) > TIME_INTERVAL) {
                long timeDifference = (currentTime - lastUpdate);
                lastUpdate = currentTime;

                float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / timeDifference * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    // Display a Toast with the acceleration
                    String msg = "Tu Aceleracion: " + speed + "m/s^2";
                    Toast.makeText(AppActivity.this, msg, Toast.LENGTH_SHORT).show();

                }

                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
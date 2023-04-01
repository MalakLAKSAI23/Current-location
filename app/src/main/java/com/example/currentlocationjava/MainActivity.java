package com.example.currentlocationjava;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button bt_location;
    TextView textView1, textView2, textView3, textView4, textView5;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_location = findViewById(R.id.bt_location);
        textView1 = findViewById(R.id.text_view1);
        textView2 = findViewById(R.id.text_view2);
        textView3 = findViewById(R.id.text_view3);
        textView4 = findViewById(R.id.text_view4);
        textView5 = findViewById(R.id.text_view5);
        bt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getLocation();
            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


    }
    public void getLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 44);
        } else {
            //Récupération de la dernière localisation
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        try {
//initialiser geocoder
                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
//initialiser l’adresse de localisation
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                                    location.getLongitude(), 1);
//afficher la latitude dans le textview
                            textView1.setText(Html.fromHtml("<font color='#6200EE'><b>Latitude : </b><br></font>" + addresses.get(0).getLatitude()));

                            // afficher la longitude dans le textview
                            textView2.setText(Html.fromHtml(
                                    "<font color='#6200EE'><b>Longitude : </b><br></font>"
                                            + addresses.get(0).getLongitude()
                            ));
// afficher le pays dans le textview
                            textView3.setText(Html.fromHtml(
                                    "<font color='#6200EE'><b>Nom de pays : </b><br></font>"
                                            + addresses.get(0).getCountryName()
                            ));
// afficher la localité dans le textview
                            textView4.setText(Html.fromHtml(
                                    "<font color='#6200EE'><b>Localité : </b><br></font>"
                                            + addresses.get(0).getLocality()
                            ));
// afficher l’adresse dans le textview
                            textView5.setText(Html.fromHtml(
                                    "<font color='#6200EE'><b>Adresse : </b><br></font>"
                                            + addresses.get(0).getAddressLine(0)
                            ));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Aucune position enregistrée",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            });
            fusedLocationProviderClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }
}
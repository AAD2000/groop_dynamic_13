package com.example.client;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {


    int REQUEST_CODE=1;
    Person account;
    GoogleMap mMap;
    Integer selected = -1;
    Integer from = -1;
    Integer to = -1;
    ArrayList<Marker> markers = new ArrayList<Marker>();
    String[] tittles;
    SharedPreferences nickname;
    SharedPreferences password;
    final String nn="";
    final String pw="";


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        markers.add(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(56.852502, 53.211487))
                .title("Центральная площадь")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(56.875877, 53.205766))
                .title("ТРЦ Талисман")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(56.872374, 53.292187))
                .title("ТРК Петровский")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(56.861238, 53.177036))
                .title("Парк имени Кирова")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(56.845687, 53.199847))
                .title("Парк имени Горького")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(56.847458, 53.203977))
                .title("Аксион")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(56.865512, 53.175523))
                .title("Зоопарк")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(56.845735, 53.288988))
                .title("ТЦ Кит")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .position(new LatLng(56.870221, 53.280253))
                .title("ТРЦ Столица")));

        tittles = new String[markers.size()];
        for (int i = 0; i < markers.size(); i++) {
            tittles[i] = markers.get(i).getTitle();
        }

        for (Marker m : markers) {
            m.setAlpha(0.5f);

            m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(56.852502, 53.211487), 12.0f));

        mMap.setOnMarkerClickListener(v -> {
            Button order = (Button) findViewById(R.id.order_button);

            order.setVisibility(View.INVISIBLE);
            if (selected != -1)
                markers.get(selected).setAlpha(0.5f);
            v.showInfoWindow();
            v.setAlpha(1f);
            selected = markers.indexOf(v);
            LinearLayout selectButton = (LinearLayout) findViewById(R.id.buttons_to_order);
            selectButton.setVisibility(View.VISIBLE);
            return true;
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        account = new Person("Alex", "Danilov", "aUTKFTHFFUIYFUOFYIKFYUad", "+79130105461", "123123123");


        LinearLayout selectButton = (LinearLayout) findViewById(R.id.buttons_to_order);
        selectButton.setVisibility(View.INVISIBLE);

        Button from_button = (Button) findViewById(R.id.from_button);
        Button to_button = (Button) findViewById(R.id.to_button);

        from_button.setOnClickListener(b -> {
            Button order = (Button) findViewById(R.id.order_button);
            order.setVisibility(View.INVISIBLE);
            if (from == -1) {
                markers.get(selected).setAlpha(1f);
                from = selected;
                selected = -1;
                markers.get(from).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            } else {
                markers.get(from).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                markers.get(from).setAlpha(0.5f);
                markers.get(selected).setAlpha(1f);
                from = selected;
                selected = -1;
                markers.get(from).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
            selectButton.setVisibility(View.INVISIBLE);
            if (from != -1 && to != -1 && !markers.get(from).getTitle().equals(markers.get(to).getTitle())) {
                order.setVisibility(View.VISIBLE);
            }
        });

        to_button.setOnClickListener(b -> {
            Button order = (Button) findViewById(R.id.order_button);
            order.setVisibility(View.INVISIBLE);
            if (to == -1) {
                markers.get(selected).setAlpha(1f);
                to = selected;
                selected = -1;
                markers.get(to).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            } else {
                markers.get(to).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                markers.get(to).setAlpha(0.5f);
                markers.get(selected).setAlpha(1f);
                to = selected;
                selected = -1;
                markers.get(to).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
            selectButton.setVisibility(View.INVISIBLE);
            if (from != -1 && to != -1 && !markers.get(from).getTitle().equals(markers.get(to).getTitle())) {
                order.setVisibility(View.VISIBLE);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button order = (Button) findViewById(R.id.order_button);
        order.setVisibility(View.INVISIBLE);
        order.setOnClickListener(b -> {
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("tittles", tittles);
            intent.putExtra("intFrom", from);
            intent.putExtra("intTo", to);
            startActivity(intent);

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                TextView ns=(TextView)drawerView.findViewById(R.id.nav_name_surname);
                ns.setText(account.getName()+" "+account.getSurname());
                TextView tph=(TextView)drawerView.findViewById(R.id.nav_phone);
                tph.setText(account.getTelephone());
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            String[]arr=data.getStringArrayExtra("data");
            if(arr!=null) {
                account.setName(arr[0]);
                account.setSurname(arr[1]);
                account.setTelephone(arr[2]);
            }
            //TextView ns=(TextView)findViewById(R.id.nav_name_surname);
            //ns.setText(account.getName()+" "+account.getSurname());
            //TextView tph=(TextView)findViewById(R.id.nav_phone);
            //tph.setText(account.getTelephone());

        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_person) {
            Intent intent = new Intent(this, PersonData.class);
            intent.putExtra("data", account);
            startActivityForResult(intent,REQUEST_CODE);
        }

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_history) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

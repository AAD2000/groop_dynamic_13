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
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    GoogleMap mMap;
    Marker selected;
    Marker from;
    Marker to;
    ArrayList<Marker> markers=new ArrayList<Marker>();
    @Override

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


// Add a marker in Sydney and move the camera
        markers.add(mMap.addMarker(new MarkerOptions()
                .snippet("Ижевск, " +
                        "республика Удмуртия, " +
                        "426008")
                .position(new LatLng(56.852502, 53.211487))
                .title("Центральная площадь")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .snippet("Ижевск, " +
                        "республика Удмуртия, " +
                        "426011")
                .position(new LatLng(56.875877, 53.205766))
                .title("ТРЦ Талисман")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .snippet("Ижевск, " +
                        "республика Удмуртия")
                .position(new LatLng(56.872374, 53.292187))
                .title("ТРК Петровский")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .snippet("Ижевск, " +
                        "республика Удмуртия, " +
                        "426033")
                .position(new LatLng(56.861238, 53.177036))
                .title("Парк имени Кирова")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .snippet("Ижевск, " +
                        "республика Удмуртия, " +
                        "426051")
                .position(new LatLng(56.845687, 53.199847))
                .title("Парк имени Горького")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .snippet("Ижевск, " +
                        "республика Удмуртия, " +
                        "426057")
                .position(new LatLng(56.847458, 53.203977))
                .title("Аксион")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .snippet("Ижевск, " +
                        "республика Удмуртия, " +
                        "426033")
                .position(new LatLng(56.865512, 53.175523))
                .title("Зоопарк")));
        markers.add(mMap.addMarker(new MarkerOptions()
                .snippet("Ижевск, " +
                        "республика Удмуртия, " +
                        "426075")
                .position(new LatLng(56.845735, 53.288988))
                .title("ТЦ Кит")));

        markers.add(mMap.addMarker(new MarkerOptions()
                .snippet("Ижевск, " +
                        "республика Удмуртия, " +
                        "426065")
                .position(new LatLng(56.870221, 53.280253))
                .title("ТРЦ Столица")));

        for (Marker m :markers) {
            m.setAlpha(0.5f);
            m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(56.852502, 53.211487), 12.0f));

        mMap.setOnMarkerClickListener(v -> {
            Button order =(Button)findViewById(R.id.order_button);

  order.setVisibility(View.INVISIBLE);
            if(selected!=null)
                selected.setAlpha(0.5f);
            v.showInfoWindow();
            v.setAlpha(1f);
            selected=v;
            LinearLayout selectButton= (LinearLayout)findViewById(R.id.buttons_to_order);
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

        LinearLayout selectButton= (LinearLayout)findViewById(R.id.buttons_to_order);
        selectButton.setVisibility(View.INVISIBLE);

        Button from_button =(Button)findViewById(R.id.from_button);
        Button to_button =(Button)findViewById(R.id.to_button);

        from_button.setOnClickListener(b->{
            Button order =(Button)findViewById(R.id.order_button);
            order.setVisibility(View.INVISIBLE);
            if(from==null) {
                selected.setAlpha(1f);
                from = selected;
                selected = null;
                from.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }else{
                from.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                from.setAlpha(0.5f);
                selected.setAlpha(1f);
                from = selected;
                selected = null;
                from.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
            selectButton.setVisibility(View.INVISIBLE);
            if(from!=null && to!=null && !from.getTitle().equals(to.getTitle())) {
                order.setVisibility(View.VISIBLE);
            }
        });

        to_button.setOnClickListener(b->{
            Button order =(Button)findViewById(R.id.order_button);
            order.setVisibility(View.INVISIBLE);
            if(to==null) {
                selected.setAlpha(1f);
                to = selected;
                selected = null;
                to.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }else{
                to.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                to.setAlpha(0.5f);
                selected.setAlpha(1f);
                to = selected;
                selected = null;
                to.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }
            selectButton.setVisibility(View.INVISIBLE);
            if(from!=null && to!=null && !from.getTitle().equals(to.getTitle())){
                order.setVisibility(View.VISIBLE);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button order =(Button)findViewById(R.id.order_button);
        order.setVisibility(View.INVISIBLE);

        //order.setOnClickListener(b->{});

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_person) {
            Intent intent = new Intent(this, PersonData.class);
            startActivity(intent);
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

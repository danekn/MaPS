package com.example.android.handyshop;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;

    private double latitude;
    private double longitude;
    HashMap<String,Marker> MarkerSetRequest =null;
    HashMap<String,Marker> MarkerSetOffer =null;

    ArrayList<Object> requestList=null;
    ArrayList<Object> offerList=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getActionBar().hide();


        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");


        requestList = (ArrayList<Object>) args.getSerializable("REQUESTLIST");
        offerList = (ArrayList<Object>) args.getSerializable("OFFERLIST");

        if (requestList !=null ) {

            System.out.println("Marker to view: " + requestList.size());
            for (int i = 0; i < requestList.size(); i++) {

                Request r = (Request) requestList.get(i);
                System.out.println("TITOLO" + i + "RICHIESTA");
                System.out.println(r.getTitle());
                System.out.println("LONGITUDINE" + i + "RICHIESTA");
                System.out.println(r.getLongitude());
                System.out.println("LONGITUDINE" + i + "RICHIESTA");
                System.out.println(r.getLatitude());

            }
        }

            if (offerList !=null ) {

                System.out.println("Marker to view: " + offerList.size());
                for (int i = 0; i < offerList.size(); i++) {

                    Offer o = (Offer) offerList.get(i);
                    System.out.println("TITOLO" + i + "OFFERTA");
                    System.out.println(o.getTitle());
                    System.out.println("LONGITUDINE" + i + "OFFERTA");
                    System.out.println(o.getLongitude());
                    System.out.println("LONGITUDINE" + i + "OFFERTA");
                    System.out.println(o.getLatitude());

                }
            }
            String lon2 = args.getString("MYLONGITUDE");
            longitude = Double.parseDouble(lon2);
            String lat2 = args.getString("MYLATITUDE");
            latitude = Double.parseDouble(lat2);



        }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in your position and move the camera
        LatLng myPos = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(myPos).title("Your Position"));

        //setting MARKER OFFERTE
        if(MarkerSetOffer!=null) {
            Iterator it = MarkerSetOffer.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Marker MK = MarkerSetOffer.get(entry.getKey());
                MK.remove();
            }
        }


        if(offerList != null) {
            MarkerSetOffer = new HashMap<>(offerList.size());

            for (int i = 0; i < offerList.size(); i++) {
                Offer o = (Offer) offerList.get(i);
                LatLng reqPos = new LatLng(o.getLatitude()*180/Math.PI, o.getLongitude()*180/Math.PI);
                Marker M = mMap.addMarker(new MarkerOptions().position(reqPos).title(o.getTitle())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                //TODO sostituire titolo-id richiesta
                MarkerSetOffer.put(o.getTitle(), M);


            }
        }

        //SETTING MARKERS RICHIESTE
        if(MarkerSetRequest!=null) {
            Iterator it = MarkerSetRequest.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                Marker MK = MarkerSetRequest.get(entry.getKey());
                MK.remove();
            }
        }



        if(requestList != null) {
            MarkerSetRequest = new HashMap<>(requestList.size());

            for (int i = 0; i < requestList.size(); i++) {
                Request r = (Request) requestList.get(i);
                LatLng reqPos = new LatLng(r.getLatitude()*180/Math.PI, r.getLongitude()*180/Math.PI);
                Marker M = mMap.addMarker(new MarkerOptions().position(reqPos).title(r.getTitle())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                //TODO sostituire titolo-id richiesta
                MarkerSetRequest.put(r.getTitle(), M);


            }
        }



        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 15.0f ) );
    }



    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
    }
}

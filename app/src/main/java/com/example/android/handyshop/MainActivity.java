package com.example.android.handyshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;


import java.io.Serializable;

import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;


import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import junit.framework.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements LocationListener {


    static DatabaseReference handyShopDB;
    CollectionPagerAdapter myCollectionPagerAdapter;

    static CustomViewPager mViewPager;

    static double latitude;
    static double longitude;
    static double latitudeRad;
    static double longitudeRad;
    static String id;
    static LocationStruct ls = null;
    static List<Request> nRequestList = null;
    static List<Offer> nOfferList=null;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, this);

        myCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());

        getActionBar().hide();

        mViewPager = (CustomViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(myCollectionPagerAdapter);

        handyShopDB = FirebaseDatabase.getInstance().getReference();
        handyShopDB.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //System.out.println(snapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError DatabaseError) {
                System.out.println("The read failed: " + DatabaseError.getMessage());
            }
        });

        nRequestList = new ArrayList<Request>();
        nOfferList=new ArrayList<Offer>();
    }


    @Override
    public void onLocationChanged(Location location) {


        latitude = (location.getLatitude());
        latitudeRad = (latitude * Math.PI) / 180;
        longitude = (location.getLongitude());
        longitudeRad = (longitude * Math.PI) / 180;

        System.out.println("RADIANTI Latitude: " + latitudeRad + ", Longitude: " + longitudeRad);
        System.out.println("latitude  "+latitude+"longitude  "+longitude);

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();

    }


    public static class CollectionPagerAdapter extends FragmentStatePagerAdapter {

        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            //Bundle args=null;
            switch (i) {
                case 0:
                    fragment = new ActivityFragment();
                    return fragment;
                case 1:
                    fragment = new HomeFragment();
                    return fragment;
                case 2:
                    fragment = new RequestsFragment();
                    return fragment;
                case 3:
                    fragment = new OffersFragment();
                    return fragment;
                case 4:
                    fragment = new InsertFragment();
                    return fragment;
                default:
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Activity";

                case 1:
                    return "Home";

                case 2:
                    return "Requests";

                case 3:
                    return "Offers";

                case 4:
                    return "Insert";

                default:
                    return "";
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }


    public static class HomeFragment extends Fragment {


        Button insert_button = null;
        Button telegram_button=null;
        View rootView = null;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            rootView = inflater.inflate(R.layout.home, container, false);
            insert_button = (Button) rootView.findViewById(R.id.insert_button);
            insert_button.setOnClickListener(insert);
            telegram_button= (Button) rootView.findViewById(R.id.telegram_chat);
            telegram_button.setOnClickListener(telegram_mes);

            return rootView;
        }

        private void buildAlertMessageNoGps() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                Context c=getContext();
                final LocationManager manager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);

                if ( !manager.isProviderEnabled(LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                }


            }
        }

        View.OnClickListener insert = new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MapsActivity.class);
                Bundle args = new Bundle();
                if(nRequestList!=null) {
                    args.putSerializable("REQUESTLIST", (Serializable) nRequestList);
                }

                if(nOfferList!=null) {
                    args.putSerializable("OFFERLIST", (Serializable) nOfferList);
                }

                String lat2 = String.valueOf(latitude);
                String lon2 = String.valueOf(longitude);
                args.putString("MYLATITUDE",lat2);
                args.putString("MYLONGITUDE",lon2);

                intent.putExtra("BUNDLE",args);

                startActivity(intent);

            }
        };


        View.OnClickListener telegram_mes = new View.OnClickListener() {
            public void onClick(View v) {
                final String appName = "org.telegram.messenger";
                String msg="ciao";
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                myIntent.setPackage(appName);
                myIntent.putExtra(Intent.EXTRA_TEXT, msg);//
                startActivity(Intent.createChooser(myIntent, "Share with"));


            }
        };



    }

    public static class RequestsFragment extends Fragment {

        SparseArray<Group> requestsList = new SparseArray<Group>();
        ExpandableRequestsOffersListAdapter adapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            View rootView = null;
            rootView = inflater.inflate(R.layout.requests, container, false);
            //list = buildData();
            ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listView);
            adapter = new ExpandableRequestsOffersListAdapter(getActivity(), requestsList);
            listView.setAdapter(adapter);
            return rootView;
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {


                ls = ((MainActivity)getActivity()).computeRadius(latitudeRad,longitudeRad,10);

                final DatabaseReference ref = handyShopDB.child("requests");
                Query queryRef = ref.orderByChild("latitude").startAt(ls.getMinLat()).endAt(ls.getMaxLat());

                requestsList.clear();

                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (snapshot.getChildrenCount() == 0)
                        {
                            System.out.println("niente req");
                            return;
                        }
                        else
                        {
                            System.out.println("req esistenti");
                            for (DataSnapshot d : snapshot.getChildren()) {
                                String x=d.getKey();
                                System.out.println(snapshot);

                                Request req = d.getValue(Request.class);
                                if(req.getLongitude()<=ls.getMaxLon() && req.getLongitude() >= ls.getMinLon())
                                {
                                    double dist=computeDistance(req.getLatitude(),req.getLongitude(),latitudeRad,longitudeRad);

                                    Group group = new Group(req.getTitle()+"  "+(Math.floor(dist * 100)/100)+" Km");

                                    group.children.add("Category");
                                    group.children.add(req.getCategory());
                                    group.children.add("Subcategory");
                                    group.children.add(req.getSubCategory());
                                    group.children.add("Description");
                                    group.children.add(req.getDescription());
                                    group.children.add("Email");
                                    group.children.add(x);
                                    requestsList.append(requestsList.size(), group);


                                    nRequestList.add(req);

                                    System.out.println("NUMERO RICHIESTE"+nRequestList.size());
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError f) {
                    }
                });

            }
            else{
                if(adapter!=null) {
                    requestsList.clear();
                    adapter.notifyDataSetChanged();
                }
            }


        }
    }


    public static class OffersFragment extends Fragment {
        View rootView = null;
        SparseArray<Group> offersList = new SparseArray<Group>();
        ExpandableRequestsOffersListAdapter adapter;
        Spinner spinner = null;
        ArrayAdapter<String> spinnerAdapter = null;
        String text_category=null;



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            rootView = inflater.inflate(R.layout.offers, container, false);

            ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listView);

            adapter = new ExpandableRequestsOffersListAdapter(getActivity(), offersList);
            listView.setAdapter(adapter);


            spinner = (Spinner) rootView.findViewById(R.id.category_choice_filter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    spinner = (Spinner) rootView.findViewById(R.id.category_choice_filter);
                    offersList.clear();
                    adapter.notifyDataSetChanged();
                    text_category = spinner.getSelectedItem().toString();
                    System.out.println(text_category);

                    ls = ((MainActivity) getActivity()).computeRadius(latitudeRad, longitudeRad, 10);

                    final DatabaseReference ref = handyShopDB.child("offers");
                    Query queryRef = ref.orderByChild("latitude").startAt(ls.getMinLat()).endAt(ls.getMaxLat());


                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            offersList.clear();
                            adapter.notifyDataSetChanged();

                            if (snapshot.getChildrenCount() == 0)
                                return;
                            else {
                                for (DataSnapshot d : snapshot.getChildren()) {
                                    String x = d.getKey();
                                    Offer off = d.getValue(Offer.class);
                                    if(text_category.equals("All"))
                                    {

                                        if (off.getLongitude() <= ls.getMaxLon() && off.getLongitude() >= ls.getMinLon()) {
                                            double dist = computeDistance(off.getLatitude(), off.getLongitude(), latitudeRad, longitudeRad);
                                            Group group = new Group(off.getTitle() + "  " + (Math.floor(dist * 100) / 100) + " Km");
                                            group.children.add("Category");
                                            group.children.add(off.getCategory());
                                            group.children.add("Subcategory");
                                            group.children.add(off.getSubCategory());
                                            group.children.add("Description");
                                            group.children.add(off.getDescription());
                                            group.children.add("Email");
                                            group.children.add(x);
                                            offersList.append(offersList.size(), group);
                                            System.out.println(group.children);
                                            double dist2 = computeDistance(latitude, longitude, latitude + 20, longitude + 50);
                                            nOfferList.add(off);
                                    }

                                    }

                                    else {
                                        offersList.clear();
                                        adapter.notifyDataSetChanged();
                                        if (off.getLongitude() <= ls.getMaxLon() && off.getLongitude() >= ls.getMinLon() && off.getCategory().equals(text_category)) {
                                            double dist = computeDistance(off.getLatitude(), off.getLongitude(), latitudeRad, longitudeRad);
                                            Group group = new Group(off.getTitle() + "  " + (Math.floor(dist * 100) / 100) + " Km");
                                            group.children.add("Category");
                                            group.children.add(off.getCategory());
                                            group.children.add("Subcategory");
                                            group.children.add(off.getSubCategory());
                                            group.children.add("Description");
                                            group.children.add(off.getDescription());
                                            group.children.add("Email");
                                            group.children.add(x);
                                            offersList.append(offersList.size(), group);
                                            System.out.println(group.children);
                                            System.out.println("CAMPI");
                                            System.out.println(group.children.get(0));
                                            System.out.println(group.children.get(1));
                                            System.out.println(group.children.get(2));
                                            System.out.println(group.children.get(3));
                                            System.out.println(group.children.get(4));
                                            System.out.println(group.children.get(5));
                                            System.out.println(group.children.get(6));
                                            System.out.println(group.children.get(7));




                                            double dist2 = computeDistance(latitude, longitude, latitude + 20, longitude + 50);
                                            nOfferList.add(off);
                                        }

                                    }


                                }
                                adapter.notifyDataSetChanged();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError f) {
                        }
                    });


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }

            });


            return rootView;
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);

            if (isVisibleToUser) {

            }
        }
    }


    public static class InsertFragment extends Fragment {

        public static final String ARG_OBJECT = "object";

        View rootView = null;
        Spinner spinner_category = null;
        Spinner spinner_subcategory = null;
        ArrayList<String> subcategory_choices = null;
        ArrayAdapter<String> spinnerAdapter = null;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            rootView = inflater.inflate(R.layout.insert, container, false);

            Spinner spinner = (Spinner) rootView.findViewById(R.id.category_choice);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    spinner_category = (Spinner) rootView.findViewById(R.id.category_choice);

                    spinner_subcategory = (Spinner) rootView.findViewById(R.id.subcategory_choice);
                    String text_category = spinner_category.getSelectedItem().toString();
                    //System.out.println(text_category);

                    subcategory_choices = new ArrayList<>();

                    switch (text_category) {

                        case "Services":
                            subcategory_choices.add("Senior Companions");
                            subcategory_choices.add("Go to Supermarke");
                            subcategory_choices.add("Others");

                            spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_item, subcategory_choices);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_subcategory.setAdapter(spinnerAdapter);
                            spinnerAdapter.notifyDataSetChanged();
                            break;
                        case "Rentals":
                            subcategory_choices.add("Auto/Pick UP");
                            subcategory_choices.add("Bike");
                            subcategory_choices.add("Moto");
                            subcategory_choices.add("");

                            spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_item, subcategory_choices);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_subcategory.setAdapter(spinnerAdapter);
                            spinnerAdapter.notifyDataSetChanged();
                            break;

                        case "Tools":
                            subcategory_choices.add("Repairs");
                            subcategory_choices.add("Music");
                            subcategory_choices.add("Sport");
                            subcategory_choices.add("Carpeteing");

                            spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_item, subcategory_choices);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner_subcategory.setAdapter(spinnerAdapter);
                            spinnerAdapter.notifyDataSetChanged();
                            break;
                        default:
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });


            return rootView;
        }
    }


    public static class ActivityFragment extends Fragment {

        SparseArray<Group> activitiesList = new SparseArray<Group>();
        ExpandableRequestsOffersListAdapter adapter;

        public void getActivities(final String type){
            final DatabaseReference ref = handyShopDB.child(type);
            Query queryRef = ref.orderByChild("userId").equalTo(id);

            queryRef.addListenerForSingleValueEvent(new

            ValueEventListener() {
                @Override
                public void onDataChange (DataSnapshot snapshot){

                    if (snapshot.getChildrenCount() == 0)
                        return;
                    else {
                        for (DataSnapshot d : snapshot.getChildren()) {

                            if(type=="requests") {
                                Request req = d.getValue(Request.class);
                                //System.out.println(request.toString());
                                Group group = new Group(req.getCategory());
                                group.children.add(req.getDescription());
                                activitiesList.append(activitiesList.size(), group);
                            }
                            else{
                                Offer off = d.getValue(Offer.class);
                                Group group = new Group(off.getCategory());
                                group.children.add(off.getDescription());
                                activitiesList.append(activitiesList.size(), group);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled (DatabaseError f){
                }
            });
         }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            View rootView = null;
            rootView = inflater.inflate(R.layout.activities, container, false);

            ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listView);
            adapter = new ExpandableRequestsOffersListAdapter(getActivity(), activitiesList);
            listView.setAdapter(adapter);
            return rootView;
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {

                activitiesList.clear();
                getActivities("requests");
                //getActivities("offers");
            }

        }
    }

    public void sendRequest(View view) {

        Spinner spinner_category = (Spinner) findViewById(R.id.category_choice);
        Spinner spinner_subcategory = (Spinner) findViewById(R.id.subcategory_choice);

        String category = spinner_category.getSelectedItem().toString();
        String subcategory = spinner_subcategory.getSelectedItem().toString();

        EditText titleEdit = (EditText) findViewById(R.id.editTitle);
        EditText descriptionEdit = (EditText) findViewById(R.id.editDescription);

        String title = titleEdit.getText().toString();
        String description = descriptionEdit.getText().toString();

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioType);
        // get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        String type = (String) radioButton.getText();
        System.out.println(type);

        switch (type) {

            case "Offer":
                DatabaseReference ref = handyShopDB.child("offers");
                Request req = new Request(id, title, category, subcategory, description, latitudeRad, longitudeRad);
                ref.push().setValue(req);
                break;

            case "Request":
                DatabaseReference re = handyShopDB.child("requests");
                Request rq = new Request(id, title, category, subcategory, description, latitudeRad, longitudeRad);
                re.push().setValue(rq);
                break;
            default:
        }

    }


    public LocationStruct computeRadius (double lat, double lon, double maxRadius){

        double radius = maxRadius/6371;
        double temp = Math.sin(radius)/Math.cos(lat);
        double deltaLon = Math.asin(temp);

        LocationStruct locationStruct = new LocationStruct();
        locationStruct.setMinLat(lat-radius);
        locationStruct.setMaxLat(lat+radius);
        locationStruct.setMinLon(lon-deltaLon);
        locationStruct.setMaxLon(lon+deltaLon);

        return locationStruct;

    }


    public static double computeDistance( double lat1, double lon1,double lat2, double lon2 ){

        double R=6371;

        double dist = Math.acos(Math.sin(lat1)
                * Math.sin(lat2) + Math.cos(lat1)
                * Math.cos(lat2) * Math.cos(lon1 - lon2))
                * R;

        return dist;
    }
}











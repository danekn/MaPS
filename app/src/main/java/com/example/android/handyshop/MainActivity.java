package com.example.android.handyshop;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActionBar;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.widget.Spinner;


import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends FragmentActivity implements LocationListener {


    static Firebase handyShopDB;
    CollectionPagerAdapter myCollectionPagerAdapter;
    static CallbackManager callbackManager;
    static CustomViewPager mViewPager;
    static AccessToken accessToken = null;
    static AccessTokenTracker accessTokenTracker = null;
    static double latitude;
    static double longitude;
    static String id;


    private GoogleApiClient client;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //localization
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 12000, 1000, this);

        myCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());

        getActionBar().hide();
        //set adapter for viewPager
        mViewPager = (CustomViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(myCollectionPagerAdapter);
        //set Database and callbacks
        Firebase.setAndroidContext(this);
        handyShopDB = new Firebase("https://amber-torch-5366.firebaseio.com");
        handyShopDB.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //System.out.println(snapshot.getValue());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
                System.out.println("currentAccess");
                Button insert_button = (Button) findViewById(R.id.insert_button);
                if (accessToken == null) {
                    mViewPager.setPagingEnabled(false);
                    if (insert_button != null)
                        insert_button.setVisibility(View.INVISIBLE);
                    //     findViewById(R.id.header_home).setVisibility(View.INVISIBLE);
                } else {
                    mViewPager.setPagingEnabled(true);
                    if (insert_button != null)
                        insert_button.setVisibility(View.VISIBLE);
//                    findViewById(R.id.header_home).setVisibility(View.VISIBLE);
                }
            }
        };
        accessToken = AccessToken.getCurrentAccessToken();


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if (savedInstanceState == null) {
            mViewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onLocationChanged(Location location) {


        latitude = (location.getLatitude());
        longitude = (location.getLongitude());

        Log.i("Geo_Location", "Latitude: " + latitude + ", Longitude: " + longitude);
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
        // TODO Auto-generated method stub

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            locationManager.removeUpdates(locListener);*/

    }

   /* public void onChangeLocationProvidersSettingsClick(View view) {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }*/


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.android.handyshop/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }


    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.android.handyshop/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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
                    //args = new Bundle();
                    //args.putInt(HomeFragment.ARG_OBJECT, i);
                    //fragment.setArguments(args);
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

            /*final Button button_1 = (Button) rootView.findViewById(R.id.request_button);
            button_1.setOnClickListener(request);*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Button insert_button = (Button) findViewById(R.id.insert_button);
        if (accessToken == null) {
            mViewPager.setPagingEnabled(false);
            insert_button.setVisibility(View.INVISIBLE);
            findViewById(R.id.header_home).setVisibility(View.INVISIBLE);
        } else {
            mViewPager.setPagingEnabled(true);
            insert_button.setVisibility(View.VISIBLE);
            findViewById(R.id.header_home).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        accessTokenTracker.stopTracking();
        System.out.println("logout");
    }


    public static class HomeFragment extends Fragment {

        String name = null;
        String email = null;
        Button insert_button = null;
        View rootView = null;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            rootView = inflater.inflate(R.layout.home, container, false);
            insert_button = (Button) rootView.findViewById(R.id.insert_button);
            insert_button.setOnClickListener(insert);
            LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
            loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
            //loginButton.setFragment(this);
            //loginButton.setOnClickListener(login);

            if (accessToken == null) {
                mViewPager.setPagingEnabled(false);
                insert_button.setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.header_home).setVisibility(View.INVISIBLE);
            } else {
                mViewPager.setPagingEnabled(true);
                insert_button.setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.header_home).setVisibility(View.VISIBLE);
            }

            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.v("LoginActivity", response.toString());

                                    // Application code
                                    try {
                                        email = object.getString("email");
                                        id = object.getString("id");
                                        name = object.getString("name");
                                    } catch (JSONException e) {
                                    }
                                    final Firebase ref = handyShopDB.child("users");

                                    Query queryRef = ref.orderByChild("userId").equalTo(id);
                                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {

                                            if (snapshot.getChildrenCount() == 0) {
                                                User usr = new User(id, name, email);
                                                ref.push().setValue(usr);

                                            } else
                                                System.out.println("Utente gia presente");
                                        }

                                        @Override
                                        public void onCancelled(FirebaseError f) {
                                        }
                                    });
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,email");
                    request.setParameters(parameters);
                    request.executeAsync();

                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                }
            });


            return rootView;
        }


        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                System.out.println("gg");

                   /* if(accessToken==null)
                    insert_button.setVisibility(View.INVISIBLE);
                    else insert_button.setVisibility(View.INVISIBLE);*/
            }
        }

        View.OnClickListener login = new View.OnClickListener() {
            public void onClick(View v) {
                // do something here
                if (accessToken == null)
                    LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile"));
            }
        };

        View.OnClickListener insert = new View.OnClickListener() {
            public void onClick(View v) {
                // do something here
                mViewPager.setCurrentItem(4);
            }
        };


    }

    public static class RequestsFragment extends Fragment {

        LocationStruct ls=null;
        SparseArray<Group> requestsList = new SparseArray<Group>();
        MyExpandableListAdapter adapter;




        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            View rootView = null;
            rootView = inflater.inflate(R.layout.requests, container, false);
            //list = buildData();
            createData();
            ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listView);
            adapter = new MyExpandableListAdapter(getActivity(), requestsList);
            listView.setAdapter(adapter);
            return rootView;
        }

        public void createData() {
            for (int j = 0; j < 5; j++) {
                Group group = new Group("Test " + j);
                for (int i = 0; i < 5; i++) {
                    group.children.add("Sub Item" + i);
                }
                requestsList.append(j, group);
            }
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {

                ls = ((MainActivity)getActivity()).computeRadius(latitude,longitude,5);

                final Firebase ref = handyShopDB.child("requests");
                Query queryRef = ref.orderByChild("latitude").startAt(ls.getMinLat()).endAt(ls.getMaxLat());

                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        if (snapshot.getChildrenCount() == 0)
                            return;
                        else
                        {
                            for (DataSnapshot d : snapshot.getChildren()) {

                                Request req = d.getValue(Request.class);
                                //System.out.println(request.toString());
                                if(req.getLongitude()<=ls.getMaxLon() && req.getLongitude() >= ls.getMinLon())
                                {
                                    Group group = new Group(req.getCategory());
                                    group.children.add(req.getDescription());
                                    requestsList.append(requestsList.size(), group);
                                }


                            }
                            adapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(FirebaseError f) {
                    }



           /*     Firebase ref = handyShopDB.child("requests");

                Query queryRef = ref.orderByChild("userId").startAt(10);

                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() == 0) System.out.println("nullo");
                        for (DataSnapshot d : snapshot.getChildren()) {

                            String k = d.getKey();
                            Request req = d.getValue(Request.class);

                            //System.out.println(request.toString());
                            //System.out.println(req.getTitle() + " - " + req.getCategory());
                            Group group = new Group(req.getCategory());
                            group.children.add(req.getDescription());
                            requestsList.append(requestsList.size(), group);
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(FirebaseError f) {
                    }
                });


                // Get a reference to our posts
               /* Firebase ref = handyShopDB.child("requests");

                // Attach an listener to read the data at our posts reference
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        System.out.println("There are " + snapshot.getChildrenCount() + " Requests");

                        for (DataSnapshot d: snapshot.getChildren()){

                            String k = d.getKey();
                            Request req = d.getValue(Request.class);

                            //System.out.println(request.toString());
                            System.out.println(req.getTitle() + " - " + req.getCategory());

                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                    */
                });

            }

        }
    }


    public static class OffersFragment extends Fragment {

        SparseArray<Group> offersList = new SparseArray<Group>();
        MyExpandableListAdapter adapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            View rootView = inflater.inflate(R.layout.offers, container, false);

            createData();
            ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listView);
            adapter = new MyExpandableListAdapter(getActivity(), offersList);
            listView.setAdapter(adapter);
            return rootView;
        }

        public void createData() {
            for (int j = 0; j < 5; j++) {
                Group group = new Group("Test " + j);
                for (int i = 0; i < 5; i++) {
                    group.children.add("Sub Item" + i);
                }
                offersList.append(j, group);
            }
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                Group group = new Group("Leo");
                group.children.add("cazzone");
                offersList.append(offersList.size(), group);
                adapter.notifyDataSetChanged();
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

        public void sendRequest() {


        }

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

            Button mButton = (Button) getActivity().findViewById(R.id.insertButton);
            EditText mEdit = (EditText) getActivity().findViewById(R.id.editTitle);

/*
            mButton.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View view) {
                            String category = spinner_category.getSelectedItem().toString();
                            String subcategory = spinner_subcategory.getSelectedItem().toString();
                            EditText titleEdit = (EditText) getActivity().findViewById(R.id.editTitle);
                            EditText descriptionEdit = (EditText) getActivity().findViewById(R.id.editDescription);

                            String title = titleEdit.getText().toString();
                            String description = descriptionEdit.getText().toString();

                            Firebase ref = handyShopDB.child("requests");
                            Request req = new Request(id, title, category, subcategory, description, latitude, longitude);
                            ref.push().setValue(req);
                        }

                    });

*/
            return rootView;
        }
    }


    public static class ActivityFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Bundle args = getArguments();
            View rootView = null;
            //rootView = inflater.inflate(R.layout.insert, container, false);
            return rootView;
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

        Firebase ref = handyShopDB.child("requests");
        Request req = new Request(id, title, category, subcategory, description, latitude, longitude);
        System.out.println("latitude" + req.getLatitude());

        ref.push().setValue(req);
        System.out.println(latitude);
        System.out.println(longitude);


    }


    public LocationStruct computeRadius (double lat, double lon, int maxRadius){

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


    }











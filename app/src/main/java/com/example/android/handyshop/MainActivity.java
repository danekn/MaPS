package com.example.android.handyshop;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.net.Uri;
import android.os.Bundle;

import java.io.IOException;
import java.util.Arrays;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
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
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import junit.framework.Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends FragmentActivity implements LocationListener {
    private int PICK_IMAGE_REQUEST = 1;
    public static int DELETE_ITEM = 666;
    static public double RadiusOffer=10;
    static public double RadiusRequest=10;
    static DatabaseReference handyShopDB;
    CollectionPagerAdapter myCollectionPagerAdapter;

    static Bitmap bitmap;

    static CustomViewPager mViewPager;

    static double latitude;
    static double longitude;
    static double latitudeRad;
    static double longitudeRad;
    static String id;
    static LocationStruct ls = null;

    static List<Request> nRequestList = null;
    static List<Offer> nOfferList=null;

    static ArrayList<Group> activitiesList = null;
    static ExpandableActivitiesListAdapter adapteract;


    public static StorageReference storageRef=null;
    public static FirebaseStorage storage=null;
    public static StorageReference imagesRef=null;


    static CallbackManager callbackManager;
    static AccessToken accessToken = null;
    static AccessTokenTracker accessTokenTracker = null;
    private GoogleApiClient client;

    static boolean tmp;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onNewIntent(getIntent());

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


        /* Creating Storage for image */
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://amber-torch-5366.appspot.com");


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
                System.out.println("currentAccess");
                checkIfLogged(null);
            }
        };
        accessToken = AccessToken.getCurrentAccessToken();


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if (savedInstanceState == null) {
            mViewPager.setCurrentItem(1);
        }
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


    }

    @Override
    public void onProviderEnabled(String provider) {


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
        accessTokenTracker.stopTracking();
        System.out.println("logout");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri uri = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    // Log.d(TAG, String.valueOf(bitmap));

                    ImageView imageView = (ImageView) findViewById(R.id.imageGallery);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (requestCode == DELETE_ITEM) {
                System.out.println("APPOST ");


                // This pending intent will open after notification click
                /*
                PendingIntent i=PendingIntent.getActivity(this, 0,
                        new Intent(this, NotifyMessage.class),
                        0);

                note.setLatestEventInfo(this, "Android Example Notification Title",
                        "This is the android example notification message", i);

                //After uncomment this line you will see number of notification arrived
                //note.number=2;
                mgr.notify(NOTIFY_ME_ID, note);
                */

                callbackManager.onActivityResult(requestCode, resultCode, data);
                checkIfLogged(null);
            }


        }
    }


    @Override
    public void onNewIntent(Intent intent) {
        String str=intent.getStringExtra("dane");
        System.out.println(str);
    }




    public static class HomeFragment extends Fragment {

        Button telegram_button=null;
        View rootView = null;
        String name = null;
        String email = null;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            rootView = inflater.inflate(R.layout.home, container, false);


            LoginButton loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
            loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
            //loginButton.setFragment(this);
            //loginButton.setOnClickListener(login);
            ((MainActivity)getActivity()).checkIfLogged(rootView);

            Profile profile = Profile.getCurrentProfile();
            if(profile!=null)
                id = profile.getId();

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
                                    final DatabaseReference ref = handyShopDB.child("users");


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
                                        public void onCancelled(DatabaseError f) {
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


        /*View.OnClickListener download_img = new View.OnClickListener() {
            public void onClick(View v) {

                imagesRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        ImageView imageView= (ImageView) rootView.findViewById(R.id.imageLogo0);
                        imageView.setImageBitmap(bmp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

            }
        };

        */



    }



    public static class RequestsFragment extends Fragment {

        ArrayList<Group> requestsList = new ArrayList<Group>();
        Spinner spinner = null;
        ExpandableRequestsOffersListAdapter adapter;
        ArrayAdapter<String> spinnerAdapter = null;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            View rootView = null;
            rootView = inflater.inflate(R.layout.requests, container, false);
            //list = buildData();
            ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listView);
            adapter = new ExpandableRequestsOffersListAdapter(getContext(), requestsList);
            listView.setAdapter(adapter);
            spinner = (Spinner) rootView.findViewById(R.id.category_choice_filter_request);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //spinner = (Spinner) rootView.findViewById(R.id.category_choice_filter_request);
                    ((MainActivity) getActivity()).updateList("requests", adapter, requestsList, spinner);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }

            });

            SeekBar seekBar = (SeekBar)rootView.findViewById(R.id.seekBar_request);
            seekBar.setMax(10);
            final TextView seekBarValue = (TextView)rootView.findViewById(R.id.seekbar_title_request);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {

                    seekBarValue.setText(String.valueOf("Choice Distance Range " + progress + " km"));
                    RadiusRequest = progress;
                    ((MainActivity) getActivity()).updateList("requests", adapter, requestsList, spinner);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            return rootView;
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                ((MainActivity) getActivity()).updateList("requests", adapter, requestsList, spinner);
            } else {
                if (adapter != null) {
                    requestsList.clear();
                    adapter.notifyDataSetChanged();
                }
            }


        }
    }


    public static class OffersFragment extends Fragment {

        ArrayList<Group> offersList = new ArrayList<Group>();
        ExpandableRequestsOffersListAdapter adapter;
        Spinner spinner = null;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            View rootView = inflater.inflate(R.layout.offers, container, false);

            ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listView);
            adapter = new ExpandableRequestsOffersListAdapter(getContext(), offersList);
            listView.setAdapter(adapter);
            spinner = (Spinner) rootView.findViewById(R.id.category_choice_filter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //spinner = (Spinner) rootView.findViewById(R.id.category_choice_filter_request);
                    ((MainActivity) getActivity()).updateList("offers", adapter, offersList, spinner);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }

            });

            SeekBar seekBar = (SeekBar)rootView.findViewById(R.id.seekBar_offer);
            seekBar.setMax(10);
            final TextView seekBarValue = (TextView)rootView.findViewById(R.id.seekbar_title_offer);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {

                    seekBarValue.setText(String.valueOf("Choice Distance Range " + progress + " km"));
                    RadiusOffer=progress;
                    ((MainActivity) getActivity()).updateList("offers", adapter, offersList, spinner);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            return rootView;
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {

            super.setUserVisibleHint(isVisibleToUser);

            if (isVisibleToUser) {
                ((MainActivity) getActivity()).updateList("offers", adapter, offersList, spinner);
            } else {
                if(adapter!=null) {
                    offersList.clear();
                    adapter.notifyDataSetChanged();
                }
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



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            View rootView = null;
            activitiesList = new ArrayList<>();
            rootView = inflater.inflate(R.layout.activities, container, false);

            ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listView);
            adapteract = new ExpandableActivitiesListAdapter(getActivity(), activitiesList);
            listView.setAdapter(adapteract);
            return rootView;
        }

        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {

                activitiesList.clear();
                ((MainActivity)getActivity()).getActivities("requests");
                ((MainActivity)getActivity()).getActivities("offers");
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

                uploadImg(id,title);
                break;

            case "Request":
                DatabaseReference re = handyShopDB.child("requests");
                Request rq = new Request(id, title, category, subcategory, description, latitudeRad, longitudeRad);
                re.push().setValue(rq);
                uploadImg(id,title);
                break;
            default:
        }

    }

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
                                                                        Request req = d.getValue(Request.class);
                                                                        addToList(activitiesList, req, 1);

                                                                    }
                                                                    adapteract.notifyDataSetChanged();
                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled (DatabaseError f){
                                                            }
                                                        });
    }


    public void uploadImg(String id, String title){

            imagesRef = storageRef.child("images/"+id+title+".jpeg");
            System.out.println(imagesRef);
            ImageView imageView= (ImageView) findViewById(R.id.imageGallery);
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            byte[] data = baos.toByteArray();


            UploadTask uploadTask = imagesRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    Toast.makeText(MainActivity.this, "Unsuccessful Insertion! Please try later!",
                            Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(MainActivity.this, "Successful Insertion!"+downloadUrl,
                            Toast.LENGTH_LONG).show();
                    ImageView myImageView = (ImageView)findViewById(R.id.imageGallery);
                    myImageView.setImageResource(R.drawable.noimageicon);



                }
            });

        }

    public void getImage(View view){

        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

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

    public void checkIfLogged(View v){
        LinearLayout layout;
        GridLayout grid;
        if(v!=null) {
            layout = (LinearLayout) v.findViewById(R.id.header_home);
            grid =(GridLayout) v.findViewById(R.id.middle_home);
        }
        else{
            layout = (LinearLayout) findViewById(R.id.header_home);
            grid =(GridLayout) findViewById(R.id.middle_home);
        }
        if (accessToken == null && grid!=null && layout!=null) {
            mViewPager.setPagingEnabled(false);
            layout.setVisibility(View.INVISIBLE);
            grid.setVisibility(View.INVISIBLE);

        } else {
            mViewPager.setPagingEnabled(true);
            if(grid!=null && layout!=null) {
                grid.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
            }
        }
    }


    public void addToList(ArrayList<Group> list, Request q, int i){
        double dist = computeDistance(q.getLatitude(), q.getLongitude(), latitudeRad, longitudeRad);
        Group group;
        if(i==0)
            group = new Group(q.getTitle() +"?"+ "  " + (Math.floor(dist * 100) / 100) + " Km");
        else
            group = new Group(q.getTitle());
        group.children.add("CATEGORY: "+q.getCategory());
        group.children.add("SUBCATEGORY: "+q.getSubCategory());
        group.children.add("DESCRIPTION: "+q.getDescription());
        //TODO prendere la mail dall id dell utente
        group.children.add("EMAIL: "+"email");
        list.add(list.size(), group);
    }

    public void updateList(final String type, final ExpandableRequestsOffersListAdapter adapter, final ArrayList<Group> list, Spinner spinner){
        final String text_category = spinner.getSelectedItem().toString();
    System.out.println(type);
        if(type=="requests") {
            ls = computeRadius(latitudeRad, longitudeRad, RadiusRequest);
            nOfferList=null;
            if(nRequestList==null)
                nRequestList = new ArrayList<Request>();
        }
        else {
            ls = computeRadius(latitudeRad, longitudeRad, RadiusOffer);
            nRequestList = null;
            if (nOfferList == null)
                nOfferList = new ArrayList<Offer>();
        }

        final DatabaseReference ref = handyShopDB.child(type);
        Query queryRef = ref.orderByChild("latitude").startAt(ls.getMinLat()).endAt(ls.getMaxLat());

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                adapter.notifyDataSetChanged();

                if (snapshot.getChildrenCount() == 0)
                    return;
                else {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        Request req = d.getValue(Request.class);
                        if (req.getLongitude() <= ls.getMaxLon() && req.getLongitude() >= ls.getMinLon()) {
                            if (text_category.equals("All") || (!text_category.equals("All") && req.getCategory().equals(text_category))){
                                if (type=="requests"){
                                    nRequestList.add(req);
                                }
                                else {
                                    Offer off = d.getValue(Offer.class);
                                    nOfferList.add(off);
                                }
                                addToList(list, req, 0);
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

    public static boolean removeChild(final String type, final String title){
        final DatabaseReference ref = handyShopDB.child(type);
        tmp=false;
        Query queryRef = ref.orderByChild("userId").equalTo(id);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot snapshot){
                if (snapshot.getChildrenCount() > 0)
                {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        Request req = d.getValue(Request.class);
                        if (req.getTitle()==title){
                            handyShopDB.child(type+"/"+d.getKey()).removeValue();
                            tmp=true;
                        }
                    }

                }

            }

            @Override
            public void onCancelled (DatabaseError f){
            }
        });
        return tmp;
    }

    public void deleteChild(View v){
        RelativeLayout vwParentRow = (RelativeLayout)v.getParent();
        TextView child = (TextView)vwParentRow.getChildAt(0);

        if(!removeChild("requests", (String)child.getText()))
            removeChild("offers", (String)child.getText());
        for(int i=0; i<activitiesList.size();i++){
            Group g = activitiesList.get(i);
            if(g.string==child.getText())
                activitiesList.remove(i);
        }
        adapteract.notifyDataSetChanged();
    }


    public void openMap (View v){

        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
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


    public void telecazzo(View v) {
       /* Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, "addresses");

        setResult(DELETE_ITEM, intent);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), DELETE_ITEM);


        }*/
        Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://telegram.me/Porcodio"));
        startActivity(telegram);
    }

    public void saveFeedback(View v) {

        String userId = "ehehhehe";
        String requestId = "danie";
        Request req = new Request(id,"tritolo", "catory","subCategory",
                "description",879879,6565);
        Date date = new Date();
        long timeCreation = date.getTime();

        DatabaseReference ref = handyShopDB.child("feedback");
        FeedBack feedback = new FeedBack(id, userId, requestId, timeCreation,
                req.getTitle(), req.getCategory(), req.getSubCategory(), req.getDescription());
            ref.push().setValue(feedback);
        }



    public void checkPending(View v){

            final DatabaseReference ref = handyShopDB.child("feedback");
            tmp=false;
            Query queryRef = ref.orderByChild("yourId").equalTo(id);
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot snapshot){
                if (snapshot.getChildrenCount() > 0)
                {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        FeedBack fdb = d.getValue(FeedBack.class);
                        Date date = new Date();
                        if(date.getTime()-fdb.getTimeCreation()>30000);
                        addNotification(fdb);
                    }
                }
            }

            @Override
            public void onCancelled (DatabaseError f){
            }
        });
        //return tmp;


    }




public void addNotification(FeedBack feedback){

        Intent intent = new Intent(this, FeedbackActivity.class);
        intent.putExtra("userId", feedback.getUserId());
        intent.putExtra("requestId",feedback.getRequestId());
        intent.putExtra("Category", feedback.getCategory());
        intent.putExtra("SubCategory", feedback.getSubCategory());
        intent.putExtra("userId", feedback.getUserId());
        intent.putExtra("description", feedback.getDescription());

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, uniqueInt, intent, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Hello! You have to leave feedback")
                .setContentText(feedback.getTitleRequest())
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);


    }




    public void leavefeedback(View v){
/*
        final DatabaseReference ref = handyShopDB.child("users");
        tmp=false;
        //Query queryRef = ref.orderByChild("userId").equalTo(userId);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot snapshot){
                if (snapshot.getChildrenCount() > 0)
                {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        FeedBack fdb = d.getValue(FeedBack.class);
                        Date date = new Date();
                        if(date.getTime()-fdb.getTimeCreation()>30000);
                        addNotification(fdb);
                    }
                }
            }

            @Override
            public void onCancelled (DatabaseError f){
            }
        });

*/

    }



}











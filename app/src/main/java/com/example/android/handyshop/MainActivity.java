/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.handyshop;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
     * each object in a collection. We use a {@link android.support.v4.app.FragmentStatePagerAdapter}
     * derivative, which will destroy and re-create fragments as needed, saving and restoring their
     * state in the process. This is important to conserve memory and is a best practice when
     * allowing navigation between objects in a potentially large collection.
     */
    CollectionPagerAdapter myCollectionPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    static ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an adapter that when requested, will return a fragment representing an object in
        // the collection.
        // 
        // ViewPager and its adapters use support library fragments, so we must use
        // getSupportFragmentManager.
        myCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());

        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(myCollectionPagerAdapter);
    }


    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
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
                    fragment = new HomeFragment();
                    //args = new Bundle();
                    //args.putInt(HomeFragment.ARG_OBJECT, i);
                    //fragment.setArguments(args);
                    return fragment;
                case 1:
                    fragment = new RequestsFragment();

                    return fragment;
                case 2:
                    fragment = new OffersFragment();

                    return fragment;
                case 3:
                    fragment = new InsertFragment();
                    return fragment;
                default:
                    return fragment;
            }
        }
        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Home";

                case 1:
                    return "Offers";

                case 2:
                    return "Requests";

                case 3:
                    return "Insert";

                default:
                    return "";

            }

        }
    }

    public static class HomeFragment extends Fragment{

        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            View rootView = null;
            rootView = inflater.inflate(R.layout.home, container, false);
            ((TextView) rootView.findViewById(R.id.account)).setText("dane");
            return rootView;
        }

        public static void insert(View v) {
            System.out.println("tappooo");
            mViewPager.setCurrentItem(3);

        }
    }

    public static class RequestsFragment extends Fragment {

        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            View rootView = null;
            rootView = inflater.inflate(R.layout.requests, container, false);
            ((TextView) rootView.findViewById(R.id.titolo_r)).setText("Richiedimento");
            return rootView;
        }



    }

    public static class OffersFragment extends Fragment {

        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            View rootView = null;
            rootView = inflater.inflate(R.layout.offers, container, false);
            ((TextView) rootView.findViewById(R.id.titolo_o)).setText("Offrimento");
            return rootView;
        }



    }

    public static class InsertFragment extends Fragment {

        public static final String ARG_OBJECT = "object";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle args = getArguments();
            View rootView = null;
            rootView = inflater.inflate(R.layout.insert, container, false);
            ((TextView) rootView.findViewById(R.id.titolo_i)).setText("Inserimento");
            return rootView;
        }



    }
}



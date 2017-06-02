package com.example.ali.homeschool.controller.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ali.homeschool.R;
import com.example.ali.homeschool.adapter.SampleCoursesToolbarAdapter;
import com.example.ali.homeschool.childEnrolledCourses.MyCoursesFragment;
import com.example.ali.homeschool.controller.fragments.StudentFeaturedCoursesFragment;
import com.example.ali.homeschool.data.firebase.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
    We enter this class from the Student image button as it has more than one fragment
    it contains the navigation bar and we use the fragments and replace them
    to get the desired fragment
 */
public class StudentHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public int type;
    ViewPager mViewPager;
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppBarLayout appBarLayout;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    SampleCoursesToolbarAdapter imageCollapsingToolBarAdapter;
    Intent intent;
    Bundle bundle;
    UserModel userModel;
    ImageView userPhotoId;
    TextView UserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        /*
        setting the Content View
        setting the toolbar and collapsing toolbar
        setting the drawer layout
        setting the appbar layout
        setting the navigation view for menu item click listener
         */
        setContentView(R.layout.courseslayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        //setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        userPhotoId=(ImageView) navigationView.getHeaderView(0).findViewById(R.id.photoid);
        UserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.navigationTextName);


        mViewPager = (ViewPager) findViewById(R.id.viewPage_collapsing_toolbar);
        imageCollapsingToolBarAdapter = new SampleCoursesToolbarAdapter(this);
        mViewPager.setAdapter(imageCollapsingToolBarAdapter);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
//                Log.v("Test", "Pager Touch");
                return false;
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mViewPager.getParent().requestDisallowInterceptTouchEvent(true);
            }
        });

//        Fragment newFragment = new StudentFeaturedCoursesFragment();
//        newFragment.setArguments(bundle);
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment1, newFragment);
//        transaction.commit();

    }

    /*
     this is used to close the drawer (Navigation Bar)
     */

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference ref = databaseReference;
        Log.e("onStart: ", user.getUid());
     //   photoId.setImageResource(R.drawable.a);

// Attach a listener to read the data at our posts reference
        databaseReference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userModel = dataSnapshot.getValue(UserModel.class);
//                Log.e("onDataChange: ",userModel.getPhoto());
                UserName.setText(userModel.getName());
                Glide.with(getApplicationContext()).load(userModel.getPhoto()).into(userPhotoId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
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
    //
    //    @Override
    //    public boolean onOptionsItemSelected(MenuItem item) {
    //        // Handle action bar item clicks here. The action bar will
    //        // automatically handle clicks on the Home/Up button, so long
    //        // as you specify a parent activity in AndroidManifest.xml.
    //        int id = item.getItemId();
    //
    //        //noinspection SimplifiableIfStatement
    //        if (id == R.id.action_settings) {
    //            return true;
    //        }
    //
    //        return super.onOptionsItemSelected(item);
    //    }


    // Here we choose which part in the navigation menu is being selected
    // and depending on that we use the if else to see which fragment should be used

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.coursesNav) {
//            Log.v("Test", "Test");
            //appBarLayout.removeView(cllapsingToolbarLayout);
            appBarLayout.setExpanded(false);
            appBarLayout.addOnOffsetChangedListener(new   AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    appBarLayout.setExpanded(false,false);
                }
            });
            mViewPager.removeAllViews();

            Fragment newFragment = new MyCoursesFragment();
            newFragment.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.fragment1, newFragment);
            // transaction.addToBackStack(null);
            transaction.commit();

            // Commit the transaction
        } else if (id == R.id.featuredCoursesNaV) {
//            Log.v("Test", "Test");
            //appBarLayout.removeView(cllapsingToolbarLayout);
            Log.v("Nav","Featured");
            appBarLayout.setExpanded(true);
            mViewPager.setAdapter(imageCollapsingToolBarAdapter);

            // mViewPager.removeAllViews();
            Fragment newFragment1 = new StudentFeaturedCoursesFragment();
            newFragment1.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.fragment1, newFragment1);
            //  transaction.addToBackStack(null);
            transaction.commit();

            // Commit the transaction
        }  else if (id == R.id.settings) {

        } else if (id == R.id.signout) {
            mAuth.getInstance().signOut();
            signOut101();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void signOut101() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("finish", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
        startActivity(intent);
        finish();
    }
}

package com.android.chalkboard.dashboard.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chalkboard.R;
import com.android.chalkboard.dashboard.presenter.DashboardContract;
import com.android.chalkboard.dashboard.presenter.DashboardPresenterImpl;
import com.android.chalkboard.login.view.LoginActivity;
import com.android.chalkboard.nav_select_institute;
import com.android.chalkboard.school.model.ClassesList;
import com.android.chalkboard.school.model.SchoolListResponse;
import com.android.chalkboard.studentStory.QR_Code;
import com.android.chalkboard.studentStory.Qr_Code_Reader;
import com.android.chalkboard.studentStory.classes.classFragment;
import com.android.chalkboard.studentStory.news.classNewsFragment;
import com.android.chalkboard.studentStory.profile.profileFragment;
import com.android.chalkboard.teacherrequest.view.InstituteListFragment;
import com.android.chalkboard.util.CommonUtils;
import com.android.chalkboard.util.FontChangeCrawler;
import com.android.chalkboard.util.SharedPrefUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.android.chalkboard.util.Constant.ID;
import static com.android.chalkboard.util.SharedPrefUtils.NAME_KEY;
import static com.android.chalkboard.util.SharedPrefUtils.ROLE;

public class NavDashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DashboardContract.View, View.OnClickListener, LocationListener {

    private Toolbar toolbar, collapseToolbar;
    private DrawerLayout drawer;
    private CollapsingToolbarLayout collapseToolbarLayout;
    private AppBarLayout appBarLayout;
    private FloatingActionButton addSchoolFab;
    private InstituteListFragment instituteListFragment;
    private ImageView bannerImage;
    private LocationManager locationManager;
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;
    private Location location;
    private String role;
    private RelativeLayout toolbarLayout;
    private TextView toolbar_title,name_maenu;
    private ImageView toolbar_back;
    private FragmentManager manager;
    private LinearLayout select_institute,qr_code,scan_qr,my_profile,lecture_agenda,news;
    View profile_sep;
    CircleImageView profile_image;
    private LinearLayout logout;
    private List<SchoolListResponse> responseList;
    View nav_student_view;

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    public void setAppBarLayout(AppBarLayout appBarLayout) {
        this.appBarLayout = appBarLayout;
    }

    private TextView collapsedTitle;
    private static final String TAG = DashboardFragment.class.getName();
    private DashboardContract.DashboardPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_dash_board);

        role = SharedPrefUtils.getFromSharedPref(this, SharedPrefUtils.ROLE);
        manager = getSupportFragmentManager();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        qr_code = (LinearLayout) findViewById(R.id.qr_code);
        scan_qr = (LinearLayout) findViewById(R.id.scan_qr);
        my_profile = (LinearLayout) findViewById(R.id.my_profile);
        lecture_agenda = (LinearLayout) findViewById(R.id.lecture_agenda);
        news = (LinearLayout) findViewById(R.id.news);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        profile_sep = findViewById(R.id.profile_sep);

        nav_student_view = findViewById(R.id.nav_student_view);

        if(role.equalsIgnoreCase("parent") || role.equalsIgnoreCase("student") || role.equalsIgnoreCase("teacher")) {
            nav_student_view.setVisibility(View.VISIBLE);
            if(role.equalsIgnoreCase("parent")) {
                scan_qr.setVisibility(View.VISIBLE);
                qr_code.setVisibility(View.GONE);
                my_profile.setVisibility(View.GONE);
                profile_sep.setVisibility(View.GONE);
            }
            else if(role.equalsIgnoreCase("teacher")) {
                scan_qr.setVisibility(View.GONE);
                qr_code.setVisibility(View.GONE);
                profile_image.setImageResource(R.drawable.teacher_role);
            }
            else {
                scan_qr.setVisibility(View.GONE);
                qr_code.setVisibility(View.VISIBLE);
                my_profile.setVisibility(View.VISIBLE);
                profile_sep.setVisibility(View.VISIBLE);
            }
        }
        else {
            nav_student_view.setVisibility(View.GONE);
            navigationView.inflateMenu(R.menu.activity_nav_dash_board_drawer);
            navigationView.inflateHeaderView(R.layout.nav_header_nav_dash_board);
            ((TextView)navigationView.getHeaderView(0).findViewById(R.id.roleMenu)).setText(SharedPrefUtils.getFromSharedPref(this,ROLE));
            navigationView.setNavigationItemSelectedListener(this);
        }

        DashboardPresenterImpl.createPresenter(this);
        performBasicRequests();
        instituteListFragment = new InstituteListFragment();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        collapseToolbarLayout = findViewById(R.id.toolbar_layout);
        appBarLayout = findViewById(R.id.app_bar);
        collapsedTitle = findViewById(R.id.toolbar_collapsed_title);
        bannerImage = findViewById(R.id.iv_banner_view);
        addSchoolFab = findViewById(R.id.fab);
        DashboardFragment dashboardFragment = new DashboardFragment();
        appBarLayout.setExpanded(false);


        name_maenu = (TextView) findViewById(R.id.nameMenu);
        name_maenu.setText(SharedPrefUtils.getFromSharedPref(this,NAME_KEY));


        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int index = getSupportFragmentManager().getBackStackEntryCount();
                /*if (index > -1) {
                    FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
                    String tag = backEntry.getName();
                    android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);*/
                    if (index == 0) {
                        appBarLayout.setExpanded(false);

                    }

            }
        });


        select_institute = findViewById(R.id.select_institute);
        select_institute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (role.toLowerCase()) {

                    case "student" :
                    case "parent":

                        replace_Fragment(new nav_select_institute(),1);

                        break;

                    case "admin" :

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        instituteListFragment.show(transaction, TAG);

                        break;

                }


                drawer.closeDrawer(GravityCompat.START);
            }
        });

        scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(NavDashBoardActivity.this,
                        Manifest.permission.CAMERA);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NavDashBoardActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            2000);
                }
                else {
                    replace_Fragment(new Qr_Code_Reader(), 1);
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replace_Fragment(new profileFragment(),1);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        lecture_agenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classFragment classFragment = new classFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("choice",1);
                classFragment.setArguments(bundle);
                replace_Fragment(classFragment,1);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replace_Fragment(new classNewsFragment(),1);
                drawer.closeDrawer(GravityCompat.START);
            }
        });


        qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replace_Fragment(new QR_Code(),1);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replace_Fragment(new profileFragment(),1);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        logout = (LinearLayout) findViewById(R.id.log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new logout_Dialog(NavDashBoardActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                drawer.closeDrawer(GravityCompat.START);
                dialog.show();

            }
        });

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_back = (ImageView) findViewById(R.id.toolbar_back);
        toolbarLayout = (RelativeLayout) findViewById(R.id.toolbarLayout);
        setUpToolbar();
//        addSchoolFab.setOnClickListener(this);
        replace_Fragment(dashboardFragment,0);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.fragment_content_holder, dashboardFragment, TAG);
//        ft.commit();

        appBarLayout.setExpanded(false);
        ((TextView) findViewById(R.id.roleMenu)).setText(SharedPrefUtils.getFromSharedPref(this, ROLE));
//        ((TextView) findViewById(R.id.nameMenu)).setText(SharedPrefUtils.getFromSharedPref(this, NAME_KEY));
//        Toast.makeText(this,"Name"+SharedPrefUtils.getFromSharedPref(this, NAME_KEY),Toast.LENGTH_SHORT).show();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int index = getSupportFragmentManager().getBackStackEntryCount();
                /*if (index > -1) {
                    FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
                    String tag = backEntry.getName();
                    android.support.v4.app.Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);*/
                if (index == 0) {
                    appBarLayout.setExpanded(false);

                }

            }
        });

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        String provider = LocationManager.GPS_PROVIDER;
//        turnLocationOn(provider);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "muli_black.ttf");
        fontChanger.replaceFonts((ViewGroup) findViewById(R.id.drawer_layout));
    }

    public void logout() {
        SharedPrefUtils.clearSharedPref(getApplicationContext());
        Intent navToLogin = new Intent(NavDashBoardActivity.this, LoginActivity.class);
        startActivity(navToLogin);
        finish();
    }

    public void performBasicRequests(){
        String role = SharedPrefUtils.getFromSharedPref(this, SharedPrefUtils.ROLE);
        if(role.equalsIgnoreCase("admin"))
            presenter.getInstitution();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    protected void setUpToolbar() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapseToolbarLayout.setTitle(getString(R.string.dashboard_title));
                    isShow = true;
                } else if(isShow) {
                    collapseToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
        toolbar = findViewById(R.id.toolbar_collapsable);
        collapseToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.appGreenColor));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_dash_board, menu);

        return true;
    }

    public void setCollapsableTitle(String title){
        collapsedTitle.setText(title);
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

        if (id == R.id.nav_school_list) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            instituteListFragment.show(transaction, TAG);
            /*transaction.replace(R.id.fragment_content_holder, instituteListFragment, TAG);
            transaction.commit();*/
        }

        if (id == R.id.log_out) {
            SharedPrefUtils.clearSharedPref(getApplicationContext());
            Intent navToLogin = new Intent(this, LoginActivity.class);
            startActivity(navToLogin);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        /*int id = view.getId();
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_content_holder);
        String tag = currentFragment.getTag();
        if(tag.equalsIgnoreCase(DASHBOARD_FRAG)){
            Intent intent = new Intent(NavDashBoardActivity.this, AddSchoolActivity.class);
            startActivity(intent);
            return;
        }else if(tag.equalsIgnoreCase(ADMIN_NEWS_FRAG)){

        }*/
    }
    @Override
    public void bindPresenter(DashboardPresenterImpl dashboardPresenter) {
        presenter = dashboardPresenter;
    }

    @Override
    public void storeInstituteData(Object success) {
        if (((List<SchoolListResponse>)success) != null && ((List<SchoolListResponse>)success).size() > 0) {
            SharedPrefUtils.storeInstitituteObject(this, ((List<SchoolListResponse>) success));
            ArrayList<SchoolListResponse> institute = SharedPrefUtils.retrieveInstituteList(this);
            int selectedInstitute = institute.get(0).getId();
            CommonUtils.setDefaultInstitute(this, selectedInstitute);
            presenter.getClasses(selectedInstitute);
        }

    }

    @Override
    public void storeClassesofInstitute(Object success) {
        if(((List<ClassesList>)success) != null && ((List<ClassesList>)success).size() >= 0){
            SharedPrefUtils.storeClasses(this, ((List<ClassesList>)success));
        }
    }

    public ImageView getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(ImageView bannerImage) {
        this.bannerImage = bannerImage;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == 2000 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            replace_Fragment(new Qr_Code_Reader(), 1);
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    public void showFloatingActionButton() {
        addSchoolFab.show();
    }

    public void hideFloatingActionButton() {
        addSchoolFab.hide();
    }

    public void getInstituteClasses(int id) {
        if(presenter==null){
            DashboardPresenterImpl.createPresenter(this);
        }
        presenter.getClasses(id);
    }

    @Override
    public void showError(String message){
        Snackbar.make(bannerImage, message, Snackbar.LENGTH_INDEFINITE).setAction("OK", null).show();
    }

    @Override
    public  void setRequestedInstitute(Object success){
        if(success != null && ((List<SchoolListResponse>)success).size() > 0){
            this.responseList = new ArrayList<>(((List<SchoolListResponse>)success));
            SharedPrefUtils.storeRequestedInstituteObject(this, this.responseList);
        }
    }

    public void replace_Fragment(Fragment fragment, int i) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_content_holder, fragment, null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
        changeToolbar(i);
        transaction.addToBackStack("Fragment");
    }


    public void showFragmentToolbar(String title) {


        toolbar_title.setText(title);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(getSupportFragmentManager().getBackStackEntryCount() != 1)
                manager.popBackStack();
            else
                finish();
        }
    }

    public void changeToolbar(int i) {
        Log.e("Backstack Count",String.valueOf(manager.getBackStackEntryCount()));
        if (i==0) {
            toolbarLayout.setVisibility(View.GONE);
            appBarLayout.setVisibility(View.VISIBLE);

        } else {
            toolbarLayout.setVisibility(View.VISIBLE);
            appBarLayout.setVisibility(View.GONE);
        }
    }


    public Location getLocation(String provider) {


        if (ContextCompat.checkSelfPermission(NavDashBoardActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


        locationManager.requestLocationUpdates(provider,
                MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(provider);
            return location;
        }

        return null;
    }

//    private void turnLocationOn(String provider) {
//
//        if (locationManager.isProviderEnabled(provider)) {
//            setUpLocoation(provider);
//        } else {
//            showSettingsAlert();
////            if (!locationManager.isProviderEnabled(provider))
////                Toast.makeText(NavDashBoardActivity.this,"GPS still not enabled!",Toast.LENGTH_SHORT).show();
////            else
////                setUpLocoation(provider);
//        }
//
//    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                NavDashBoardActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        NavDashBoardActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }


    private void setUpLocoation(String provider) {

        Location location = getLocation(provider);
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
//                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                            sb.append(address.getAddressLine(i)).append("\n");
//                        }
//                        sb.append(address.getLocality()).append("\n");
//                        sb.append(address.getPostalCode()).append("\n");
//                        sb.append(address.getCountryName());
                sb.append(address.getLocality());
                result = sb.toString();
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable connect to Geocoder", e);
        } finally {
            Message message = Message.obtain();
            message.setTarget(new GeocoderHandler());
            if (result != null) {
                message.what = 1;
                Bundle bundle = new Bundle();
                result = "Latitude: " + latitude + " Longitude: " + longitude +
                        "\n\nAddress:\n" + result;
                bundle.putString("address", result);
                message.setData(bundle);
            } else {
                message.what = 1;
                Bundle bundle = new Bundle();
                result = "Latitude: " + latitude + " Longitude: " + longitude +
                        "\n Unable to get address for this lat-long.";
                bundle.putString("address", result);
                message.setData(bundle);
            }
            message.sendToTarget();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }

            Toast.makeText(NavDashBoardActivity.this, locationAddress, Toast.LENGTH_LONG).show();

        }
    }


}

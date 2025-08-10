package com.example.recipebook;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends All implements HomeFragment.RecipeSelectListener, UserFragment.UserRecipeSelected {
    private BottomNavigationView bottomNav;
    private FloatingActionButton fab;
    private RecipeViewModel recipeViewModel;
    private static final int REQUEST_CODE_ID = 12;
    private HomeFragment homeFragment;// = new HomeFragment();
    private UserFragment userFragment;// =new UserFragment();

    private static final int MY_PERMISSIONS_REQUEST_RECEIVE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open the add recipe activity
                Intent intent = new Intent(MainActivity.this, AddRecipeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ID);
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment.class, null, "home").commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        boolean res;
        if (requestCode == REQUEST_CODE_ID && resultCode == RESULT_OK) {
            recipeViewModel.getRecipes(this);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // By using switch we can easily get
            // the selected fragment
            // by using there id.
            getSupportFragmentManager().popBackStack();
            switch (item.getItemId()) {
                case R.id.homeBtnBar:
                    // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();

                    homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home");
                    if (homeFragment == null) {

                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, HomeFragment.class, null, "home")
                                .commit();
                    } else {
                        getSupportFragmentManager().beginTransaction().show(homeFragment).commit();
                    }
                    //getSupportFragmentManager().executePendingTransactions();
                    break;
                case R.id.myBtnBar:
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, userFragment).commit();

                    userFragment = (UserFragment) getSupportFragmentManager().findFragmentByTag("user");
                    if (userFragment == null) {
                        //getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, UserFragment.class, null, "user")
                                .commit();
                    } else {
                        getSupportFragmentManager().beginTransaction().show(userFragment).commit();
                    }
                    //getSupportFragmentManager().executePendingTransactions();
                    //selectedFragment = new UserFragment();
                    break;
            }
            //getSupportFragmentManager().executePendingTransactions();
            // It will help to replace the
            // one fragment to other.

            return true;
        }
    };


    @Override
    public void onRecipeSelected(int position) {
        DetailsFragment detailsFragment = (DetailsFragment) getSupportFragmentManager().findFragmentByTag("details");
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (detailsFragment == null)
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, DetailsFragment.class, null, "details")
                        .addToBackStack("details")
                        .commit();
            else {
                getSupportFragmentManager().beginTransaction().show(detailsFragment).commit();
            }
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    public void doPositiveClick() {
        Recipe recipe = recipeViewModel.getRecipe();
        recipeViewModel.getRecipes(this).getValue().remove(recipe);
        recipeViewModel.deleteRecipe(recipe);
    }


    @Override
    protected void onResume() {
        // check if the permission is not granted
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED)
        {
            // if the permission is not been granted then check if the user has denied the permission
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)){
                // Do nothing as user ha denied it
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_RECEIVE);

            }else{
                // a pop up will appear for required permission i.e Allow or Deny
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_RECEIVE);
            }
        }//onCreate change it later







//        if (ContextCompat.checkSelfPermission(
//                this, Manifest.permission.RECEIVE_SMS) ==
//                PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(
//                this, Manifest.permission.READ_SMS) ==
//                PackageManager.PERMISSION_GRANTED) {
//            //receive and read SMS
//
//            Log.i("granted", " if");
//            // You can use the API that requires the permission.
//            // BroadcastReceiver br = new MyBroadcastReceiver();
//            //performAction(...);
//        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.RECEIVE_SMS)&&ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.READ_SMS)) {
//            // In an educational UI, explain to the user why your app requires this
//            // permission for a specific feature to behave as expected. In this UI,
//            // include a "cancel" or "no thanks" button that allows the user to
//            // continue using your app without granting the permission.
//            //showInContextUI(...);
//            Log.i("elseif", "else if");
//        } else {
//            // You can directly ask for the permission.
//            // The registered ActivityResultCallback gets the result of this request.
//            requestPermissionLauncher.launch(
//                    Manifest.permission.RECEIVE_SMS);
////            //Log.i("elseeeee","gh");
////            requestPermissionLauncher.launch(
////                    Manifest.permission.READ_SMS);
//        }
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       //will check the requestCode
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_RECEIVE:
                //check whether the length of grantResults is greater than 0 and is equal to PERMISSION_GRANTED
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //Now broadcast receiver will work in background
                    Toast.makeText(this, "SMS receiver is permitted", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this, "You must permit to receive permission!!", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }











    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // toast broradcast

                } else {
                    Log.i("elseeeee","8");
                    //need to give permissin
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });
}
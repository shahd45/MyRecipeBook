package com.example.recipebook;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class All extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.exit:
                /* Open the Dialog */
                showDialog(R.string.exit, R.string.exit_message);
                return true;
            default:
                break;
        }
        return false;
    }

    public void showDialog(int title, int message){
       DialogFragment newFragment = MyAlertDialog.newInstance(title, message);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void exitApp(){
        Log.i("Exit Dialog", "Positive click!");
        finish();
        System.exit(0);
    }
}

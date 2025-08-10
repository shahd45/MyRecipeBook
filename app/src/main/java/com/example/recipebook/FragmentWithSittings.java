package com.example.recipebook;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

public class FragmentWithSittings extends Fragment {

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.sittings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MySittingsActivity s = (MySittingsActivity) getFragmentManager().findFragmentByTag("");
        switch (item.getItemId()) {
            case R.id.sittings:
                if (s == null)
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MySittingsActivity(), "").addToBackStack("").commit();
                else
                    getFragmentManager().beginTransaction().attach(s).commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class MySittingsActivity extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preference_screen, rootKey);
        }
    }
}

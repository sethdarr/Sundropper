package com.sundropelectric.sundropper.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity {
    private ViewPager pager = null;
    private SharedPreferences prefs = null;
    private static final String PREF_LAST_MENU_ITEM = "lastMenuItem";
    private static final String PREF_SAVE_LAST_MENU_ITEM = "saveLastMenuItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new MenuPagerAdapter(getSupportFragmentManager()));
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.getAll();
        if (prefs.getBoolean(PREF_SAVE_LAST_MENU_ITEM, false)) {
            pager.setCurrentItem(prefs.getInt(PREF_LAST_MENU_ITEM, 0));
        }
    }

    @Override
    public void onPause() {
        if (prefs != null) {
            int position = pager.getCurrentItem();
            prefs.edit().putInt(PREF_LAST_MENU_ITEM, position).apply();
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!this.getClass().equals(LoginActivity.class)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_logout:
                Intent i = new Intent(this, LoginActivity.class);
                i.putExtra("LOGOUT", true);
                //i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(i);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

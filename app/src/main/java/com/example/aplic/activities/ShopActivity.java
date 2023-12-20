package com.example.aplic.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.aplic.R;
import com.example.aplic.activities.ShopFragments.InventoryFragment;
import com.example.aplic.activities.ShopFragments.PreviewFragment;
import com.example.aplic.activities.ShopFragments.ShopBuyFragment;
import com.google.android.material.navigation.NavigationView;

public class ShopActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Bundle bitmapArrays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_preview);
        navigationView.setNavigationItemSelectedListener(this);

        bitmapArrays = getIntent().getExtras();
        // sets the activity to preview fragment when activity is created
        PreviewFragment previewFragment = new PreviewFragment();
        previewFragment.setArguments(bitmapArrays);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, previewFragment, "tagPreview");
        fragmentTransaction.commit();
    }
    // if the drawer is open, pressing the back button will close it instead of finishing the activity
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    // creates and replaces with previous fragment, the fragment according to navigation item selected, and gives it a tag
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_preview:
                PreviewFragment previewFragment = new PreviewFragment();
                previewFragment.setArguments(bitmapArrays);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, previewFragment, "tagPreview");
                fragmentTransaction.commit();
                break;
            case R.id.nav_inventory:
                InventoryFragment inventoryFragment = new InventoryFragment();
                inventoryFragment.setArguments(bitmapArrays);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, inventoryFragment, "tagInventory");
                fragmentTransaction.commit();
                break;
            case R.id.nav_icon:
                ShopBuyFragment iconFragment = new ShopBuyFragment(this);
                iconFragment.setArguments(bitmapArrays);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, iconFragment, "tagIcon");
                fragmentTransaction.commit();
                break;
            case R.id.nav_background:
                ShopBuyFragment backgroundFragment = new ShopBuyFragment(this);
                backgroundFragment.setArguments(bitmapArrays);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, backgroundFragment, "tagBackground");
                fragmentTransaction.commit();
                break;
            case R.id.nav_bonus:
                ShopBuyFragment bonusFragment = new ShopBuyFragment(this);
                bonusFragment.setArguments(bitmapArrays);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, bonusFragment, "tagBonus");
                fragmentTransaction.commit();
                break;
            case R.id.nav_power_up:
                ShopBuyFragment powerUpFragment = new ShopBuyFragment(this);
                powerUpFragment.setArguments(bitmapArrays);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, powerUpFragment, "tagPowerUp");
                fragmentTransaction.commit();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
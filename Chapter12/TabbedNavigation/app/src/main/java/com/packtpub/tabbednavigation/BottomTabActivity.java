package com.packtpub.tabbednavigation;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BottomTabActivity extends AppCompatActivity {

    private ViewPager container;
    private BottomNavigationView navigation;

    private final int[] pageIds = new int[]{
            R.id.navigation_search,
            R.id.navigation_upcoming,
            R.id.navigation_flown
    };

    private final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        public boolean onNavigationItemSelected(final MenuItem item) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack(
                        fragmentManager.getBackStackEntryAt(0).getId(),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            for (int i = 0; i < pageIds.length; i++) {
                if (pageIds[i] == item.getItemId()) {
                    container.setCurrentItem(i);
                    return true;
                }
            }

            return false;
        }
    };

    private final ViewPager.OnPageChangeListener onPageChangeListener =
            new ViewPager.SimpleOnPageChangeListener() {
                public void onPageSelected(final int position) {
                    navigation.setSelectedItemId(pageIds[position]);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_tab);

        container = findViewById(R.id.container);
        container.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return PlaceholderFragment.newInstance("Tab: " + position);
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        container.addOnPageChangeListener(onPageChangeListener);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                final String location = item.getTitle().toString();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.host, PlaceholderFragment.newInstance(location))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(location)
                        .commit();

                final DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

}

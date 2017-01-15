package com.ussol.employeetracker;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ussol.employeetracker.helpers.CustomerGoalPagerAdapter;

/**
 * Created by HOA-NX on 2017/01/14.
 */

public class CustomerGoalActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] SELECTED_ICON = {R.drawable.calendar, R.drawable.ic_account_switch_black_24dp};
    private int[] UNSELECTED_ICON = {R.drawable.calendar2, R.drawable.account_card_details};
    private String[] FRAGMENT_NAME = {"KHÁCH HÀNG", "MỤC TIÊU"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_goal_main_activity);
        // Setting up Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(FRAGMENT_NAME[0]);
        setSupportActionBar(toolbar);

        //Initialize ViewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //Setup ViewPager Adapter
        setupViewPager(viewPager);
        //Tablayout initialization
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);
        //setup Listeners to Tabs
        tabLayout.setOnTabSelectedListener(this);

        // Set Icons to Tabs at Specific position
//        tabLayout.getTabAt(0).setIcon(SELECTED_ICON[0]);
//        tabLayout.getTabAt(1).setIcon(UNSELECTED_ICON[1]);
//        tabLayout.getTabAt(2).setIcon(UNSELECTED_ICON[2]);
//        tabLayout.getTabAt(3).setIcon(UNSELECTED_ICON[3]);
    }

    // This method will call Adapter for ViewPager
    private void setupViewPager(ViewPager viewPager) {
        CustomerGoalPagerAdapter adapter = new CustomerGoalPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Customer_Fragment(), FRAGMENT_NAME[0]);
        adapter.addFragment(new Goal_Fragment(), FRAGMENT_NAME[1]);

        //Set adapter to ViewPager
        viewPager.setAdapter(adapter);
    }

    // These are the methods which handles Tab Selection, Unselection & Reselection
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //Set the tab icon from selected array
        //tab.setIcon(SELECTED_ICON[tab.getPosition()]);
        toolbar.setTitle(FRAGMENT_NAME[tab.getPosition()]);
        //When Tab is clicked this line set the viewpager to corresponding fragment
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        // Set icon from unselected tab array
        //tab.setIcon(UNSELECTED_ICON[tab.getPosition()]);

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        //tab.setIcon(SELECTED_ICON[tab.getPosition()]);
    }

}


package com.solodroid.ecommerce;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

public class ActivityInformation extends FragmentActivity implements
        ActionBar.TabListener {

    private ViewPager viewPager;
    private ActionBar actionBar;
    private AdapterInformation tabPagerAdapter;
    private String[] tabs = { "Payment", "Complaint", "Cancellation" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipetab);
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabPagerAdapter = new AdapterInformation(getSupportFragmentManager());
        viewPager.setAdapter(tabPagerAdapter);
        actionBar = getActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.header)));
        actionBar.setTitle("Information");
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /**
             * on swipe select the respective tab
             * */
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) { }

            @Override
            public void onPageScrollStateChanged(int arg0) { }
        });
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) { }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
    
    @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition (R.anim.open_main, R.anim.close_next);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
            	this.finish();
            	overridePendingTransition (R.anim.open_main, R.anim.close_next);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
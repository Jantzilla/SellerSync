package com.sellersync.jantz.sellersync;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import static com.sellersync.jantz.sellersync.HomeFragment.int_items;


public class MyAdapter  extends FragmentPagerAdapter {
    Context context;


    public MyAdapter(FragmentManager fm, Context nContext)
    {
        super(fm);
        context = nContext;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new SummaryListFragment();
            case 1:
                return new InventoryFragment();
            case 2:
                return new ListingsFragment();
            case 3:
                return new ActivityFragment();


        }
        return null;
    }

    @Override
    public int getCount() {


        return int_items;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return context.getString(R.string.summary);
            case 1:
                return context.getString(R.string.inventory);
            case 2:
                return context.getString(R.string.listings);
            case 3:
                return context.getString(R.string.activity);


        }

        return null;
    }
}

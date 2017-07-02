package com.example.victor.bakingapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.victor.bakingapp.model.PlaceholderFragment;
import com.example.victor.bakingapp.model.Step;

import java.util.ArrayList;

/**
 * Created by Victor on 29/06/2017.
 */

public class StepsPagerAdapter extends FragmentPagerAdapter {
        public ArrayList<Step> steps = new ArrayList<>();

        public StepsPagerAdapter(FragmentManager fm, ArrayList<Step> steps) {
            super(fm);
            this.steps = steps;
           // users.remove(items.size()-1);
        }

        @Override
        public Fragment getItem(int position) {
            //Step current = steps.get(position);
            return PlaceholderFragment.newInstance(position, steps);
        }

        @Override
        public int getCount() {
            return steps.size();

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return  String.valueOf(steps.get(position).getId()) ;
        }

}

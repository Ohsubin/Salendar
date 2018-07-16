package com.example.user.project;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;

public class Chart_Frg2 extends Fragment {
    PagerAdapter adapter;
    ViewPager pager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout=(LinearLayout)inflater.inflate(R.layout.activity_chart__frg2,container,false);
        TabLayout tab=(TabLayout)layout.findViewById(R.id.tab);
        tab.addTab(tab.newTab().setText("섭취"));
        tab.addTab(tab.newTab().setText("소모"));
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager=(ViewPager)layout.findViewById(R.id.pager);
        adapter=new PagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private class PagerAdapter extends FragmentStatePagerAdapter{
        public PagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new Chart_Frg2_t1();
                case 1:
                    return new Chart_Frg2_t2();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {

            return POSITION_NONE;
        }
    }
}

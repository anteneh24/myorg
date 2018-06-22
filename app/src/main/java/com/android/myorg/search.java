package com.android.myorg;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anteneh on 5/26/2016.
 */
public class search extends AppCompatActivity
        {

    DataSource dataSource;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private orgRecycleAdapter adapter;
    private List<org> list;
            private TabLayout tabLayout;
            private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        ImageButton button=(ImageButton)findViewById(R.id.delete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
            }
        });

    }

            @Override
            protected void onPause() {
                super.onPause();
                finish();
            }

            private void setupViewPager(ViewPager viewPager){
                ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
                adapter.addFragment(new org_search(),"Business");
                adapter.addFragment(new branch_search(),"Branch's");
                viewPager.setAdapter(adapter);
            }

            class ViewPagerAdapter extends FragmentPagerAdapter {

                private final List<Fragment> mFragment=new ArrayList<>();
                private final List<String> title=new ArrayList<>();

                public ViewPagerAdapter(FragmentManager manager){
                    super(manager);
                }

                @Override
                public Fragment getItem(int position) {
                    return mFragment.get(position);
                }

                @Override
                public int getCount() {
                    return mFragment.size();
                }

                public void addFragment(Fragment fragment, String title){
                    mFragment.add(fragment);
                    this.title.add(title);
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    return title.get(position);
                }
            }


    }


package com.android.myorg;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anteneh on 6/1/2016.
 */
public class fragment_main extends Fragment {

    Dialog dialog;
    DataSource dataSource;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private orgRecycleAdapter adapter;
    FloatingActionButton search_action;
    public fragment_main(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_feed,container,false);
        search_action=(FloatingActionButton)view.findViewById(R.id.search_action);
        search_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),search.class);
                startActivity(i);
            }
        });
        dataSource=new DataSource(getActivity());
        dataSource.open();
        List<org> list=dataSource.findFilteredOrg(DatabaseOpenHelper.state+" = 'sponser'",
                null,null,null,DatabaseOpenHelper.org_name);
        if(list.size()==0) {
            org orga=new org();
            orga.setOrg_name("No result");
            //orga.setLogo(R.drawable.noting);
            list.add(orga);

        }
        final List<org> list1=new ArrayList<org>();
        for(int i=0;i<list.size();i++){
            list1.add(list.get(i));
        }
        Log.i("bbb",""+list1.size());
        Log.i("bbb",""+list.size());
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter=new orgRecycleAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0||dy<0&&search_action.isShown()){
                    search_action.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    search_action.show();
                super.onScrollStateChanged(recyclerView, newState);

            }
        });
        orgRecycleAdapter.OnItemClickListener onItemClickListener=new orgRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i=new Intent(getActivity(),ScrollingActivity.class);
                i.putExtra(DatabaseOpenHelper.org_name,list1.get(position).getOrg_name());
                Log.i("bbb id",list1.get(position).getOrg_id()+"");
                i.putExtra(DatabaseOpenHelper.org_id,list1.get(position).getOrg_id()+"");
                i.putExtra(DatabaseOpenHelper.tell,list1.get(position).getTell());
                i.putExtra(DatabaseOpenHelper.fax,list1.get(position).getFax());
                i.putExtra(DatabaseOpenHelper.po_box,list1.get(position).getPo_box());
                i.putExtra(DatabaseOpenHelper.email,list1.get(position).getEmail());
                i.putExtra(DatabaseOpenHelper.street_name,list1.get(position).getStreet_name());
                i.putExtra(DatabaseOpenHelper.address,list1.get(position).getAddress());
                i.putExtra(DatabaseOpenHelper.city,list1.get(position).getCity());
                i.putExtra(DatabaseOpenHelper.phone,list1.get(position).getPhone()+"");
                i.putExtra(DatabaseOpenHelper.country,list1.get(position).getCountry());
                i.putExtra(DatabaseOpenHelper.logo,list1.get(position).getLogo());
                i.putExtra(DatabaseOpenHelper.latitude,list1.get(position).getLatitude());
                i.putExtra(DatabaseOpenHelper.longitude,list1.get(position).getLongitude());
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);

            }
        };
        adapter.setOnItemClickListener(onItemClickListener);
        return view;
    }

    public void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    public void onPause() {
        super.onPause();
        dataSource.close();
    }



}

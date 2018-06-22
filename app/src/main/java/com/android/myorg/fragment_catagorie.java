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
public class fragment_catagorie extends Fragment {
    Dialog dialog;
    DataSource dataSource;
    private FloatingActionButton fab;
    private boolean isListView;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private cataRecycleAdapter adapter;
    public fragment_catagorie(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.cata_main,container,false);

        fab= (FloatingActionButton)view.findViewById(R.id.fab_catagorie);
        dataSource=new DataSource(getActivity());
        dataSource.open();
        List<cata> list=dataSource.findFilteredCata(null,null,null,null,DatabaseOpenHelper.cata_name);
        if(list.size()==0) {
            cata orga=new cata();
            orga.setCata_name("No result");

            list.add(orga);


        }
        final List<cata> list1=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            list1.add(list.get(i));
        }
        Log.i("bbb",""+list.size());
        recyclerView=(RecyclerView)view.findViewById(R.id.list);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        isListView=false;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(getActivity(),search.class);
                startActivity(i);
            }
        });
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        adapter=new cataRecycleAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        cataRecycleAdapter.OnItemClickListener onItemClickListener=new cataRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i=new Intent(getActivity(),orgaListMain.class);
                i.putExtra(DatabaseOpenHelper.cata_id,list1.get(position).getCata_id());
                i.putExtra(DatabaseOpenHelper.cata_name,list1.get(position).getCata_name());

                getActivity().overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                setAllowEnterTransitionOverlap(true);
                Log.i("position",list1.get(position).getCata_id()+"");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.slide_out_top, R.anim.slide_in_bottom);

            }
        };
        adapter.setOnItemClickListener(onItemClickListener);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0||dy<0&&fab.isShown()){
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                    fab.show();
                super.onScrollStateChanged(recyclerView, newState);

            }
        });
        return view;
    }
    private void toggle(View view) {
        FloatingActionButton item = (FloatingActionButton)view.findViewById(R.id.fab_catagorie);
        if (isListView) {
            item.setImageResource(R.drawable.ic_action_list);
            isListView = false;
            staggeredGridLayoutManager.setSpanCount(1);
        } else {
            item.setImageResource(R.drawable.ic_action_grid);
            isListView = true;
            staggeredGridLayoutManager.setSpanCount(2);
        }
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

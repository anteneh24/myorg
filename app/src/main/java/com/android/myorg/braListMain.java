package com.android.myorg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * Created by anteneh on 5/26/2016.
 */
public class braListMain extends AppCompatActivity {
    DataSource dataSource;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private braRecycleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        dataSource=new DataSource(this);
        dataSource.open();
        List<bra> list=dataSource.findFilteredBra(null,null,null,null,DatabaseOpenHelper.bra_name);
        for(int i=0;i<list.size();i++){
            List<org> logo=dataSource.findFilteredOrg(DatabaseOpenHelper.org_id+"="+list.get(i).getorg_id(),null,null,null,null);
            list.get(i).setLogo(logo.get(0).getLogo());
            Log.i("image",logo.get(0).getLogo());
        }
        if(list.size()==0) {
            bra orga=new bra();
            orga.setBra_name("No result");
            //orga.setLogo(R.drawable.noting);
            list.add(orga);
        }
        Log.i("bbb",""+list.size());
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        try{
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
        }catch (NullPointerException e){}
        adapter=new braRecycleAdapter(this,list);
        recyclerView.setAdapter(adapter);
        braRecycleAdapter.OnItemClickListener onItemClickListener=new braRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(braListMain.this,"Clicked"+position,Toast.LENGTH_SHORT).show();
            }
        };
        adapter.setOnItemClickListener(onItemClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }

}

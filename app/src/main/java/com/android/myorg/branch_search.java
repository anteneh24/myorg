package com.android.myorg;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anteneh on 6/7/2016.
 */
public class branch_search extends Fragment {
    Dialog dialog;
    DataSource dataSource;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private braRecycleAdapter adapter;
    public branch_search(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_search_tab,container,false);

        dataSource=new DataSource(getActivity());
        dataSource.open();
        final List<bra> list=dataSource.findFilteredBra(null,null,null,null,DatabaseOpenHelper.bra_name);
        for(int i=0;i<list.size();i++){
            String logo=dataSource.findlogoforbra(DatabaseOpenHelper.org_id+"="+list.get(i).getBra_id());
            list.get(i).setLogo(logo);
//            list.get(i).setLogo(logo.get(i).getLogo());
  //          Log.i("image",logo.get(i).getLogo());
        }
        final List<bra> list1=new ArrayList<bra>();
        for(int i=0;i<list.size();i++){
            list1.add(list.get(i));
        }
        Log.i("bbb",""+list1.size());
        Log.i("bbb",""+list.size());
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter=new braRecycleAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
        EditText tx=(EditText)view.findViewById(R.id.search_bar);
        tx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i=0;i<list.size();i++){
                    list.remove(i);
                }
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        braRecycleAdapter.OnItemClickListener onItemClickListener=new braRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent i=new Intent(getActivity(),ScrollingActivity.class);
                i.putExtra(DatabaseOpenHelper.bra_id,list1.get(position).getBra_id()+"");
                Log.i("bbb phone",list1.get(position).getBra_id()+"");
                i.putExtra(DatabaseOpenHelper.org_name,list1.get(position).getBra_name());
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
                startActivity(i);
            }
        };
        adapter.setOnItemClickListener(onItemClickListener);
        return view;
    }
    public void CustomDialog(){
        dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window=dialog.getWindow();
        WindowManager.LayoutParams param=window.getAttributes();
        param.gravity= Gravity.CENTER;
        dialog.setCanceledOnTouchOutside(true);


        dialog.show();
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

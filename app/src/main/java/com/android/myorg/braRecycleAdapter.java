package com.android.myorg;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anteneh on 5/26/2016.
 */
public class braRecycleAdapter  extends RecyclerView.Adapter<braRecycleAdapter.ViewHolder> implements Filterable{
    private Context context;
    OnItemClickListener itemClickListener;
    DataSource dataSource;
    List<bra> placeList;
    String[] sponsor={"","update","Sponser"};
    List<bra> custom;
    List<bra> a;
    private CustomFilter mFilter;
    int id;
    public braRecycleAdapter(Context context,List<bra> placeList){
        this.context=context;
        this.placeList=placeList;
        custom=new ArrayList<bra>();
        custom.addAll(placeList);
        a=new ArrayList<>();
        mFilter=new CustomFilter(braRecycleAdapter.this);
        Log.i("bbb","inside con");
        DisplayImageOptions defaultOptions=new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(context.getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();


        ImageLoader.getInstance().init(config);
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row,null);

        ViewHolder customViewHolder=new ViewHolder(view);
        return  customViewHolder;

    }

    @Override
    public int getItemCount() {

        return placeList.size();
    }

    @Override
    public void onBindViewHolder(braRecycleAdapter.ViewHolder holder, int position) {
        bra org;
        org = placeList.get(position);
        holder.textView.setText(org.getBra_name());
        ImageLoader.getInstance().displayImage(org.getLogo(),holder.imageView);

        holder.title.setText(sponsor[org.getState()]);
        id=org.bra_id;
        position=id;
    }
    public List<bra> geta() {
        return a;
    }

    public void seta(List<bra> a) {
        this.a = a;
        System.out.println(a.size()+"anteneh temesgen");
    }
    @Override
    public Filter getFilter() {
        return mFilter;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView imageView;
        protected TextView textView;
        protected TextView title;
        protected RelativeLayout placeholder;
        View view;
        public ViewHolder(View view){
            super(view);

            this.view=view;
            this.imageView=(ImageView)view.findViewById(R.id.thumbnail);
            this.textView=(TextView)view.findViewById(R.id.title);
            this.title=(TextView)view.findViewById(R.id.textView);
            placeholder=(RelativeLayout)view.findViewById(R.id.mainHolder);
            placeholder.setOnClickListener(this);
        }

        public void onClick(View v){
            if(itemClickListener!=null){
                itemClickListener.onItemClick(view,getPosition());
            }
        }
    }
    public void setOnItemClickListener(OnItemClickListener ItemClickListener) {
        this.itemClickListener = ItemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int postion);
    }
    class CustomFilter extends Filter {
        private braRecycleAdapter adapter;
        private CustomFilter(braRecycleAdapter adapter){
            super();
            this.adapter=adapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            placeList.clear();
            final FilterResults results=new FilterResults();
            if(constraint.length()==0){

                placeList.addAll(custom);
            }else{
                final String filter=constraint.toString();
                System.out.print("0"+constraint.length());
                for(final bra bra:custom){
                    if(bra.getBra_name().toLowerCase().startsWith(filter))
                        placeList.add(bra);
                }
            }
            System.out.println("Count Number " + custom.size());
            seta(placeList);
            results.values=placeList;
            results.count=placeList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            System.out.println("Count Number 2 " + ((List<bra>) results.values).size());
            this.adapter.notifyDataSetChanged();
        }
    }
}

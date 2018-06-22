package com.android.myorg;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by anteneh on 5/26/2016.
 */
public class cataRecycleAdapter extends RecyclerView.Adapter<cataRecycleAdapter.viewHolder>{
    Context context;
    OnItemClickListener itemClickListener;
    DataSource dataSource;
    List<cata> placeList;
    String[] sponsor={"","update","Sponser"};
    int id;
    public cataRecycleAdapter(Context context,List<cata> placeList){
        this.context=context;
        this.placeList=placeList;
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

    @Override
    public int getItemCount() {

        return placeList.size();
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_places,null);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final cataRecycleAdapter.viewHolder holder, int position) {
        final cata org;
        org=placeList.get(position);
        holder.placeName.setText(org.getCata_name());
        ImageLoader.getInstance().displayImage(org.getLogo(),holder.placeImage);

        Bitmap photo;

        photo = BitmapFactory.decodeFile(org.getLogo());
        if(photo!=null)
        Palette.generateAsync(photo,new Palette.PaletteAsyncListener(){
            @Override
            public void onGenerated(Palette palette) {
                int bgcolor=palette.getDarkMutedColor(context.getResources().getColor(android.R.color.holo_blue_dark));
                holder.placeNameHolder.setBackgroundColor(bgcolor);
            }
        });
        id=org.getCata_id();
        position=id;
    }

    public void setOnItemClickListener(OnItemClickListener ItemClickListener) {
        this.itemClickListener = ItemClickListener;
    }
    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public LinearLayout placeHolder;
        public LinearLayout placeNameHolder;
        public TextView placeName;
        public ImageView placeImage;
        View item;

        public viewHolder(View item){
            super(item);
            this.item=item;
            placeHolder=(LinearLayout)item.findViewById(R.id.mainHolder);
            placeNameHolder=(LinearLayout)item.findViewById(R.id.placeNameHolder);
            placeName=(TextView)item.findViewById(R.id.placeName);
            placeImage=(ImageView)item.findViewById(R.id.placeImage);
            placeHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener!=null)
                itemClickListener.onItemClick(item,getPosition());
        }

    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }
}

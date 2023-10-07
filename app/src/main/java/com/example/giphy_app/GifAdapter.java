package com.example.giphy_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GifAdapter extends RecyclerView.Adapter<GifAdapter.GifHolder> {

    private final Context context;
    private final List<Gif> gifList;

    public GifAdapter(Context context, List<Gif> gifs){
        this.context=context;
        gifList=gifs;
    }

    @NonNull
    @NotNull
    @Override
    public GifHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new GifHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GifHolder holder, int position) {

        Gif gif = gifList.get(position);
        holder.title.setText(gif.getTitle());
        Glide.with(context).load(gif.getGifURL()).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return gifList.size();
    }

    public static class GifHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;


        public GifHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            title=itemView.findViewById(R.id.title_gif);
        }
    }
}

package com.speedrocket.progmine.speedrocket.Control;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.speedrocket.progmine.speedrocket.MainChatLayout;
import com.speedrocket.progmine.speedrocket.Model.UsersSearchContent;
import com.speedrocket.progmine.speedrocket.R;
import com.speedrocket.progmine.speedrocket.TestFireBaseTessssssssssssssssssst;
import com.speedrocket.progmine.speedrocket.User_Profle;

import java.util.ArrayList;
import java.util.List;

// K.M


public class UsersSearchAdapter extends RecyclerView.Adapter<UsersSearchAdapter.MyViewHolder> implements Filterable {
    private List<UsersSearchContent> UsersList;
    private List<UsersSearchContent> UsersFilter;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title ;
        ImageView image;
        Button Profile_btn , main_btn;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textViewSingleListName);
            image = (ImageView)view.findViewById(R.id.circleImageViewUserImage);
            Profile_btn = (Button)view.findViewById(R.id.profile_btn);
            main_btn = (Button)view.findViewById(R.id.main_btn);

        }
    }

    public void setMovieList(Context context,final List<UsersSearchContent> UsersList){
        this.context = context;
        if(this.UsersList == null){
            this.UsersList = UsersList;
            this.UsersFilter = UsersList;
            notifyItemChanged(0, UsersFilter.size());
        } else {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return UsersSearchAdapter.this.UsersList.size();
                }

                @Override
                public int getNewListSize() {
                    return UsersList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return UsersSearchAdapter.this.UsersList.get(oldItemPosition).getId() == UsersList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

                    UsersSearchContent newMovie = UsersSearchAdapter.this.UsersList.get(oldItemPosition);

                    UsersSearchContent oldMovie = UsersList.get(newItemPosition);

                    return newMovie.getTitle() == oldMovie.getTitle() ;
                }
            });
            this.UsersList = UsersList;
            this.UsersFilter = UsersList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public UsersSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersSearchAdapter.MyViewHolder holder, final int position) {
        try {

             final Integer UserId = UsersList.get(position).getId();
            final UsersSearchContent images = UsersList.get(position);
            final String profile_name = UsersList.get(position).getTitle();
            holder.title.setText(UsersFilter.get(position).getTitle());
           Glide.with(context).load("https://speed-rocket.com/upload/users/"+images.getImageUrl()).apply(RequestOptions.centerCropTransform()).into(holder.image);


            holder.Profile_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),TestFireBaseTessssssssssssssssssst.class);
                    intent.putExtra("UserId",UserId.toString());
                    intent.putExtra("profile_name",profile_name.toString());
                    intent.putExtra("image_profile",images.getImageUrl());
                  //  Bundle bundle = new Bundle();

                    v.getContext().startActivity(intent);



                }
            });
            holder.main_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(),MainChatLayout.class);
                    v.getContext().startActivity(intent);
                }
            });

        }catch (Exception e){
            Log.e("image","image has error");
        }

    }

    @Override
    public int getItemCount() {

        if(UsersList != null){
            return UsersFilter.size();
        } else {
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    UsersFilter = UsersList;
                } else {
                    List<UsersSearchContent> filteredList = new ArrayList<>();
                    for (UsersSearchContent movie : UsersList) {
                        if (movie.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(movie);
                        }
                    }
                    UsersFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = UsersFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                UsersFilter = (List<UsersSearchContent>) filterResults.values;

                notifyDataSetChanged();

            }
        };
    }



}

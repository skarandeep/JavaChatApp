package com.example.javachatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

// UserAdapter to extend RecyclerView and member methods to be implemented, attach the ViewHolder defined below, and finally implementing methods again
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context homeActivity;
    ArrayList<Users> usersArrayList;

    public UserAdapter(HomeActivity homeActivity, ArrayList<Users> usersArrayList) {
        this.homeActivity = homeActivity;
        this.usersArrayList = usersArrayList;

    }

    @NonNull
    @Override
    //changing the longer RecyclerViewHolder to ViewHolder only
    //public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(homeActivity).inflate(R.layout.item_user_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    /*removing the BindHolder

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    }
    */

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    //defining ViewHolder class and building its Constructor
        class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userProfile;
        TextView userName, userStatus;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
//now that adapter is made, you need to bind it
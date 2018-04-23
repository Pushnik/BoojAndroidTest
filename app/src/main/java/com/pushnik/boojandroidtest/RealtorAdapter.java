package com.pushnik.boojandroidtest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by John Pushnik on 4/21/2018.
 */

public class RealtorAdapter extends RecyclerView.Adapter<RealtorAdapter.RealtorViewHolder> {

    private List<Realtor> realtorList;
    private PopupCallback callback;

    public class RealtorViewHolder extends RecyclerView.ViewHolder {

        private TextView firstName, lastName, phoneNumber;
        private ImageView photo;

        public RealtorViewHolder(View view) {
            super(view);

            firstName = (TextView) view.findViewById(R.id.first_name);
            lastName = (TextView) view.findViewById(R.id.last_name);
            phoneNumber = (TextView) view.findViewById(R.id.phone_number);
            photo = (ImageView) view.findViewById(R.id.image_realtor);
        }
    }

    public RealtorAdapter(List<Realtor> realtorList, PopupCallback callback) {
        this.realtorList = realtorList;
        this.callback = callback;
    }

    @Override
    public RealtorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_realtor, parent, false);
        return new RealtorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RealtorViewHolder holder, int position) {
        Realtor realtor = realtorList.get(position);

        holder.firstName.setText(realtor.getFirstName());
        holder.lastName.setText(realtor.getLastName());
        holder.phoneNumber.setText(realtor.getPhoneNumber());
        callback.loadImage(holder.photo, realtor.getPhoto(), 30);
        setOnClickListener(holder.itemView, realtor);
    }

    @Override
    public int getItemCount() {
        return realtorList.size();
    }

    private void setOnClickListener(View view, final Realtor realtor) {
        view.setClickable(true);
        view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callback.showPopup(view, realtor);
                    }
                }
        );
    }

}

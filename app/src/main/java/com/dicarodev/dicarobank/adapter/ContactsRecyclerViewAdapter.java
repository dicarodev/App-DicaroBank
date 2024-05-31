package com.dicarodev.dicarobank.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.dicarodev.dicarobank.R;
import com.dicarodev.dicarobank.model.bizum.Bizum;
import com.dicarodev.dicarobank.model.contact.Contact;

import java.util.List;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder>{

    private List<Contact> contactsList;
    private Bizum bizum;
    private NavController navController;

    public ContactsRecyclerViewAdapter(List<Contact> contactsList, Bizum bizum, NavController navController) {
        this.contactsList = contactsList;
        this.bizum = bizum;
        this.navController = navController;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvContactName, tvContactPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvContactName = itemView.findViewById(R.id.contactName_tv);
            tvContactPhone = itemView.findViewById(R.id.contactPhone_et);
        }
    }

    @NonNull
    @Override
    public ContactsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_contact, parent, false);
        return new ContactsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsRecyclerViewAdapter.ViewHolder holder, int position) {
        Contact contact = contactsList.get(position);
        holder.tvContactName.setText(contact.getName());
        holder.tvContactPhone.setText(contact.getPhone());

        holder.itemView.setOnClickListener(v -> {
            if (bizum != null) {
                bizum.setContact(contact);
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable("bizum", bizum);
            navController.navigate(R.id.nav_bizumDetails, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }
}

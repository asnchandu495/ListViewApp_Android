package com.example.listviewapp;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentViewHolder;

import com.google.android.material.internal.TextDrawableHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class PersonAdapter extends ArrayAdapter<Person> {
    private Context mContext;
    private int mResource;
    private  CheckBox cBox;
    public boolean showCheckboxes;
    public PersonAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Person> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        showCheckboxes = false;
    }

    public void setShowCheckboxes(boolean showCheckboxes) {
        this.showCheckboxes = showCheckboxes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater =LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);
        cBox = convertView.findViewById(R.id.checkBox);
         if(showCheckboxes){
             cBox.setVisibility(View.VISIBLE);
         } else {
             cBox.setVisibility(View.GONE);
         }
         TextView  title = (TextView) convertView.findViewById(R.id.txtName);
         TextView des = (TextView) convertView.findViewById(R.id.txtDes);
         title.setText(getItem(position).getName());
         des.setText(getItem(position).getDes());
         cBox.setChecked(getItem(position).isSelected());
         Person item = getItem(position);
         cBox.setChecked(item.isSelected);
         cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getItem(position).setSelected(isChecked);
                getSelectedCount();
            }
        });
         return  convertView;
    }
    public int getSelectedCount (){
        int count = 0;
        for (int i = 0 ; i< getCount(); i++) {
            if (getItem(i).isSelected){
                count++;
            }
        }
        Log.e("selection count", count+"");
        return count;
    }
}

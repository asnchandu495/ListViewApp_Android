package com.example.listviewapp;



import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyboardShortcutGroup;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class MainActivity extends AppCompatActivity {

    // on below line we are creating variables.
    private ListView languageLV;
    private Button addBtn ,updateBtn, deleteBtn, selectBtn;
    private EditText itemEdt, itemDes;
    private ArrayList<Person> lngList;
    private ArrayList<Person>  deleteList ;
    private Integer indexValue, index;
    private TextView textName,textDes;
    private int id,selIDVal;
    public  PersonAdapter adapter ;


    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this);

        // on below line we are initializing our variables.
        languageLV = findViewById(R.id.idLVLanguages);
        addBtn = findViewById(R.id.idBtnAdd);
        itemEdt = findViewById(R.id.idEdtItemName);
        itemDes = findViewById(R.id.idEdtItemDes);
        updateBtn = findViewById(R.id.idBtnUpdate);
        textName = findViewById(R.id.txtName);
        textDes = findViewById(R.id.txtDes);
        deleteBtn = findViewById(R.id.idBtnDel);
        selectBtn = findViewById(R.id.idBtnCB);


        lngList = new ArrayList<>();
        deleteList = new ArrayList<>();

        loadDataInListView();


        // on below line we are adding items to our list
//        lngList.add("C++");
//        lngList.add("Python");

        // on the below line we are initializing the adapter for our list view.
        adapter = new PersonAdapter(this, R.layout.list_row, lngList);

        // on below line we are setting adapter for our list view.
        languageLV.setAdapter(adapter);

        // on below line we are adding click listener for our button.
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // on below line we are getting text from edit text
                String Name = itemEdt.getText().toString();
                String Des = itemDes.getText().toString();

                // on below line we are checking if item is not empty
                if (!Name.isEmpty() && Name.length()>0  && !Des.isEmpty()&& Des.length()>0) {

                    // on below line we are adding item to our list.
                    Person newItem = new Person( Name,Des);
                    if(dbHelper.addItem(Name, Des)){
                        lngList.add(newItem);
//                        loadDataInListView();
                        adapter.notifyDataSetChanged();
                        showToast( "Added : " + Name +"  " +Des);
                        itemEdt.setText("");
                        itemDes.setText("");
                    }

                    showToast( "Mobiles Number Already Exist");



                    // on below line we are notifying adapter
                    // that data in list is updated to
                    // update our list view.


                }
                else {
                showToast("!! Nothing to Add");
                }
            }
        });

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               adapter.setShowCheckboxes(!adapter.showCheckboxes);
               adapter.notifyDataSetChanged();
            }
        });

        languageLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemEdt.setText(lngList.get(position).getName());
                itemDes.setText(lngList.get(position).getDes());
//                 selIDVal = dbHelper.getIdValue(lngList.get(position).getName(),lngList.get(position).getDes());

                indexValue = lngList.get(position).getId();
                index = position;
                adapter.notifyDataSetChanged();
//                lngList.get(position).setSelected(!lngList.get(position).isSelected());
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Name = itemEdt.getText().toString();
                String Des = itemDes.getText().toString();
                if (!Name.isEmpty() && Name.length()>0  && !Des.isEmpty()&& Des.length()>0) {

                    // on below line we are adding item to our list.
                    Person person = new  Person(Name,Des);
                    dbHelper.updateUserData(indexValue,person.getName(),person.getDes());
                    lngList.set(index,new Person( Name,Des));
//                    loadDataInListView();
//                    adapter.setShowCheckboxes(!adapter.showCheckboxes);
                    showToast( "Updated : " + Name +"  " +Des);

                    // on below line we are notifying adapter
                    // that data in list is updated to
                    // update our list view.
                    adapter.notifyDataSetChanged();
                    itemEdt.setText("");
                    itemDes.setText("");
                }
                else {
                    showToast("!! Nothing to Update");
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int delCount = adapter.getSelectedCount();
                if(delCount==0){
                    showToast("No Selected Items ");
                    return;
                }
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Are you Sure")
                        .setMessage("Do You Want To Delete "+delCount+" Items")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (int i = lngList.size()-1; i>=0; i--) {
                                    Person perValue = lngList.get(i);

                                    if(perValue.isSelected()){
                                     dbHelper.deleteUserData(perValue.getName());
                                        lngList.remove(i);
                                    }
                                }
                                clearSelection();
                                adapter.setShowCheckboxes(!adapter.showCheckboxes);
                                adapter.notifyDataSetChanged();
                                itemEdt.setText("");
                                itemDes.setText("");
                                showToast("Selected Items are Deleted");
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return ;
            }
        });
//        delBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dbHelper.deleteData();
//                loadDataInListView();
//                adapter.notifyDataSetChanged();
//
//            }
//        });

        languageLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               new AlertDialog.Builder(MainActivity.this)
                       .setTitle("Are you Sure ?")
                       .setMessage("Do You Want To Delete This Item ?")
                       .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               Person person = lngList.get(position);
                               dbHelper.deleteUserData(person.getName());
                               lngList.remove(position);
                               adapter.notifyDataSetChanged();
                           }
                       })
                       .setNegativeButton("No", null)
                       .show();
               return true;
            }
        });
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        dbHelper.deleteData();
//        loadDataInListView();
//        adapter.notifyDataSetChanged();
//
//    }

    private void showToast(String msg ) {
        Toast.makeText(MainActivity.this ,msg ,Toast.LENGTH_SHORT).show();
    }
    private void clearSelection(){
        for (Person perValue : lngList) {
            perValue.setSelected(false);
        }
    }
    public void loadDataInListView(){
//        dbHelper.deleteData();
        lngList = dbHelper.getAllData();
        PersonAdapter personAdapter = new PersonAdapter(this, R.layout.list_row, lngList);
        languageLV.setAdapter(personAdapter);
        personAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.deleteData();
//        loadDataInListView();
        adapter.notifyDataSetChanged();
    }
}

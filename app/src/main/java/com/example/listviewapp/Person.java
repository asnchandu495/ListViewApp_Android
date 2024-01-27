package com.example.listviewapp;

public class Person {
    String Name;
    String Des;
    int id;

    boolean isSelected;
    boolean isShowCheckboxes;

    public Person( int id, String name, String des) {

        Name = name;
        Des = des;
        this.id = id;
        isSelected = false;
        isShowCheckboxes = false;

    }
    public Person( String name, String des) {

        Name = name;
        Des = des;
        isSelected = false;
        isShowCheckboxes = false;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }

    public  boolean isSelected(){
        return  isSelected;
    }

    public  void setSelected(boolean selected) {
        isSelected = selected;
    }
}

package com.wl;

import com.wl.entry.ZD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 15/10/16.
 */
public class ChildEntity {

    private int groupColor;

    private String groupName;

    private ArrayList<String> childNames;





    public int getGroupColor() {
        return groupColor;
    }

    public String getGroupName() {
        return groupName;
    }

    public ArrayList<String> getChildNames() {
        return childNames;
    }



    public void setGroupColor(int groupColor) {
        this.groupColor = groupColor;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setChildNames(ArrayList<String> childNames) {
        this.childNames = childNames;
    }



    public List<ZD> abc;

}

package com.wl;

import java.util.ArrayList;

/**
 * Created by Frank on 15/10/16.
 */
public class ParentSumEntity {

    private int groupColor;

    private String groupName;

    private ArrayList<ParentEntity> parent;

    public int getGroupColor() {
        return groupColor;
    }

    public String getGroupName() {
        return groupName;
    }

    public ArrayList<ParentEntity> getParent() {
        return parent;
    }

    public void setGroupColor(int groupColor) {
        this.groupColor = groupColor;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setParent(ArrayList<ParentEntity> parent) {
        this.parent = parent;
    }
}

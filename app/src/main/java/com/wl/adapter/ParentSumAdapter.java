package com.wl.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.TextView;

import com.wl.ChildEntity;
import com.wl.ParentEntity;
import com.wl.ParentSumEntity;
import com.wl.ParentSumEntity;
import com.wl.cfwl.R;

import java.util.ArrayList;

/**
 * Created by Frank on 15/10/16.
 */
public class ParentSumAdapter extends BaseExpandableListAdapter {
    private static final long DOUBLE_CLICK_TIME_DELTA = 300; // 双击间隔阈值，单位为毫秒
    private long lastClickTime = 0;
    private Context mContext;

    private ArrayList<ParentSumEntity> mParents;

    private OnChildTreeViewClickListener mTreeViewClickListener;

    public ParentSumAdapter(Context context, ArrayList<ParentSumEntity> parents) {
        this.mContext = context;
        this.mParents = parents;
    }

    @Override
    public ParentEntity getChild(int groupPosition, int childPosition) {
        return mParents.get(groupPosition).getParent().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return mParents.get(groupPosition).getParent() != null ? mParents
                .get(groupPosition).getParent().size() : 0;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isExpanded, View convertView, ViewGroup parent) {

        final ExpandableListView eListView = getExpandableListView();

        ArrayList<ParentEntity> childs = new ArrayList<ParentEntity>();

        final ParentEntity child = getChild(groupPosition, childPosition);
        int groupp=groupPosition;
        int childp=childPosition;
        childs.add(child);

        final ParentAdapter childAdapter = new ParentAdapter(this.mContext,
                childs);
        childAdapter.setOnChildItemClickListener(new ParentAdapter.OnChildItemClickListener() {
            @Override
            public void onChildItemClicked( int groupPosition, int childPosition) {
                Log.e("收到","qq"+groupp);
                if (mTreeViewClickListener != null) {

                    mTreeViewClickListener.onClickPosition(groupp,childp,
                            groupPosition, childPosition);
                }

            }
        });
        eListView.setAdapter(childAdapter);

        eListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1,
                                        int groupIndex, int childIndex, long arg4) {
              /*  long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    if (mTreeViewClickListener != null) {

                        mTreeViewClickListener.onClickPosition(groupPosition,
                                childPosition, childIndex);
                    }

                }
                lastClickTime = clickTime;*/
                    return false;

            }
        });


        /**
         * @author frank
         * <p/>
         * 子ExpandableListView展开时，因为group只有一项，所以子ExpandableListView的总高度 =（子ExpandableListView的child数量 + 1 ）* 每一项的高度
         * */
        eListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                LayoutParams lp = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, (child
                        .getChilds().get(0).getChildNames().size() +child
                        .getChilds().get(1).getChildNames().size()+ 1)
                        * ((int) mContext.getResources().getDimension(
                        R.dimen.parent_expandable_list_height))+300);
                eListView.setLayoutParams(lp);
            }
        });

        /**
         * @author frank
         * <p/>
         * 子ExpandableListView关闭时，此时只剩下group这一项，所以子ExpandableListView的总高度即为一项的高度
         * */
        eListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

                LayoutParams lp = new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, (int) mContext
                        .getResources().getDimension(
                                R.dimen.parent_expandable_list_height));
                eListView.setLayoutParams(lp);
            }
        });
        return eListView;

    }

    /**
     * @author frank
     * <p/>
     * 动态创建子ExpandableListView
     */
    public ExpandableListView getExpandableListView() {
        ExpandableListView mExpandableListView = new ExpandableListView(
                mContext);
        LayoutParams lp = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) mContext
                .getResources().getDimension(
                        R.dimen.parent_expandable_list_height));
        mExpandableListView.setLayoutParams(lp);
        mExpandableListView.setDividerHeight(0);
    /*    mExpandableListView.setDividerHeight(0);// 取消group项的分割线
        mExpandableListView.setChildDivider(null);// 取消child项的分割线
        mExpandableListView.setGroupIndicator(null);// 取消展开折叠的指示图标*/
        return mExpandableListView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mParents.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mParents != null ? mParents.size() : 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.parent_group_item, null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        holder.update(mParents.get(groupPosition));
        return convertView;
    }

    class GroupHolder {

        private TextView parentGroupTV;

        public GroupHolder(View v) {
            parentGroupTV = (TextView) v.findViewById(R.id.parentGroupTV);
        }

        public void update(ParentSumEntity model) {
            parentGroupTV.setText(model.getGroupName());
            parentGroupTV.setTextColor(Color.parseColor("#000000"));
            parentGroupTV.setTextSize(30);
           parentGroupTV.setTypeface(null, Typeface.BOLD);
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setOnChildTreeViewClickListener(
            OnChildTreeViewClickListener treeViewClickListener) {
        this.mTreeViewClickListener = treeViewClickListener;
    }

    public interface OnChildTreeViewClickListener {

        void onClickPosition(int parentPositionsum,int parentPosition, int groupPosition,
                             int childPosition);
    }
}

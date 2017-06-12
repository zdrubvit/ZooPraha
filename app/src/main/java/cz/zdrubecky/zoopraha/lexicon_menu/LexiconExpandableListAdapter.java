package cz.zdrubecky.zoopraha.lexicon_menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import cz.zdrubecky.zoopraha.R;

public class LexiconExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<String> mExpandableListTitles;
    private HashMap<String, List<String>> mExpandableListData;

    public LexiconExpandableListAdapter(Context context, List<String> expandableListTitles,
                                        HashMap<String, List<String>> expandableListData) {
        mContext = context;
        mExpandableListTitles = expandableListTitles;
        mExpandableListData = expandableListData;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return mExpandableListData.get(mExpandableListTitles.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fragment_lexicon_menu_list_item, null);
        }

        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.fragment_lexicon_menu_list_item_title);
        expandedListTextView.setText(expandedListText);

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return mExpandableListData.get(mExpandableListTitles.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return mExpandableListTitles.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return mExpandableListTitles.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fragment_lexicon_menu_list_group, null);
        }

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.fragment_lexicon_menu_list_group_title);
        listTitleTextView.setText(listTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}

package cz.zdrubecky.zoopraha.section.lexicon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.zdrubecky.zoopraha.R;
import cz.zdrubecky.zoopraha.section.lexicon.menu.LexiconExpandableListAdapter;
import cz.zdrubecky.zoopraha.section.lexicon.menu.LexiconExpandableListDataMapper;


public class LexiconMenuTaxonomyFragment extends Fragment {
    private ExpandableListView mTaxonomyExpandableListView;
    private ExpandableListAdapter mTaxonomyExpandableListAdapter;
    private List<String> mTaxonomyExpandableListTitles;
    private HashMap<String, List<String>> mTaxonomyExpandableListData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTaxonomyExpandableListData = LexiconExpandableListDataMapper.getTaxonomyData(getActivity());
        mTaxonomyExpandableListTitles = new ArrayList<>(mTaxonomyExpandableListData.keySet());
        mTaxonomyExpandableListAdapter = new LexiconExpandableListAdapter(getActivity(), mTaxonomyExpandableListTitles, mTaxonomyExpandableListData);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lexicon_menu_tab, container, false);

        mTaxonomyExpandableListView = (ExpandableListView) v.findViewById(R.id.activity_lexicon_menu_expandable_listview);
        mTaxonomyExpandableListView.setAdapter(mTaxonomyExpandableListAdapter);
        mTaxonomyExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity().getApplicationContext(),
                        mTaxonomyExpandableListTitles.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        mTaxonomyExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String groupName = mTaxonomyExpandableListTitles.get(groupPosition);
                String key = "order_name";
                String value = mTaxonomyExpandableListData.get(groupName).get(childPosition);

                Intent i = LexiconListActivity.newIntent(getActivity(), key, value);

                startActivity(i);

                return true;
            }
        });

        return v;
    }
}

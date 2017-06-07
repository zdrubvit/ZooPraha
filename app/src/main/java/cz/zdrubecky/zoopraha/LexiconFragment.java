package cz.zdrubecky.zoopraha;

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

import cz.zdrubecky.zoopraha.lexicon_menu.TaxonomyExpandableListAdapter;
import cz.zdrubecky.zoopraha.lexicon_menu.TaxonomyListDataMapper;


public class LexiconFragment extends Fragment {
    private ExpandableListView mTaxonomyExpandableListView;
    private ExpandableListAdapter mTaxonomyExpandableListAdapter;
    private List<String> mTaxonomyExpandableListTitles;
    private HashMap<String, List<String>> mTaxonomyExpandableListData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lexicon, container, false);

        mTaxonomyExpandableListView = (ExpandableListView) v.findViewById(R.id.activity_lexicon_taxonomy_expandable_listview);
        mTaxonomyExpandableListData = TaxonomyListDataMapper.getData(getActivity());
        mTaxonomyExpandableListTitles = new ArrayList<>(mTaxonomyExpandableListData.keySet());
        mTaxonomyExpandableListAdapter = new TaxonomyExpandableListAdapter(getActivity(), mTaxonomyExpandableListTitles, mTaxonomyExpandableListData);
        mTaxonomyExpandableListView.setAdapter(mTaxonomyExpandableListAdapter);
        mTaxonomyExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity().getApplicationContext(),
                        mTaxonomyExpandableListTitles.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        mTaxonomyExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity().getApplicationContext(),
                        mTaxonomyExpandableListTitles.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        mTaxonomyExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        mTaxonomyExpandableListTitles.get(groupPosition)
                                + " -> "
                                + mTaxonomyExpandableListData.get(
                                mTaxonomyExpandableListTitles.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();

                return false;
            }
        });

        return v;
    }
}

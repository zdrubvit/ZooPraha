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
import cz.zdrubecky.zoopraha.lexicon_menu.LexiconExpandableListAdapter;
import cz.zdrubecky.zoopraha.lexicon_menu.LexiconExpandableListDataMapper;


public class LexiconMenuFiltersFragment extends Fragment {
    private ExpandableListView mFiltersExpandableListView;
    private ExpandableListAdapter mFiltersExpandableListAdapter;
    private List<String> mFiltersExpandableListTitles;
    private HashMap<String, List<String>> mFiltersExpandableListData;
    private HashMap<String, String> mFilterGroups;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFilterGroups = new HashMap<>();
        mFilterGroups.put(getString(R.string.lexicon_menu_biotopes_filter_text), "biotopes");
        mFilterGroups.put(getString(R.string.lexicon_menu_continents_filter_text), "continents");
        mFilterGroups.put(getString(R.string.lexicon_menu_food_filter_text), "food");

        mFiltersExpandableListData = LexiconExpandableListDataMapper.getFilterData(getActivity(), mFilterGroups, getString(R.string.lexicon_menu_locations_filter_text));
        mFilterGroups.put(getString(R.string.lexicon_menu_locations_filter_text), "locations");
        mFiltersExpandableListTitles = new ArrayList<>(mFiltersExpandableListData.keySet());
        mFiltersExpandableListAdapter = new LexiconExpandableListAdapter(getActivity(), mFiltersExpandableListTitles, mFiltersExpandableListData);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lexicon_menu_tab, container, false);

        mFiltersExpandableListView = (ExpandableListView) v.findViewById(R.id.activity_lexicon_menu_expandable_listview);
        mFiltersExpandableListView.setAdapter(mFiltersExpandableListAdapter);
        mFiltersExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity().getApplicationContext(),
                        mFiltersExpandableListTitles.get(groupPosition) + " List Expanded for " + mFilterGroups.get(mFiltersExpandableListTitles.get(groupPosition)),
                        Toast.LENGTH_SHORT).show();
            }
        });

        mFiltersExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String groupName = mFiltersExpandableListTitles.get(groupPosition);
                String key = mFilterGroups.get(groupName);
                String value = mFiltersExpandableListData.get(groupName).get(childPosition);

                Intent i = LexiconListActivity.newIntent(getActivity(), key, value);

                startActivity(i);

                return true;
            }
        });

        return v;
    }
}

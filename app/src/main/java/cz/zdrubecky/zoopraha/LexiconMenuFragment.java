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


public class LexiconMenuFragment extends Fragment {
    private ExpandableListView mTaxonomyExpandableListView;
    private ExpandableListView mFiltersExpandableListView;
    private ExpandableListAdapter mTaxonomyExpandableListAdapter;
    private ExpandableListAdapter mFiltersExpandableListAdapter;
    private List<String> mTaxonomyExpandableListTitles;
    private List<String> mFiltersExpandableListTitles;
    private HashMap<String, List<String>> mTaxonomyExpandableListData;
    private HashMap<String, List<String>> mFiltersExpandableListData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HashMap<String, String> filterGroups = new HashMap<>();
        filterGroups.put("biotopes", getString(R.string.lexicon_menu_biotopes_filter_text));
        filterGroups.put("continents", getString(R.string.lexicon_menu_continents_filter_text));
        filterGroups.put("food", getString(R.string.lexicon_menu_food_filter_text));

        mFiltersExpandableListData = TaxonomyListDataMapper.getData(getActivity(), filterGroups, getString(R.string.lexicon_menu_locations_filter_text));
        mFiltersExpandableListTitles = new ArrayList<>(mFiltersExpandableListData.keySet());
        mFiltersExpandableListAdapter = new TaxonomyExpandableListAdapter(getActivity(), mFiltersExpandableListTitles, mFiltersExpandableListData);

        mTaxonomyExpandableListData = TaxonomyListDataMapper.getData(getActivity());
        mTaxonomyExpandableListTitles = new ArrayList<>(mTaxonomyExpandableListData.keySet());
        mTaxonomyExpandableListAdapter = new TaxonomyExpandableListAdapter(getActivity(), mTaxonomyExpandableListTitles, mTaxonomyExpandableListData);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lexicon_menu, container, false);
/*
// todo implement either 3-level list or some cooperation between two lists
        TextView button = (TextView) v.findViewById(R.id.activity_lexicon_taxonomy_toggler_button);
        button.setOnClickListener(new View.OnClickListener() {
            private boolean mVisible = false;
            @Override
            public void onClick(View v) {
                int visibility = mVisible ? View.GONE : View.VISIBLE;
                mVisible = !mVisible;

                mTaxonomyExpandableListView.setVisibility(visibility);
            }
        });

        mTaxonomyExpandableListView = (ExpandableListView) v.findViewById(R.id.activity_lexicon_taxonomy_expandable_listview);
        mTaxonomyExpandableListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
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
*/
        mFiltersExpandableListView = (ExpandableListView) v.findViewById(R.id.activity_lexicon_filters_expandable_listview);
        mFiltersExpandableListView.setAdapter(mFiltersExpandableListAdapter);
        mFiltersExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity().getApplicationContext(),
                        mFiltersExpandableListTitles.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        mFiltersExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity().getApplicationContext(),
                        mFiltersExpandableListTitles.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        mFiltersExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        mFiltersExpandableListTitles.get(groupPosition)
                                + " -> "
                                + mFiltersExpandableListData.get(
                                mFiltersExpandableListTitles.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();

                return false;
            }
        });

        return v;
    }
}

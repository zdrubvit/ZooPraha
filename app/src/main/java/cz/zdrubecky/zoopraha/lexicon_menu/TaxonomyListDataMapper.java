package cz.zdrubecky.zoopraha.lexicon_menu;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.zdrubecky.zoopraha.manager.ClassificationManager;
import cz.zdrubecky.zoopraha.manager.FilterManager;
import cz.zdrubecky.zoopraha.model.Classification;
import cz.zdrubecky.zoopraha.model.Filter;

public class TaxonomyListDataMapper {
    public static HashMap<String, List<String>> getData(Context context) {
        HashMap<String, List<String>> expandableListData = new HashMap<String, List<String>>();
        ClassificationManager manager = new ClassificationManager(context);
        List<Classification> taxonomy = manager.getTaxonomy();

        for (Classification classification : taxonomy) {
            List<String> orderNames = new ArrayList<>();

            for (Classification order : classification.getOrders()) {
                orderNames.add(order.getName());
            }

            expandableListData.put(classification.getName(), orderNames);
        }

        return expandableListData;
    }

    public static HashMap<String, List<String>> getFilterData(Context context, HashMap<String, String> groups) {
        HashMap<String, List<String>> expandableListData = new HashMap<String, List<String>>();
        FilterManager filterManager = new FilterManager(context);
        String[] filterNames = new String[] {"biotopes", "continents", "food"};

        for (HashMap.Entry<String, String> entry : groups.entrySet()) {
            List<String> filterValues = getFilterValues(filterManager, entry.getKey());
            expandableListData.put(entry.getValue(), filterValues);
        }

        return expandableListData;
    }

    public static List<String> getFilterValues(FilterManager filterManager, String filterName) {
        List<Filter> filters = filterManager.getFilters(filterName);
        List<String> filterValues = new ArrayList<>();

        for (Filter filter : filters) {
            filterValues.add(filter.getValue());
        }

        return filterValues;
    }
}

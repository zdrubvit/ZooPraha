package cz.zdrubecky.zoopraha.section.lexicon.menu;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cz.zdrubecky.zoopraha.R;
import cz.zdrubecky.zoopraha.manager.ClassificationManager;
import cz.zdrubecky.zoopraha.manager.FilterManager;
import cz.zdrubecky.zoopraha.manager.LocationManager;
import cz.zdrubecky.zoopraha.model.Classification;
import cz.zdrubecky.zoopraha.model.Filter;
import cz.zdrubecky.zoopraha.model.Location;

public class LexiconExpandableListDataMapper {
    public static HashMap<String, List<String>> getTaxonomyData(Context context) {
        HashMap<String, List<String>> expandableListData = new LinkedHashMap<>();
        ClassificationManager manager = new ClassificationManager(context);
        List<Classification> taxonomy = manager.getTaxonomy();

        for (Classification classification : taxonomy) {
            List<String> orderNames = new ArrayList<>();

            // Put an option to choose the whole class instead of single orders in the first position
            orderNames.add(context.getString(R.string.lexicon_menu_classifications_filter_first_item_text));

            for (Classification order : classification.getOrders()) {
                orderNames.add(order.getName());
            }

            expandableListData.put(classification.getName(), orderNames);
        }

        return expandableListData;
    }

    public static HashMap<String, List<String>> getFilterData(Context context, HashMap<String, String> filterGroups, String locationGroupName) {
        HashMap<String, List<String>> expandableListData = new LinkedHashMap<>();
        FilterManager filterManager = new FilterManager(context);
        LocationManager locationManager = new LocationManager(context);

        for (HashMap.Entry<String, String> entry : filterGroups.entrySet()) {
            List<String> filterValues = getFilterValues(filterManager, entry.getValue());
            expandableListData.put(entry.getKey(), filterValues);
        }

        expandableListData.put(locationGroupName, getLocationValues(locationManager));

        return expandableListData;
    }

    public static List<String> getFilterValues(FilterManager filterManager, String filterName) {
        List<Filter> filters = filterManager.getFilters(filterName);
        List<String> filterValues = new ArrayList<>();

        for (Filter filter : filters) {
            // Concatenate the filter's value with the number of records that fulfill it
            filterValues.add(filter.getValue() + " (" + Integer.toString(filter.getCount()) + ")");
        }

        return filterValues;
    }

    public static List<String> getLocationValues(LocationManager locationManager) {
        List<Location> locations = locationManager.getLocations();
        List<String> filterValues = new ArrayList<>();

        for (Location location : locations) {
            filterValues.add(location.getName());
        }

        return filterValues;
    }
}

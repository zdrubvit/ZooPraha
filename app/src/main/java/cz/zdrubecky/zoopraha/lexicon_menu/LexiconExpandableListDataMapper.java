package cz.zdrubecky.zoopraha.lexicon_menu;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.zdrubecky.zoopraha.manager.ClassificationManager;
import cz.zdrubecky.zoopraha.manager.FilterManager;
import cz.zdrubecky.zoopraha.manager.LocationManager;
import cz.zdrubecky.zoopraha.model.Classification;
import cz.zdrubecky.zoopraha.model.Filter;
import cz.zdrubecky.zoopraha.model.Location;

public class LexiconExpandableListDataMapper {
    public static HashMap<String, List<String>> getTaxonomyData(Context context) {
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

    public static HashMap<String, List<String>> getFilterData(Context context, HashMap<String, String> filterGroups, String locationGroupName) {
        HashMap<String, List<String>> expandableListData = new HashMap<String, List<String>>();
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
            filterValues.add(filter.getValue());
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

package cz.zdrubecky.zoopraha.lexicon_menu;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.zdrubecky.zoopraha.manager.ClassificationManager;
import cz.zdrubecky.zoopraha.model.Classification;

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
}

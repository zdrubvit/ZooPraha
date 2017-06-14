package cz.zdrubecky.zoopraha;

import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public abstract class SearchActivity extends SingleFragmentActivity {
    private static final String TAG ="SearchActivity";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_list, menu);

        // Get the item directly from the menu, the view will follow (API 11 allowed this)
        final MenuItem searchItem = menu.findItem(R.id.fragment_list_menu_item_search);
        // The correct call to get the View - it can't be done through the MenuItem itself
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Collapse the View - again, compat specific method call
                searchView.onActionViewCollapsed();

                // Get the currently focused view (soft keyboard)
                View view = getCurrentFocus();
                if (view != null) {
                    // Get the system service and use it to hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                updateItems(query);

                setSearchQuery(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: " + newText);

                return false;
            }
        });

        // Show the currently active search query after entering the search view
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = getSearchQuery();
                searchView.setQuery(searchQuery, false);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_list_menu_item_search_clear:
                if (getSearchQuery() != null) {
                    setSearchQuery(null);

                    updateItems(null);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Method to be called whenever there's a need to update and replace the visible list of items
    protected abstract void updateItems(String searchQuery);

    // Return a section-specific stored search query string
    protected abstract String getSearchQuery();

    // Save the query to a section-specific storage
    protected abstract void setSearchQuery(String searchQuery);
}

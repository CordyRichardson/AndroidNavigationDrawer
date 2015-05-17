package richardson.com.navigationdrawerwork;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.Locale;


public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mShipNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mShipNames = getResources().getStringArray(R.array.ships_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                         R.string.drawer_open, R.string.drawer_close){

                //called when a drawer has settled in a complete close
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();    //created call to onPrepareOptionsMenu()
            }
        };

        //set the drawer toggle as the drawerlistener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //set the adapter for the list view
        //Cordy note: the adapter fills listview that's in the drawer
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mShipNames));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }



    //click listener
    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id){
            selectItem(position);
        }
    }

    //called whenever we cal invalidateOptionsMenu()
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        //if hte nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }



    //swap fragments in the main content view
    private void selectItem(int position){
       //create a new fragment and specify the ship to show based on position
        Fragment fragment = new ShipFragment();
        Bundle args = new Bundle();
        args.putInt(ShipFragment.ARG_SHIP_NUMBER, position);
        fragment.setArguments(args);

        //Insert the fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        //highlight the selected item, update the title and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mShipNames[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    public static class ShipFragment extends Fragment{
        public static final String ARG_SHIP_NUMBER = "ship_number";

        public ShipFragment(){
            //empty constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.fragment_ship, container, false);
            int i = getArguments().getInt(ARG_SHIP_NUMBER);
            String ship = getResources().getStringArray
                    (R.array.ships_array)[i];
            int imageId = getResources().getIdentifier(ship.toLowerCase
                    (Locale.getDefault()),
                        "drawable", getActivity().getPackageName());
                        ((ImageView)rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(ship);
            return rootView;
        }
    }
}
package edu.osu.cse.todolist.to_dolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Zicong on 2015/11/6.
 */
public class LocationListFragment extends Fragment {
    private RecyclerView mLocationRecyclerView;
    private LocationAdapter mAdapter;
    private TextView mNoLocationTextView;
    private com.github.clans.fab.FloatingActionButton mFloatingAddLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.fragment_location_list, container, false);
        mLocationRecyclerView = (RecyclerView) view.findViewById(R.id.location_recycler_view);
        mNoLocationTextView = (TextView) view.findViewById(R.id.no_location_text_view);
        mLocationRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), null));
        mLocationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLocationRecyclerView.setItemAnimator(new DefaultItemAnimator());


        mFloatingAddLocation = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.floating_action_add_location);
        mFloatingAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location mLocation = new Location();
                mLocation.setConfig(Location.ConfigType.GPS);
                ToDoLab.get(getActivity()).addLocation(mLocation);
                Intent intent = LocationDetailActivity.newIntent(getActivity(), mLocation.getId());
                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        updateUI();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        ToDoLab toDoLab = ToDoLab.get(getActivity());
        List<Location> locations = toDoLab.getLocations();
        if (locations.isEmpty()) {
            mLocationRecyclerView.setVisibility(View.GONE);
            mNoLocationTextView.setVisibility(View.VISIBLE);
        } else {
            mNoLocationTextView.setVisibility(View.GONE);
            mLocationRecyclerView.setVisibility(View.VISIBLE);
            if (mAdapter == null) {
                mAdapter = new LocationAdapter(locations);
                mLocationRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private class LocationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mLocationTitleTextView;
        private TextView mLocationTypeTextView;
        private Location mLocation;

        public LocationHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mLocationTitleTextView = (TextView) itemView.findViewById(R.id.list_item_location_title_text_view);
            mLocationTypeTextView = (TextView) itemView.findViewById(R.id.list_item_location_type_text_view);
        }


        @Override
        public void onClick(View v) {
            Intent intent = LocationDetailActivity.newIntent(getActivity(), mLocation.getId());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        }

        public void bindLocation(Location location) {
            mLocation = location;
            mLocationTitleTextView.setText(mLocation.getTitle());
            switch (mLocation.getConfig()) {
                case GPS:
                    mLocationTypeTextView.setText(R.string.list_based_on_gps);
                    break;
                case WiFi:
                    mLocationTypeTextView.setText(R.string.list_based_on_wifi);
                    break;
            }
        }
    }

    private class LocationAdapter extends RecyclerView.Adapter<LocationHolder> {
        private List<Location> mLocations;

        public LocationAdapter(List<Location> locations) {
            mLocations = locations;
        }

        @Override
        public LocationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_location, parent, false);
            return new LocationHolder(view);
        }

        @Override
        public void onBindViewHolder(LocationHolder holder, int position) {
            Location location = mLocations.get(position);
            holder.bindLocation(location);
        }

        @Override
        public int getItemCount() {
            return mLocations.size();
        }

    }
}

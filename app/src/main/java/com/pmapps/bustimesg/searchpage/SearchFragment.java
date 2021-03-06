package com.pmapps.bustimesg.searchpage;

import static com.pmapps.bustimesg.helper.HelperMethods.*;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.pmapps.bustimesg.R;
import com.pmapps.bustimesg.database.AppDatabase;
import com.pmapps.bustimesg.database.LTADao;
import com.pmapps.bustimesg.database.LTAModel;
import com.pmapps.bustimesg.database.RouteDao;
import com.pmapps.bustimesg.database.RouteModel;
import com.pmapps.bustimesg.favpage_nearbypage.bus.BusTimingsActivity;
import com.pmapps.bustimesg.favpage_nearbypage.busstop.BusStopAdapter;
import com.pmapps.bustimesg.favpage_nearbypage.busstop.BusStopItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private Context mContext;
    private ProgressBar progressBar;

    // routes
    private RouteDao routeDao;
    private RoutesAdapter routesAdapter;
    private ArrayList<RouteModel> routeModelArrayList;
    private RecyclerView searchResultsRecyclerView_ROUTES;

    // stops
    private LTADao ltaDao;
    private BusStopAdapter busStopAdapter;
    private ArrayList<BusStopItem> busStopItemArrayList;
    private RecyclerView searchResultsRecyclerView_STOPS;

    // constants
    private final static String[] OPERATORS = {"SBST","SMRT","GAS","TTS"};
    private final static String TIH_URL = "https://tih-api.stb.gov.sg/transport/v1/bus_service/operator/";
    private final static String ARRAY_NAME = "data";
    private final static String BUSNUM_STRING = "number";

    // initialising the context of fragment, for ease of use. no need to call requireContext() all the time.
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        initialiseStuffs(v);
        return v;
    }


    private void initialiseStuffs(View v) {
        //create database instance
        AppDatabase db = AppDatabase.getInstance(mContext.getApplicationContext());
        routeDao = db.routeDao();
        ltaDao = db.ltaDao();

        //initialise views
        progressBar = v.findViewById(R.id.progress_bar);
        SearchView searchView = v.findViewById(R.id.search_view);
        ChipGroup chipGroup = v.findViewById(R.id.chip_group);

        // search results for routes
        searchResultsRecyclerView_ROUTES = v.findViewById(R.id.search_results_routes_recycler_view);
        searchResultsRecyclerView_ROUTES.setHasFixedSize(true);
        searchResultsRecyclerView_ROUTES.setLayoutManager(new LinearLayoutManager(mContext));
        routeModelArrayList = new ArrayList<>();
        routesAdapter = new RoutesAdapter(mContext, routeModelArrayList, this::openBusRoute);
        searchResultsRecyclerView_ROUTES.setAdapter(routesAdapter);

        // search results for stops
        searchResultsRecyclerView_STOPS = v.findViewById(R.id.search_results_stops_recycler_view);
        searchResultsRecyclerView_STOPS.setHasFixedSize(true);
        searchResultsRecyclerView_STOPS.setLayoutManager(new LinearLayoutManager(mContext));
        busStopItemArrayList = new ArrayList<>();
        busStopAdapter = new BusStopAdapter(mContext, busStopItemArrayList, this::openBusStop);
        searchResultsRecyclerView_STOPS.setAdapter(busStopAdapter);

        // GET: gets the "cross button" component/child of the search view
        AppCompatImageView imageView = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);

        // SET: sets the image of the "cross button"
        imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_searchview_cancel_icon));

        // GET: gets the edit text component/child of the searchview
        EditText editText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        // SET: sets text appearance as 12sp, white, circularstd font (light)
        editText.setTextAppearance(R.style.EditTextStyle);

        // SET: sets hint text "find bus stops / bus routes"
        editText.setHint(R.string.searchview_hint_text);

        // SET: re-sets the hint text color to faded white, since the original text appearance is white.
        editText.setHintTextColor(ContextCompat.getColor(mContext,R.color.faded_white));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Chip selectedChip = chipGroup.findViewById(chipGroup.getCheckedChipId());
                if (Objects.equals(selectedChip.getTag(), "0")) {
                    progressBar.setVisibility(View.VISIBLE);
                    searchResultsRecyclerView_ROUTES.setVisibility(View.GONE);
                    searchResultsRecyclerView_STOPS.setVisibility(View.GONE);

                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(),0);

                    routeDao.clearAllRoutes();
                    routeModelArrayList.clear();
                    serviceCall0(TIH_URL + OPERATORS[0] + "?apikey=" + TIH_API_KEY(mContext), query);
                } else if (Objects.equals(selectedChip.getTag(),"1")) {
                    progressBar.setVisibility(View.VISIBLE);
                    searchResultsRecyclerView_ROUTES.setVisibility(View.GONE);
                    searchResultsRecyclerView_STOPS.setVisibility(View.GONE);

                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(),0);

                    busStopItemArrayList.clear();

                    // now, query for results, add to list
                    for (LTAModel model: ltaDao.getMatchingData(query)) {
                        BusStopItem busStopItem = new BusStopItem(model.getBusStopName(), model.getBusStopRoad(), model.getBusStopCode());
                        busStopItemArrayList.add(busStopItem);
                    }
                    // once added,
                    progressBar.setVisibility(View.GONE);
                    searchResultsRecyclerView_STOPS.setVisibility(View.VISIBLE);
                    busStopAdapter.notifyDataSetChanged();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void serviceCall0(String url0, String query) {
        JsonObjectRequest request = new JsonObjectRequest(url0, response -> {

            try {
                JSONArray jsonArray = response.getJSONArray(ARRAY_NAME);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject busHit = jsonArray.getJSONObject(i);
                    String busNumber = busHit.getString(BUSNUM_STRING);
                    RouteModel model = new RouteModel(busNumber,OPERATORS[0]);
                    routeDao.insertRoute(model);
                }
                // on response success of 0, call 1
                serviceCall1(TIH_URL + OPERATORS[1] + "?apikey=" + TIH_API_KEY(mContext), query);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        Volley.newRequestQueue(mContext).add(request);
    }

    private void serviceCall1(String url1, String query) {
        JsonObjectRequest request = new JsonObjectRequest(url1, response -> {

            try {
                JSONArray jsonArray = response.getJSONArray(ARRAY_NAME);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject busHit = jsonArray.getJSONObject(i);
                    String busNumber = busHit.getString(BUSNUM_STRING);
                    RouteModel model = new RouteModel(busNumber,OPERATORS[1]);
                    routeDao.insertRoute(model);
                }
                // on response success of 1, call 2
                serviceCall2(TIH_URL + OPERATORS[2] + "?apikey=" + TIH_API_KEY(mContext), query);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        Volley.newRequestQueue(mContext).add(request);
    }

    private void serviceCall2(String url2, String query) {
        JsonObjectRequest request = new JsonObjectRequest(url2, response -> {

            try {
                JSONArray jsonArray = response.getJSONArray(ARRAY_NAME);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject busHit = jsonArray.getJSONObject(i);
                    String busNumber = busHit.getString(BUSNUM_STRING);
                    RouteModel model = new RouteModel(busNumber,OPERATORS[2]);
                    routeDao.insertRoute(model);
                }
                // on response success of 2, call 3
                serviceCall3(TIH_URL + OPERATORS[3] + "?apikey=" + TIH_API_KEY(mContext), query);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        Volley.newRequestQueue(mContext).add(request);
    }

    private void serviceCall3(String url3, String query) {
        JsonObjectRequest request = new JsonObjectRequest(url3, response -> {

            try {
                JSONArray jsonArray = response.getJSONArray(ARRAY_NAME);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject busHit = jsonArray.getJSONObject(i);
                    String busNumber = busHit.getString(BUSNUM_STRING);
                    RouteModel model = new RouteModel(busNumber,OPERATORS[3]);
                    routeDao.insertRoute(model);
                }
                // on response success of 3, get MATCHING DATA
                progressBar.setVisibility(View.GONE);
                searchResultsRecyclerView_ROUTES.setVisibility(View.VISIBLE);

                routeModelArrayList.addAll((ArrayList<RouteModel>) routeDao.getMatchingData(query));
                routesAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        Volley.newRequestQueue(mContext).add(request);
    }

    private void openBusRoute(RouteModel routeModel) {
        Intent intent = new Intent(mContext, BusRouteActivity.class);
        intent.putExtra("BUS_ROUTE_NUM",routeModel.getBusNumber());
        startActivity(intent);
    }

    private void openBusStop(BusStopItem busStopItem) {
        Intent intent = new Intent(mContext, BusTimingsActivity.class);
        intent.putExtra("BUS_STOP_ITEM",busStopItem);
        startActivity(intent);
    }

}
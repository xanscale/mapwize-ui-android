package io.mapwize.mapwizeui.refacto;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.transition.Fade;
import androidx.transition.Scene;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionManager;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;

import java.util.List;

import io.mapwize.mapwizesdk.api.DirectionMode;
import io.mapwize.mapwizesdk.api.Floor;
import io.mapwize.mapwizesdk.api.MapwizeObject;
import io.mapwize.mapwizesdk.api.Place;
import io.mapwize.mapwizesdk.api.Placelist;
import io.mapwize.mapwizesdk.api.Universe;
import io.mapwize.mapwizesdk.api.Venue;
import io.mapwize.mapwizesdk.core.MapwizeConfiguration;
import io.mapwize.mapwizesdk.map.ClickEvent;
import io.mapwize.mapwizesdk.map.FollowUserMode;
import io.mapwize.mapwizesdk.map.MapOptions;
import io.mapwize.mapwizesdk.map.MapwizeMap;
import io.mapwize.mapwizesdk.map.MapwizeView;
import io.mapwize.mapwizeui.CompassView;
import io.mapwize.mapwizeui.FloorControllerView;
import io.mapwize.mapwizeui.MapwizeFragmentUISettings;
import io.mapwize.mapwizeui.R;
import io.mapwize.mapwizeui.SearchResultList;

public class MapFragment extends Fragment implements BaseFragment, MapwizeMap.OnVenueEnterListener,
        MapwizeMap.OnVenueExitListener, MapwizeMap.OnUniverseChangeListener, MapwizeMap.OnFloorChangeListener,
        MapwizeMap.OnFloorsChangeListener, MapwizeMap.OnDirectionModesChangeListener, MapwizeMap.OnLanguageChangeListener,
        MapwizeMap.OnFollowUserModeChangeListener, MapwizeMap.OnClickListener, SearchBar.SearchBarListener,
        SearchResultList.SearchResultListListener, FloorControllerView.OnFloorClickListener {

    // Options
    private static String ARG_OPTIONS = "param_options";
    private static String ARG_UI_SETTINGS = "param_ui_settings";
    private static String ARG_MAPWIZE_CONFIGURATION = "param_mapwize_configuration";

    // Component initialization params
    private MapOptions initializeOptions = null;
    private MapwizeFragmentUISettings initializeUiSettings = null;
    private Place initializePlace = null;

    // Component map & mapwize
    private MapwizeMap mapwizeMap;
    private MapwizeView mapwizeView;
    private MapwizeConfiguration mapwizeConfiguration;

    private BasePresenter presenter;

    private ViewGroup sceneRoot;
    private Scene currentScene;
    Scene defaultScene;
    Scene searchScene;

    // Component listener
    private MapFragment.OnFragmentInteractionListener listener;

    /**
     * Create a instance of MapwizeFragment
     * @param mapOptions used to setup the map
     * @return a new instance of MapwizeFragment
     */
    public static MapFragment newInstance(@NonNull MapOptions mapOptions) {
        MapFragment mf = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_OPTIONS, mapOptions);
        MapwizeFragmentUISettings uiSettings = new MapwizeFragmentUISettings.Builder().build();
        bundle.putParcelable(ARG_UI_SETTINGS, uiSettings);
        MapwizeConfiguration mapwizeConfiguration = MapwizeConfiguration.getInstance();
        bundle.putParcelable(ARG_MAPWIZE_CONFIGURATION, mapwizeConfiguration);
        mf.setArguments(bundle);
        return mf;
    }

    /**
     * Create a instance of MapwizeFragment
     * @param mapwizeConfiguration use to setup de sdk configuration
     * @param mapOptions used to setup the map
     * @return a new instance of MapwizeFragment
     */
    public static MapFragment newInstance(@NonNull MapwizeConfiguration mapwizeConfiguration, @NonNull MapOptions mapOptions) {
        MapFragment mf = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_OPTIONS, mapOptions);
        MapwizeFragmentUISettings uiSettings = new MapwizeFragmentUISettings.Builder().build();
        bundle.putParcelable(ARG_UI_SETTINGS, uiSettings);
        bundle.putParcelable(ARG_MAPWIZE_CONFIGURATION, mapwizeConfiguration);
        mf.setArguments(bundle);
        return mf;
    }

    /**
     * Create a instance of MapwizeFragment
     * @param mapOptions used to setup the map
     * @param uiSettings used to display/hide UI elements
     * @return a new instance of MapwizeFragment
     */
    public static MapFragment newInstance(@NonNull MapOptions mapOptions, @NonNull MapwizeFragmentUISettings uiSettings) {
        MapFragment mf = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_OPTIONS, mapOptions);
        bundle.putParcelable(ARG_UI_SETTINGS, uiSettings);
        MapwizeConfiguration mapwizeConfiguration = MapwizeConfiguration.getInstance();
        bundle.putParcelable(ARG_MAPWIZE_CONFIGURATION, mapwizeConfiguration);
        mf.setArguments(bundle);
        return mf;
    }

    /**
     * Create a instance of MapwizeFragment
     * @param mapwizeConfiguration use to setup de sdk configuration
     * @param mapOptions used to setup the map
     * @param uiSettings used to display/hide UI elements
     * @return a new instance of MapwizeFragment
     */
    public static MapFragment newInstance(@NonNull MapwizeConfiguration mapwizeConfiguration, @NonNull MapOptions mapOptions, @NonNull MapwizeFragmentUISettings uiSettings) {
        MapFragment mf = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_OPTIONS, mapOptions);
        bundle.putParcelable(ARG_UI_SETTINGS, uiSettings);
        bundle.putParcelable(ARG_MAPWIZE_CONFIGURATION, mapwizeConfiguration);
        mf.setArguments(bundle);
        return mf;
    }

    /**
     * Create a instance of MapwizeFragment
     * @param mapOptions used to setup the map
     * @param uiSettings used to display/hide UI elements
     * @param mapboxMapOptions used to pass Mapbox options at start
     * @return a new instance of MapwizeFragment
     */
    public static MapFragment newInstance(@NonNull MapOptions mapOptions, @NonNull MapwizeFragmentUISettings uiSettings, @NonNull MapboxMapOptions mapboxMapOptions) {
        MapFragment mf = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_OPTIONS, mapOptions);
        bundle.putParcelable(ARG_UI_SETTINGS, uiSettings);
        MapwizeConfiguration mapwizeConfiguration = MapwizeConfiguration.getInstance();
        bundle.putParcelable(ARG_MAPWIZE_CONFIGURATION, mapwizeConfiguration);
        mf.setArguments(bundle);
        return mf;
    }

    /**
     * Create a instance of MapwizeFragment
     * @param mapwizeConfiguration use to setup de sdk configuration
     * @param mapOptions used to setup the map
     * @param uiSettings used to display/hide UI elements
     * @param mapboxMapOptions used to pass Mapbox options at start
     * @return a new instance of MapwizeFragment
     */
    public static MapFragment newInstance(@NonNull MapwizeConfiguration mapwizeConfiguration, @NonNull MapOptions mapOptions, @NonNull MapwizeFragmentUISettings uiSettings, @NonNull MapboxMapOptions mapboxMapOptions) {
        MapFragment mf = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_OPTIONS, mapOptions);
        bundle.putParcelable(ARG_UI_SETTINGS, uiSettings);
        bundle.putParcelable(ARG_MAPWIZE_CONFIGURATION, mapwizeConfiguration);
        mf.setArguments(bundle);
        return mf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            initializeOptions = bundle.getParcelable(ARG_OPTIONS);
            initializeUiSettings = bundle.getParcelable(ARG_UI_SETTINGS);
            mapwizeConfiguration = bundle.getParcelable(ARG_MAPWIZE_CONFIGURATION);
        }
        if (initializeOptions == null) {
            initializeOptions = new MapOptions.Builder().build();
        }
        if (initializeUiSettings == null) {
            initializeUiSettings = new MapwizeFragmentUISettings.Builder().build();
        }
        if (mapwizeConfiguration == null) {
            mapwizeConfiguration = MapwizeConfiguration.getInstance();
        }
        presenter = new MapPresenter(this, mapwizeConfiguration, initializeOptions);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Mapbox.getInstance(container.getContext(), "pk.mapwize");
        return inflater.inflate(R.layout.mwz_map_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sceneRoot = view.findViewById(R.id.scene_root);
        mapwizeView = new MapwizeView(view.getContext(), mapwizeConfiguration, initializeOptions);
        FrameLayout layout = view.findViewById(R.id.mapViewContainer);
        layout.addView(mapwizeView);
        mapwizeView.onCreate(savedInstanceState);

        // Instantiate Mapwize sdk
        mapwizeView.getMapAsync(mMap -> {
            mapwizeMap = mMap;
            mapwizeMap.getMapboxMap().getUiSettings().setCompassEnabled(false);
            mapwizeMap.addOnClickListener(this);
            mapwizeMap.addOnVenueEnterListener(this);
            mapwizeMap.addOnVenueExitListener(this);
            mapwizeMap.addOnDirectionModesChangeListener(this);
            mapwizeMap.addOnUniverseChangeListener(this);
            mapwizeMap.addOnLanguageChangeListener(this);
            mapwizeMap.addOnFollowUserModeChangeListener(this);
            mapwizeMap.addOnFloorChangeListener(this);
            mapwizeMap.addOnFloorsChangeListener(this);
            presenter.onMapLoaded();
        });
        defaultScene = Scene.getSceneForLayout(sceneRoot, R.layout.mwz_map_scene_default, getContext());
        searchScene = Scene.getSceneForLayout(sceneRoot, R.layout.mwz_search_scene, getContext());
    }

    @Override
    public void onInflate(@Nullable Context context, @Nullable AttributeSet attrs, @Nullable Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mapwizeView != null) {
            mapwizeView.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapwizeView != null) {
            mapwizeView.onResume();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapwizeView != null) {
            mapwizeView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapwizeView != null) {
            mapwizeView.onLowMemory();
        }
    }

    @Override
    public void onPause() {
        if (mapwizeView != null) {
            mapwizeView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onStop() {
        if (mapwizeView != null) {
            mapwizeView.onStop();
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mapwizeView != null) {
            mapwizeView.onDestroy();
        }
    }



    // Scene management
    public void showDefaultScene() {
        if (!defaultScene.equals(currentScene)) {
            currentScene = defaultScene;
            Transition transition = TransitionInflater.from(getContext()).inflateTransition(R.transition.venue_to_default);
            transition.excludeChildren(R.id.mwz_search_bar_placeholder, true);
            TransitionManager.go(defaultScene, transition);
        }
        SearchBarPlaceholder searchBarPlaceholder = defaultScene.getSceneRoot().findViewById(R.id.mwz_search_bar_placeholder);
        searchBarPlaceholder.setText(getResources().getString(R.string.search_venue));
        searchBarPlaceholder.setDirectionButtonVisible(false);
        searchBarPlaceholder.setMenuButtonVisible(!initializeUiSettings.isMenuButtonHidden());
        ProgressBar progressBar = defaultScene.getSceneRoot().findViewById(R.id.mwz_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        searchBarPlaceholder.setListener(new SearchBarPlaceholder.Listener() {
            @Override
            public void onMenuButtonClick() {
                listener.onMenuButtonClick();
            }

            @Override
            public void onDirectionButtonClick() {
                presenter.onDirectionButtonClick();
            }

            @Override
            public void onQueryClick() {
                presenter.onQueryClick();
            }
        });
        CompassView compassView = defaultScene.getSceneRoot().findViewById(R.id.mwz_compass_view);
        compassView.setMapboxMap(mapwizeMap.getMapboxMap());
    }

    public void showVenueEntering(Venue venue, String language) {
        ProgressBar progressBar = defaultScene.getSceneRoot().findViewById(R.id.mwz_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        SearchBarPlaceholder searchBarPlaceholder = defaultScene.getSceneRoot().findViewById(R.id.mwz_search_bar_placeholder);
        String searchPlaceHolder = getResources().getString(R.string.loading_venue_placeholder);
        searchBarPlaceholder.setText(String.format(searchPlaceHolder, venue.getTranslation(language).getTitle()));
    }

    @Override
    public void showSearchScene() {
        currentScene = searchScene;
        Transition transition = TransitionInflater.from(getContext()).inflateTransition(R.transition.default_to_search);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(@NonNull Transition transition) {
                SearchBar searchBar = searchScene.getSceneRoot().findViewById(R.id.mwz_search_bar);
                searchBar.setListener(MapFragment.this);
                searchBar.textFieldRequestFocus();
                presenter.onSearchQueryChange("");
                SearchResultList resultList = searchScene.getSceneRoot().findViewById(R.id.mwz_search_results_list);
                resultList.setListener(MapFragment.this);
            }

            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
//                presenter.onSearchQueryChange("");
            }

            @Override
            public void onTransitionCancel(@NonNull Transition transition) {

            }

            @Override
            public void onTransitionPause(@NonNull Transition transition) {

            }

            @Override
            public void onTransitionResume(@NonNull Transition transition) {

            }
        });
        TransitionManager.go(searchScene, transition);

    }

    @Override
    public void backFromSearchScene() {
        currentScene = defaultScene;
        Transition transition = TransitionInflater.from(getContext()).inflateTransition(R.transition.search_to_default);
        TransitionManager.go(defaultScene, transition);
        SearchBarPlaceholder searchBarPlaceholder = currentScene.getSceneRoot().findViewById(R.id.mwz_search_bar_placeholder);
        searchBarPlaceholder.setText(getResources().getString(R.string.search_venue));
        searchBarPlaceholder.setDirectionButtonVisible(false);
        searchBarPlaceholder.setMenuButtonVisible(!initializeUiSettings.isMenuButtonHidden());
        ProgressBar progressBar = currentScene.getSceneRoot().findViewById(R.id.mwz_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        searchBarPlaceholder.setListener(new SearchBarPlaceholder.Listener() {
            @Override
            public void onMenuButtonClick() {
                listener.onMenuButtonClick();
            }

            @Override
            public void onDirectionButtonClick() {
                presenter.onDirectionButtonClick();
            }

            @Override
            public void onQueryClick() {
                presenter.onQueryClick();
            }
        });
        CompassView compassView = currentScene.getSceneRoot().findViewById(R.id.mwz_compass_view);
        compassView.setMapboxMap(mapwizeMap.getMapboxMap());
        FloorControllerView floorController = currentScene.getSceneRoot().findViewById(R.id.mwz_floor_controller);
        floorController.setListener(this);
    }

    public void showInVenueScene(Venue venue, String language) {
        SearchBarPlaceholder searchBarPlaceholder = currentScene.getSceneRoot().findViewById(R.id.mwz_search_bar_placeholder);
        String searchPlaceHolder = getResources().getString(R.string.search_in_placeholder);
        searchBarPlaceholder.setText(String.format(searchPlaceHolder, venue.getTranslation(language).getTitle()));
        searchBarPlaceholder.setDirectionButtonVisible(true);
        searchBarPlaceholder.setMenuButtonVisible(!initializeUiSettings.isMenuButtonHidden());
        ProgressBar progressBar = currentScene.getSceneRoot().findViewById(R.id.mwz_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);
        searchBarPlaceholder.setListener(new SearchBarPlaceholder.Listener() {
            @Override
            public void onMenuButtonClick() {
                listener.onMenuButtonClick();
            }

            @Override
            public void onDirectionButtonClick() {
                presenter.onDirectionButtonClick();
            }

            @Override
            public void onQueryClick() {
                presenter.onQueryClick();
            }
        });
        CompassView compassView = currentScene.getSceneRoot().findViewById(R.id.mwz_compass_view);
        compassView.setMapboxMap(mapwizeMap.getMapboxMap());
    }

    public void setActiveFloors(List<Floor> floors) {
        FloorControllerView floorController = currentScene.getSceneRoot().findViewById(R.id.mwz_floor_controller);
        floorController.setFloors(floors);
    }

    public void setActiveFloor(Floor floor) {
        FloorControllerView floorController = currentScene.getSceneRoot().findViewById(R.id.mwz_floor_controller);
        if (floorController == null) {
            return;
        }
        floorController.setFloor(floor);
    }

    @Override
    public void showSearchResults(List<MapwizeObject> results) {
        if (currentScene == searchScene) {
            SearchResultList resultList = searchScene.getSceneRoot().findViewById(R.id.mwz_search_results_list);
            resultList.showData(results);
        }
    }

    @Override
    public void centerOnVenue(Venue venue) {
        mapwizeMap.centerOnVenue(venue, 300);
    }

    @Override
    public void centerOnPlace(Place place, Universe universe) {
        mapwizeMap.centerOnPlace(place, 0);
        mapwizeMap.setUniverse(universe);
    }

    // Map Listeners
    @Override
    public void onDirectionModesChange(@NonNull List<DirectionMode> directionModes) {
        presenter.onDirectionModesChange(directionModes);
    }

    @Override
    public void onFloorWillChange(@Nullable Floor floor) {
        presenter.onFloorWillChange(floor);
    }

    @Override
    public void onFloorChange(@Nullable Floor floor) {
        presenter.onFloorChange(floor);
    }

    @Override
    public void onFloorChangeError(@Nullable Floor floor, @NonNull Throwable error) {

    }

    @Override
    public void onFloorsChange(@NonNull List<Floor> list) {
        presenter.onFloorsChange(list);
    }

    @Override
    public void onLanguageChange(@NonNull String language) {
        presenter.onLanguageChange(language);
    }

    @Override
    public void onUniversesChange(@NonNull List<Universe> universes) {
        presenter.onUniversesChange(universes);
    }

    @Override
    public void onUniverseWillChange(@NonNull Universe universe) {
        presenter.onUniverseWillChange(universe);
    }

    @Override
    public void onUniverseChange(@Nullable Universe universe) {
        presenter.onUniverseChange(universe);
    }

    @Override
    public void onUniverseChangeError(@NonNull Universe universe, @NonNull Throwable error) {

    }

    @Override
    public void onVenueEnter(@NonNull Venue venue) {
        presenter.onVenueEnter(venue);
    }

    @Override
    public void onVenueWillEnter(@NonNull Venue venue) {
        presenter.onVenueWillEnter(venue);
    }

    @Override
    public void onVenueEnterError(@NonNull Venue venue, @NonNull Throwable error) {

    }

    @Override
    public void onVenueExit(@NonNull Venue venue) {
        presenter.onVenueExit(venue);
    }

    @Override
    public void onClickEvent(@NonNull ClickEvent clickEvent) {
        if (clickEvent.getEventType() == ClickEvent.VENUE_CLICK) {
            mapwizeMap.centerOnVenue(clickEvent.getVenuePreview(), 300);
        }
        presenter.onClickEvent(clickEvent);
    }

    @Override
    public void onFollowUserModeChange(@NonNull FollowUserMode followUserMode) {
        presenter.onFollowUserModeChange(followUserMode);
    }

    @Override
    public void onQueryChange(String query) {
        presenter.onSearchQueryChange(query);
    }

    @Override
    public void onSearchBackButtonClick() {
        presenter.onSearchBackButtonClick();
    }

    @Override
    public void onSearchResultNull() {

    }

    @Override
    public void onSearchResult(Place place, Universe universe) {
        presenter.onSearchResultPlaceClick(place, universe);
    }

    @Override
    public void onSearchResult(Placelist placelist) {
        presenter.onSearchResultPlacelistClick(placelist);
    }

    @Override
    public void onSearchResult(Venue venue) {
        presenter.onSearchResultVenueClick(venue);
    }

    @Override
    public void onFloorClick(Floor floor) {
        mapwizeMap.setFloor(floor.getNumber());
        setActiveFloor(floor);
    }


    public interface OnFragmentInteractionListener {
        void onMenuButtonClick();
        default void onInformationButtonClick(MapwizeObject mapwizeObject) {

        }
        default void onFragmentReady(MapwizeMap mapwizeMap) {

        }
        default void onFollowUserButtonClickWithoutLocation() {

        }
        default boolean shouldDisplayInformationButton(MapwizeObject mapwizeObject) {
            return false;
        }
        default boolean shouldDisplayFloorController(List<Floor> floors) {
            return true;
        }
    }
}
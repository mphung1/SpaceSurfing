package com.example.SpaceSurfing.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.SpaceSurfing.R;
import com.example.SpaceSurfing.models.Post;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapFragment extends Fragment implements OnMapReadyCallback{

    public static final String TAG = "MapFragment";
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final float ZOOM_LEVEL_HIGH = 20;
    public static final float ZOOM_LEVEL_MEDIUM = 17;
    public static final float ZOOM_LEVEL_LOW = 15;
    public static final int POSTS_BOTTOM_SHEET_REQUEST_CODE = 11111;

    private final static String KEY_LOCATION = "location";

    private MapView mapView;
    private GoogleMap googleMap;
    private AutocompleteSupportFragment autocompleteFragment;
    private RelativeLayout relativeLayoutAutocomplete;
    private FrameLayout flContainer;

    private Map<Marker, Post> markerPost = new HashMap<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference postsRef = db.collection(Post.KEY_POSTS);

    private Post post;

    public MapFragment() {
        // Required empty public constructor
    }

    public MapFragment(Post post) {
        this.post = post;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.map);
        relativeLayoutAutocomplete = view.findViewById(R.id.relativeLayoutAutocomplete);

        flContainer = view.findViewById(R.id.flContainer);

        initGoogleMap(savedInstanceState);
        setUpAddressAutoComplete();
    }

    private void setUpAddressAutoComplete() {
        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), getString(R.string.map_key), Locale.US);
        }

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocompleteFragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng latLng = place.getLatLng();
                CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(ZOOM_LEVEL_LOW);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e(TAG, "An error occurred: " + status);
            }
        });
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        googleMap = map;

        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(getContext());
        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(ZOOM_LEVEL_LOW);
                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

        setMarkers();
    }

    private void setMarkers() {
        postsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Post post = documentSnapshot.toObject(Post.class);
                    if (post.getLatitude() != 0 && post.getLongitude() != 0) {
                        post.setPostId(documentSnapshot.getId());

                        LatLng location = new LatLng(post.getLatitude(), post.getLongitude());
                        MarkerOptions marker = new MarkerOptions()
                                .position(location)
                                .title(post.getName());
                        Marker theMarker = googleMap.addMarker(marker);
                        markerPost.put(theMarker, post);
                    }
                }

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), ZOOM_LEVEL_MEDIUM));
                        PostBottomSheetDialog postDialog =
                                PostBottomSheetDialog.newInstance(markerPost.get(marker)); // make this specific
                        postDialog.setTargetFragment(MapFragment.this, POSTS_BOTTOM_SHEET_REQUEST_CODE);
                        postDialog.show(getFragmentManager(), "PostBottomSheetDialog");

                        return true;
                    }
                });

                if (post != null)
                    displayPost();
            }
        });
    }

    private void displayPost() {
        LatLng postLatLng = new LatLng(post.getLatitude(), post.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(postLatLng, ZOOM_LEVEL_HIGH));
        PostBottomSheetDialog postDialog =
                PostBottomSheetDialog.newInstance(post); // make this specific
        postDialog.setTargetFragment(MapFragment.this, POSTS_BOTTOM_SHEET_REQUEST_CODE);
        postDialog.show(getFragmentManager(), "PostBottomSheetDialog");
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

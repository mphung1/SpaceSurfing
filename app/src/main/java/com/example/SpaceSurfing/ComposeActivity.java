package com.example.SpaceSurfing;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.SpaceSurfing.fragments.PhotoBottomSheetDialog;
import com.example.SpaceSurfing.fragments.ProfileFragment;
import com.example.SpaceSurfing.models.Post;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class ComposeActivity extends AppCompatActivity implements View.OnFocusChangeListener,
        PhotoBottomSheetDialog.PhotoBottomSheetListener {

    private static final String TAG = "ComposeActivity";
    private static final float DAYS_IN_MONTH = 30;
    private static final String STRING_LOOKING_FOR_PLACE = "Looking for a place";
    public static final int RADIUS = 30;

    private TextInputLayout layoutTitle, layoutDescription;
    private EditText etRent, etNumRooms;
    private RadioGroup radioGroupOne, radioGroupFurnished;
    private RadioButton radioButtonHouse, radioButtonFurnished;
    private ImageView ivImagePreview;
    private TextView tvStartDate, tvEndDate, tvTextNumRooms;
    private AutocompleteSupportFragment autocompleteFragment;
    private RelativeLayout relativeLayoutAutocomplete;
    private SeekBar seekBarNumRooms;
    private ProgressBar progressBar;

    private String title, description, startMonth, startDate, endDate;
    private String photoUrl = "", name = "", address = "";
    private Date start, end;
    private int numRooms = 1, rent = -1, numMonths;
    private boolean lookingForHouse = true, furnished = true;
    private double latitude = 0, longitude = 0;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CollectionReference postRef = db.collection(Post.KEY_POSTS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        layoutTitle = findViewById(R.id.layoutTitle);
        layoutDescription = findViewById(R.id.layoutDescription);
        radioGroupOne = findViewById(R.id.radioGroupOne);
        radioGroupFurnished = findViewById(R.id.radioGroupFurnished);
        etRent = findViewById(R.id.etRent);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        ivImagePreview = findViewById(R.id.ivImagePreview);
        relativeLayoutAutocomplete = findViewById(R.id.relativeLayoutAutocomplete);
        tvTextNumRooms = findViewById(R.id.tvTextNumRooms);
        seekBarNumRooms = findViewById(R.id.seekBarNumRooms);
        progressBar = findViewById(R.id.progressBar);

        seekBarNumRooms.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                tvTextNumRooms.setText("Number of rooms: " + progress);
                numRooms = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        setUpAddressAutoComplete();
    }

    private void setUpAddressAutoComplete() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.map_key), Locale.US);
        }

        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        ((EditText)autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input)).setTextSize(16.0f);

        relativeLayoutAutocomplete.setVisibility(View.GONE);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS,
                                                            Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng latLng = place.getLatLng();
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                address = place.getAddress();
                name = place.getName();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e(TAG, "An error occurred: " + status);
            }
        });
    }

    // onClick method for place radio buttons
    public void checkPlaceRadioButton(View view) {
        int radioId = radioGroupOne.getCheckedRadioButtonId();
        radioButtonHouse = findViewById(radioId);
        if (radioButtonHouse.getText().equals(STRING_LOOKING_FOR_PLACE)) {
            lookingForHouse = true;
            relativeLayoutAutocomplete.setVisibility(View.GONE);
        } else {
            lookingForHouse = false;
            relativeLayoutAutocomplete.setVisibility(View.VISIBLE);
        }
    }

    // onClick method for furnished radio buttons
    public void checkFurnishedRadioButton(View view) {
        int radioId = radioGroupFurnished.getCheckedRadioButtonId();
        radioButtonFurnished = findViewById(radioId);
        if (radioButtonFurnished.getText().equals("Yes")) {
            furnished = true;
        } else {
            furnished = false;
        }
    }

    // onClick method for post button - uploads new post to database
    public void makePost(View view) {
        progressBar.setVisibility(View.VISIBLE);
        title = layoutTitle.getEditText().getText().toString();
        description = layoutDescription.getEditText().getText().toString();

        boolean error = false;
        layoutTitle.setError(null);
        layoutDescription.setError(null);
        layoutTitle.clearFocus();
        layoutDescription.clearFocus();

        if (title.isEmpty()) {
            layoutTitle.setError("Title is required");
            error = true;
        }
        if (description.isEmpty()) {
            layoutDescription.setError("Description is required");
            error = true;
        }
        if (tvStartDate.getText().toString().isEmpty()) {
            Toast.makeText(ComposeActivity.this, "Start date is required!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (tvEndDate.getText().toString().isEmpty()) {
            Toast.makeText(ComposeActivity.this, "End date is required!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (error) {
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (!etRent.getText().toString().isEmpty()) {
            rent = Integer.parseInt(etRent.getText().toString());
        }

        // calculate number of months between two dates, rounded up to the nearest whole month
        float daysBetween = ((end.getTime() - start.getTime()) / (1000*60*60*24));
        numMonths = (int) Math.ceil(daysBetween / DAYS_IN_MONTH);

        final Post post = new Post(new java.util.Date(), new ArrayList<String>(), 0, title,
                description, startMonth, firebaseAuth.getCurrentUser().getUid(), numRooms, numMonths,
                rent, furnished, lookingForHouse, startDate, endDate, photoUrl, name, address,
                latitude, longitude);

        postRef.add(post).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressBar.setVisibility(View.GONE);
                post.setPostId(documentReference.getId());
                Intent intent = new Intent();
                intent.putExtra("newPost", Parcels.wrap(post));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    // onClick method for calendar icon for choosing start date
    public void chooseStartDate(View view) {
        DatePickerDialog startDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                tvStartDate.setText(month + "/" + day + "/" + year);
                tvStartDate.setVisibility(View.VISIBLE);
                startMonth = new DateFormatSymbols().getMonths()[month];
                startDate = month + "/" + day + "/" + year;
                try {
                    start = dateFormat.parse(startDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        startDialog.show();
    }

    // onClick method for calendar icon for choosing end date
    public void chooseEndDate(View view) {
        DatePickerDialog endDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                tvEndDate.setText(month + "/" + day + "/" + year);
                tvEndDate.setVisibility(View.VISIBLE);
                endDate = month + "/" + day + "/" + year;
                try {
                    end = dateFormat.parse(endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        endDialog.show();
    }

    public void openBottomSheet(View view) {
        PhotoBottomSheetDialog photoDialog =
                PhotoBottomSheetDialog.newInstance("postImages", UUID.randomUUID().toString());
        photoDialog.show(getSupportFragmentManager(), "PhotoBottomSheetDialog");
    }

    @Override
    public void sendPhotoUri(Uri photoUri) {
        photoUrl = photoUri.toString();
        Glide.with(getApplicationContext()).load(photoUrl).transform(new RoundedCorners(RADIUS)).into(ivImagePreview);
        ivImagePreview.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            switch (view.getId()) {
                case R.id.inputTitle:
                    layoutTitle.setError(null);
                    break;
                case R.id.inputDescription:
                    layoutDescription.setError(null);
                    break;
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
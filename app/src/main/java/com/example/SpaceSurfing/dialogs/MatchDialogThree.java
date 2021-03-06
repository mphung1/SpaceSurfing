package com.example.SpaceSurfing.dialogs;

import androidx.appcompat.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.SpaceSurfing.MatchConstants;
import com.example.SpaceSurfing.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class MatchDialogThree extends DialogFragment {

    public static final String TAG = "MatchDialogThree";
    public static final String KEY_CURR_ACTIVITIES = "currActivities";
    public static final String KEY_CURR_HOBBIES = "currHobbies";
    private static final String KEY_CURR_ACTIVITY_VISIBLE = "currActivityVisible";
    private static final String KEY_CURR_HOBBY_VISIBLE = "currHobbyVisible";
    private PageThreeListener mListener;

    private ChipGroup chipGroupActivities, chipGroupHobbies;
    private Button btnAddActivity, btnAddHobby;
    private EditText etActivity, etHobby;

    private ArrayList<String> activities = new ArrayList<>(), hobbies = new ArrayList<>();
    private CheckBox checkActivityVisibility, checkHobbyVisibility;
    private boolean activityVisible = true, hobbyVisible = true;

    private ImageView ivInfoActivity, ivInfoHobby;

    public interface PageThreeListener {
        void sendPageThreeInputs(int nextPage, ArrayList<String> activities, ArrayList<String> hobbies,
                                 boolean activityVisible, boolean hobbyVisible);
    }

    public static MatchDialogThree newInstance(ArrayList<String> currActivities, ArrayList<String> currHobbies,
                                               boolean currActivityVisible, boolean currHobbyVisible) {

        Bundle args = new Bundle();
        MatchDialogThree fragment = new MatchDialogThree();
        args.putStringArrayList(KEY_CURR_ACTIVITIES, currActivities);
        args.putStringArrayList(KEY_CURR_HOBBIES, currHobbies);
        args.putBoolean(KEY_CURR_ACTIVITY_VISIBLE, currActivityVisible);
        args.putBoolean(KEY_CURR_HOBBY_VISIBLE, currHobbyVisible);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_pagethree, null, false);

        findViews(view);
        setPreviousValues();

        builder.setView(view).setTitle("Interests (Page 3/6)").setPositiveButton("Next", null)
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.sendPageThreeInputs(MatchConstants.PAGE_TWO, activities, hobbies, activityVisible, hobbyVisible);
                        dismiss();
                    }
                });

        return builder.create();
    }

    private void findViews(View view) {

        chipGroupActivities = view.findViewById(R.id.chipGroupActivities);
        chipGroupHobbies = view.findViewById(R.id.chipGroupHobbies);
        btnAddActivity = view.findViewById(R.id.btnAddActivity);
        btnAddHobby = view.findViewById(R.id.btnAddHobby);
        etActivity = view.findViewById(R.id.etActivity);
        etHobby = view.findViewById(R.id.etHobby);
        checkActivityVisibility = view.findViewById(R.id.checkActivityVisibility);
        checkHobbyVisibility = view.findViewById(R.id.checkHobbyVisibility);
        ivInfoActivity = view.findViewById(R.id.ivInfoActivity);
        ivInfoHobby = view.findViewById(R.id.ivInfoHobby);

        ivInfoActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInfoDialog();
            }
        });

        ivInfoHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInfoDialog();
            }
        });

        checkActivityVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                activityVisible = b;
            }
        });

        checkHobbyVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                hobbyVisible = b;
            }
        });

        btnAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tag = etActivity.getText().toString().trim().toLowerCase();
                if (tag.isEmpty())
                    return;
                if (activities.contains(tag)) {
                    Toast.makeText(getContext(), "Can't add a duplicate activity!", Toast.LENGTH_SHORT).show();
                    return;
                }
                activities.add(tag);
                addActivityChip(tag);
                etActivity.getText().clear();
            }
        });

        btnAddHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = etHobby.getText().toString().trim().toLowerCase();
                if (tag.isEmpty())
                    return;
                if (hobbies.contains(tag)) {
                    Toast.makeText(getContext(), "Can't add a duplicate hobby!", Toast.LENGTH_SHORT).show();
                    return;
                }
                hobbies.add(tag);
                addHobbyChip(tag);
                etHobby.getText().clear();
            }
        });
    }

    private void openInfoDialog() {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.ic_baseline_info_24)
                .setTitle("Privacy Information")
                .setMessage("Check this box.")
                .setPositiveButton("Done", null).show();
    }

    private void addHobbyChip(final String toAdd) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        Chip chip = (Chip) inflater.inflate(R.layout.item_chip, null, false);
        chip.setText(toAdd);
        chipGroupHobbies.addView(chip);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroupHobbies.removeView(view);
                hobbies.remove(toAdd);
            }
        });
    }

    private void addActivityChip(final String toAdd) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        Chip chip = (Chip) inflater.inflate(R.layout.item_chip, null, false);
        chip.setText(toAdd);
        chipGroupActivities.addView(chip);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipGroupActivities.removeView(view);
                activities.remove(toAdd);
            }
        });
    }

    private void setPreviousValues() {
        activities = getArguments().getStringArrayList(KEY_CURR_ACTIVITIES);
        hobbies = getArguments().getStringArrayList(KEY_CURR_HOBBIES);
        activityVisible = getArguments().getBoolean(KEY_CURR_ACTIVITY_VISIBLE);
        hobbyVisible = getArguments().getBoolean(KEY_CURR_HOBBY_VISIBLE);

        checkActivityVisibility.setChecked(activityVisible);
        checkHobbyVisibility.setChecked(hobbyVisible);

        for (String activity : activities)
        {
            addActivityChip(activity);
        }
        for (String hobby : hobbies) {
            addHobbyChip(hobby);
        }
    }

    // overrides the next button so that dialog doesn't dismiss if all fields are not answered
    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            Button positiveButton = (Button) dialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.sendPageThreeInputs(MatchConstants.PAGE_FOUR, activities, hobbies, activityVisible, hobbyVisible);
                    dismiss();
                }
            });
        }
    }

    // called when fragment is attached to a host fragment
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (PageThreeListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MatchDialogThree");
        }
    }
}

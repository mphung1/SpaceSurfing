package com.example.SpaceSurfing.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.SpaceSurfing.MainActivity;
import com.example.SpaceSurfing.MatchConstants;
import com.example.SpaceSurfing.MatchDetailActivity;
import com.example.SpaceSurfing.R;
import com.example.SpaceSurfing.fragments.ProfileFragment;
import com.example.SpaceSurfing.models.Post;
import com.example.SpaceSurfing.models.SurveyResponse;

import org.parceler.Parcels;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    public static final String TAG = "MatchAdapter";

    public static final String GENDER_SELF_IDENTIFY = "I identify as: ";
    public static final String GENDER_NO_ANSWER = "I identify as: No answer";
    public static final String GENDER_FEMALE = "I identify as: Female";
    public static final String GENDER_MALE = "I identify as: Male";

    public static final String GENDER_PREF_NONE = "Gender preference: No preference";
    public static final String GENDER_PREF_FEMALE = "Gender preference: Female";
    public static final String GENDER_PREF_MALE = "Gender preference: Male";

    public static final String SMOKING_NON_SMOKER_NOT_OKAY = "Smoking: Non-smoking, not okay with smokers";
    public static final String SMOKING_NON_SMOKER_OKAY = "Smoking: Non-smoking, okay with smokers";
    public static final String SMOKING_SMOKER = "Smoking: Smoker";

    private Context context;
    private List<SurveyResponse> responses;
    private Fragment fragment;

    public MatchAdapter(Context context, List<SurveyResponse> responses, Fragment fragment) {
        this.context = context;
        this.responses = responses;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommendation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SurveyResponse response = responses.get(position);
        holder.bind(response);
    }

    @Override
    public int getItemCount() {
        return responses.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        responses.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAllReponses(List<SurveyResponse> responseList) {
        responses.addAll(responseList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfileImage;
        TextView tvUserName, tvMajorYear, tvDescription, tvCompatibilityScore;
        TextView tvGenderIdentity, tvGenderPreference, tvSmokingPreference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvMajorYear = itemView.findViewById(R.id.tvMajorYear);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvCompatibilityScore = itemView.findViewById(R.id.tvCompatibilityScore);
            tvGenderIdentity = itemView.findViewById(R.id.tvGenderIdentity);
            tvGenderPreference = itemView.findViewById(R.id.tvGenderPreference);
            tvSmokingPreference = itemView.findViewById(R.id.tvSmokingPreference);

            itemView.setOnClickListener(this);
        }

        public void bind(final SurveyResponse response) {
            if (!response.getImageUrl().isEmpty() && response.getImageUrl() != null) {
                Glide.with(context.getApplicationContext()).load(response.getImageUrl()).circleCrop().into(ivProfileImage);
            }
            tvUserName.setText(response.getName());
            tvMajorYear.setText(response.getMajor() + ", " + response.getYear());
            tvDescription.setText(response.getDescription());
            String score = String.format("%.2f", response.getCompatibilityScore());
            tvCompatibilityScore.setText(score + "% match");

            if (response.isPersonalVisible()) {
                bindPersonalInfo(response);
            } else {
                tvGenderIdentity.setVisibility(View.GONE);
                tvGenderPreference.setVisibility(View.GONE);
                tvSmokingPreference.setVisibility(View.GONE);
            }

            // onClickListeners to open ProfileFragment
            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openProfileFragment(response.getUserId());
                }
            });
            tvUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openProfileFragment(response.getUserId());
                }
            });
        }

        private void bindPersonalInfo(SurveyResponse response) {
            tvGenderIdentity.setVisibility(View.VISIBLE);
            tvGenderPreference.setVisibility(View.VISIBLE);
            tvSmokingPreference.setVisibility(View.VISIBLE);

            String gender = response.getGender();
            String genderPref = response.getGenderPref();
            String smoking = response.getSmoking();

            if (gender.equals(MatchConstants.Gender.SELF_IDENTIFY.toString())) {
                tvGenderIdentity.setText(GENDER_SELF_IDENTIFY + response.getSelfIdentifyGender());
            } else if (gender.equals(MatchConstants.Gender.NO_ANSWER.toString())){
                tvGenderIdentity.setText(GENDER_NO_ANSWER);
            } else if (gender.equals(MatchConstants.Gender.FEMALE.toString())) {
                tvGenderIdentity.setText(GENDER_FEMALE);
            } else if (gender.equals(MatchConstants.Gender.MALE.toString())) {
                tvGenderIdentity.setText(GENDER_MALE);
            }

            if (genderPref.equals(MatchConstants.GenderPref.NO_PREFERENCE.toString())) {
                tvGenderPreference.setText(GENDER_PREF_NONE);
            } else if (genderPref.equals(MatchConstants.GenderPref.FEMALE.toString())) {
                tvGenderPreference.setText(GENDER_PREF_FEMALE);
            } else if (genderPref.equals(MatchConstants.GenderPref.MALE.toString())) {
                tvGenderPreference.setText(GENDER_PREF_MALE);
            }

            if (smoking.equals(MatchConstants.Smoke.NON_SMOKER_NO.toString())) {
                tvSmokingPreference.setText(SMOKING_NON_SMOKER_NOT_OKAY);
            } else if (smoking.equals(MatchConstants.Smoke.NON_SMOKER_YES.toString())) {
                tvSmokingPreference.setText(SMOKING_NON_SMOKER_OKAY);
            } else if (smoking.equals(MatchConstants.Smoke.SMOKER.toString())) {
                tvSmokingPreference.setText(SMOKING_SMOKER);
            }
        }

        private void openProfileFragment(String userId) {
            FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
            Fragment fragment = new ProfileFragment(userId);
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(null).commit();
        }

        @Override
        public void onClick(View view) {
            SurveyResponse response = responses.get(getAdapterPosition());
            Intent intent = new Intent(context, MatchDetailActivity.class);
            intent.putExtra(SurveyResponse.class.getSimpleName(), Parcels.wrap(response));
            fragment.startActivity(intent);
            ((MainActivity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

}

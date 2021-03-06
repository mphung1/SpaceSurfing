package com.example.SpaceSurfing.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.SpaceSurfing.MessageActivity;
import com.example.SpaceSurfing.R;
import com.example.SpaceSurfing.adapters.ChatsAdapter;
import com.example.SpaceSurfing.adapters.PostsAdapter;
import com.example.SpaceSurfing.models.Chat;
import com.example.SpaceSurfing.models.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {

    public static final String TAG = "MessagesFragment";
    private static final int REQUEST_CODE_MESSAGE = 77;

    private RecyclerView rvChats;
    private ChatsAdapter adapter;
    private List<Chat> allChats;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CollectionReference chatsRef = db.collection(Chat.KEY_CHATS);

    private ListenerRegistration listenerRegistration;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvChats = view.findViewById(R.id.rvChats);

        allChats = new ArrayList<>();
        adapter = new ChatsAdapter(getContext(), allChats, this);
        rvChats.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvChats.setLayoutManager(layoutManager);
    }

    @Override
    public void onStart() {
        super.onStart();
        listenerRegistration = chatsRef.whereArrayContains(Chat.KEY_MEMBERS, firebaseAuth.getCurrentUser().getUid())
                .orderBy(Chat.KEY_LAST_MESSAGE_DATE, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        Log.d(TAG, "onEvent: ");
                        if (error != null) {
                            Toast.makeText(getContext(), "Error loading chats!", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Error loading chats: ", error);
                        }

                        adapter.clear();
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Chat chat = documentSnapshot.toObject(Chat.class);
                            chat.setChatId(documentSnapshot.getId());

                            allChats.add(chat);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // coming back from MessageActivity
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_MESSAGE) {
            Parcelable updatedChatParcel = data.getParcelableExtra("updatedChat");

            if (updatedChatParcel != null) {
                Chat updatedChat = Parcels.unwrap(updatedChatParcel);

                // find adapter position (where the tweet was)
                int position = -1;
                for (int i = 0; i < allChats.size(); i++) {
                    if (allChats.get(i).getChatId().equals(updatedChat.getChatId())) {
                        position = i;
                        break;
                    }
                }
                allChats.remove(position);
                allChats.add(position, updatedChat);
                adapter.notifyItemChanged(position);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        listenerRegistration.remove();
    }
}

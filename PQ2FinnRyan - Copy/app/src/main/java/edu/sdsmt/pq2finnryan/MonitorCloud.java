package edu.sdsmt.pq2finnryan;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class MonitorCloud {
    private static final String TAG = "monitor";
    private final FirebaseAuth userAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;
    private final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");

    public final static MonitorCloud INSTANCE = new MonitorCloud();
    private MonitorCloud() {}

    public void createUser(String user, String email, String pass) {
        Task<AuthResult> result = userAuth.createUserWithEmailAndPassword(email, pass);
        result.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "createUserWithEmail:onComplete: " + task.isSuccessful());
                firebaseUser = userAuth.getCurrentUser();
                HashMap<String, Object> res = new HashMap<>();
                res.put("/usernames/" + user, email);
                res.put("/" + firebaseUser.getUid() + "/username", user);
                res.put("/" + firebaseUser.getUid() + "/email", email);
                res.put("/" + firebaseUser.getUid() + "/password", pass);
                userRef.updateChildren(res);
            } else
                Log.d(TAG, "createUserWithEmail:failed: " + Objects.requireNonNull(task.getException()).getMessage());
        });
    }

    public void login(String user, String pass) {
        userRef.child("usernames").child(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                signIn(snapshot.getValue(String.class), pass);
                startAuthListening();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void signIn(String email, String pass) {
        userAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                Log.d(TAG, "signInWithEmail:onComplete: " + task.isSuccessful());
            else
                Log.d(TAG, "signInWithEmail:failed", task.getException());
        });
    }

    private void startAuthListening() {
        userAuth.addAuthStateListener(firebaseAuth -> {
            firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null)
                Log.d(TAG, "onAuthStateChanged:signed_in: " +  firebaseUser.getUid());
            else
                Log.d(TAG, "onAuthStateChanged:signed_out");
        });
    }
}

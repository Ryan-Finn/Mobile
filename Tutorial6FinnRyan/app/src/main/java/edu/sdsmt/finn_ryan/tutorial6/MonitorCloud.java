package edu.sdsmt.finn_ryan.tutorial6;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class MonitorCloud {
    private static final String USER = "name";
    private static final String EMAIL = "fake@email.com";
    private static final String PASSWORD = "12345678";
    private static final String TAG = "monitor";
    private final FirebaseAuth userAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;
    private final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
    private boolean authenticated = false;
    public final static MonitorCloud INSTANCE = new MonitorCloud();

    private MonitorCloud() {
        signIn(true);
        startAuthListening();
    }

    private void createUser() {
        Task<AuthResult> result = userAuth.createUserWithEmailAndPassword(EMAIL, PASSWORD);
        result.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                firebaseUser = userAuth.getCurrentUser();
                HashMap<String, Object> res = new HashMap<>();
                res.put("/screenName/"+USER, true);
                res.put("/"+firebaseUser.getUid()+"/name", USER);
                res.put("/"+firebaseUser.getUid()+"/password", PASSWORD);
                res.put("/"+firebaseUser.getUid()+"/email", EMAIL);
                userRef.updateChildren(res);
                signIn(false);
            } else {
                Log.d(TAG, "Problem: " + Objects.requireNonNull(task.getException()).getMessage());
                authenticated = false;
            }
        });
    }

    private void signIn(Boolean firstAttempt) {
        Task<AuthResult> result = userAuth.signInWithEmailAndPassword(EMAIL, PASSWORD);
        result.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                authenticated = true;
            } else {
                Log.w(TAG, "signInWithEmail:failed", task.getException());
                authenticated = false;
                if (firstAttempt)
                    createUser();
            }
        });
    }

    private void startAuthListening() {
        userAuth.addAuthStateListener(firebaseAuth -> {
            firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                authenticated = true;
                Log.d(TAG, "onAuthStateChanged:signed_in:" +  firebaseUser.getUid());
            } else {
                authenticated = false;
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        });
    }

    public String getUserUid() {
        if (firebaseUser == null)
            return "";
        else
            return firebaseUser.getUid();
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}

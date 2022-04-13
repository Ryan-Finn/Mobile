package edu.sdsmt.pq2finnryan;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MonitorCloud {
    private static final String email = "fake@email.com";
    private static final String pass = "12345678";
    private static final String TAG = "monitor";
    private final FirebaseAuth userAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser;

    public final static MonitorCloud INSTANCE = new MonitorCloud();
    private MonitorCloud() {
        signIn(true);
        startAuthListening();
    }

    private void createUser() {
        Task<AuthResult> result = userAuth.createUserWithEmailAndPassword(email, pass);
        result.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "createUserWithEmail:onComplete: " + task.isSuccessful());
                signIn(false);
            } else
                Log.d(TAG, "createUserWithEmail:failed: " + Objects.requireNonNull(task.getException()).getMessage());
        });
    }

    private void signIn(Boolean firstAttempt) {
        userAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                Log.d(TAG, "signInWithEmail:onComplete: " + task.isSuccessful());
            else {
                Log.d(TAG, "signInWithEmail:failed", task.getException());
                if (firstAttempt)
                    createUser();
            }
        });
    }

    private void startAuthListening() {
        userAuth.addAuthStateListener(firebaseAuth -> {
            firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null)
                Log.d(TAG, "onAuthStateChanged:signed_in: " + firebaseUser.getUid());
            else
                Log.d(TAG, "onAuthStateChanged:signed_out");
        });
    }
}

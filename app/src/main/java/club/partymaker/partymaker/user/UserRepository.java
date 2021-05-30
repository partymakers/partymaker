package club.partymaker.partymaker.user;

import android.app.Activity;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import club.partymaker.partymaker.R;

@Singleton
public class UserRepository {
    private final LiveData<FirebaseUser> firebaseUser;
    private final List<AuthUI.IdpConfig> loginProviders = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );
    private final LiveData<String> userId;

    @Inject
    public UserRepository() {
        MutableLiveData<FirebaseUser> mutableFirebaseUser = new MutableLiveData<>();
        MutableLiveData<String> mutableUserId = new MutableLiveData<>();
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            mutableFirebaseUser.setValue(firebaseAuth.getCurrentUser());
            mutableUserId.setValue(firebaseAuth.getUid());
        });
        this.firebaseUser = mutableFirebaseUser;
        this.userId = mutableUserId;
    }

    public Intent getAuthUiIntent() {
        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(loginProviders)
                .setTheme(R.style.LoginTheme)
                .setIsSmartLockEnabled(false, true)
                .build();
    }

    public LiveData<String> getUserId() {
        return userId;
    }

    public String getUserIdValue() {
        return userId.getValue();
    }

    public void updateDisplayedName(String name, OnCompleteListener<Void> onCompleteListener) {
        FirebaseUser user = firebaseUser.getValue();
        if (user == null) {
            return;
        }
        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(changeRequest)
                .addOnCompleteListener(onCompleteListener);
    }

    public void updateEmail(String password, String newEmail,
                            OnFailureListener onAuthenticationFailureListener,
                            OnCompleteListener<Void> onUpdatingEmailListener) {
        FirebaseUser user = firebaseUser.getValue();
        if (user != null && user.getProviderId().equals(EmailAuthProvider.PROVIDER_ID)) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);

            user.reauthenticate(credential)
                    .addOnFailureListener(onAuthenticationFailureListener)
                    .addOnSuccessListener(unused -> {
                        user.verifyBeforeUpdateEmail(newEmail).addOnCompleteListener(onUpdatingEmailListener);
                    });
        }
    }

    public void updatePassword(String oldPassword, String newPassword,
                               OnFailureListener onAuthenticationFailureListener,
                               OnCompleteListener<Void> onUpdatingPasswordListener) {
        FirebaseUser user = firebaseUser.getValue();
        if (user != null && user.getProviderId().equals(EmailAuthProvider.PROVIDER_ID)) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

            user.reauthenticate(credential)
                    .addOnFailureListener(onAuthenticationFailureListener)
                    .addOnSuccessListener(unused -> {
                        user.updatePassword(newPassword).addOnCompleteListener(onUpdatingPasswordListener);
                    });
        }
    }

    public void linkWithGoogle(Activity activity, Runnable onSuccessListener, OnFailureListener onFailureListener) {
        FirebaseUser user = firebaseUser.getValue();
        String googleAuthId = GoogleAuthProvider.PROVIDER_ID;
        if (user != null && !user.getProviderId().equals(googleAuthId)) {
            OAuthProvider provider = OAuthProvider.newBuilder(googleAuthId).build();
            user.startActivityForLinkWithProvider(activity, provider)
                    .addOnSuccessListener(activity, authResult -> onSuccessListener.run())
                    .addOnFailureListener(activity, onFailureListener);
        }
    }

    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }
}

package club.partymaker.partymaker.user;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import club.partymaker.partymaker.R;

@Singleton
public class UserRepository {
    private final List<AuthUI.IdpConfig> loginProviders = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );
    private final LiveData<String> userId;

    @Inject
    public UserRepository() {
        MutableLiveData<String> mutableUserId = new MutableLiveData<>();
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            mutableUserId.setValue(firebaseAuth.getUid());
        });
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
}

package club.partymaker.partymaker.user;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

@HiltViewModel
public class DashboardViewModel extends ViewModel {
    private final MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    private final LiveData<String> name = Transformations.map(user,
            input -> input != null ? input.getDisplayName() : null);
    private final LiveData<String> email = Transformations.map(user,
            input -> input != null ? input.getEmail() : null);
    private final MutableLiveData<String> status = new MutableLiveData<>();

    @Inject
    public DashboardViewModel(UserRepository userRepository) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user.setValue(firebaseAuth.getCurrentUser());
        firebaseAuth.addAuthStateListener(changedAuth -> {
            FirebaseUser newUser = changedAuth.getCurrentUser();
            if (newUser != user.getValue()) {
                user.setValue(newUser);
            }
        });
    }

    public boolean isLoggedIn() {
        return user.getValue() != null;
    }

    public void handleAuthUiResult(int activityResultCode, IdpResponse authResponse, String logcatTag) {
        String statusMessage;
        if (activityResultCode == RESULT_OK) {
            statusMessage = "Login successful";
            Log.i(logcatTag, statusMessage);
        } else if (activityResultCode == RESULT_CANCELED || authResponse == null) {
            statusMessage = "Login cancelled";
            Log.w(logcatTag, statusMessage);
        } else {
            statusMessage = "Login failed";
            if (authResponse.getError() != null) {
                int errorCode = authResponse.getError().getErrorCode();
                statusMessage += ". Error code: " + errorCode;
            }
            Log.e(logcatTag, statusMessage);
        }
        status.setValue(statusMessage);
    }

    public LiveData<String> getName() {
        return name;
    }

    public LiveData<String> getEmail() {
        return email;
    }

    public LiveData<String> getStatus() {
        return status;
    }
}

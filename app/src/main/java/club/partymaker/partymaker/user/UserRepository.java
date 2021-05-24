package club.partymaker.partymaker.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {
    private final LiveData<String> userId;

    @Inject
    public UserRepository() {
        MutableLiveData<String> mutableUserId = new MutableLiveData<>();
        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            mutableUserId.setValue(firebaseAuth.getUid());
        });
        this.userId = mutableUserId;
    }

    public LiveData<String> getUserId() {
        return userId;
    }

    public String getUserIdValue() {
        return userId.getValue();
    }
}

package club.partymaker.partymaker.user;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UserSettingsViewModel extends ViewModel {
    private final UserRepository userRepository;

    @Inject
    public UserSettingsViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void updateDisplayedName(String name){
        this.userRepository.updateDisplayedName(name, v->{});
    }

    public void updateEmail(String password, String email){
        this.userRepository.updateEmail(password, email, v->{}, v->{});
    }

    public void updatePassword(String oldPassword, String newPassword){
        this.userRepository.updateEmail(oldPassword, newPassword, v->{}, v->{});
    }

    public void signOut() {
        this.userRepository.signOut();
    }

    public String getDisplayedName() {
        return this.userRepository.getDisplayedName();
    }

    public String getEmail() {
        return this.userRepository.getEmail();
    }
}

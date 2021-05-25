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
}

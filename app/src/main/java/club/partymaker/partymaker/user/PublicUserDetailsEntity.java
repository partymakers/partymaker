package club.partymaker.partymaker.user;

import com.google.firebase.firestore.DocumentId;

public class PublicUserDetailsEntity {
    @DocumentId
    private String userId;
    private String displayedName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayedName() {
        return displayedName;
    }

    public void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
    }
}

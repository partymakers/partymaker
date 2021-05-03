package com.github.partymakers.partymaker.party;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.github.partymakers.partymaker.BR;
import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;

public class PartyEntity extends BaseObservable implements Parcelable {

    public static final Parcelable.Creator<PartyEntity> CREATOR
            = new Creator<PartyEntity>() {
        @Override
        public PartyEntity createFromParcel(Parcel source) {
            return new PartyEntity(source);
        }

        @Override
        public PartyEntity[] newArray(int size) {
            return new PartyEntity[size];
        }
    };
    private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    @DocumentId
    private String id;
    private List<String> organizersIds = new ArrayList<>();
    private List<String> participantsIds = new ArrayList<>();
    private String name;
    private String description;
    private Long timestamp;
    private String location;
    private String theme;
    private String dressCode;
    private List<String> food = new ArrayList<>();
    private List<String> drinks = new ArrayList<>();
    private String fee;
    private boolean allowsPartner;
    private boolean allowsChildren;
    private boolean allowsFriends;
    private boolean allowsPets;
    private String parkingDetails;
    private String additionalInformation;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeStringList(organizersIds);
        dest.writeStringList(participantsIds);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(timestamp);
        dest.writeString(location);
        dest.writeString(theme);
        dest.writeString(dressCode);
        dest.writeStringList(food);
        dest.writeStringList(drinks);
        dest.writeString(fee);
        boolean[] allows = {allowsPartner, allowsChildren, allowsFriends, allowsPets};
        dest.writeBooleanArray(allows);
        dest.writeString(parkingDetails);
        dest.writeString(additionalInformation);
    }

    public PartyEntity() {
    }

    public PartyEntity(Parcel parcel) {
        id = parcel.readString();
        parcel.readStringList(organizersIds);
        parcel.readStringList(participantsIds);
        name = parcel.readString();
        description = parcel.readString();
        timestamp = parcel.readLong();
        location = parcel.readString();
        theme = parcel.readString();
        dressCode = parcel.readString();
        parcel.readStringList(food);
        parcel.readStringList(drinks);
        fee = parcel.readString();
        boolean[] allows = new boolean[4];
        parcel.readBooleanArray(allows);
        allowsPartner = allows[0];
        allowsChildren = allows[1];
        allowsFriends = allows[2];
        allowsPets = allows[3];
        parkingDetails = parcel.readString();
        additionalInformation = parcel.readString();
    }

    public String getId() {
        return id;
    }

    @Bindable
    public List<String> getOrganizersIds() {
        return organizersIds;
    }

    public void setOrganizersIds(List<String> organizersIds) {
        this.organizersIds = organizersIds;
    }

    @Bindable
    public List<String> getParticipantsIds() {
        return participantsIds;
    }

    public void setParticipantsIds(List<String> participantsIds) {
        this.participantsIds = participantsIds;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Bindable
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
        notifyPropertyChanged(BR.timestamp);
    }

    @Bindable
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Bindable
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    @Bindable
    public String getDressCode() {
        return dressCode;
    }

    public void setDressCode(String dressCode) {
        this.dressCode = dressCode;
    }

    @Bindable
    public List<String> getFood() {
        return food;
    }

    public void setFood(List<String> food) {
        this.food = food;
    }

    @Bindable
    public List<String> getDrinks() {
        return drinks;
    }

    public void setDrinks(List<String> drinks) {
        this.drinks = drinks;
    }

    @Bindable
    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    @Bindable
    public boolean isAllowsPartner() {
        return allowsPartner;
    }

    public void setAllowsPartner(boolean allowsPartner) {
        this.allowsPartner = allowsPartner;
    }

    @Bindable
    public boolean isAllowsChildren() {
        return allowsChildren;
    }

    public void setAllowsChildren(boolean allowsChildren) {
        this.allowsChildren = allowsChildren;
    }

    @Bindable
    public boolean isAllowsFriends() {
        return allowsFriends;
    }

    public void setAllowsFriends(boolean allowsFriends) {
        this.allowsFriends = allowsFriends;
    }

    @Bindable
    public boolean isAllowsPets() {
        return allowsPets;
    }

    public void setAllowsPets(boolean allowsPets) {
        this.allowsPets = allowsPets;
    }

    @Bindable
    public String getParkingDetails() {
        return parkingDetails;
    }

    public void setParkingDetails(String parkingDetails) {
        this.parkingDetails = parkingDetails;
    }

    @Bindable
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}

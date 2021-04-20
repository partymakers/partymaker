package com.github.partymakers.partymaker.party;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PartyEntity {
    private String name;
    private String description;
    private Long timestamp;
    private String location;
    private String theme;
    private String dressCode;
    private List<String> food = new ArrayList<>();
    private List<String> drinks = new ArrayList<>();
    private BigDecimal fee;
    private boolean allowsPartner;
    private boolean allowsChildren;
    private boolean allowsFriends;
    private boolean allowsPets;
    private String parkingDetails;
    private String additionalInformation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDressCode() {
        return dressCode;
    }

    public void setDressCode(String dressCode) {
        this.dressCode = dressCode;
    }

    public List<String> getFood() {
        return food;
    }

    public List<String> getDrinks() {
        return drinks;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public boolean isAllowsPartner() {
        return allowsPartner;
    }

    public void setAllowsPartner(boolean allowsPartner) {
        this.allowsPartner = allowsPartner;
    }

    public boolean isAllowsChildren() {
        return allowsChildren;
    }

    public void setAllowsChildren(boolean allowsChildren) {
        this.allowsChildren = allowsChildren;
    }

    public boolean isAllowsFriends() {
        return allowsFriends;
    }

    public void setAllowsFriends(boolean allowsFriends) {
        this.allowsFriends = allowsFriends;
    }

    public boolean isAllowsPets() {
        return allowsPets;
    }

    public void setAllowsPets(boolean allowsPets) {
        this.allowsPets = allowsPets;
    }

    public String getParkingDetails() {
        return parkingDetails;
    }

    public void setParkingDetails(String parkingDetails) {
        this.parkingDetails = parkingDetails;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }
}

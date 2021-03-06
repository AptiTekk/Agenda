package com.aptitekk.agenda.core.entity;

import com.aptitekk.agenda.core.utilities.EqualsHelper;
import com.aptitekk.agenda.core.utilities.time.SegmentedTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the Room database table.
 */
@Entity
@NamedQuery(name = "Asset.findAll", query = "SELECT r FROM Asset r")
public class Asset implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(columnDefinition = "time")
    private SegmentedTime availabilityEnd;

    @Column(columnDefinition = "time")
    private SegmentedTime availabilityStart;

    private String name;

    private Boolean needsApproval = false;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.REMOVE)
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToOne
    private AssetType assetType;

    @ManyToOne
    private UserGroup owner;

    @ManyToMany
    @OrderBy("name")
    private List<Tag> tags = new ArrayList<>();

    private String imageFileName;

    public Asset() {
    }

    public Asset(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SegmentedTime getAvailabilityEnd() {
        return this.availabilityEnd;
    }

    public void setAvailabilityEnd(SegmentedTime availabilityEnd) {
        this.availabilityEnd = availabilityEnd;
    }

    public SegmentedTime getAvailabilityStart() {
        return this.availabilityStart;
    }

    public void setAvailabilityStart(SegmentedTime availabilityStart) {
        this.availabilityStart = availabilityStart;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getNeedsApproval() {
        return this.needsApproval;
    }

    public void setNeedsApproval(Boolean needsApproval) {
        this.needsApproval = needsApproval;
    }

    public List<Reservation> getReservations() {
        return this.reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Reservation addReservation(Reservation reservation) {
        getReservations().add(reservation);
        reservation.setAsset(this);

        return reservation;
    }

    public Reservation removeReservation(Reservation reservation) {
        getReservations().remove(reservation);
        reservation.setAsset(null);

        return reservation;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType type) {
        this.assetType = type;
    }

    public UserGroup getOwner() {
        return owner;
    }

    public void setOwner(UserGroup owner) {
        this.owner = owner;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null) return false;

        if (!(o instanceof Asset)) return false;

        Asset other = (Asset) o;

        return EqualsHelper.areEquals(getName(), other.getName())
                && EqualsHelper.areEquals(getAvailabilityStart(), other.getAvailabilityStart())
                && EqualsHelper.areEquals(getAvailabilityEnd(), other.getAvailabilityEnd())
                && EqualsHelper.areEquals(getNeedsApproval(), other.getNeedsApproval())
                && EqualsHelper.areEquals(getImageFileName(), other.getImageFileName());
    }

    @Override
    public int hashCode() {
        return EqualsHelper.calculateHashCode(getName(), getAvailabilityStart(), getAvailabilityEnd(), getNeedsApproval(), getImageFileName());
    }

}

package coursework.database.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class AdvertisementTagEntityPK implements Serializable {
    private int advertisementId;
    private int tagId;

    @Column(name = "advertisementId")
    @Id
    public int getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(int advertisementId) {
        this.advertisementId = advertisementId;
    }

    @Column(name = "tagId")
    @Id
    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdvertisementTagEntityPK that = (AdvertisementTagEntityPK) o;

        if (advertisementId != that.advertisementId) return false;
        if (tagId != that.tagId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = advertisementId;
        result = 31 * result + tagId;
        return result;
    }
}

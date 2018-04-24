package coursework.database.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "advertisement_tag", schema = "ads_hibernate_test_db", catalog = "")
@IdClass(AdvertisementTagEntityPK.class)
public class AdvertisementTagEntity implements Serializable
{
    private int advertisementId;
    private int tagId;

    @Id
    @Column(name = "advertisementId")
    public int getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(int advertisementId) {
        this.advertisementId = advertisementId;
    }

    @Id
    @Column(name = "tagId")
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

        AdvertisementTagEntity that = (AdvertisementTagEntity) o;

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

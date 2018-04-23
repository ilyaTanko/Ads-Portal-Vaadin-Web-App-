package coursework.database.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "advertisement", schema = "ads_hibernate_test_db", catalog = "")
public class AdvertisementEntity {
    private int id;
    private String headline;
    private Integer categoryId;
    private String content;
    private int userId;
    private Timestamp publishTime;
    private int viewCount;
    private byte actual;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "headline")
    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    @Basic
    @Column(name = "categoryId")
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "userId")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "publishTime")
    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }

    @Basic
    @Column(name = "viewCount")
    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    @Basic
    @Column(name = "actual")
    public byte getActual() {
        return actual;
    }

    public void setActual(byte actual) {
        this.actual = actual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdvertisementEntity that = (AdvertisementEntity) o;

        if (id != that.id) return false;
        if (userId != that.userId) return false;
        if (viewCount != that.viewCount) return false;
        if (actual != that.actual) return false;
        if (headline != null ? !headline.equals(that.headline) : that.headline != null) return false;
        if (categoryId != null ? !categoryId.equals(that.categoryId) : that.categoryId != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (publishTime != null ? !publishTime.equals(that.publishTime) : that.publishTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (headline != null ? headline.hashCode() : 0);
        result = 31 * result + (categoryId != null ? categoryId.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + userId;
        result = 31 * result + (publishTime != null ? publishTime.hashCode() : 0);
        result = 31 * result + viewCount;
        result = 31 * result + (int) actual;
        return result;
    }
}

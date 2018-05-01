package coursework.gui_forms;

import com.vaadin.server.FileResource;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.AdvertisementEntity;
import coursework.database.entities.AdvertisementTagEntity;
import coursework.database.entities.TagEntity;
import coursework.database.entities.UserEntity;
import coursework.gui_designs.AdFullFormDesign;
import coursework.session.UserSession;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdFullForm extends AdFullFormDesign
{
    private MyUI ui;
    private Window subWindow = new Window();
    AdvertisementEntity ad;

    public AdFullForm(MyUI ui, AdvertisementEntity ad)
    {
        this.ui = ui;
        this.ad = ad;

        image.setSource(new FileResource(new File(getPhotoPath())));
        //image.setWidth(200, Unit.PIXELS);

        updateViewCount();

        headlineLabel.setValue(ad.getHeadline());
        contentLabel.setValue(ad.getContent());
        if (contentLabel.getValue().length() > 60)
            contentLabel.setWidth(700, Unit.PIXELS);
        viewCountLabel.setValue("Число просмотров: " + String.valueOf(ad.getViewCount()));
        authorAndDateLabel.setValue(getAuthorAndDateInfo());
        categoryLabel.setValue(getCategoryName());
        tagsLabel.setValue(getTags());
    }

    private String getPhotoPath()
    {
        int userId = ad.getUserId();
        int adId = ad.getId();
        return UserSession.getBasePath() + "/VAADIN/images/ads/" + userId + "/" + adId;
    }

    private void updateViewCount()
    {
        ad.setViewCount(ad.getViewCount() + 1);
        DatabaseWorker.updateAd(ad);
    }

    private String getAuthorAndDateInfo()
    {
        UserEntity author = DatabaseWorker.getUser(ad.getUserId());
        String authorLogin = author.getLogin();
        String authorName = author.getName();
        String date = String.valueOf(ad.getPublishTime());

        return "Автор: " + authorName + ", " + date;
    }

    private String getCategoryName()
    {
        if (ad.getCategoryId() == null)
            return "Без категории";
        return "Категория: " + DatabaseWorker.getCategory(ad.getCategoryId()).getName();
    }

    @SuppressWarnings("Duplicates")
    private String getTags()
    {
        final String tagsString = "Теги: ";
        ArrayList<String> tags = new ArrayList<>();

        List<AdvertisementTagEntity> adTags = DatabaseWorker.getAdTagsByAdId(ad.getId());
        if (adTags.isEmpty())
        {
            tagsLabel.setVisible(false);
            return "";
        }
        for (AdvertisementTagEntity adTag : adTags)
        {
            TagEntity tag = DatabaseWorker.getTag(adTag.getTagId());
            tags.add(tag.getName());
        }
        return tagsString + String.join(", ", tags);
    }

    @SuppressWarnings("Duplicates")
    public Window getWindow()
    {
        subWindow.setContent(this);
        subWindow.getContent().setSizeUndefined();
        subWindow.setModal(true);
        subWindow.center();
        return subWindow;
    }
}

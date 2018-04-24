package coursework.gui_forms;

import com.vaadin.server.FileResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.AdvertisementEntity;
import coursework.database.entities.AdvertisementTagEntity;
import coursework.database.entities.TagEntity;
import coursework.gui_designs.AdShortFormDesign;
import coursework.session.UserSession;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdShortForm extends AdShortFormDesign
{
    private MyUI ui;
    AdvertisementEntity ad;

    private final int TEXT_LIMIT = 200;

    public AdShortForm(MyUI ui, AdvertisementEntity ad)
    {
        this.ui = ui;
        this.ad = ad;

        adImage.setSource(new FileResource(new File(getPhotoPath())));
        adImage.setWidth(200, Unit.PIXELS);

        adNameLabel.setValue(ad.getHeadline());
        adShortTextLabel.setValue(truncateContent());
        adShortTextLabel.setWidth(250, Unit.PIXELS);
        adShortTextLabel.setHeight(250, Unit.PIXELS);
        dateLabel.setValue("Время добавления: " + String.valueOf(ad.getPublishTime()));
        categoryLabel.setValue(getCategoryName());
        tagsLabel.setValue(getTags());

        moreInfoButton.addClickListener(new MoreInfoButtonClickListener());
    }

    private String getPhotoPath()
    {
        int userId = ad.getUserId();
        int adId = ad.getId();
        return UserSession.getBasePath() + "/VAADIN/images/ads/" + userId + "/" + adId;
    }

    private String truncateContent()
    {
        String adContent = ad.getContent();
        if (adContent.length() > TEXT_LIMIT)
            return adContent.substring(0, TEXT_LIMIT) + "...";
        return adContent;
    }

    private String getCategoryName()
    {
        if (ad.getCategoryId() == null)
            return "Без категории";
        return "Категория: " + DatabaseWorker.getCategory(ad.getCategoryId()).getName();
    }

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

    private class MoreInfoButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent) {

        }
    }
}

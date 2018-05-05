package coursework.gui_forms;

import com.vaadin.server.FileResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.AdvertisementEntity;
import coursework.database.entities.AdvertisementTagEntity;
import coursework.database.entities.TagEntity;
import coursework.database.entities.UserEntity;
import coursework.gui_designs.AdShortFormDesign;
import coursework.session.UserSession;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GeneralAdShortForm extends AdShortFormDesign
{
    private MyUI ui;
    AdvertisementEntity ad;

    private final int TEXT_LIMIT = 200;

    public GeneralAdShortForm(MyUI ui, AdvertisementEntity ad)
    {
        this.ui = ui;
        this.ad = ad;

        editButton.setVisible(false);
        deleteButton.setVisible(false);
        if (UserSession.getCurrentUser() != null &&
            UserSession.getCurrentUser().getType() == UserEntity.USER_ADMIN)
        {
            editButton.setVisible(true);
            deleteButton.setVisible(true);
        }
        authorLabel.setVisible(false);

        adImage.setSource(new FileResource(new File(getPhotoPath())));
        adImage.setWidth(200, Unit.PIXELS);

        adNameLabel.setValue(ad.getHeadline());
        adShortTextLabel.setValue(truncateContent());
        adShortTextLabel.setWidth(300, Unit.PIXELS);
        viewCountLabel.setValue("Число просмотров: " + String.valueOf(ad.getViewCount()));
        dateLabel.setValue(String.valueOf(ad.getPublishTime()));
        categoryLabel.setValue(getCategoryName());
        tagsLabel.setValue(getTags());

        moreInfoButton.addClickListener(new MoreInfoButtonClickListener());
        editButton.addClickListener(new EditButtonClickListener());
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
            adContent = adContent.substring(0, TEXT_LIMIT) + "...";
        adContent = adContent.replaceAll("\n", "<br>");
        return adContent;
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

    private class MoreInfoButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            Window fullAdSubWindow = new AdFullForm(ui, ad).getWindow();
            ui.addWindow(fullAdSubWindow);
        }
    }

    protected class EditButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            Window newAddSubWindow = new EditAdForm(ui, ad).getWindow();
            ui.addWindow(newAddSubWindow);
        }
    }
}

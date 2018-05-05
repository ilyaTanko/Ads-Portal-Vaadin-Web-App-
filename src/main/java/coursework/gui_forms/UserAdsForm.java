package coursework.gui_forms;

import com.vaadin.server.FileResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.AdvertisementEntity;
import coursework.gui_designs.UserAdsFormDesign;
import coursework.gui_forms.AddAdForm;
import coursework.session.UserSession;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserAdsForm extends UserAdsFormDesign
{
    private MyUI ui;

    private List<AdvertisementEntity> newAds = new ArrayList<>();
    private List<AdvertisementEntity> editedAds = new ArrayList<>();
    private List<AdvertisementEntity> publishedAds = new ArrayList<>();

    private Panel newAdsPanel = new Panel();
    private Panel editedAdsPanel = new Panel();
    private Panel publishedAdsPanel = new Panel();

    private VerticalLayout newAdsTab = new VerticalLayout();
    private VerticalLayout editedAdsTab = new VerticalLayout();
    private VerticalLayout publishedAdsTab = new VerticalLayout();

    private Window subWindow = new Window();

    public UserAdsForm(MyUI ui)
    {
        this.ui = ui;

        configureAddButton();

        newAdsPanel.setContent(newAdsTab);
        editedAdsPanel.setContent(editedAdsTab);
        publishedAdsPanel.setContent(publishedAdsTab);

        newAdsPanel.setStyleName("borderless");
        editedAdsPanel.setStyleName("borderless");
        publishedAdsPanel.setStyleName("borderless");

        newAdsPanel.setHeight(800, Unit.PIXELS);
        editedAdsPanel.setHeight(800, Unit.PIXELS);
        publishedAdsPanel.setHeight(800, Unit.PIXELS);

        adsTabSheet.addTab(newAdsPanel, "Неопубликованные");
        adsTabSheet.addTab(editedAdsPanel, "Отредактированные (на модерации)");
        adsTabSheet.addTab(publishedAdsPanel, "Опубликованные");

        fillAds();

        addAdButton.addClickListener(new AddButtonClickListener());
    }

    private void configureAddButton() {
        addAdButton.setIcon(new FileResource(new File(UserSession.getBasePath() + "/VAADIN/images/app/plus2.png")));
    }

    private class AddButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            Window newAddSubWindow = new AddAdForm(ui).getWindow();
            ui.addWindow(newAddSubWindow);
        }
    }

    public void fillAds()
    {
        List<AdvertisementEntity> ads = DatabaseWorker.getAdsByUserId(UserSession.getCurrentUser().getId());

        fillNewAds(ads);
        fillEditedAds(ads);
        fillPublishedAds(ads);
    }

    @SuppressWarnings("Duplicates")
    private void fillNewAds(List<AdvertisementEntity> ads)
    {
        for (AdvertisementEntity ad : ads)
            if (ad.getActual() == AdvertisementEntity.AD_NOT_APPROVED)
                newAds.add(ad);
        sortAdsByDate(newAds);

        for (AdvertisementEntity ad : newAds)
            newAdsTab.addComponent(new UserAdShortForm(ui, ad));

        if (newAds.size() == 0)
        {
            newAdsPanel.setHeight(300, Unit.PIXELS);
            newAdsPanel.setWidth(600, Unit.PIXELS);
        }
        else if (newAds.size() < 3)
            newAdsPanel.setHeight(String.valueOf(newAdsPanel.getContent().getHeight()));
        else
            newAdsPanel.setHeight(800, Unit.PIXELS);
    }

    @SuppressWarnings("Duplicates")
    private void fillEditedAds(List<AdvertisementEntity> ads)
    {
        for (AdvertisementEntity ad : ads)
            if (ad.getActual() == AdvertisementEntity.AD_CHANGES_NOT_APPROVED)
                editedAds.add(ad);
        sortAdsByDate(editedAds);

        for (AdvertisementEntity ad : editedAds)
            editedAdsTab.addComponent(new UserAdShortForm(ui, ad));

        if (editedAds.size() == 0)
        {
            editedAdsPanel.setHeight(300, Unit.PIXELS);
            editedAdsPanel.setWidth(600, Unit.PIXELS);
        }
        else if (editedAds.size() < 3)
            editedAdsPanel.setHeight(String.valueOf(editedAdsPanel.getContent().getHeight()));
        else
            newAdsPanel.setHeight(800, Unit.PIXELS);
    }

    @SuppressWarnings("Duplicates")
    private void fillPublishedAds(List<AdvertisementEntity> ads)
    {
        for (AdvertisementEntity ad : ads)
            if (ad.getActual() == AdvertisementEntity.AD_APPROVED)
                publishedAds.add(ad);
        sortAdsByDate(publishedAds);

        for (AdvertisementEntity ad : publishedAds)
            publishedAdsTab.addComponent(new UserAdShortForm(ui, ad));

        if (publishedAds.size() == 0)
        {
            publishedAdsPanel.setHeight(300, Unit.PIXELS);
            publishedAdsPanel.setWidth(600, Unit.PIXELS);
        }
        else if (publishedAds.size() < 3)
            publishedAdsPanel.setHeight(String.valueOf(publishedAdsPanel.getContent().getHeight()));
        else
            newAdsPanel.setHeight(800, Unit.PIXELS);
    }


    private void sortAdsByDate(List<AdvertisementEntity> ads)
    {
        Collections.sort(ads, new Comparator<AdvertisementEntity>() {
            public int compare(AdvertisementEntity ad1, AdvertisementEntity ad2) {
                return ad1.getPublishTime().compareTo(ad2.getPublishTime());
            }
        });
        Collections.reverse(ads);
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

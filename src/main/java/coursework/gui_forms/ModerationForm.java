package coursework.gui_forms;

import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.AdvertisementEntity;
import coursework.gui_designs.ModerationFormDesign;
import coursework.gui_designs.UserAdsFormDesign;
import coursework.session.UserSession;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ModerationForm extends ModerationFormDesign
{
    private MyUI ui;
    private List<AdvertisementEntity> newAds;
    private List<AdvertisementEntity> editedAds;
    private Window subWindow = new Window();

    private Panel newAdsPanel = new Panel();
    private Panel editedAdsPanel = new Panel();

    private VerticalLayout newAdsTab = new VerticalLayout();
    private VerticalLayout editedAdsTab = new VerticalLayout();

    public ModerationForm(MyUI ui)
    {
        this.ui = ui;

        newAdsPanel.setContent(newAdsTab);
        editedAdsPanel.setContent(editedAdsTab);
        newAdsPanel.setStyleName("borderless");
        editedAdsPanel.setStyleName("borderless");
        newAdsPanel.setHeight(800, Unit.PIXELS);
        editedAdsPanel.setHeight(800, Unit.PIXELS);

        adsTabSheet.addTab(newAdsPanel, "Новые");
        adsTabSheet.addTab(editedAdsPanel, "Изменённые");

        fillAds();
    }

    public void fillAds()
    {
        fillNewAds();
        fillEditedAds();
    }

    @SuppressWarnings("Duplicates")
    private void fillNewAds()
    {
        newAds = DatabaseWorker.getAdsByActuality(AdvertisementEntity.AD_NOT_APPROVED);
        sortAdsByDate(newAds);

        for (AdvertisementEntity ad : newAds)
            newAdsTab.addComponent(new ModerationAdShortForm(ui, ad));

        if (newAds.size() == 0)
        {
            newAdsPanel.setHeight(300, Unit.PIXELS);
            newAdsPanel.setWidth(500, Unit.PIXELS);
        }
        else if (newAds.size() < 3)
            newAdsPanel.setHeight(String.valueOf(newAdsPanel.getContent().getHeight()));
    }

    @SuppressWarnings("Duplicates")
    private void fillEditedAds()
    {
        editedAds = DatabaseWorker.getAdsByActuality(AdvertisementEntity.AD_CHANGES_NOT_APPROVED);
        sortAdsByDate(editedAds);

        for (AdvertisementEntity ad : editedAds)
            editedAdsTab.addComponent(new ModerationAdShortForm(ui, ad));

        if (editedAds.size() == 0)
        {
            editedAdsPanel.setHeight(300, Unit.PIXELS);
            editedAdsPanel.setWidth(500, Unit.PIXELS);
        }
        else if (editedAds.size() < 3)
            editedAdsPanel.setHeight(String.valueOf(editedAdsPanel.getContent().getHeight()));
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

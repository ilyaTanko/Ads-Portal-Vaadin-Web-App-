package coursework.gui_forms;

import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.AdvertisementEntity;
import coursework.database.entities.UserEntity;
import coursework.gui_designs.MainFormDesign;
import coursework.session.UserSession;

import java.util.*;

public class MainForm extends MainFormDesign
{
    private MyUI ui;
    private List<AdvertisementEntity> ads;

    public MainForm(MyUI ui)
    {
        this.ui = ui;

        fillCategories();
        fillTags();
        fillAds();

        categoriesLayout.setSizeFull();
        centerLayout.setSizeFull();
        tagsLayout.setSizeFull();

        handleUser();

        userAdsButton.addClickListener(new UserAdsButtonClickListener());
        moderationButton.addClickListener(new ModerationButtonClickListener());
        adminButton.addClickListener(new AdminButtonClickListener());

        categoriesGroup.addValueChangeListener(new CategoriesTagsChangeListener());
    }

    private void handleUser()
    {
        if (UserSession.getCurrentUser() == null)
        {
            usernameLabel.setVisible(false);
            userAdsButton.setVisible(false);
            userTypeLabel.setVisible(false);
            moderationButton.setVisible(false);
            adminButton.setVisible(false);
        }
        else
        {
            usernameLabel.setValue("Добро пожаловать, " +
                    UserSession.getCurrentUser().getLogin());

            byte userType = UserSession.getCurrentUser().getType();
            switch (userType)
            {
                case UserEntity.USER_REGULAR:
                    userTypeLabel.setVisible(false);
                    moderationButton.setVisible(false);
                    adminButton.setVisible(false);
                    break;
                case UserEntity.USER_ADMIN:
                    userTypeLabel.setValue("Администратор");
                    moderationButton.setVisible(false);
                    break;
                case UserEntity.USER_MODERATOR:
                    userTypeLabel.setValue("Модератор");
                    adminButton.setVisible(false);
                    break;
            }
        }
    }

    @SuppressWarnings("Duplicates")
    private void fillCategories()
    {
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("Без категории");
        categoryNames.add("Любые");
        List<String> dbCategoryNames = DatabaseWorker.getCategoryNames();
        Collections.sort(dbCategoryNames);
        categoryNames.addAll(dbCategoryNames);
        categoriesGroup.setItems(categoryNames);

        categoriesGroup.setSelectedItem("Любые");
    }

    private void fillTags()
    {
        List<String> tagNames = DatabaseWorker.getTagNames();
        Collections.sort(tagNames);
        tagsGroup.setItems(tagNames);
    }

    private void fillAds()
    {
        ads = DatabaseWorker.getAdsByActuality(AdvertisementEntity.AD_APPROVED);
        sortAdsByDate();

        for (AdvertisementEntity ad : ads)
            lastAdsListLayout.addComponent(new GeneralAdShortForm(ui, ad));

        if (ads.size() < 3)
            lastAdsPanel.setHeight(String.valueOf(lastAdsPanel.getContent().getHeight()));
    }

    private void sortAdsByDate()
    {
        Collections.sort(ads, new Comparator<AdvertisementEntity>() {
            public int compare(AdvertisementEntity ad1, AdvertisementEntity ad2) {
                return ad1.getPublishTime().compareTo(ad2.getPublishTime());
            }
        });
        Collections.reverse(ads);
    }

    private class CategoriesTagsChangeListener implements ValueChangeListener
    {
        @Override
        public void valueChange(ValueChangeEvent valueChangeEvent)
        {
            String selectedItem = valueChangeEvent.getValue().toString();

            // TODO: display ads with categories and tags
        }
    }

    private class UserAdsButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            Window subWindow = new UserAdsForm(ui).getWindow();
            ui.addWindow(subWindow);
        }
    }

    private class ModerationButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            Window subWindow = new ModerationForm(ui).getWindow();
            ui.addWindow(subWindow);
        }
    }

    private class AdminButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {

        }
    }
}

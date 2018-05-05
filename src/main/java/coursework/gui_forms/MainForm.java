package coursework.gui_forms;

import com.vaadin.data.HasValue;
import com.vaadin.data.HasValue.ValueChangeEvent;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.*;
import coursework.gui_designs.MainFormDesign;
import coursework.session.UserSession;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

public class MainForm extends MainFormDesign
{
    private MyUI ui;
    private List<AdvertisementEntity> ads;
    private String searchedValue = "";

    private final int SORT_DATE = 1;
    private final int SORT_VIEWS = 2;

    public MainForm(MyUI ui)
    {
        this.ui = ui;

        fillCategories();
        fillTags();
        fillAds();
        fillSortValues();

        handleUser();
        configureLogo();
        configureSearchButton();

        searchField.setPlaceholder("Поиск...");

        categoriesLayout.setSizeFull();
        centerLayout.setSizeFull();
        tagsLayout.setSizeFull();

        sortRadioGroup.setSelectedItem("По дате");
        disallowDateCheckbox.setValue(true);

        UserButtonClickListener userListener = new UserButtonClickListener();
        userButton.addClickListener(userListener);
        userLinkButton.addClickListener(userListener);
        settingsButton.addClickListener(new SettingsButtonClickListener());

        userAdsButton.addClickListener(new UserAdsButtonClickListener());
        moderationButton.addClickListener(new ModerationButtonClickListener());
        adminButton.addClickListener(new AdminButtonClickListener());

        categoriesGroup.addValueChangeListener(new FilterListener());
        tagsGroup.addValueChangeListener(new FilterListener());
        searchButton.addClickListener(new SearchButtonListener());

        selectCategoryButton.addClickListener(new EnterCategoryListener());
        selectTagButton.addClickListener(new EnterTagListener());
        resetCategoryButton.addClickListener(new ResetCategoryListener());
        resetTagButton.addClickListener(new ResetTagListener());

        sortRadioGroup.addValueChangeListener(new SortChangeListener());
        disallowDateCheckbox.addValueChangeListener(new DisallowDateCheckboxListener());
        dateFromField.addValueChangeListener(new DateChangeListener());
        dateToField.addValueChangeListener(new DateChangeListener());
    }

    private void handleUser()
    {
        userButton.setHeightUndefined();
        userButton.setWidthUndefined();
        if (UserSession.getCurrentUser() == null)
        {
            settingsButton.setVisible(false);
            userLinkButton.setCaption("Войти");
            userButton.setIcon(new FileResource(new File(UserSession.getBasePath() + "/VAADIN/images/app/user2.png")));
        } else
        {
            userLinkButton.setCaption("Выйти");
            userButton.setIcon(new FileResource(new File(UserSession.getBasePath() + "/VAADIN/images/app/door3.png")));
        }

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

    private void configureLogo() {
        logoImage.setIcon(new FileResource(new File(UserSession.getBasePath() + "/VAADIN/images/app/logo2.png")));
    }

    private void configureSearchButton()
    {
        searchButton.setHeightUndefined();
        searchButton.setWidthUndefined();
        searchButton.setIcon(new FileResource(new File(UserSession.getBasePath() + "/VAADIN/images/app/search2.png")));
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

        categoriesCombo.setItems(categoryNames);
        categoriesCombo.setSelectedItem("Любые");
    }

    private void fillTags()
    {
        List<String> tagNames = DatabaseWorker.getTagNames();
        Collections.sort(tagNames);
        tagsGroup.setItems(tagNames);
        tagsCombo.setItems(tagNames);
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

    private void fillSortValues()
    {
        sortRadioGroup.clear();
        ArrayList<String> sortValues = new ArrayList<>();
        sortValues.add("По дате");
        sortValues.add("По количеству просмотров");
        sortRadioGroup.setItems(sortValues);
        sortRadioGroup.setSelectedItem("По дате");
    }

    //----------------------------------------------------------------------------------------------

    private class UserButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent)
        {
            if (UserSession.getCurrentUser() == null)
            {
                Window subWindow;
                subWindow = new LoginForm(ui).getWindow();
                ui.addWindow(subWindow);
            } else
            {
                UserSession.setCurrentUser(null);
                Page.getCurrent().reload();
            }
        }
    }

    private class SettingsButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent)
        {
            Window subWindow = new UserSettingsForm(ui).getWindow();
            ui.addWindow(subWindow);
        }
    }

    private class FilterListener implements ValueChangeListener
    {
        @SuppressWarnings("Duplicates")
        @Override
        public void valueChange(ValueChangeEvent valueChangeEvent)
        {
            lastAdsListLayout.removeAllComponents();
            ads = DatabaseWorker.getAdsByActuality(AdvertisementEntity.AD_APPROVED);
            filterAds();
            refillAds();
        }
    }

    private class DateChangeListener implements ValueChangeListener
    {
        @SuppressWarnings("Duplicates")
        @Override
        public void valueChange(ValueChangeEvent valueChangeEvent)
        {
            lastAdsListLayout.removeAllComponents();
            ads = DatabaseWorker.getAdsByActuality(AdvertisementEntity.AD_APPROVED);
            filterAds();
            refillAds();

            checkDates();
        }

        private void checkDates()
        {
            LocalDate dateFrom = dateFromField.getValue();
            LocalDate dateTo = dateToField.getValue();

            if (dateFrom != null && dateTo != null)
                if (dateFrom.isAfter(dateTo))
                    Notification.show(null, "Проверьте диапазон дат!",
                            Notification.Type.ERROR_MESSAGE);
        }
    }

    private class EnterCategoryListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            String enteredCategory = categoriesCombo.getValue();
            if (enteredCategory == null || enteredCategory.isEmpty())
                return;
            List<CategoryEntity> categories = DatabaseWorker.getCategoriesByName(enteredCategory);
            if (categories.isEmpty())
            {
                if (enteredCategory.toLowerCase().equals("Без категории".toLowerCase()))
                    categoriesGroup.setSelectedItem("Без категории");
                else if (enteredCategory.toLowerCase().equals("Любые".toLowerCase()))
                    categoriesGroup.setSelectedItem("Любые");
            }
            else
            {
                CategoryEntity category = categories.get(0);
                categoriesGroup.setSelectedItem(category.getName());
            }
            categoriesCombo.clear();
        }
    }

    private class EnterTagListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            String enteredTag = tagsCombo.getValue();
            if (enteredTag == null || enteredTag.isEmpty())
                return;
            List<TagEntity> tags = DatabaseWorker.getTagsByName(enteredTag);
            if (!tags.isEmpty())
                tagsGroup.select(tags.get(0).getName());
            else
            {
                tags = DatabaseWorker.getTagsByLikeName(enteredTag);
                for (TagEntity tag : tags)
                    tagsGroup.select(tag.getName());
            }
            tagsCombo.clear();
        }
    }

    private class ResetCategoryListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent) {
            categoriesCombo.clear();
        }
    }

    private class ResetTagListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent) {
            tagsCombo.clear();
        }
    }

    private class SortChangeListener implements ValueChangeListener
    {
        @Override
        public void valueChange(ValueChangeEvent valueChangeEvent) {
            applySort(getSortType());
            lastAdsListLayout.removeAllComponents();
            refillAds();
        }
    }

    private class SearchButtonListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            searchedValue = searchField.getValue();
            lastAdsListLayout.removeAllComponents();
            ads = DatabaseWorker.getAdsByActuality(AdvertisementEntity.AD_APPROVED);
            filterAds();
            refillAds();
        }
    }

    private class DisallowDateCheckboxListener implements ValueChangeListener
    {
        @Override
        public void valueChange(ValueChangeEvent valueChangeEvent)
        {
            if (disallowDateCheckbox.getValue())
            {
                dateFromField.setEnabled(false);
                dateToField.setEnabled(false);
            }
            else
            {
                dateFromField.setEnabled(true);
                dateToField.setEnabled(true);
            }
            lastAdsListLayout.removeAllComponents();
            ads = DatabaseWorker.getAdsByActuality(AdvertisementEntity.AD_APPROVED);
            filterAds();
            refillAds();
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
            Window subWindow = new AdminForm(ui).getWindow();
            ui.addWindow(subWindow);
        }
    }

    //----------------------------------------------------------------------------------------------

    private void applyCategoriesFilter()
    {
        String selectedCategory = categoriesGroup.getSelectedItem().get();

        // Categories filter
        if (!selectedCategory.equals("Любые"))
        {
            ArrayList<AdvertisementEntity> filteredAds = new ArrayList<>();
            if (!selectedCategory.equals("Без категории"))
            {
                CategoryEntity category = DatabaseWorker.getCategoriesByName(selectedCategory).get(0);
                for (AdvertisementEntity ad : ads)
                    if (ad.getCategoryId() == category.getId())
                        filteredAds.add(ad);
            }

            else
            {
                for (AdvertisementEntity ad : ads)
                    if (ad.getCategoryId() == null)
                        filteredAds.add(ad);
            }
            ads = filteredAds;
        }
    }

    private void applyTagsFilter()
    {
        Set<String> selectedTags = tagsGroup.getSelectedItems();

        // Tags filter
        if (!selectedTags.isEmpty())
        {
            List<AdvertisementEntity> filteredAds = new ArrayList<>();
            List<TagEntity> tags = new ArrayList<>();
            for (String tagName : selectedTags)
                tags.add(DatabaseWorker.getTagsByName(tagName).get(0));

            List<Integer> tagIds = new ArrayList<>();
            for (TagEntity tag : tags)
                tagIds.add(tag.getId());

            for (AdvertisementEntity ad : ads)
            {
                List<AdvertisementTagEntity> adTags = DatabaseWorker.getAdTagsByAdId(ad.getId());
                for (AdvertisementTagEntity adTag : adTags)
                    if (tagIds.contains(adTag.getTagId()) && !filteredAds.contains(ad))
                        filteredAds.add(ad);
            }
            ads = filteredAds;
        }
    }

    private int getSortType()
    {
        String strSortType = sortRadioGroup.getSelectedItem().get();
        int sortType;
        switch (strSortType)
        {
            case "По дате":
                sortType = SORT_DATE;
                break;
            case "По количеству просмотров":
                sortType = SORT_VIEWS;
                break;
            default:
                sortType = SORT_DATE;
        }
        return sortType;
    }

    private void applySort(int sortType)
    {
        switch (sortType)
        {
            case SORT_DATE:
                sortAdsByDate();
                break;
            case SORT_VIEWS:
                sortAdsByViews();
                break;
        }
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

    private void sortAdsByViews()
    {
        Collections.sort(ads, new Comparator<AdvertisementEntity>() {
            public int compare(AdvertisementEntity ad1, AdvertisementEntity ad2) {
                return Integer.compare(ad1.getViewCount(), ad2.getViewCount());
            }
        });
        Collections.reverse(ads);
    }

    private void applySearch()
    {
        if (searchedValue.equals(""))
            return;

        List<AdvertisementEntity> filteredAds = new ArrayList<>();

        for (AdvertisementEntity ad : ads)
        {
            if (ad.getContent().toLowerCase().contains(searchedValue.toLowerCase()) ||
                    ad.getHeadline().toLowerCase().contains(searchedValue.toLowerCase()))
                filteredAds.add(ad);
        }
        ads = filteredAds;
    }

    private void applyDateRestriction()
    {
        if (disallowDateCheckbox.getValue())
            return;

        LocalDate dateFrom = dateFromField.getValue();
        LocalDate dateTo = dateToField.getValue();

        List<AdvertisementEntity> filteredAds = new ArrayList<>();

        if (dateFrom == null)
        {
            if (dateTo == null)
                return;
            else
            {
                // from - null, to - exists
                for (AdvertisementEntity ad : ads)
                    if (!ad.getPublishTime().toLocalDateTime().toLocalDate().isAfter(dateTo))
                        filteredAds.add(ad);
            }
        }
        else
        {
            if(dateTo == null)
            {
                // from - exists, to - null
                for (AdvertisementEntity ad : ads)
                    if (!ad.getPublishTime().toLocalDateTime().toLocalDate().isBefore(dateFrom))
                        filteredAds.add(ad);
            }
            else
            {
                // both exist
                for (AdvertisementEntity ad : ads)
                    if (!ad.getPublishTime().toLocalDateTime().toLocalDate().isBefore(dateFrom) &&
                            !ad.getPublishTime().toLocalDateTime().toLocalDate().isAfter(dateTo))
                        filteredAds.add(ad);
            }
        }
        ads = filteredAds;
    }

    private void refillAds()
    {
        for (AdvertisementEntity ad : ads)
            lastAdsListLayout.addComponent(new GeneralAdShortForm(ui, ad));

        if (ads.size() == 0)
            lastAdsPanel.setHeight(0, Unit.PIXELS);
        else if (ads.size() < 3)
            lastAdsPanel.setHeight(String.valueOf(lastAdsPanel.getContent().getHeight()));
        else
            lastAdsPanel.setHeight(800, Unit.PIXELS);
    }

    private void filterAds()
    {
        applyCategoriesFilter();
        applyTagsFilter();
        applySearch();
        applyDateRestriction();
        applySort(getSortType());
    }
}

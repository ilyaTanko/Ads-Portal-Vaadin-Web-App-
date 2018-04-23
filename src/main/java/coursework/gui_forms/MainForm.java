package coursework.gui_forms;

import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.session.UserSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainForm extends MainFormDesign
{
    private MyUI ui;

    public MainForm(MyUI ui)
    {
        this.ui = ui;

        fillCategories();
        fillTags();
        fillLastAds();

        categoriesLayout.setSizeFull();
        centerLayout.setSizeFull();
        tagsLayout.setSizeFull();

        handleUser();
    }

    private void fillCategories()
    {
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("Без категории");
        List<String> dbCategoryNames = DatabaseWorker.getCategoryNames();
        Collections.sort(dbCategoryNames);
        categoryNames.addAll(dbCategoryNames);
        categoriesGroup.setItems(categoryNames);
    }

    private void fillTags()
    {
        List<String> tagNames = DatabaseWorker.getTagNames();
        Collections.sort(tagNames);
        tagsGroup.setItems(tagNames);
    }

    private void fillLastAds()
    {
        // CHANGE TO ACTUAL ADS
        lastAdsListLayout.addComponent(new AdShortForm(ui));
        lastAdsListLayout.addComponent(new AdShortForm(ui));
        lastAdsListLayout.addComponent(new AdShortForm(ui));
    }

    private void handleUser()
    {
        if (UserSession.getCurrentUser() == null)
        {
            usernameLabel.setVisible(false);
            userAdsButton.setVisible(false);
        }
        else
            usernameLabel.setValue("Добро пожаловать, " +
                    UserSession.getCurrentUser().getLogin());
    }
}

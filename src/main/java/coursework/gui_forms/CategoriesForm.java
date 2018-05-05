package coursework.gui_forms;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.AdvertisementEntity;
import coursework.database.entities.CategoryEntity;
import coursework.gui_designs.CategoriesFormDesign;

import java.util.Collections;
import java.util.List;

public class CategoriesForm extends CategoriesFormDesign
{
    private MyUI ui;
    private Window subWindow = new Window();

    public CategoriesForm(MyUI ui)
    {
        this.ui = ui;

        titleLabel.setValue("Управление категориями");
        fillCategories();

        addButton.addClickListener(new AddButtonClickListener());
        editButton.addClickListener(new EditButtonClickListener());
        deleteButton.addClickListener(new DeleteButtonClickListener());
    }

    private void fillCategories()
    {
        List<String> categoryNames = DatabaseWorker.getCategoryNames();
        Collections.sort(categoryNames);
        valuesCombo.setItems(categoryNames);
        valuesCombo.setSelectedItem(categoryNames.get(0));
    }

    private class AddButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            String newCategoryName = valueField.getValue();
            if (newCategoryName.isEmpty())
                return;

            if (DatabaseWorker.categoryExists(newCategoryName))
            {
                Notification.show(null, "Категория уже существует",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }

            if (newCategoryName.toLowerCase().equals("Без категории".toLowerCase()) ||
                    newCategoryName.toLowerCase().equals("Любые".toLowerCase()))
            {
                Notification.show(null, "Такое имя недопустимо",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }

            if (newCategoryName.length() > 45)
            {
                Notification.show(null, "Длина категории не может быть больше 45 символов",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }

            CategoryEntity category = new CategoryEntity();
            category.setName(newCategoryName);
            DatabaseWorker.addCategory(category);
            Notification.show(null, "Категория добавлена",
                    Notification.Type.HUMANIZED_MESSAGE);
            valueField.clear();
            fillCategories();
        }
    }

    private class EditButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            String oldCategoryName = valuesCombo.getValue();
            if (oldCategoryName == null)
                return;

            String newCategoryName = valueField.getValue();
            if (newCategoryName.isEmpty())
                return;

            if (DatabaseWorker.categoryExists(newCategoryName))
            {
                Notification.show(null, "Категория уже существует",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }


            if (newCategoryName.toLowerCase().equals("Без категории".toLowerCase()) ||
                    newCategoryName.toLowerCase().equals("Любые".toLowerCase()))
            {
                Notification.show(null, "Такое имя недопустимо",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }

            if (newCategoryName.length() > 45)
            {
                Notification.show(null, "Длина категории не может быть больше 45 символов",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }

            CategoryEntity category = DatabaseWorker.getCategoriesByName(oldCategoryName).get(0);
            category.setName(newCategoryName);
            DatabaseWorker.updateCategory(category);
            Notification.show(null, "Категория изменена",
                    Notification.Type.HUMANIZED_MESSAGE);
            valueField.clear();
            fillCategories();
        }
    }

    private class DeleteButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            String categoryName = valuesCombo.getValue();
            if (categoryName == null)
                return;

            CategoryEntity category = DatabaseWorker.getCategoriesByName(categoryName).get(0);
            List<AdvertisementEntity> ads = DatabaseWorker.getAdsByCategoryId(category.getId());
            for (AdvertisementEntity ad : ads)
            {
                ad.setCategoryId(null);
                DatabaseWorker.updateAd(ad);
            }
            DatabaseWorker.deleteCategory(category);
        }
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

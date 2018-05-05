package coursework.gui_forms;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.AdvertisementEntity;
import coursework.database.entities.AdvertisementTagEntity;
import coursework.database.entities.CategoryEntity;
import coursework.database.entities.TagEntity;
import coursework.gui_designs.CategoriesFormDesign;

import java.util.Collections;
import java.util.List;

public class TagsForm extends CategoriesFormDesign
{
    private MyUI ui;
    private Window subWindow = new Window();

    public TagsForm(MyUI ui)
    {
        this.ui = ui;

        titleLabel.setValue("Управление тегами");
        fillTags();

        addButton.addClickListener(new AddButtonClickListener());
        editButton.addClickListener(new EditButtonClickListener());
        deleteButton.addClickListener(new DeleteButtonClickListener());
    }

    private void fillTags()
    {
        List<String> tagNames = DatabaseWorker.getTagNames();
        Collections.sort(tagNames);
        valuesCombo.setItems(tagNames);
        valuesCombo.setSelectedItem(tagNames.get(0));
    }

    private class AddButtonClickListener implements Button.ClickListener
    {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent)
        {
            String newTagName = valueField.getValue();
            if (newTagName.isEmpty())
                return;

            if (DatabaseWorker.tagExists(newTagName))
            {
                Notification.show(null, "Тег уже существует",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }

            if (newTagName.length() > 45)
            {
                Notification.show(null, "Длина тега не может быть больше 45 символов",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }

            TagEntity tag = new TagEntity();
            tag.setName(newTagName);
            DatabaseWorker.addTag(tag);
            Notification.show(null, "Тег добавлен",
                    Notification.Type.HUMANIZED_MESSAGE);
            valueField.clear();
            fillTags();
        }
    }

    private class EditButtonClickListener implements Button.ClickListener
    {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent)
        {
            String oldTagName = valuesCombo.getValue();
            if (oldTagName == null)
                return;

            String newTagName = valueField.getValue();
            if (newTagName.isEmpty())
                return;

            if (DatabaseWorker.tagExists(newTagName))
            {
                Notification.show(null, "Тег уже существует",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }

            if (newTagName.length() > 45)
            {
                Notification.show(null, "Длина тега не может быть больше 45 символов",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }

            TagEntity tag = DatabaseWorker.getTagsByName(oldTagName).get(0);
            tag.setName(newTagName);
            DatabaseWorker.updateTag(tag);
            Notification.show(null, "Тег добавлен",
                    Notification.Type.HUMANIZED_MESSAGE);
            valueField.clear();
            fillTags();
        }
    }

    private class DeleteButtonClickListener implements Button.ClickListener
    {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent)
        {
            String tagName = valuesCombo.getValue();
            if (tagName == null)
                return;

            TagEntity tag = DatabaseWorker.getTagsByName(tagName).get(0);
            List<AdvertisementTagEntity> adTags = DatabaseWorker.getAdTagsByTagId(tag.getId());
            for (AdvertisementTagEntity adTag : adTags)
                DatabaseWorker.deleteAdTag(adTag);
            DatabaseWorker.deleteTag(tag);
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
package coursework.gui_forms;

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.*;
import coursework.gui_designs.AddAdFormDesign;
import coursework.session.UserSession;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditAdForm extends AddAdFormDesign
{
    private MyUI ui;
    private AdvertisementEntity ad;
    private boolean photoChanged = false;
    private Window subWindow = new Window();
    private File imageFile;
    private List<String> tags = new ArrayList<>();

    public EditAdForm(MyUI ui, AdvertisementEntity ad)
    {
        this.ui = ui;
        this.ad = ad;

        titleLabel.setValue("Редактировать объявление");
        submitButton.setCaption("Сохранить изменения");

        fillCategories();
        fillPhoto();

        setCategoryName();
        setTags();
        headlineField.setValue(ad.getHeadline());
        contentField.setValue(ad.getContent());

        ImageUploader uploader = new ImageUploader();
        addPhotoButton.setReceiver(uploader);
        addPhotoButton.addSucceededListener(uploader);

        addTagButton.addClickListener(new addTagButtonListener());
        deleteTagButton.addClickListener(new deleteTagButtonListener());
        submitButton.addClickListener(new SubmitButtonClickListener());
    }

    private String getPhotoDirectoryPath()
    {
        int userId = ad.getUserId();
        return UserSession.getBasePath() + "/VAADIN/images/ads/" + userId + "/";
    }

    @SuppressWarnings("Duplicates")
    private void fillCategories()
    {
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("Без категории");
        List<String> dbCategoryNames = DatabaseWorker.getCategoryNames();
        Collections.sort(dbCategoryNames);
        categoryNames.addAll(dbCategoryNames);
        categorySelect.setItems(categoryNames);
        categorySelect.setSelectedItem("Без категории");
    }

    private void fillPhoto()
    {
        uploadedImage.setSource(new FileResource(new File(getPhotoDirectoryPath() +
            ad.getId())));
        uploadedImage.setHeight(200, Unit.PIXELS);
    }

    private void setCategoryName()
    {
        if (ad.getCategoryId() == null)
             categorySelect.setValue("Без категории");
        categorySelect.setValue(DatabaseWorker.getCategory(ad.getCategoryId()).getName());
    }

    private void setTags()
    {
        List<AdvertisementTagEntity> adTags = DatabaseWorker.getAdTagsByAdId(ad.getId());
        for (AdvertisementTagEntity adTag : adTags)
        {
            TagEntity tag = DatabaseWorker.getTag(adTag.getTagId());
            tags.add(tag.getName());
        }
        refreshTagsLabel();
    }

    private class ImageUploader implements Receiver, SucceededListener
    {
        @SuppressWarnings("Duplicates")
        @Override
        public OutputStream receiveUpload(String fileName, String mimeType)
        {
            FileOutputStream fos = null;
            try
            {
                imageFile = new File( UserSession.getBasePath() + "/VAADIN/images/tmp/" + fileName);
                fos = new FileOutputStream(imageFile);
            }
            catch (final FileNotFoundException e)
            {
                new Notification("Не удалось открыть файл",
                        e.getMessage(),
                        Notification.Type.ERROR_MESSAGE)
                        .show(Page.getCurrent());
                return null;
            }
            return fos;
        }

        @Override
        public void uploadSucceeded(SucceededEvent event)
        {
            uploadedImage.setSource(new FileResource(imageFile));
            uploadedImage.setHeight(200, Unit.PIXELS);
            uploadedImage.setVisible(true);
            photoChanged = true;
        }
    }

    private class addTagButtonListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            String tag = tagField.getValue();
            if (!tag.equals("") && !tags.contains(tag))
            {
                tags.add(tag.toLowerCase());
                refreshTagsLabel();
                tagField.clear();
            }
        }
    }

    private class deleteTagButtonListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            String tag = tagField.getValue();
            if (!tag.equals("") && tags.contains(tag))
            {
                tags.remove(tag);
                refreshTagsLabel();
            }
        }
    }

    private void refreshTagsLabel() {
        tagsLabel.setValue(String.join(", ", tags));
    }

    private class SubmitButtonClickListener implements ClickListener
    {
        @SuppressWarnings("Duplicates")
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            if (!checkInput())
                return;
            saveAd();
            saveTags();

            if (photoChanged)
                saveImage();

            if (UserSession.getCurrentUser().getType() == UserEntity.USER_REGULAR)
                Notification.show(null, "Объявление отправлено на модерацию",
                        Notification.Type.HUMANIZED_MESSAGE);
            else
                Notification.show(null, "Объявление изменено",
                        Notification.Type.HUMANIZED_MESSAGE);
            subWindow.close();
            Page.getCurrent().reload();
        }

        private boolean checkInput()
        {
            if (headlineField.getValue().equals("") || contentField.getValue().equals("")
                    || categorySelect.getValue().equals(""))
            {
                Notification.show(null, "Заполните необходимые значения",
                        Notification.Type.WARNING_MESSAGE);
                return false;
            }

            if (headlineField.getValue().length() > 200)
            {
                Notification.show(null, "Длина заголовка не может быть больше 200 символов",
                        Notification.Type.WARNING_MESSAGE);
                return false;
            }

            return true;
        }

        private void saveAd()
        {
            ad.setHeadline(headlineField.getValue());
            int categoryId = getCategoryId();
            if (categoryId == CategoryEntity.CATEGORY_NULL)
                ad.setCategoryId(null);
            else
                ad.setCategoryId(categoryId);
            ad.setContent(contentField.getValue());

            if (UserSession.getCurrentUser().getType() == UserEntity.USER_REGULAR)
                ad.setActual(AdvertisementEntity.AD_CHANGES_NOT_APPROVED);
            else
                ad.setActual(AdvertisementEntity.AD_APPROVED);

            DatabaseWorker.updateAd(ad);
        }

        private void saveTags()
        {
            List<AdvertisementTagEntity> adTags = DatabaseWorker.getAdTagsByAdId(ad.getId());
            for (AdvertisementTagEntity adTag : adTags)
                DatabaseWorker.deleteAdTag(adTag);

            for(String tagName : tags)
            {
                int tagId = 0;

                List<TagEntity> tagsByName = DatabaseWorker.getTagsByName(tagName);
                if (tagsByName.isEmpty())
                    tagId = saveTag(tagName);
                else
                {
                    TagEntity tag = tagsByName.get(0);
                    tagId = tag.getId();
                }

                AdvertisementTagEntity adTag = new AdvertisementTagEntity();
                adTag.setAdvertisementId(ad.getId());
                adTag.setTagId(tagId);
                DatabaseWorker.addAdTag(adTag);
            }
        }

        private int saveTag(String tagName)
        {
            TagEntity tagEntity = new TagEntity();
            tagEntity.setName(tagName);
            return DatabaseWorker.addTag(tagEntity);
        }

        private void saveImage()
        {
            try
            {
                File file = new File(getPhotoDirectoryPath() + ad.getId());
                Files.copy(imageFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) { e.printStackTrace(); }
        }

        private int getCategoryId()
        {
            assert (categorySelect.getValue() != null);
            List<CategoryEntity> categories = DatabaseWorker.getCategoriesByName(categorySelect.getValue());
            if (categories.isEmpty())
                return CategoryEntity.CATEGORY_NULL;
            return categories.get(0).getId();
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

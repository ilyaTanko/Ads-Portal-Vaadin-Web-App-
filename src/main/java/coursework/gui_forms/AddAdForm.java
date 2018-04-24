package coursework.gui_forms;

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.AdvertisementEntity;
import coursework.database.entities.AdvertisementTagEntity;
import coursework.database.entities.CategoryEntity;
import coursework.database.entities.TagEntity;
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

public class AddAdForm extends AddAdFormDesign
{
    private MyUI ui;
    private Window subWindow = new Window();
    private File imageFile;
    private String directoryPath = getPhotoDirectoryPath();
    private List<String> tags = new ArrayList<>();

    public AddAdForm(MyUI ui)
    {
        this.ui = ui;

        uploadedImage.setVisible(false);

        fillCategories();

        ImageUploader uploader = new ImageUploader();
        addPhotoButton.setReceiver(uploader);
        addPhotoButton.addSucceededListener(uploader);

        addTagButton.addClickListener(new addTagButtonListener());
        deleteTagButton.addClickListener(new deleteTagButtonListener());
        submitButton.addClickListener(new SubmitButtonClickListener());
    }

    private String getPhotoDirectoryPath()
    {
        int userId = UserSession.getCurrentUser().getId();
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

    private class ImageUploader implements Receiver, SucceededListener
    {
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
                tags.add(tag);
                refreshTagsLabel();
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
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            if (!checkInput())
                return;
            int adId = saveAd();
            saveTags(adId);
            saveImage(adId);

            Notification.show(null, "Объявление добавлено",
                    Notification.Type.HUMANIZED_MESSAGE);
            subWindow.close();
        }

        private boolean checkInput()
        {
            if (headlineField.getValue().equals("") || contentField.getValue().equals("")
                    || categorySelect.getValue().equals("")) {
                Notification.show(null, "Заполните необходимые значения",
                        Notification.Type.WARNING_MESSAGE);
                return false;
            }

            if (imageFile == null) {
                Notification.show(null, "Загрузите изображение",
                        Notification.Type.WARNING_MESSAGE);
                return false;
            }
            return true;
        }
        
        private int saveAd()
        {
            AdvertisementEntity ad = new AdvertisementEntity();
            ad.setHeadline(headlineField.getValue());
            int categoryId = getCategoryId();
            if (categoryId == CategoryEntity.CATEGORY_NULL)
                ad.setCategoryId(null);
            else
                ad.setCategoryId(categoryId);
            ad.setContent(contentField.getValue());
            ad.setUserId(UserSession.getCurrentUser().getId());
            ad.setPublishTime(Timestamp.valueOf(LocalDateTime.now()));
            ad.setViewCount(0);
            ad.setActual(AdvertisementEntity.AD_ACTUAL);
            return DatabaseWorker.addAd(ad);    
        }
        
        private void saveTags(int adId)
        {
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
                adTag.setAdvertisementId(adId);
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

        private void saveImage(int adId)
        {
            try
            {
                new File(getPhotoDirectoryPath()).mkdirs();
                File file = new File(getPhotoDirectoryPath() + adId);
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

        private String getExtension(String filePath)
        {
            String extension = "";
            int i = filePath.lastIndexOf('.');
            if (i > 0)
                extension = filePath.substring(i + 1);
            return extension;
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

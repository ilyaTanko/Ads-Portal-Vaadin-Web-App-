package coursework.gui_forms;

import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.AdvertisementEntity;
import coursework.database.entities.UserEntity;

public class ModerationAdShortForm extends UserAdShortForm
{
    public ModerationAdShortForm(MyUI ui, AdvertisementEntity ad)
    {
        super(ui, ad);

        authorLabel.setVisible(true);
        authorLabel.setValue("Автор: " + getAuthorName());

        editButton.setCaption("Модерация");
    }

    private String getAuthorName()
    {
        UserEntity user = DatabaseWorker.getUser(ad.getUserId());
        return user.getLogin();
    }

}

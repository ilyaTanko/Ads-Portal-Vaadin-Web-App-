package coursework.gui_forms;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import com.vaadin.v7.ui.Grid;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.AdvertisementEntity;
import coursework.database.entities.AdvertisementTagEntity;
import coursework.database.entities.UserEntity;
import coursework.gui_designs.UserRolesFormDesign;
import coursework.session.UserSession;

import java.util.ArrayList;
import java.util.List;

import static com.vaadin.v7.ui.Grid.SelectionMode.SINGLE;

public class UserRolesForm extends UserRolesFormDesign
{
    private MyUI ui;
    private Window subWindow = new Window();

    List<UserEntity> users = new ArrayList<>();

    public UserRolesForm(MyUI ui)
    {
        this.ui = ui;

        fillRoles();
        fillUsers();

        usersGrid.setSelectionMode(SINGLE);

        submitButton.addClickListener(new SubmitButtonClickListener());
        deleteButton.addClickListener(new DeleteButtonClickListener());
    }

    private void fillRoles()
    {
        List<String> roles = new ArrayList<>();
        roles.add("Администратор");
        roles.add("Модератор");
        roles.add("Пользователь");
        rolesCombo.setItems(roles);
        rolesCombo.setSelectedItem("Администратор");
    }

    private void fillUsers()
    {
        usersGrid.removeAllColumns();
        usersGrid.setColumns("Логин", "Имя", "Роль");
        users = DatabaseWorker.getAllUsers();
        for (UserEntity user : users)
            usersGrid.addRow(user.getLogin(), user.getName(),
                    roleToString(user.getType()));
    }

    private String roleToString(int role)
    {
        switch (role)
        {
            case UserEntity.USER_ADMIN:
                return "Администратор";
            case UserEntity.USER_MODERATOR:
                return "Модератор";
            default:
                return "Пользователь";
        }
    }

    private byte stringToRole(String roleString)
    {
        switch (roleString)
        {
            case "Администратор":
                return UserEntity.USER_ADMIN;
            case "Модератор":
                return UserEntity.USER_MODERATOR;
            default:
                return UserEntity.USER_REGULAR;
        }
    }

    private class SubmitButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent)
        {
            Object selectedRow = usersGrid.getSelectedRow();
            if (selectedRow == null)
                return;

            String newRoleString = rolesCombo.getValue();
            if (newRoleString == null)
                return;

            byte newRole = stringToRole(newRoleString);
            int index = (Integer)selectedRow - 1;
            UserEntity user = users.get(index);

            if (user.getType() == newRole)
                return;

            if (user.equals(UserSession.getCurrentUser()))
            {
                Notification.show(null, "Вы не можете изменить свою роль",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }

            user.setType(newRole);
            DatabaseWorker.updateUser(user);
            Notification.show(null, "Роль изменена",
                    Notification.Type.HUMANIZED_MESSAGE);

            usersGrid.getContainerDataSource().removeAllItems();
            fillUsers();
        }
    }

    private class DeleteButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent)
        {
            Object selectedRow = usersGrid.getSelectedRow();
            if (selectedRow == null)
                return;

            int index = (Integer)selectedRow - 1;
            UserEntity user = users.get(index);

            if (user.equals(UserSession.getCurrentUser()))
            {
                Notification.show(null, "Вы не можете удалить свою запись администратора",
                        Notification.Type.WARNING_MESSAGE);
                return;
            }

            List<AdvertisementEntity> ads = DatabaseWorker.getAdsByUserId(user.getId());
            for (AdvertisementEntity ad : ads)
            {
                List<AdvertisementTagEntity> adTags = DatabaseWorker.getAdTagsByAdId(ad.getId());
                for (AdvertisementTagEntity adTag : adTags)
                    DatabaseWorker.deleteAdTag(adTag);
                DatabaseWorker.deleteAd(ad);
            }
            DatabaseWorker.deleteUser(user);

            Notification.show(null, "Пользователь удалён",
                    Notification.Type.HUMANIZED_MESSAGE);

            usersGrid.getContainerDataSource().removeAllItems();
            fillUsers();
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

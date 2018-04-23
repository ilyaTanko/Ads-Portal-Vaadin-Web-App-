package coursework.gui_forms;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.UserEntity;
import coursework.session.UserSession;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class UserSettingsForm extends UserSettingsFormDesign
{
    private MyUI ui;
    private Window subWindow = new Window();

    public UserSettingsForm(MyUI ui)
    {
        this.ui = ui;

        loginField.setValue("");
        passwordField.setValue("");

        submitButton.addClickListener(new SubmitButtonClickListener());
    }

    private class SubmitButtonClickListener implements Button.ClickListener
    {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent)
        {
            UserEntity user = checkChanges();
            if (user == null)
                return;

            UserSession.setCurrentUser(user);

            subWindow.close();
            ui.removeWindow(subWindow);

            Page.getCurrent().reload();
            Notification.show(null, "Изменения приняты",
                    Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    @SuppressWarnings("Duplicates")
    private UserEntity checkChanges()
    {
        String login = loginField.getValue();
        String password = passwordField.getValue();
        String name = nameField.getValue();
        LocalDate birthdayDate = dateField.getValue();

        if (nullFieldsExist(login, password, name, birthdayDate) ||
                anotherLoginExists(login) || !isOlderThan18(birthdayDate))
            return null;

        UserEntity user = new UserEntity();
        user.setLogin(login);
        user.setPassword(password);
        user.setName(name);
        user.setBirthDate(Timestamp.valueOf(birthdayDate.atStartOfDay()));
        user.setType(UserEntity.USER_REGULAR);
        DatabaseWorker.updateUser(user);
        return user;
    }

    private boolean nullFieldsExist(String login, String password,
                                    String name, LocalDate birthdayDate)
    {
        if (login == null || password == null || name == null || birthdayDate == null)
        {
            Notification.show(null, "Заполните все поля",
                    Notification.Type.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    private boolean anotherLoginExists(String login)
    {
        if (DatabaseWorker.loginExists(login) &&
                !login.equals(UserSession.getCurrentUser().getLogin()))
        {
            Notification.show(null, "Этот логин занят. Выберите другой",
                    Notification.Type.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    private boolean isOlderThan18(LocalDate birthdayDate)
    {
        long age = LocalDate.from(birthdayDate).until(LocalDate.now(), ChronoUnit.YEARS);
        if (age < 18)
        {
            Notification.show(null, "Вы должны быть старше 18 лет",
                    Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
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

package coursework.gui_forms;

import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.UserEntity;
import coursework.gui_designs.RegisterFormDesign;
import coursework.session.UserSession;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RegisterForm extends RegisterFormDesign //implements View
{
    private MyUI ui;
    private Window subWindow = new Window();

    public RegisterForm(MyUI ui)
    {
        this.ui = ui;

        loginField.setValue("");
        passwordField.setValue("");

        registerButton.addClickListener(new RegisterButtonClickListener());
    }

    private class RegisterButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            UserEntity user = checkRegistration();
            if (user == null)
                return;

            UserSession.setCurrentUser(user);

            subWindow.close();
            ui.removeWindow(subWindow);

            Page.getCurrent().reload();
        }

        private UserEntity checkRegistration()
        {
            String login = loginField.getValue();
            String password = passwordField.getValue();
            String name = nameField.getValue();
            LocalDate birthdayDate = dateField.getValue();

            if (nullFieldsExist(login, password, name, birthdayDate) ||
                    loginExists(login) || !isOlderThan18(birthdayDate))
                return null;

            if (!checkLength(login, password, name))
                return null;

            UserEntity user = new UserEntity();
            user.setLogin(login);
            user.setPassword(password);
            user.setName(name);
            user.setBirthDate(Timestamp.valueOf(birthdayDate.atStartOfDay()));
            user.setType(UserEntity.USER_REGULAR);
            DatabaseWorker.addUser(user);
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

        private boolean checkLength(String login, String password, String name)
        {
            if (login.length() > 45)
            {
                Notification.show(null, "Логин не может быть длиннее 45 символов",
                        Notification.Type.WARNING_MESSAGE);
                return false;
            }

            if (password.length() > 45)
            {
                Notification.show(null, "Пароль не может быть длиннее 45 символов",
                        Notification.Type.WARNING_MESSAGE);
                return false;
            }

            if (name.length() > 99)
            {
                Notification.show(null, "Имя не может быть длиннее 99 символов",
                        Notification.Type.WARNING_MESSAGE);
                return false;
            }

            return true;
        }

        private boolean loginExists(String login)
        {
            if (DatabaseWorker.loginExists(login))
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

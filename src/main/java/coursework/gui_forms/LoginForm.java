package coursework.gui_forms;

import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.database.DatabaseWorker;
import coursework.database.entities.UserEntity;
import coursework.session.UserSession;

import java.util.List;

public class LoginForm extends LoginFormDesign
{
    private MyUI ui;
    private Window subWindow = new Window();

    public LoginForm(MyUI ui)
    {
        this.ui = ui;

        loginButton.addClickListener(new LoginButtonClickListener());
        registerButton.addClickListener(new RegisterButtonClickListener());
    }

    private class LoginButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            String login = loginField.getValue();
            String password = passwordField.getValue();

            if (nullFieldsExist(login, password))
                return;

            List<UserEntity> users = DatabaseWorker.getUsersByLogin(login);
            if (!DatabaseWorker.loginExists(login) || !(users.get(0).getPassword().equals(password)))
            {
                Notification.show(null, "Пользователь с указанными данными не найден",
                        Notification.Type.ERROR_MESSAGE);
                return;
            }
            UserEntity user = users.get(0);
            UserSession.setCurrentUser(user);

            subWindow.close();
            ui.removeWindow(subWindow);

            Page.getCurrent().reload();
        }
    }

    private boolean nullFieldsExist(String login, String password)
    {
        if (login == null || password == null)
        {
            Notification.show(null, "Заполните все поля",
                    Notification.Type.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    private class RegisterButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            subWindow.close();
            ui.removeWindow(subWindow);
            subWindow = new RegisterForm(ui).getWindow();
            ui.addWindow(subWindow);
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

package coursework.gui_forms;

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.gui_designs.HeaderFormDesign;
import coursework.session.UserSession;

import java.io.File;

public class HeaderForm extends HeaderFormDesign
{
    private MyUI ui;

    public HeaderForm(MyUI ui)
    {
        this.ui = ui;

        configureLogo();
        handleUser();
        configureSearchButton();

        UserButtonClickListener userListener = new UserButtonClickListener();
        userButton.addClickListener(userListener);
        userLinkButton.addClickListener(userListener);
        settingsButton.addClickListener(new SettingsButtonClickListener());
    }

    private void handleUser()
    {
        userButton.setHeightUndefined();
        userButton.setWidthUndefined();
        if (UserSession.getCurrentUser() == null)
        {
            settingsButton.setVisible(false);
            userButton.setIcon(new FileResource(new File(UserSession.getBasePath() + "/VAADIN/images/app/user2.png")));
        } else
        {
            userLinkButton.setVisible(false);
            userButton.setIcon(new FileResource(new File(UserSession.getBasePath() + "/VAADIN/images/app/door2.png")));
        }
    }

    private void configureLogo() {
        logoImage.setIcon(new FileResource(new File(UserSession.getBasePath() + "/VAADIN/images/app/logo.png")));
    }

    private void configureSearchButton()
    {
        searchButton.setHeightUndefined();
        searchButton.setWidthUndefined();
        searchButton.setIcon(new FileResource(new File(UserSession.getBasePath() + "/VAADIN/images/app/search2.png")));
    }

    private class UserButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent)
        {
            if (UserSession.getCurrentUser() == null)
            {
                Window subWindow;
                subWindow = new LoginForm(ui).getWindow();
                ui.addWindow(subWindow);
            } else
            {
              UserSession.setCurrentUser(null);
              Page.getCurrent().reload();
            }
        }
    }

    private class SettingsButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent)
        {
            Window subWindow = new UserSettingsForm(ui).getWindow();
            ui.addWindow(subWindow);
        }
    }
}

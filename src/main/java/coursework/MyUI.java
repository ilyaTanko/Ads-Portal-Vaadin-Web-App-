package coursework;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import coursework.gui_forms.MainForm;
import coursework.session.UserSession;
import coursework.gui_forms.MainView;

@Theme("mytheme")
public class MyUI extends UI
{
    private MainView mainView = new MainView(this);
    //private RegisterForm registerView = new RegisterForm(this);

    @Override
    protected void init(VaadinRequest vaadinRequest)
    {
        setContent(new MainForm(this));
        Navigator navigator = UserSession.createNavigator(this);
        addNavigatorViews();
        navigator.navigateTo("mainView");

    }

    private void addNavigatorViews()
    {
        Navigator navigator = UserSession.getNavigator();
        navigator.addView("mainView", mainView);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}

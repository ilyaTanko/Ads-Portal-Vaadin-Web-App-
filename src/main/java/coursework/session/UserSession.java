package coursework.session;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.database.entities.UserEntity;

public class UserSession
{
    private static UserEntity currentUser;
    private static Navigator navigator;

    public static void setCurrentUser(UserEntity user) { currentUser = user; }
    public static UserEntity getCurrentUser() { return currentUser; }

    public static Navigator createNavigator(MyUI ui)
    {
        navigator = new Navigator(ui, ui);
        return navigator;
    }

    public static Navigator getNavigator() { return navigator; }

    public static String getBasePath() { return VaadinService.getCurrent().getBaseDirectory().getAbsolutePath(); }

    public static void removeAllWindows()
    {
        for (Window window : UI.getCurrent().getWindows())
        {

            UI.getCurrent().removeWindow(window);
            window.close();
        }
    }
}

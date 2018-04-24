package coursework.gui_forms;

import com.vaadin.server.FileResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.gui_designs.UserAdsFormDesign;
import coursework.gui_forms.AddAdForm;
import coursework.session.UserSession;

import java.io.File;

public class UserAdsForm extends UserAdsFormDesign
{
    private MyUI ui;
    private Window subWindow = new Window();

    public UserAdsForm(MyUI ui)
    {
        this.ui = ui;

        configureAddButton();

        addAdButton.addClickListener(new AddButtonClickListener());
    }

    private void configureAddButton() {
        addAdButton.setIcon(new FileResource(new File(UserSession.getBasePath() + "/VAADIN/images/app/plus2.png")));
    }

    private class AddButtonClickListener implements ClickListener
    {
        @Override
        public void buttonClick(ClickEvent clickEvent)
        {
            Window newAddSubWindow = new AddAdForm(ui).getWindow();
            ui.addWindow(newAddSubWindow);
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

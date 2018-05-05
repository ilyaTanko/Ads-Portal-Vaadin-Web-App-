package coursework.gui_forms;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import coursework.MyUI;
import coursework.gui_designs.AdminFormDesign;

public class AdminForm extends AdminFormDesign
{
    private MyUI ui;
    private Window subWindow = new Window();

    public AdminForm(MyUI ui)
    {
        this.ui = ui;

        categoriesButton.addClickListener(new CategoriesButtonListener());
        tagsButton.addClickListener(new TagsButtonListener());
        usersButton.addClickListener(new UsersButtonListener());
        adsButton.addClickListener(new AdsButtonListener());
    }

    private class CategoriesButtonListener implements ClickListener
    {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent)
        {
            Window subWindow = new CategoriesForm(ui).getWindow();
            ui.addWindow(subWindow);
        }
    }

    private class TagsButtonListener implements ClickListener
    {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent)
        {
            Window subWindow = new TagsForm(ui).getWindow();
            ui.addWindow(subWindow);
        }
    }

    private class UsersButtonListener implements ClickListener
    {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            Window subWindow = new UserRolesForm(ui).getWindow();
            ui.addWindow(subWindow);
        }
    }

    private class AdsButtonListener implements ClickListener
    {
        @Override
        public void buttonClick(Button.ClickEvent clickEvent) {
            Notification.show(null, "Как администратор, вы можете " +
                            "редактировать и удалять любые объявления",
                    Notification.Type.HUMANIZED_MESSAGE);
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

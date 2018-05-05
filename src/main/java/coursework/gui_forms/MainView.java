package coursework.gui_forms;

import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;
import coursework.MyUI;

public class MainView extends VerticalLayout implements View
{
    private MyUI ui;

    private MainForm mainForm;

    public MainView(MyUI ui)
    {
        this.ui = ui;

        mainForm = new MainForm(ui);
        addComponent(mainForm);
    }
}

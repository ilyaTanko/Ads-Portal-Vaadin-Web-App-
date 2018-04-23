package coursework.gui_forms;

import com.vaadin.navigator.View;
import com.vaadin.ui.VerticalLayout;
import coursework.MyUI;

public class MainView extends VerticalLayout implements View
{
    private MyUI ui;

    private HeaderForm headerForm;
    private MainForm mainForm;

    public MainView(MyUI ui)
    {
        this.ui = ui;

        headerForm = new HeaderForm(ui);
        mainForm = new MainForm(ui);

        addComponent(headerForm);
        addComponent(mainForm);
    }
}

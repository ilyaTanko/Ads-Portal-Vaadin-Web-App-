package coursework.gui_forms;

import com.vaadin.server.FileResource;
import coursework.MyUI;
import coursework.session.UserSession;

import java.io.File;

public class AdShortForm extends AdShortFormDesign
{
    private MyUI ui;

    public AdShortForm(MyUI ui)
    {
        this.ui = ui;
        adImage.setIcon(new FileResource(new File(UserSession.getBasePath() + "/VAADIN/images/ads/userx/car.jpg")));
        adImage.setWidth(250, Unit.PIXELS);
        adImage.setHeight(250, Unit.PIXELS);
        adShortTextLabel.setWidth(250, Unit.PIXELS);
        adShortTextLabel.setHeight(250, Unit.PIXELS);
        topLayout.setHeight(260, Unit.PIXELS);
        // other
    }
}

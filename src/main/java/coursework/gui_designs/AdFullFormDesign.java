package coursework.gui_designs;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Image;

/**
 * !! DO NOT EDIT THIS FILE !!
 * <p>
 * This class is generated by Vaadin Designer and will be overwritten.
 * <p>
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class AdFullFormDesign extends VerticalLayout {
    protected Label headlineLabel;
    protected Image image;
    protected Label contentLabel;
    protected VerticalLayout infoLayout;
    protected Label authorAndDateLabel;
    protected Label categoryLabel;
    protected Label tagsLabel;
    protected Label viewCountLabel;

    public AdFullFormDesign() {
        Design.read(this);
    }
}

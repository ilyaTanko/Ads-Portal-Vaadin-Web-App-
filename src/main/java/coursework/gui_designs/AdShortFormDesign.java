package coursework.gui_designs;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

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
public class AdShortFormDesign extends Panel {
    protected VerticalLayout adShortLayout;
    protected Panel titlePanel;
    protected VerticalLayout titleLayout;
    protected Label adNameLabel;
    protected Label dateLabel;
    protected Panel contentPanel;
    protected HorizontalLayout contentLayout;
    protected Image adImage;
    protected Label adShortTextLabel;
    protected Panel infoPanel;
    protected VerticalLayout infoLayout;
    protected Label authorLabel;
    protected Label viewCountLabel;
    protected Label categoryLabel;
    protected Label tagsLabel;
    protected Panel bottomPanel;
    protected HorizontalLayout bottomLayout;
    protected Button moreInfoButton;
    protected Button editButton;
    protected Button deleteButton;

    public AdShortFormDesign() {
        Design.read(this);
    }
}

package coursework.gui_designs;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Panel;

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
public class AddAdFormDesign extends Panel {
    protected Label titleLabel;
    protected TextField headlineField;
    protected TextArea contentField;
    protected Upload addPhotoButton;
    protected Image uploadedImage;
    protected ComboBox<String> categorySelect;
    protected HorizontalLayout tagsLayout;
    protected TextField tagField;
    protected Button addTagButton;
    protected Button deleteTagButton;
    protected Label tagsLabel;
    protected Button submitButton;

    public AddAdFormDesign() {
        Design.read(this);
    }
}
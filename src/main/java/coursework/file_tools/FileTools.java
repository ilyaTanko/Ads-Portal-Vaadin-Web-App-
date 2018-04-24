package coursework.file_tools;

import javax.swing.*;
import java.awt.*;

public class FileTools
{
    public static class FileNotChosenException extends Exception {};

    public static class TopFileChooser extends JFileChooser
    {
        TopFileChooser(String path) { super(path); }

        @Override
        protected JDialog createDialog(Component parent) throws HeadlessException
        {
            JDialog dialog = super.createDialog(parent);
            dialog.setLocationByPlatform(true);
            dialog.setAlwaysOnTop(true);
            return dialog;
        }

        public boolean fileOpened() { return showOpenDialog(null) == JFileChooser.APPROVE_OPTION; }
        public boolean fileSaved() { return showSaveDialog(null) == JFileChooser.APPROVE_OPTION; }
    }
}

package mvpFileCreator;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManifestFileModifier extends AnAction {

    private Project project;
    private JDialog jFrame;

    JTextField viewName;
    String moduleName;

    FileFactory fileFactory;
    @Override
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert action logic here
        VirtualFile selectedFile = DataKeys.VIRTUAL_FILE.getData(event.getDataContext());
        String modulePath = selectedFile.toString().substring(7);

        if(selectedFile.isDirectory())
            return;  //do nothing..
        project = event.getData(PlatformDataKeys.PROJECT);
        String packageName = Util.readPackageName(modulePath);
        fileFactory = new FileFactory(packageName,modulePath);
        fileFactory.createFile(FileFactory.CodeType.Manifest);
    }
}

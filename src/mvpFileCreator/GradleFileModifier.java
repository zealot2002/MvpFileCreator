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

public class GradleFileModifier extends AnAction {

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
        initSelectView();
    }
    private void initSelectView() {
        jFrame = new JDialog();// 定义一个窗体Container container = getContentPane();
        jFrame.setModal(true);
        Container container = jFrame.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        JPanel panelname = new JPanel();// /定义一个面板
        panelname.setLayout(new GridLayout(1, 1));
        panelname.setBorder(BorderFactory.createTitledBorder("请输入module开关名称"));

        viewName = new JTextField();
        viewName.setText("1");
        panelname.add(viewName);

        container.add(panelname);

        JPanel menu = new JPanel();
        menu.setLayout(new FlowLayout());

        Button cancle = new Button();
        cancle.setLabel("取消");
        cancle.addActionListener(actionListener);

        Button ok = new Button();
        ok.setLabel("确定");
        ok.addActionListener(actionListener);

        menu.add(ok);
        menu.add(cancle);
        container.add(menu);

        jFrame.setSize(200, 150);
        jFrame.setLocationRelativeTo(null);

        jFrame.setVisible(true);
    }
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            jFrame.dispose();
            String currentUser = System.getProperty("user.name");
            if (e.getActionCommand().equals("确定")) {

                fileFactory.setOnOff(viewName.getText());
                fileFactory.createFile(FileFactory.CodeType.Gradle);

                Messages.showInfoMessage(project,"生成完毕，请刷新文件夹","提示");
            }
        }
    };
}

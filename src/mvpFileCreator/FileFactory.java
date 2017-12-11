package mvpFileCreator;

import java.io.*;

public class FileFactory {
    public static enum CodeType {
        Activity,DataProvider,Constact,Presenter,Service,Debug,Manifest,Gradle
    }

    private String packageName;
    private String viewName;
    private String moduleName;
    private String modulePath;
    private String author;
    private String onOff;

    public FileFactory(String packageName,String modulePath){
        this.packageName = packageName;
        this.modulePath = modulePath;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setOnOff(String onOff) {
        this.onOff = onOff;
    }

    public void createMvpFiles() {
        createFile(CodeType.Activity);
        createFile(CodeType.Constact);
        createFile(CodeType.Presenter);
        createFile(CodeType.DataProvider);
        createFile(CodeType.Service);
    }

    public void createFile(CodeType codeType){
        String TemplateFileName,content;
        switch (codeType){
            case Activity:
                TemplateFileName = "TemplateActivity.txt";
                content = ReadFile(TemplateFileName);
                // 1.通用流程,处理顶部注释
                content  = addHeader(content);
                content = dealActivity(content);
                writetoFile(content, modulePath+"/view", viewName + "Activity.java");
                break;

            case Constact:
                TemplateFileName = "TemplateContract.txt";
                content = ReadFile(TemplateFileName);
                // 1.通用流程,处理顶部注释
                content  = addHeader(content);
                content = dealContract(content);
                writetoFile(content, modulePath+"/contract", viewName + "Contract.java");
                break;

            case DataProvider:
                TemplateFileName = "TemplateDataProvider.txt";
                content = ReadFile(TemplateFileName);
                // 1.通用流程,处理顶部注释
                content  = addHeader(content);
                content = dealDataProvider(content);
                writetoFile(content, modulePath+"/model",  "DataProvider.java");
                break;

            case Presenter:
                TemplateFileName = "TemplatePresenter.txt";
                content = ReadFile(TemplateFileName);
                // 1.通用流程,处理顶部注释
                content  = addHeader(content);
                content = dealPresenter(content);
                writetoFile(content, modulePath+"/presenter",  viewName + "Presenter.java");
                break;

            case Service:
                TemplateFileName = "TemplateIService.txt";
                content = ReadFile(TemplateFileName);
                // 1.通用流程,处理顶部注释
                content  = addHeader(content);
                content = content.replace("$moduleName",moduleName);
                content = content.replace("$packageName", packageName);

                writetoFile(content, modulePath+"/service",  "I"+moduleName + "Service.java");

                TemplateFileName = "TemplateService.txt";
                content = ReadFile(TemplateFileName);
                // 1.通用流程,处理顶部注释
                content  = addHeader(content);
                content = content.replace("$moduleName",moduleName);
                content = content.replace("$packageName", packageName);

                writetoFile(content, modulePath+"/service",  moduleName + "Service.java");

                break;

            case Debug:
                createDebugFile();
                break;
            case Manifest:
                modifyManifestFile();
                break;
            case Gradle:
                modifyGradleFile();
                break;
        }
    }

    private void modifyGradleFile() {
        String TemplateFileName = "TemplateGradle.txt";
        String content = ReadFile(TemplateFileName);
        content = content.replace("$onOff", onOff);
        content = content.replace("$moduleName",moduleName);
        Util.writeFile(content,modulePath);
    }

    private void modifyManifestFile() {
        File file = new File(modulePath);
        String content="";
        try {
            content = Util.txt2String(file);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String replaceC = "<application\n" +
                "        android:allowBackup=\"true\"\n" +
                "        android:label=\"@string/app_name\"\n" +
                "        android:supportsRtl=\"true\">\n" +
                "\n" +
                "    </application>";
        int p1 = content.indexOf("<application");
        int p2 = content.indexOf("</application>");
        StringBuilder sb = new StringBuilder();
        sb.append(content.substring(0,p1));
        sb.append(replaceC);
        sb.append(content.substring(p2+"</application>".length(),content.length()));
        Util.writeFile(sb.toString(),modulePath);
    }

    private void createDebugFile() {
        //manifest
        String content = ReadFile("TemplateDebugManifest.txt");
        content = content.replace("$packageName", packageName);
        content = content.replace("$moduleName", moduleName);
        writetoFile(content, modulePath,"AndroidManifest.xml");

        //Application
        String content2 = ReadFile("TemplateDebugApplication.txt");
        content2 = content2.replace("$packageName", packageName);
        content2 = content2.replace("$moduleName", moduleName);
        writetoFile(content2, modulePath,moduleName+"Application.java");

        //manifest
        String content3 = ReadFile("TemplateDebugLauncherActivity.txt");
        content3 = content3.replace("$packageName", packageName);
        writetoFile(content3, modulePath,"LauncherActivity.java");
    }

    private String addHeader(String content) {
        content = content.replace("$author", author);
        content = content.replace("$date", Util.getNowDateShort());
        return content;
    }
    private String dealActivity(String content){
        content = content.replace("$viewName",viewName);
        content = content.replace("$packageName", packageName+".view");
        return content;
    }
    private String dealContract(String content){
        content = content.replace("$viewName",viewName);
        content = content.replace("$packageName", packageName+".contract");
        return content;
    }
    private String dealDataProvider(String content){
//        content = content.replace("$viewName",viewName);
        content = content.replace("$packageName", packageName+".model");
        return content;
    }
    private String dealPresenter(String content){
        content = content.replace("$viewName",viewName);
        content = content.replace("$packageName", packageName+".presenter");
        return content;
    }
    private String ReadFile(String filename){
        InputStream in = null;
        in = this.getClass().getResourceAsStream("/mvpFileCreator/Template/" +filename);
        String content = "";
        try {
            content = new String(readStream(in));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
    public byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
                System.out.println(new String(buffer));
            }

        } catch (IOException e) {
        } finally {
            outSteam.close();
            inStream.close();
        }
        return outSteam.toByteArray();
    }
    private void writetoFile(String content, String filepath, String filename) {
        try {
            File floder = new File(filepath);
            // if file doesnt exists, then create it
            if (!floder.exists()) {
                floder.mkdirs();
            }
            File file = new File(filepath + "/" + filename);
            if (file.exists()) {
                return; //直接返回
            }

            file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

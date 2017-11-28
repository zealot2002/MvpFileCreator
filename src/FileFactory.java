import java.io.*;

public class FileFactory {
    public static enum CodeType {
        Activity,DataProvider,Constact,Presenter
    }

    private String packageName;
    private String moduleName;
    private String viewName;
    private String modulePath;
    private String author;

    public FileFactory(String packageName,String modulePath,String moduleName){
        this.packageName = packageName;
        this.modulePath = modulePath;
        this.moduleName = moduleName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void createAll() {
        createFile(CodeType.Activity);
        createFile(CodeType.Constact);
        createFile(CodeType.Presenter);
        createFile(CodeType.DataProvider);
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
        }
    }
    private String addHeader(String content) {
        content = content.replace("$author", author);
        content = content.replace("$date", Util.getNowDateShort());
        return content;
    }
    private String dealActivity(String content){
        content = content.replace("$viewName",viewName);
        content = content.replace("$packageName", packageName+"."+moduleName+".view");
        return content;
    }
    private String dealContract(String content){
        content = content.replace("$viewName",viewName);
        content = content.replace("$packageName", packageName+"."+moduleName+".contract");
        return content;
    }
    private String dealDataProvider(String content){
//        content = content.replace("$viewName",viewName);
        content = content.replace("$packageName", packageName+"."+moduleName+".model");
        return content;
    }
    private String dealPresenter(String content){
        content = content.replace("$viewName",viewName);
        content = content.replace("$packageName", packageName+"."+moduleName+".presenter");
        return content;
    }
    private String ReadFile(String filename){
        InputStream in = null;
        in = this.getClass().getResourceAsStream("/Template/"+filename);
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

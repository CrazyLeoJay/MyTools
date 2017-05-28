package me.tools;

import java.io.*;

/**
 * <p>
 * time: 16/12/9__10:09
 *
 * @author leojay
 */
public class ClearMain {
    public static void main(String[] args) {
        //设置检索目录
        String url = "/Users/leojay/Desktop/docs";
        //设置被检索的文件类型
        String type = "html";
        //操作
        File file = new File(url);
        clear(file, type);
    }

    static void clear(File file, String type) {
        if (file.isDirectory()) {
            File[] f = file.listFiles();
            if (f != null) {
                for (File fItem : f) {
                    if (fItem.isFile()) {
                        if (fItem.getName().contains("." + type)) {
                            String absolutePath = fItem.getAbsolutePath();
                            System.out.println(absolutePath);
                            try {
                                FileReader reader = new FileReader(fItem);
                                BufferedReader bufferedReader = new BufferedReader(reader);
                                String s;
                                String page = "";
                                while ((s = bufferedReader.readLine()) != null) {
//                                    Pattern pattern = Pattern.compile("http.*google\\.com[/+-|[^\\W]]*");
//                                    Matcher matcher = pattern.matcher(s);
//                                    while (matcher.find()) {
//                                        String group = matcher.group();
//                                        System.out.println(group);
//                                    }
                                    String replace = "";
                                    replace = s.replace("<script src=\"http://www.google.com/jsapi\" type=\"text/javascript\"></script>",
                                            "<!-- <script src=\"http://www.google.com/jsapi\" type=\"text/javascript\"></script> -->");
                                    page += replace + "\n";
                                }
                                writeReplace(fItem.getAbsoluteFile(), page);
                                bufferedReader.close();
                                reader.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (fItem.isDirectory()) {
                        File absoluteFile = fItem.getAbsoluteFile();
                        clear(absoluteFile, type);
                    }
                }
            }
        }
    }

    static void writeReplace(File url, String page) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(url));
            PrintWriter printWriter = new PrintWriter(writer);
            printWriter.write(page);
            printWriter.flush();
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

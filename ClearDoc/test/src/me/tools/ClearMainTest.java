package me.tools;

import org.junit.Test;

import java.io.*;

/**
 * <p>
 * time: 16/12/9__11:30
 *
 * @author leojay
 */
public class ClearMainTest {
    @org.junit.Before
    public void setUp() throws Exception {

    }

    @Test
    public void test() throws IOException {
        File file = new File("/Users/leojay/Desktop/docs/index.html");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String s = null;
        String page = "";
        while ((s = reader.readLine()) != null) {
//            System.out.println(s);
//            Pattern pattern = Pattern.compile("http.*google\\.com[/+-|[^\\W]]*");
//            Matcher matcher = pattern.matcher(s);
            String replace = "";
//            while (matcher.find()) {
//                String group = matcher.group();
//                System.out.println(group);
            replace = s.replace("<script src=\"http://www.google.com/jsapi\" type=\"text/javascript\"></script>",
                    "<!-- <script src=\"http://www.google.com/jsapi\" type=\"text/javascript\"></script> -->");
//            }
            page += replace + "\n";
        }
        reader.close();
        writeReplace(file, page);
    }

    void writeReplace(File url, String page) {
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
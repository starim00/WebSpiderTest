import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Main {
    public static void main(String args[]){
        Html html = new Html();
        String url = "https://www.zjsru.edu.cn/index/xzxw1.htm";
        Document doc = html.GetHtml(url);
        List<String> herf = html.getHerf(doc);
        while ((url=html.getNextPage(doc))!=null){
            doc = html.GetHtml(url);
            herf.addAll(html.getHerf(doc));
        }
        PrintWriter pw = null;
        try {


            for(int i = 0 ;i<herf.size();i++){
                Document temp = html.GetHtml(herf.get(i));
                pw = new PrintWriter(new FileWriter(new File("/Users/hutiance/htmlSave/html/"+temp.title()+".txt")));
                pw.println(temp.title());
                pw.println(html.getWzbt(temp));
                pw.println(html.getContent(temp));
                pw.close();
            }
        }
        catch (IOException e ){
            e.printStackTrace();
        }
        pw.close();
        System.out.println("finish get html");
        Search s = new Search();
        s.creatIndex();
        s.search("树人");
    }
}

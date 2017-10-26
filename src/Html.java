import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Html {
    public Document GetHtml(String url){
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        }
        catch (IOException e){
            System.out.println(e.getMessage());
            doc = null;
        }
        return doc;
    }
    public List<String> getHerf(Document doc){
        Elements herf = doc.select("a");
        List<String> h =herf.eachAttr("abs:href");
        List<String> res = new ArrayList<>();
        Pattern pattern = Pattern.compile("https://www.zjsru.edu.cn/info/1049");
        for(int i =0 ;i<h.size();i++){
            if(pattern.matcher(h.get(i)).find()){
                res.add(h.get(i));
            }
        }
        return res;
    }
    public String getNextPage(Document doc){
        Elements hasNext = doc.getElementsByClass("NextDisabled");
        if(!hasNext.isEmpty())
            return null;
        else{
            Elements next = doc.getElementsByClass("Next");
            return next.attr("abs:href");
        }
    }
    public List<String> getWzbt(Document doc){
        Elements div = doc.select("div.wzbt");
        return div.eachText();
    }
    public List<String> getContent(Document doc){
        Elements body = doc.select("div.nrzwys");
        body.select("p:not(:has(a))");
        return body.eachText();
    }
}

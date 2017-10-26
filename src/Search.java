import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.*;
public class Search {
    String filePath = "/Users/hutiance/htmlSave/html";
    File indexPath = new File("/Users/hutiance/htmlSave/indexSave");
    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_34);


    public void creatIndex() {
        // file-->Document
        try {
            File f = new File(filePath);
            IndexWriter indexWriter = new IndexWriter(FSDirectory.open(indexPath), analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
            for(File file:f.listFiles()) {
                Document doc = file2Document(file.getPath());
                indexWriter.addDocument(doc);
            }
            indexWriter.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void search(String queryString) {
        try {
            String[] fields = {"name","content"};
            QueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_34, fields, analyzer);
            Query query = queryParser.parse(queryString);
            //查询
            IndexSearcher indexSearcher = new IndexSearcher(FSDirectory.open(indexPath));
            Filter filter = null;
            TopDocs topDocs = indexSearcher.search(query, filter, 10000);
            System.out.println("总共有【"+topDocs.totalHits+"】条匹配结果.");
            //输出
            for(ScoreDoc scoreDoc:topDocs.scoreDocs){
                int docSn = scoreDoc.doc;
                Document doc = indexSearcher.doc(docSn);
                printDocumentInfo(doc);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static Document file2Document(String path){
        File file = new File(path);
        Document doc = new Document();
        doc.add(new Field("name",file.getName(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("content",readFileContent(file),Field.Store.YES,Field.Index.ANALYZED));
        doc.add(new Field("size",String.valueOf(file.length()),Field.Store.YES,Field.Index.NOT_ANALYZED));
        doc.add(new Field("path",file.getAbsolutePath(),Field.Store.YES,Field.Index.NOT_ANALYZED));
        return doc;
    }
    /**
     * 读取文件类容
     * */

    private static String readFileContent(File file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuffer content = new StringBuffer();
            try {
                for(String line=null;(line = reader.readLine())!=null;){
                    content.append(line).append("\n");
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
            return content.toString();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        return null;
    }
    public static void printDocumentInfo(Document doc){
        System.out.println("name -->"+doc.get("name"));
        System.out.println("content -->"+doc.get("content"));
        System.out.println("path -->"+doc.get("path"));
        System.out.println("size -->"+doc.get("size"));

    }
}

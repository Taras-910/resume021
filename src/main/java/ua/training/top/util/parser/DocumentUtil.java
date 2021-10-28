package ua.training.top.util.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import static ua.training.top.util.parser.data.CommonUtil.*;

public class DocumentUtil {
    private static Logger log = LoggerFactory.getLogger(DocumentUtil.class);
    public static Document getDocument(String url){
        log.info("\ngetDocument url={}\n", url);
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .userAgent(document_user_agent)
                    .referrer("")
                    .get();
        } catch (IOException e) {
            log.error(internet_connection_error, e.getMessage(), url);
        }
        return document;
    }
}

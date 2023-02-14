package ua.training.top.aggregator.rate;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.model.Rate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;
import static ua.training.top.aggregator.Installation.reCallRate;
import static ua.training.top.util.InformUtil.error;
import static ua.training.top.util.aggregateUtil.DocumentUtil.getDocument;
import static ua.training.top.util.aggregateUtil.data.ConstantsUtil.USDUSD;
import static ua.training.top.util.aggregateUtil.data.DateToUtil.getToLocalDate;
import static ua.training.top.util.xss.XssUtil.xssClear;

public class TradingEconomicsProvider implements RateProvider {
    private final static Logger log = LoggerFactory.getLogger(TradingEconomicsProvider.class);
    private static final String url = "https://tradingeconomics.com/currencies?base=%s";
//    https://tradingeconomics.com/currencies?base=usd

    public List<Rate> getRates(String baseCurrency) {
        log.info("get rates of baseCurrency {}", baseCurrency);
        Set<Rate> set = new LinkedHashSet<>();
        Document doc = getDocument(format(url, baseCurrency));
        Elements elements = doc == null ? null : doc.getElementsByAttributeValueStarting("class", "datatable-row");
        if (elements != null && elements.size() != 0) {
            set.addAll(getRatesList(elements));
        }
        reCallRate(set.size());
        return new ArrayList<>(set);
    }

    private List<Rate> getRatesList(Elements elements) {
        List<Rate> list = new ArrayList();
        for (Element element : elements) {
            try {
                LocalDate localDate = getToLocalDate(xssClear(element.getElementsByAttributeValue("id", "date").text()).replace("/", " "));
                if (/*localDate.isAfter(reasonValidRate)*/true) {
                    Rate r = new Rate();
                    r.setName(xssClear(element.getElementsByTag("a").tagName("b").text()));
                    r.setValueRate(Double.parseDouble(xssClear(element.getElementsByAttributeValue("id", "p").text())));
                    r.setDateRate(localDate);
                    list.add(r);
                }
            } catch (Exception e) {
                log.error(error, e.getLocalizedMessage(), element);
            }
        }
        list.add(new Rate(USDUSD, 1.0, LocalDate.now()));
        return list;
    }
}

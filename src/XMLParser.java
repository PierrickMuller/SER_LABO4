//Based on https://memorynotfound.com/query-xml-xpath-jdom

import org.jdom2.input.SAXBuilder;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
    private XPathFactory xpath;
    Document document;

    XMLParser(String file) {
        // parse and load file into memory
        InputStream in = XMLParser.class.getResourceAsStream(file);
        SAXBuilder builder = new SAXBuilder();

        try {
            document = builder.build(in);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create xpath factory
        xpath = XPathFactory.instance();
    }

    public ArrayList<String> parse(String expression){
        ArrayList<String> result = new ArrayList<>();
        XPathExpression<Element> expr = xpath.compile(expression, Filters.element());
        List<Element> courses = expr.evaluate(document);
        for (Element course : courses){
            result.add(course.getValue().trim());
        }
        return result;
    }





}

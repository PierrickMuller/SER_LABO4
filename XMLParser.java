/* ---------------------------
Laboratoire : Laboratoire 4 - SER
Fichier : InterfaceRecherchePays.java
Auteur(s) : Lievre, Mercier, Muller
Date : 16.06.2019
Remarques : Nous partons du principe que les fichiers XML fournis respectent ceux donnes par le cours.
            Nous ne verifions pas l'integrite de ceux-ci. Nous avons factorises le code au mieux mais
            c'est parfois difficile avec DOM.
            Base sur cet exemple : https://memorynotfound.com/query-xml-xpath-jdom
--------------------------- */

import org.jdom2.input.SAXBuilder;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

//parser XML
public class XMLParser {
    private XPathFactory xpath;
    Document document;

    XMLParser(File file) {
        SAXBuilder builder = new SAXBuilder();

        try {
            document = builder.build(file);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        xpath = XPathFactory.instance();
    }

    //parse une expression XPath et renvoie une liste
    public ArrayList<String> parse(String expression){
        ArrayList<String> result = new ArrayList<>();
        XPathExpression<Element> expr = xpath.compile(expression, Filters.element());
        List<Element> elements = expr.evaluate(document);

        for (Element element : elements){
            result.add(element.getValue().trim());
        }

        return result;
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.FileWriter;
import java.io.IOException;

public class InterfaceRecherchePays extends JFrame {
    private JPanel panelRecherche = new JPanel(new FlowLayout());
    private XMLParser parser = new XMLParser("countries_NEW.xml");
    private JComboBox<String> continents = new JComboBox<>();
    private JComboBox<String> langages = new JComboBox<>();
    private JButton createXSL = new JButton("Générer XSL");
    private JTextField superficieMin = new JTextField(5);
    private JTextField superficieMax = new JTextField(5);

    private Document document;

    private Element createImage(Double width){
        Element image = null;
        if(document != null){
            image = document.createElement("img");
            image.setAttribute("width", width.toString());
            Element imgName = document.createElement("xsl:attribute");
            imgName.setAttribute("name", "src");
            Element imgSrc = document.createElement("xsl:value-of");
            imgSrc.setAttribute("select", "flag");
            imgName.appendChild(imgSrc);
            image.appendChild(imgName);
        }

        return image;
    }

    public InterfaceRecherchePays(File xmlFile) {

        createXSL.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    document = DocumentBuilderFactory
                                        .newInstance()
                                        .newDocumentBuilder()
                                        .newDocument();

                    Element root = document.createElement("xsl:stylesheet");
                    root.setAttribute("version", "1.0");
                    root.setAttribute("xmlns:xsl", "http://www.w3.org/1999/XSL/Transform");

                    //OUTPUT
                    Element output = document.createElement("xsl:output");
                    output.setAttribute("method", "html");
                    output.setAttribute("encoding", "UTF-8");
                    output.setAttribute("doctype-public", "-//W3C//DTD HTML 4.01//EN");
                    output.setAttribute("doctype-system", "http://www.w3.org/TR/html4/strict.dtd");
                    output.setAttribute("indent", "yes");

                    //TEMPLATE
                    Element template = document.createElement("xsl:template");
                    template.setAttribute("match", "/");
                    Element html = document.createElement("html");

                    //HEAD
                    Element head = document.createElement("head");
                    Element title = document.createElement("title");
                    title.setTextContent("Liste des pays");
                    Element jsJquery = document.createElement("script");
                    jsJquery.setAttribute("src", "js/jquery-3.4.1.min.js");
                    Element jsBootstrap = document.createElement("script");
                    jsBootstrap.setAttribute("src", "js/bootstrap.min.js");
                    Element cssBootstrap = document.createElement("link");
                    cssBootstrap.setAttribute("rel", "stylesheet");
                    cssBootstrap.setAttribute("href", "css/bootstrap.min.css");

                    head.appendChild(title);
                    head.appendChild(jsJquery);
                    head.appendChild(jsBootstrap);
                    head.appendChild(cssBootstrap);

                    //BODY
                    //*NAME
                    Element body = document.createElement("body");

                    Element button = document.createElement("button");
                    button.setAttribute("type", "button");
                    button.setAttribute("class", "btn btn-default"); //style du bouton?
                    button.setAttribute("data-toggle", "modal");
                    Element buttonTarget = document.createElement("xsl:attribute");
                    buttonTarget.setAttribute("name", "data-target");
                    Element buttonID = document.createElement("xsl:value-of");
                    buttonID.setAttribute("select", "concat('#',alpha3Code)");
                    buttonTarget.appendChild(buttonID);
                    button.appendChild(buttonTarget);

                    Element divRows = document.createElement("div");
                    divRows.setAttribute("class", "row");

                    Element foreach = document.createElement("xsl:for-each");
                    foreach.setAttribute("select", "countries/element");
                    Element sort = document.createElement("xsl:sort");
                    sort.setAttribute("select","translations/fr");

                    foreach.appendChild(sort);

                    Element divCountries = document.createElement("div");
                    divCountries.setAttribute("class", "col-sm-3");

                    Element valueName = document.createElement("xsl:value-of");
                    valueName.setAttribute("select", "translations/fr");
                    divCountries.appendChild(valueName);

                    //*IMAGE
                    divCountries.appendChild(createImage(25.0));
                    button.appendChild(divCountries);

                    //MODAL
                    Element divModale = document.createElement("div");
                    divModale.setAttribute("class", "modal fade");

                    Element idModale = document.createElement("xsl:attribute");
                    idModale.setAttribute("name", "id");
                    Element valueIdModale = document.createElement("xsl:value-of");
                    valueIdModale.setAttribute("select", "alpha3Code");
                    idModale.appendChild(valueIdModale);
                    divModale.appendChild(idModale);


                    //divModale.setAttribute("id", "exampleModal");
                    divModale.setAttribute("tabindex", "-1");
                    divModale.setAttribute("role", "dialog");
                    divModale.setAttribute("aria-labelledby", "exampleModalLabel");
                    divModale.setAttribute("aria-hidden", "true");

                    Element divModaleDialog = document.createElement("div");
                    divModaleDialog.setAttribute("class", "modal-dialog");
                    divModaleDialog.setAttribute("role", "document");

                    Element divModaleContent = document.createElement("div");
                    divModaleContent.setAttribute("class", "modal-content");

                    Element divModaleHeader = document.createElement("div");
                    divModaleHeader.setAttribute("class", "modal-header");
                    Element titleModale = document.createElement("h5");
                    titleModale.setAttribute("class", "modal-title");

                    Element valueNameModale = document.createElement("xsl:value-of");
                    valueNameModale.setAttribute("select", "translations/fr");
                    titleModale.appendChild(valueNameModale);
                    divModaleHeader.appendChild(titleModale);

                    Element divModaleBody = document.createElement("div");
                    divModaleBody.setAttribute("class", "modal-body");
                    divModaleBody.appendChild(createImage(100.0));

                    Element capital = document.createElement("label");
                    Element valueCapital = document.createElement("xsl:value-of");
                    valueCapital.setAttribute("select", "concat('Continent : ',region)");
                    capital.appendChild(valueCapital);

                    divModaleBody.appendChild(capital);

                    Element divModaleFooter = document.createElement("div");
                    divModaleFooter.setAttribute("class", "modal-footer");
                    Element buttonCloseModale = document.createElement("button");
                    buttonCloseModale.setAttribute("type", "button");
                    buttonCloseModale.setAttribute("class", "btn btn-primary");
                    buttonCloseModale.setAttribute("data-dismiss", "modal");
                    buttonCloseModale.setTextContent("Fermer");

                    divModaleFooter.appendChild(buttonCloseModale);

                    divModaleContent.appendChild(divModaleHeader);
                    divModaleContent.appendChild(divModaleBody);
                    divModaleContent.appendChild(divModaleFooter);
                    divModaleDialog.appendChild(divModaleContent);
                    divModale.appendChild(divModaleDialog);
                    //

                    foreach.appendChild(button);
                    foreach.appendChild(divModale);

                    divRows.appendChild(foreach);
                    body.appendChild(divRows);

                    html.appendChild(head);
                    html.appendChild(body);

                    template.appendChild(html);

                    // Ecriture de la racine dans le document
                    document.appendChild(root);
                    root.appendChild(output);
                    root.appendChild(template);

                    // Ecriture dans le fichier XSL
                    DOMSource source = new DOMSource(document);

                    FileWriter writer = new FileWriter(new File("countries.xsl"));
                    StreamResult result = new StreamResult(writer);

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                    transformer.transform(source, result);

                } catch (ParserConfigurationException error) {
                    error.printStackTrace();
                } catch (IOException error) {
                    error.printStackTrace();
                } catch (TransformerConfigurationException error) {
                    error.printStackTrace();
                } catch (TransformerException error) {
                    error.printStackTrace();
                }
            }

        });

        /**
         * A compléter : Remplissage des listes de recherche (avec les continents et les langues parlées dans l'ordre alphabétique)
         */
        ArrayList<String> dataContinents
                = parser.parse("//countries/element/region[not(. = ../following-sibling::element/region)]");

        Collections.sort(dataContinents);//TRI possible via xpath?

        //fill list with continents
        for(String s : dataContinents) {
            continents.addItem(s);
        }

        ArrayList<String> dataLangage = parser.parse("//countries/element/languages/element/name[not(. = ../../../following-sibling::element/languages/element/name)]");
        Collections.sort(dataLangage); //TRI possible via xpath?
        //fill list with languages
        for(String s : dataLangage) {
            langages.addItem(s);
        }

        setLayout(new BorderLayout());

        panelRecherche.add(new JLabel("Choix d'un continent"));
        panelRecherche.add(continents);

        panelRecherche.add(new JLabel("Choix d'une langue"));
        panelRecherche.add(langages);

        panelRecherche.add(new JLabel("Superficie minimume"));
        panelRecherche.add(superficieMin);

        panelRecherche.add(new JLabel("Superficie maximum"));
        panelRecherche.add(superficieMax);

        panelRecherche.add(createXSL);

        add(panelRecherche, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setTitle("Interface de recherche de pays");
    }

    public static void main(String ... args) {
        new InterfaceRecherchePays(new File("countries.xml"));
    }

}

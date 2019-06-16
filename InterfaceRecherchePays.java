/* ---------------------------
Laboratoire : Laboratoire 4 - SER
Fichier : InterfaceRecherchePays.java
Auteur(s) : Fourni par le cours
Modifie par : Lievre, Mercier, Muller
Date : 16.06.2019
Remarques : Nous partons du principe que les fichiers XML fournis respectent ceux donnes par le cours.
            Nous ne verifions pas l'integrite de ceux-ci. Nous avons factorises le code au mieux mais
            c'est parfois difficile avec DOM. Notre panel ne ressemble pas tout a fait a celui fourni par
            le cours mais toutes les informations y sont. Notre liste de pays n'est pas carree dans un tableau
            mais est laissee a disposition de bootrstrap qui affiche le nombre d'elements necessaire par la
            lignes dans les div selon les classes fournies. Le nombre peut varier (taille d'écran, naviguateurs,
            os, etc...). Un champ vide pour le continent est laisse aussi car des donnees ne contienent pas de
            continent. C'est le cas pour l'ile Bouvet par exemple.
--------------------------- */

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

//interface pour creer un xlst compatible avec un fichier XML de pays
public class InterfaceRecherchePays extends JFrame {
    private JPanel panelRecherche = new JPanel(new FlowLayout());
    private XMLParser parser;
    private JComboBox<String> continents = new JComboBox<>();
    private JComboBox<String> langages = new JComboBox<>();
    private JButton createXSL = new JButton("Générer XSL");
    private JTextField superficieMin = new JTextField(5);
    private JTextField superficieMax = new JTextField(5);
    private Document document;

    //cree une element d'image DOM
    private Element createElementImage(Double width){
        try {
            Element image = document.createElement("img");
            image.setAttribute("width", width.toString());
            Element imgName = document.createElement("xsl:attribute");
            imgName.setAttribute("name", "src");
            Element imgSrc = document.createElement("xsl:value-of");
            imgSrc.setAttribute("select", "flag");
            imgName.appendChild(imgSrc);
            image.appendChild(imgName);

            return image;
        }
        catch(NullPointerException error){
            throw error;
        }
    }

    //cree une element de recherche de valeur DOM
    private Element createElementValue(String param){
        try {
            Element value = document.createElement("xsl:value-of");
            value.setAttribute("select", param);
            return value;
        }
        catch(NullPointerException error){
            throw error;
        }
    }

    //constructeur de l'interface de recherche
    public InterfaceRecherchePays(File xmlFile) {
        //constante pour n'imposer aucun choix dans une liste
        final String NO_CHOICE = "-";

        parser = new XMLParser(xmlFile); //parse le fichier

        createXSL.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    //recupere les elements choisis et cree un filtre
                    String filter = "";
                    if(!superficieMin.getText().isEmpty()){
                        filter = "area >= " + Double.parseDouble(superficieMin.getText());
                    }

                    if(!superficieMax.getText().isEmpty()){
                        filter += (filter != "" ? " and " : "") + "area <= " + Double.parseDouble(superficieMax.getText());
                    }

                    String continent = continents.getSelectedItem().toString();

                    if(continent != "-"){
                        filter += (filter != "" ? " and " : "") + "region = \"" + continent+"\"";
                    }

                    String lang = langages.getSelectedItem().toString();

                    if(lang != "-"){
                        filter += (filter != "" ? " and " : "") + "languages/element/name = \"" + lang+"\"";
                    }

                    //cree un document pour le remplir
                    document = DocumentBuilderFactory
                                        .newInstance()
                                        .newDocumentBuilder()
                                        .newDocument();

                    //racine
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

                    //HEADER
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
                    Element body = document.createElement("body");

                    Element button = document.createElement("button");
                    button.setAttribute("type", "button");
                    button.setAttribute("class", "btn btn-default"); //style du bouton
                    button.setAttribute("data-toggle", "modal");
                    Element buttonTarget = document.createElement("xsl:attribute");
                    buttonTarget.setAttribute("name", "data-target");
                    buttonTarget.appendChild(createElementValue("concat('#',alpha3Code)"));
                    button.appendChild(buttonTarget);

                    Element divRows = document.createElement("div");
                    divRows.setAttribute("class", "row");

                    Element foreach = document.createElement("xsl:for-each");

                    //pour recuperer tous les pays
                    String countriesQuery = "countries/element";

                    //si des filtres sont a appliques, concatene avec la query
                    if(filter != ""){
                        countriesQuery += "["+filter+"]";
                    }
                    foreach.setAttribute("select", countriesQuery); //filtre

                    //tri par nom de pays FR
                    Element sort = document.createElement("xsl:sort");
                    sort.setAttribute("select","translations/fr");
                    foreach.appendChild(sort);


                    Element divCountries = document.createElement("div");
                    divCountries.setAttribute("class", "col-sm-3");
                    button.appendChild(createElementValue("translations/fr"));

                    //*IMAGE
                    button.appendChild(createElementImage(25.0));
                    divCountries.appendChild(button);

                    //MODAL
                    Element divModale = document.createElement("div");
                    divModale.setAttribute("class", "modal fade");

                    Element idModale = document.createElement("xsl:attribute");
                    idModale.setAttribute("name", "id");
                    idModale.appendChild(createElementValue("alpha3Code"));
                    divModale.appendChild(idModale);

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

                    titleModale.appendChild(createElementValue("translations/fr"));
                    divModaleHeader.appendChild(titleModale);

                    //corps de la fenetre modale
                    Element tableBody = document.createElement("table");
                    Element trTableBody = document.createElement("tr");
                    Element tdImage = document.createElement("td");
                    Element tdDataCountry = document.createElement("td");
                    Element divModaleBody = document.createElement("div");
                    divModaleBody.setAttribute("class", "modal-body");

                    tdImage.appendChild(createElementImage(100.0));

                    tdDataCountry.appendChild(createElementValue("concat('Capitale : ',capital)"));
                    tdDataCountry.appendChild(document.createElement("br"));
                    tdDataCountry.appendChild(createElementValue("concat('Population : ',population, ' habitants')"));
                    tdDataCountry.appendChild(document.createElement("br"));
                    tdDataCountry.appendChild(createElementValue("concat('Superficie : ',area, ' km2')"));
                    tdDataCountry.appendChild(document.createElement("br"));
                    tdDataCountry.appendChild(createElementValue("concat('Continent : ',region)"));
                    tdDataCountry.appendChild(document.createElement("br"));
                    tdDataCountry.appendChild(createElementValue("concat('Sous-Continent : ',subregion)"));

                    trTableBody.appendChild(tdImage);
                    trTableBody.appendChild(tdDataCountry);
                    tableBody.appendChild(trTableBody);

                    //Langues
                    Element trTableLang = document.createElement("tr");
                    Element tdLang = document.createElement("td");

                    Element panel = document.createElement("div");
                    panel.setAttribute("class", "panel panel-default");

                    Element panelHeader = document.createElement("div");
                    panelHeader.setAttribute("class", "panel panel-heading");
                    panelHeader.setTextContent("Langues parlées");

                    Element panelBody = document.createElement("div");
                    panelBody.setAttribute("class", "panel panel-body");

                    Element tableLang = document.createElement("table");
                    tableLang.setAttribute("class", "table table-bordered");

                    Element foreachLang = document.createElement("xsl:for-each");
                    foreachLang.setAttribute("select", "languages/element");
                    Element trLstLang = document.createElement("tr");
                    Element tdLstLang = document.createElement("td");

                    tdLstLang.appendChild(createElementValue("name"));
                    trLstLang.appendChild(tdLstLang);
                    foreachLang.appendChild(trLstLang);

                    tableLang.appendChild(foreachLang);
                    panelBody.appendChild(tableLang);

                    trTableLang.appendChild(tdLang);
                    panel.appendChild(panelHeader);
                    panel.appendChild(panelBody);
                    tdLang.appendChild(panel);
                    tableBody.appendChild(trTableLang);
                    divModaleBody.appendChild(tableBody);

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

                    foreach.appendChild(divCountries);
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
                } catch(java.lang.NumberFormatException error){
                    error.printStackTrace();
                } catch(NullPointerException error){
                    error.printStackTrace();
                }
            }

        });

        ArrayList<String> dataContinents
                = parser.parse("//countries/element/region[not(. = ../following-sibling::element/region)]");

        Collections.sort(dataContinents);//tri alphabetique

        dataContinents.add(0, NO_CHOICE);//ajoute un choix vide

        //remplit la liste deroulante
        for(String s : dataContinents) {
            continents.addItem(s);
        }

        ArrayList<String> dataLangage = parser.parse("//countries/element/languages/element/name[not(. = ../../../following-sibling::element/languages/element/name)]");
        Collections.sort(dataLangage); //tri alphabetique

        dataLangage.add(0, NO_CHOICE); //ajoute un choix vide

        //remplit la liste deroulante
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
        new InterfaceRecherchePays(new File("countries_NEW.xml"));
    }
}
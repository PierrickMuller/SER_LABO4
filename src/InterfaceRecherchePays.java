import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

public class InterfaceRecherchePays extends JFrame {

    private JPanel panelRecherche = new JPanel(new FlowLayout());
    private XMLParser parser = new XMLParser("countries.xml");
    private JComboBox<String> continents = new JComboBox<>();
    private JComboBox<String> langages = new JComboBox<>();
    private JButton createXSL = new JButton("Générer XSL");
    private JTextField superficieMin = new JTextField(5);
    private JTextField superficieMax = new JTextField(5);

    public InterfaceRecherchePays(File xmlFile) {

        createXSL.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                super.mouseClicked(e);

                // Création des fichiers XSL selon ce qui est demandé

                /** A compléter... **/

            }

        });

        /**
         * A compléter : Remplissage des listes de recherche (avec les continents et les langues parlées dans l'ordre alphabétique)
         */
        ArrayList<String> dataContinents = parser.parse("//countries/element/region");
        ArrayList<String> dataLangage = parser.parse("//countries/element/region");

        for(String s : dataContinents)
        {
            continents.addItem(s);
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

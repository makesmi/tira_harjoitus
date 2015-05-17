

package kayttoliittyma;

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;
import pelinydin.Väri;


public class PeliTilaPaneeli extends JPanel{
    
    private final JLabel vuoroLaatikko = new JLabel();
    private final JLabel käynnissäLaatikko = new JLabel();
    private final Font fontti = new Font("Tahoma", Font.PLAIN, 20);
    
    public PeliTilaPaneeli(){
        setLayout(new GridLayout(0, 1));
        vuoroLaatikko.setFont(fontti);
        käynnissäLaatikko.setFont(fontti);
        add(käynnissäLaatikko);
        add(vuoroLaatikko);
        setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7));
    }

    public void asetaPeliKäynnissä(boolean käynnissä){
        String teksti = käynnissä ? "peli käynnissä" : "peli keskeytetty";
        käynnissäLaatikko.setText(teksti);
        repaint();
    }
    
    public void päivitä(ShakkiLauta lauta, PeliTila tila){
        päivitäSiirto(tila);
    }
    
    private void päivitäSiirto(PeliTila tila){
        int numero = (tila.siirtoNumero / 2) + 1;
        String vuoroTeksti;

        if(tila.vuoro() == Väri.VALKOINEN){
            vuoroTeksti = "valkoisen siirto";
        }else{
            vuoroTeksti = "mustan siirto";
        }
        
        vuoroLaatikko.setText(numero + ". " + vuoroTeksti);
        repaint();
    }
    
}

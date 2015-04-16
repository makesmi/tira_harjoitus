
package kayttoliittyma;

import javax.swing.JOptionPane;
import pelinohjaus.Pelaaja;
import pelinydin.LoppuTila;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiSiirto;

/**
 *
 * @author Markus
 */
public class PelinLopetusIlmoittaja implements Pelaaja{

    @Override
    public ShakkiSiirto pyydäSiirto(boolean edellinenHyväksytty) {
        return null;
    }

    @Override
    public void peliTilanMuutos(ShakkiLauta lauta, PeliTila tila) {
    }

    @Override
    public void pelinLoppu(LoppuTila loppu, String selitys) {
        JOptionPane.showMessageDialog(null, loppuTilaTekstinä(loppu) + ": " + selitys + "!");
    }
    
    private String loppuTilaTekstinä(LoppuTila tila){
        if(tila == tila.MUSTA_VOITTI){
            return "Musta voitti";
        }else if(tila == tila.VALKEA_VOITTI){
            return "Valkoinen voitti";
        }else{
            return "Tasapeli";
        }
    }
    
}

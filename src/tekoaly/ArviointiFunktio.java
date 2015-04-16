

package tekoaly;

import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;


public interface ArviointiFunktio {
    
    /**
     * Palauttaa arvion tällä hetkellä vuorossa olevan pelaajan kannalta.
     * @param lauta
     * @param tila
     * @return 
     */
    public double arvioi(ShakkiLauta lauta, PeliTila tila);
}

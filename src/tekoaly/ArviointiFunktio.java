

package tekoaly;

import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;


public interface ArviointiFunktio {
    
    /**
     * Palauttaa arvion pelistä tällä hetkellä vuorossa olevan pelaajan kannalta.
     * Positiivinen arvo tarkoittaa, että pelaaja on voitolla.
     * Lähtökohtana arvo 1 vastaan yhtä sotilasta.
     * @param lauta
     * @param tila
     * @return 
     */
    public double arvioi(ShakkiLauta lauta, PeliTila tila);
}



package tekoaly;

import java.util.List;
import pelinohjaus.UhkausTarkistus;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;



public interface HakuAlgoritmi {
    static final double LAITON_SIIRTO = -10000;
    static final double MATTI_ARVO = -9999;
    static final double PATTI_ARVO = 0;
    static final double LAITON_TOISEN_SIIRTO = - LAITON_SIIRTO;
    static final double MAX_ARVO = 20000;
    static final double MIN_ARVO = -20000;
    
    
    /**
     * Palauttaa arvion tällä hetkellä vuorossa olevan pelaajan kannalta
     * @param peli
     * @param arviointi
     * @return 
     */
    
    public ShakkiSiirto haku(ShakkiPeli peli, ArviointiFunktio arviointi);
    
    default boolean laitonLinnoitus(ShakkiPeli peli, List<ShakkiSiirto> siirrot){
        return UhkausTarkistus.olikoLinnoitusSiirto(peli)
                && UhkausTarkistus.linnoitusRuudutUhattu(siirrot, peli.haePeliTila().siirto);
    }
    
    default double tarkistettuArvo(double arvo, ShakkiPeli peli){
            if(arvo == LAITON_SIIRTO){
                if(UhkausTarkistus.omaKuningasUhattu(peli)){
                    return MATTI_ARVO;
                }else{
                    return PATTI_ARVO;
                }
            }else{
                return arvo;
            }
    }
}

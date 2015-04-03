

package shakkipeli;

/**
 * Tätä käytetään pelin loputtua ilmoittamaan pelin lopputulos.
 * @author markumus
 */
public enum LoppuTila {
    VALKEA_VOITTI,
    MUSTA_VOITTI,
    TASAPELI;
    
    public static LoppuTila voitto(boolean väri){
        if(väri == Väri.VALKOINEN){
            return VALKEA_VOITTI;
        }else{
            return MUSTA_VOITTI;
        }
    }
}

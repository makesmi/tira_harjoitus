

package pelinydin;


/**
 * Shakkinappula.
 * Nappula voi olla tyypiltään joko sotilas, ratsu, lähetti, torni, kuningatar tai kuningas
 * ja väriltään valkoinen tai musta.
 * Nappula ei tiedä sijaintiaan laudalla.
 * Samaan nappula-olioon ei saa olla viittausta monessa laudan ruudussa,
 * vaan jokainen samanlainen nappula täytyy olla erillinen olionsa.
 * @author markumus
 */

public class Nappula {
    public final NappulaTyyppi tyyppi;
    public final boolean väri;

    /**
     * Nappulaa ei voi muokata luomisen jälkeen.
     * @param tyyppi Nappulan tyyppi, esim. NappulaTyyppi.SOTILAS
     * @param väri Väri.VALKOINEN tai Väri.MUSTA
     */
    
    public Nappula(NappulaTyyppi tyyppi, boolean väri) {
        this.tyyppi = tyyppi;
        this.väri = väri;
    }
    
    public static boolean onkoSamanlainenNappula(Nappula a, Nappula b){
        if(a == b){
            return true;
        }else if(a == null || b == null){
            return false;
        }else{
            return a.tyyppi == b.tyyppi && a.väri == b.väri;
        }
    }
}

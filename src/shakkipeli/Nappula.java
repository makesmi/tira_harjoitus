

package shakkipeli;


/**
 * Shakkinappula.
 * Nappula voi olla tyypiltään joko sotilas, ratsu, lähetti, torni, kuningatar tai kuningas
 * ja väriltään valkoinen tai musta.
 * Nappula ei tiedä sijaintiaan laudalla.
 * Samaan nappula-olioon ei saa olla viittausta monessa laudan ruudussa,
 * vaan jokainen samanlainen nappula täytyy olla erillinen olionsa
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
    
}

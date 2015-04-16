
package pelinydin;

/**
 * <p>Shakkipeliin kuuluva siirto.
 * Tämän avulla voidaan yksikäsitteisesti määritellä kaikki mahdolliset siirrot tietyssä pelitilanteessa/asetelmassa.
 * Siirrosta tiedetään lähtöruutu ja kohderuutu laudalla, sekä mahdollinen korotus-upseeri.
 * ShakkiSiirto-olio ei tiedä siirrettävää nappulaa, siirron laillisuutta, siirtäjää tai siirtonumeroa.
 * Siirtoa ei voi muokata.
 * </p><p>
 * Koordinaatit menevät niin, että laudan vasemman laidan vaaka(x)-koordinaatti on 0 ja oikean laidan 7.
 * Kun valkoiset nappulat ovat alapuolella, alimman rivin pysty(y)-koordinaatti on 0 ja ylimmän rivin 7.</p>
 * @author markumus
 */

public class ShakkiSiirto{
    public final int lähtöX;
    public final int lähtöY;
    public final int kohdeX;
    public final int kohdeY;
    
    /**
     * Jos siirto on korotussiirto, tämä kertoo millaiseksi upseeriksi sotilas vaihdetaan.
     * Muuten null.
     */
    public final Nappula korotus;

    /**
     * Käytä tätä konstruktoria korotussiirron luomiseen.
     * @param korotus Tämä kertoo millaiseksi upseeriksi sotilas vaihdetaan.
     */
    
    public ShakkiSiirto(int lähtöX, int lähtöY, int kohdeX, int kohdeY, Nappula korotus) {
        this.lähtöX = lähtöX;
        this.lähtöY = lähtöY;
        this.kohdeX = kohdeX;
        this.kohdeY = kohdeY;
        this.korotus = korotus;
    }
    
    /**
     * Käytä tätä konstruktoria kaikkien muiden, kuin korotussiirtojen luomiseen.
     */
    
    public ShakkiSiirto(int lähtöX, int lähtöY, int kohdeX, int kohdeY) {
        this(lähtöX, lähtöY, kohdeX, kohdeY, null);
    }
    
    /**
     * Testaa onko kaksi siirtoa samoja.
     * @param siirto voi olla myös null
     * @return 
     */
    
    public boolean onkoSama(ShakkiSiirto siirto){
        return  siirto != null 
                && lähtöX == siirto.lähtöX && lähtöY == siirto.lähtöY
                && kohdeX == siirto.kohdeX && kohdeY == siirto.kohdeY
                && Nappula.onkoSamanlainenNappula(siirto.korotus, korotus);
    }
    
    @Override
    public String toString(){
        String tulos = lähtöX + "," + lähtöY + " -> " + kohdeX + "," + kohdeY;
        if(korotus != null){
            tulos += " (" + korotus.toString() + ")";
        }
        return tulos;
    }
}

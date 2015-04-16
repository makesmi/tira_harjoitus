
package pelinohjaus;

import java.util.List;
import pelinydin.LoppuTila;
import pelinydin.Nappula;
import pelinydin.NappulaTyyppi;
import static pelinydin.NappulaTyyppi.KUNINGAS;
import static pelinydin.NappulaTyyppi.LÄHETTI;
import static pelinydin.NappulaTyyppi.RATSU;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;
import pelinydin.Väri;


/**
 * Tämän avulla voidaan tarkistaa, onko shakkipeli päättynyt sääntöjen mukaan.
 * Lisäksi selvitetään pelin lopputulos ja päättymisen syy.
 * Peli voi päättyä esimerkiksi tasapeliin ja syynä voisi olla pattitilanne.
 * Yhtä shakkipeliä vastaa yksi LoppuTarkistus-olio, ks. konstruktori.
 * Tarkistamisessa käytetään apuna SiirtojenTarkistusta.
 * @author Markus M
 */


public class LoppuTarkistus {

    private final ShakkiPeli peli;
    private final SiirtojenTarkistus tarkistus;
    
    private List<ShakkiSiirto> siirrot;
    private boolean sovittuLopetus = false;

    private LoppuTila tila;
    private String selitys;
    
    /**
     * Luo LoppuTarkistus olion tietylle shakkipelille ja käyttäen valmista siirtojenTarkistusta.
     * Kutsu tätä siirtojenTarkistuksen luomisen jälkeen, juuri ennen pelin alkamista.
     * @param peli
     * @param siirtoTarkistus valmis SiirtojenTarkistus-olio, jota tullaan käyttämään apuna loppumisen tarkistuksessa.
     */
    
    public LoppuTarkistus(ShakkiPeli peli, SiirtojenTarkistus siirtoTarkistus){
        this.peli = peli;
        selitys = "";
        tila = null;
        this.tarkistus = siirtoTarkistus;
    }
    
    /**
     * Kertoo, onko shakkipeli päättynyt.
     * Tätä kannattaa kutsua aina, kun pelissä tapahtuu siirtoja.
     * Tunnistaa pelin päättymisen automaattiseti laudan asetelman ja pelitilan perusteella.
     * Kun tämä metodi palauttaa true, voit kutsua sen jälkeen metodeja 
     * haeLoppuTila ja haeSelitys, jotka kertovat, miten peli päättyi.
     * <ul>
     * <li>Jos vuorossa olevan pelaajan kuningas on uhattu, 
     * eikä hänellä ole yhtään laillista siirtoa, 
     * peli päättyy mattiin ja pelitilaksi tulee vastapelaajan voitto. Tällöin selitykseksi tulee "shakkimatti"</li>
     * <li>Jos taas kuningas ei ole uhattu, mutta ei ole yhtään laillista siirtoa,
     * peli päättyy pattiin ja lopputilaksi tulee TASAPELI. Tällöin selitykseksi tulee "patti"</li>
     * <li>Jos kummallakin pelaajalla on sellaiset nappulat, ettei niillä voi tehdä mattia,
     * peli päättyy tasapeliin, ja selitykseksi tuluu "nappuloilla ei voi tehdä mattia"</li>
     * <li>Pelaajat voivat lopettaa pelin kesken sopimalla tasapelin tai jos toinen luovuttaa.
     * Käytä metodeja luovutus() ja tasapeli()</li>
     * </ul>
     * Siirtojen laillisuuden tarkistamiseen käytetään sSiirtojenGenerointia ja SiirtojenTarkistusta.
     * @return 
     */
    
    public boolean peliPäättynyt(){
        boolean siirtäjä = peli.haePeliTila().vuoro();

        if(sovittuLopetus){
            return true;
        }else if(!laillisiaSiirtoja()){
            if(kuningasUhattu()){
                tila = LoppuTila.voitto(!siirtäjä);
                selitys = "shakkimatti";
                return true;
            }else{
                tila = LoppuTila.TASAPELI;
                selitys = "patti";
                return true;
            }
        }else if(riittämätönMateriaali()){
            tila = LoppuTila.TASAPELI;
            selitys = "nappuloilla ei voi tehdä mattia";
            return true;
        }
        
        return false;
    }
    
    /**
     * Kertoo miksi peli päättyi.
     * Kutsu tätä sen jälkeen, kun peliPäättynyt-metodi on palauttanut true.
     * @return Lyhyt sanallinen selitys pelin päättymisen syystä. Esim "shakkimatti"
     */
    
    public String haeSelitys(){
        return selitys;
    }
    
    /**
     * Kertoo miten peli päättyi.
     * Kutsu tätä sen jälkeen, kun peliPäättynyt-metodi on palauttanut true.
     * @return Kertoo kuka voitti, vai tuliko tasapeli.
     */
    
    public LoppuTila haeLoppuTila(){
        return tila;
    }
    
    /**
     * Merkitsee pelin luovutetuksi.
     * Kun seuraavan kerran kutsutaan peliPäättynyt-metodia, se palauttaa true.
     * Lopetustilaksi tulee vastapelaajan voitto.
     * Selitykseksi tulee "valkoinen luovutti" tai "musta luovutti".
     * @param pelaaja 
     */
    
    public void luovutus(boolean pelaaja){
        tila = LoppuTila.voitto(!pelaaja);
        String väri = (pelaaja == Väri.VALKOINEN) ? "valkoinen" : "musta";
        selitys = väri + " luovutti"; 
        sovittuLopetus = true;
    }
    
    /**
     * Tätä kutsutaan, kun pelaajan sopivat tasapelistä. Merkitsee pelin päättyneeksi.
     * Kun seuraavan kerran kutsutaan peliPäättynyt-metodia, se palauttaa true.
     * Lopetustilaksi tulee TASAPELI.
     * Selitykseksi tulee "sovittu tasapeli".
     */
    
    public void tasaPeli(){
        tila = LoppuTila.TASAPELI;
        selitys = "sovittu tasapeli";
        sovittuLopetus = true;
    }
    
    private boolean kuningasUhattu(){
        
        peli.teeSiirto(new ShakkiSiirto(0, 0, 0, 0));
        ShakkiLauta lauta = peli.haeLauta();
        
        boolean kuningasUhattu = false;
        
        List<ShakkiSiirto> vastaukset = SiirtojenGenerointi.haeSiirrot(lauta, peli.haePeliTila());
        for(ShakkiSiirto siirto : vastaukset){
            Nappula uhattu = lauta.haeRuutu(siirto.kohdeX, siirto.kohdeY);
            if(uhattu != null && uhattu.tyyppi == NappulaTyyppi.KUNINGAS){
                kuningasUhattu = true;
                break;
            }
        }
        
        peli.peruutaSiirto();
        
        return kuningasUhattu;
    }
    
    private boolean laillisiaSiirtoja(){
        
        siirrot = SiirtojenGenerointi.haeSiirrot(peli.haeLauta(), peli.haePeliTila());
        
        for(ShakkiSiirto siirto : siirrot){
            if(tarkistus.onkoLaillinen(siirto)){
                return true;
            }
        }
        
        return false;
    }
    
    private boolean riittämätönMateriaali(){
        
        int upseereita = 0;
        
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){         
                Nappula ruutu = peli.haeLauta().haeRuutu(x, y);
                if(ruutu != null){
                    if(ruutu.tyyppi != KUNINGAS && ruutu.tyyppi != RATSU && ruutu.tyyppi != LÄHETTI){
                        return false;
                    }else if(ruutu.tyyppi != KUNINGAS){
                        if(upseereita > 0){
                            return false;
                        }else{
                            upseereita++;
                        }
                    }
                }
            }
        }
        
        return true;
    }
}

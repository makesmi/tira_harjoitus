

package tekoaly;

import java.util.List;
import pelinohjaus.UhkausTarkistus;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;


/**
 * Hakualgoritmi vertailee mahdollisten siirtojen seurauksia käyttäen 
 * haluttua arviointifunktiota ja valitsee siten parhaan siirron.
 * Toteutuksessa on suositeltavaa käydä pelipuu rekursiivisesti läpi.
 * Samaa HakuAlgoritmi-oliota voidaan käyttää koko pelin ajan.
 * 
 * Suorituskyvyn takia hakupuun sisäisten 
 * siirtojen laillisuutta ei tarkisteta SiirtojenTarkistuksen avulla, 
 * vaan laittomat siirrot pyritään karsimaan pois tietyillä vakioarvoilla.
 * Hakualgoritmin oletetaan tunnistavan hakupuusta lopputilanteet, 
 * kuten matti ja patti.
 * Tämän helpottamiseksi rajapinta sisältää valmiita vakioarvoja ja apumetodeja.
 */


public interface HakuAlgoritmi {
    
    /**
     * Hakee parhaan siirron tietyssä pelitilanteessa käytteän haluttua arviointifunktiota.
     * @param peli pelitilanne, jossa haku suoritetaan.
     * Anna parametriksi vain kopio oikeasta ShakkiPelistä, 
     * koska algoritmi voi tehdä siirtojan pelissä testatakseen niiden seurauksia.
     * @param arviointi laudan asetelman evaluointiin käytettävä arviointifunktio
     * @return siirto, joku on algoritmin mielestä paras
     * tällä hetkellä vuorossa olevan pelaajan kannalta.
     */
    
    public ShakkiSiirto haku(ShakkiPeli peli, ArviointiFunktio arviointi);

    /**
     * Tätä voidaan kutsua samaan aikaan, kun haku-metodin suoritus on kesken.
     * Pysäyttää haku-metodin suorituksen mahdollisimman nopeasti.
     * Pysähtymistä ei kuitenkään välttämättä jäädä odottamaan.
     * Haku palauttaa parhaan siihen asti löytämänsä siirron tai null,
     * jos yhtään siirtoa ei keretty tutkia.
     */
    
    public void pysäytäHaku();
    
    
    /**
     * Laitonta siirtoa vastaava arvo siirtäjän kannalta.
     * Koska arvo on vahvasti negatiivinen, 
     * siirto karsiutuu vertailussa pois.
     * <h3>Tunnistaminen:</h3> Jos edellisellä siirrolla syöty nappula on kuningas,
     * niin sitä ennen tapahtunut siirto on laiton.
     */
    static final double LAITON_SIIRTO = -10000;

    /**
     * ShakkiMatin arvo mattiin joutuneen pelaajan kannalta.
     * Mattitilanteen tunnistaminen tapahtuu apumetodissa tarkistettuArvo().
     */
    static final double MATTI_ARVO = -9999;
    
    /**
     * Patin arvo on 0, koska se tarkoitta tasapeliä.
     * Pattitilanteen tunnistaminen tapahtuu apumetodissa tarkistettuArvo().
     */
    static final double PATTI_ARVO = 0;
    
    /**
     * Käytä tätä arvoa silloin, kun huomataan, 
     * että vasatapelaajan edellinen siirto oli laiton.
     * ks. apumetodi laitonLinnoitus.
     */
    static final double LAITON_TOISEN_SIIRTO = - LAITON_SIIRTO;
    
    static final double MAX_ARVO = 20000;
    static final double MIN_ARVO = -20000;
    
    
    
    /**
     * Apumetodi, joka tarkistaa oliko edellinen siirto laiton linnoitussiirto.
     * Jos joku kuninkaan linnoituksessa kulkelmista ruuduista on uhattu, 
     * siirto on laiton.
     * Käytä tätä siinä vaiheessa, 
     * kun tällä hetkellä vuorossa olevan pelaajan siirrot on generoitu.
     * Koska siirtojen generointi on raskasta,
     * suorituskyvyn säästämiseksi uhkaustarkistukseen käytetään samaa generoitua siirtolistaa.
     * @param peli testattava peli
     * @param siirrot SiirtojenGeneroinnin generoimat siirrot tällä hetkellä vuorossa olevalle pelaajalle.
     * @return true, jos edellinen siirto oli linnoitussiirto ja joku linnoitusruuduista on uhattuna.
     */
    
    default boolean laitonLinnoitus(ShakkiPeli peli, List<ShakkiSiirto> siirrot){
        return UhkausTarkistus.olikoLinnoitusSiirto(peli)
                && UhkausTarkistus.linnoitusRuudutUhattu(siirrot, peli.haePeliTila().siirto);
    }
    
    /**
     * Apumetodi matti- ja pattitilanteiden tunnistamiseen.
     * Käytetään silloin, kun parhaan siirron arvo on selvitetty.
     * Jos parhaan siirron arvo on LAITON_SIIRTO, 
     * niin pelaajalla ei ole yhtään laillista siirtoa.
     * Tällöin ollaan joko matti- tai pattitilanteessa riippuen siitä, onko kuningas uhattuna.
     * @param arvo parhaan siirron arvo.
     * @param peli pelitilanne, jossa siirtovuoro on pelaajalla, jonka siirtojan tutkitaan.
     * @return MATTI_ARVO tai PATTI_ARVO, jos arvo on LAITON_SIIRTO, muuten arvo
     */
    
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

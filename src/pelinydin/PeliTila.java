
package pelinydin;

/**
 * Kuvaa pelin tilaa yhden vuoron aikana.
 * Yhdessä laudan kanssa PeliTila-olio määrittää täsmällisesti yhden vuoron pelissä.
 * Kun jokaisen siirron pelitila tallennetaan, on siirrot mahdollista peruuttaa täydellisesti.
 * Pelitilaan kuuluu tietoa edellisestä siirrosta, sekä tietoa, mitkä linnoitukset tai ohestalyönnit ovat mahdollisia seuraavalla siirrolla.
 * Pelitilaa ei voi muokata, vaan se määritellään täysin konstruktorissa. Katsot tarkemmat ohjeet konstruktorin dokumentaatiosta.
 * @author markumus
 */

public class PeliTila {
    public final ShakkiSiirto siirto;
    public final Nappula syötyNappula;
    public final int ohestaLyöntiSarake;
    public final boolean korotus;
    public final boolean ohestaLyönti;
    public final int linnoitusMahdollisuudet;
    public final int siirtoNumero;

    /**
     * Kaikki pelitilaan liittyvät attribuutit annetaan konstruktorissa, eikä niitä voi myöhemmin muuttaa.
     * @param siirto edellinen siirto, pelin alkutilanteessa null
     * @param syötyNappula edellisellä siirrolla syöty nappula tai null, jos siirto ei ollut syönti
     * @param ohestaLyöntiSarake laudan vaakakoordinaatti, jota vastaavalla sarakkeella sotilasta on siirretty edellisellä siirrolla kaksi eteenpäin tai -1 jos ohestalyöntimahdollisuutta ei ole
     * @param korotus oliko edellinen siirto korotussiirto
     * @param ohestaLyönti oliko edellinen siirto ohestalyöntisiirto
     * @param linnoitusMahdollisuudet millaiset linnoitustavat ovat mahdollisia (kuninkaan tai tornin siirtäminen voi poistaa linnoitusmahdollisuuksia). 
     * Käytä Linnoitus-luokan vakioita ja binäärioperaatioita. 
     * Pelin alussa Linnoitus.kaikki
     * @param siirtoNumero monesko siirto edellinen siirto oli. Pelin alussa 0.
     */
    
    public PeliTila(ShakkiSiirto siirto, Nappula syötyNappula, int ohestaLyöntiSarake, 
            boolean korotus, boolean ohestaLyönti, int linnoitusMahdollisuudet, int siirtoNumero) {
        this.siirto = siirto;
        this.syötyNappula = syötyNappula;
        this.ohestaLyöntiSarake = ohestaLyöntiSarake;
        this.korotus = korotus;
        this.ohestaLyönti = ohestaLyönti;
        this.linnoitusMahdollisuudet = linnoitusMahdollisuudet;
        this.siirtoNumero = siirtoNumero;
    }
    
    /**
     * Kertoo onko haluttu linnoitustapa enää mahdollista.
     * Jos linnoitukseen osallistuvaa kuningasta tai tornia on siirretty pelin aikana,
     * linnoitus ei ole mahdollista. 
     * Vuorossa olevan pelaajan väriä, tiellä olevia nappuloita tai 
     * kuninkaan linnoitusreitillä olevia uhattuja ruutuja ei oteta huomioon. 
     * Ne täytyy tarkistaa erikseen.
     * @param linnoitusTapa Käytä Linnoitus-luokan vakiota, joka vastaa haluttua linnoitustapaa. 
     * Jos käytät vakiota Linnoitus.kaikki,
     * palautetaan true, jos jokin linnoitustavoista on mahdollista.
     * @return true jos haluttu linnoitustapa on vielä mahdollista. Muuten false.
     */
    
    public boolean voikoLinnoittaa(int linnoitusTapa){
        return (linnoitusMahdollisuudet & linnoitusTapa) != 0;
    }
    
    /**
     * Kertoo, onko valkoisen vai mustan pelaajan vuoro.
     * Valkoinen aloittaa aina.
     * @return Väri.VALKOINEN tai Väri.MUSTA.
     */
    
    public boolean vuoro(){
        return siirtoNumero % 2 == 0 ? Väri.VALKOINEN : Väri.MUSTA;
    }
}

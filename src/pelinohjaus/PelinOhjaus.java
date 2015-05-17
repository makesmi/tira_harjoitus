
package pelinohjaus;

import java.util.LinkedList;
import java.util.List;
import pelinydin.AloitusAsetelma;
import pelinydin.LoppuTila;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;
import pelinydin.Väri;

/**
 * Shakkipelin hallinta ja ohjaus.
 * Konstruktorissa pelille määritellään pelaajat.
 * Pelissä pelaajilta kysytään vuorotellen siirtoja, kunnes peli päättyy.
 * Pelaajille ilmoitetaan aina, kun pelissä tapahtuu siirto, kutsumalla peliTilanMuutos-metodia.
 * LoppuTarkistuksen avulla pelin loppuminen tunnistetaan automaattisesti ja tuloksesta ilmoitetaan
 * pelaajille kutsumalla pelinLoppu-metodia.
 * Jokaisen siirron laillisuus tarkistetaan.
*/


public class PelinOhjaus {
    private final Pelaaja valkoinen;
    private final Pelaaja musta;
    private final ShakkiPeli peli;
    private final SiirtojenTarkistus siirtoTarkistus;
    private final LoppuTarkistus tarkistus;
    private final List<Pelaaja> katsojat;
    private boolean peliKäynnissä;
    
    /**
     * Luo shakkipelin ja asettaa sille pelaajat. Laudalle asetetaan shakin virallinen alkuasetelma.
     * Pelin käynnistämiseksi kutsu pelaa-metodia.
     * @param valkoinen Valkoisilla pelaava pelaaja. Valkoinen aloittaa aina.
     * @param musta Mustilla pelaava pelaaja.
     */
    public PelinOhjaus(Pelaaja valkoinen, Pelaaja musta){
        this(valkoinen, musta, AloitusAsetelma.haeLauta(), AloitusAsetelma.haeTila());
    }
    
    /**
     * Luo shakkpelin, joka alkaa tietynlaisesta alkuasetelmasta ja pelitilasta.
     * @param valkoinen
     * @param musta
     * @param aloitusAsetelma
     * @param alkuTila 
     */
    public PelinOhjaus(Pelaaja valkoinen, Pelaaja musta, ShakkiLauta aloitusAsetelma, PeliTila alkuTila){
        this.valkoinen = valkoinen;
        this.musta = musta;
        peli = new ShakkiPeli(aloitusAsetelma, alkuTila);
        this.siirtoTarkistus = new SiirtojenTarkistus(peli);
        this.tarkistus = new LoppuTarkistus(peli, siirtoTarkistus);
        
        this.katsojat = new LinkedList<>();
        this.katsojat.add(valkoinen);
        this.katsojat.add(musta);
        this.peliKäynnissä = false;
    }
    
    /**
     * Tämä konstruktori on tarkoitettu testeille.
     * Shakkipeli, siirtoTarkistus ja lopputarkastus -komponentit on injektoitu,
     * jotta niihden sijaan voidaan käyttää ylikirjoitettuja testikomponentteja.
     */
    public PelinOhjaus(Pelaaja valkoinen, Pelaaja musta, ShakkiPeli peli, SiirtojenTarkistus siirtoTarkitus, LoppuTarkistus loppuTarkistus){
        this.valkoinen = valkoinen;
        this.musta = musta;
        this.peli = peli;
        this.siirtoTarkistus = siirtoTarkitus;
        this.tarkistus = loppuTarkistus;

        this.katsojat = new LinkedList<>();
        katsojat.add(valkoinen);
        katsojat.add(musta);
        peliKäynnissä = false;
    }
        
    /**
     * Lisää peliin katsojan, jolle ilmoitetaan automaattisesti tapahtuneista siirroista ja pelin loppumisesta.
     * Katsojien peliTilanMuutos-metodia kutsutaan aina kun pelissä tapahtuu siirto ja pelinLoppu-metodia kutsutaan pelin loputtua.
     * Peliin voi lisätä montakin katsojaa.
     * Katsojan teeSiirto-metodia ei koskaan kutsuta.
     * @param katsoja 
     */
    public void lisääKatsoja(Pelaaja katsoja){
        katsojat.add(katsoja);
    }
    
    public void poistaKatsojat(){
        List<Pelaaja> poistettavat = new LinkedList<>();
        for (Pelaaja katsoja : katsojat) {
            if(katsoja != musta && katsoja != valkoinen){
                poistettavat.add(katsoja);
            }
        }
        
        katsojat.removeAll(poistettavat);
    }

    
    /**
     * Suorittaa shakkipelin. Metodista palataan vasta, kun peli on päättynyt.
     * Pelaajilta kysytään vuorotellen siirtoja, kutsumalla pyydäSiirto-metoda. 
     * Aina kun pelaajan ehdottama siirto on laillinen, siirto tehdään ja vuoro vaihtuu.
     * Muuten pelaajalta kysytään siirtoa uudestaan hyväksytty-parametrin arvolla false.
     * Jos pelaaja ehdottaa 40 kertaa peräkkäin laitonta siirtoa,
     * tämä tulkitaan luovuttamiseksi. Myöskin null-siirto tulkitaan luovuttamiseksi.
     * Aina kun laudalla tapahtuu muutos, siitä ilmoitetaan pelaajille ja katsojille
     * kutsumalla peliTilanMuutos-metodia. 
     * Parametrina annettava lauta-olio on kopio pelin varsinaisesta laudasta,
     * jotta pelaajan eivät voi tehdä pelin lautaan muutoksia käsin.
     * Jokaiselle pelaajalle ja katsojalle tehdään eri kopio laudasta.
     * Samoin pelin loppumisesta ilmoitetaan kutsumalla pelinLoppu-metodia.
     * Pelin loppuminen voi tapahtua pysäyttämällä ulkopuolelta käsin sillä aikaa,
     * kun joltain pelaajalta ollaan kysymässä siirtoa.
     * Pelaajien peliTilanMuutos-metodia ei kutsuta koskaan pelinLoppu-metodin kutsumisen jälkeen.
     */
    public void pelaa(){
        peliKäynnissä = true;
       while(!tarkistus.peliPäättynyt()){
           kerroMuutoksestaPelaajille();
           siirto();
       }
                   
       if(peliKäynnissä){
           kerroMuutoksestaPelaajille();
           pelinLoppu();
       }
    }
    
    /**
     * Kertoo, onko peli tällä hetkellä käynnissä vai päättynyt.
     * @return 
     */
    
    public boolean peliKäynnissä(){
        return peliKäynnissä;
    }
    
    /**
     * Merkitään peli päättyneeksi tasapeliin.
     * Tätä voi kutsua samaan aikaan, kun pelaa()-metodin suoritus on kesken.
     * Pelaajien pelinLoppu-metodia kutsutaan, jotta ne tietävät lopettaa
     * mahdollisen siirron odottamisen tai laskemisen.
     */
    public void pysäytäPeli(){
        tarkistus.tasaPeli();
        pelinLoppu();
    }
        
    private void pelinLoppu(){
        if(peliKäynnissä){
            peliKäynnissä = false;
            kerroLoppumisestaPelaajille();
        }
    }
    
    private void siirto(){
        Pelaaja pelaaja = haeVuorossaOlevaPelaaja();
        boolean hyväksytty = true;
        
        for(int laitonSiirtoLaskuri = 0; laitonSiirtoLaskuri < 40; laitonSiirtoLaskuri++){
        
            ShakkiSiirto siirto = pelaaja.pyydäSiirto(hyväksytty);
            
            if(!peliKäynnissä){
                return;
            }else if(siirto == null){
                break;
            }else if(siirtoTarkistus.onkoLaillinen(siirto)){
                peli.teeSiirto(siirto);
                return;
            }else{
                hyväksytty = false;
            }

        }
        
        tarkistus.luovutus(peli.haePeliTila().vuoro());
    }
    
    private Pelaaja haeVuorossaOlevaPelaaja(){
        if(peli.haePeliTila().vuoro() == Väri.VALKOINEN){
            return valkoinen;
        }else{
            return musta;
        }
    }
    
    private void kerroMuutoksestaPelaajille(){
        for (Pelaaja pelaaja : katsojat) {
            ShakkiLauta kopioLaudasta = new ShakkiLauta();
            kopioLaudasta.kopioiAsetelma(peli.haeLauta());
            pelaaja.peliTilanMuutos(kopioLaudasta, peli.haePeliTila());
        }
    }
    
    private void kerroLoppumisestaPelaajille(){
        LoppuTila tila = tarkistus.haeLoppuTila();
        String selitys = tarkistus.haeSelitys();
        
        for (Pelaaja pelaaja : katsojat) {
            pelaaja.pelinLoppu(tila, selitys);
        }
    }
}

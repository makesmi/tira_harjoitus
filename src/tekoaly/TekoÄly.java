
package tekoaly;

import pelinohjaus.Pelaaja;
import pelinydin.LoppuTila;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;


/**
 * Tekoäly, joka voi osallistua shakkipeliin pelaajana.
 * Konstruktorin parametrina annetaan parhaan siirron laskemiseen
 * käytettävä hakualgoritmi ja arviointifunktio.
 * Hakualgoritmilta vaaditaan, että se palauttaa laillisen siirron.
 * @author Markus M
 */

public class TekoÄly implements Pelaaja{

    private ShakkiLauta lauta;
    private PeliTila tila;
    private final ArviointiFunktio arviointi;
    private final HakuAlgoritmi algoritmi;
    private boolean luovutus = false;
    
    /**
     * Luo tekoälyn, jonka voi lisätä pelaajaksi pelinOhjaus-olion konstruktorissa.
     * @param arviointi arviointifunktio, jota halutaan käyttää peliasetelmien evaluointiin.
     * Tekoäly välittää arviointifunktion hakualgoritmille.
     * @param algoritmi parhaan siirron laskemiseksi käytettävä hakualgoritmi
     */
    
    public TekoÄly(ArviointiFunktio arviointi, HakuAlgoritmi algoritmi){
        this.arviointi = arviointi;
        this.algoritmi = algoritmi;
    }
        
    
    /**
     * PelinOhjaus kutsuu tätä silloin, kun pelissä tulee tämän tekoäly-olion siirtovuoro.
     * Valitsee siirron käytteän konstruktorissa annettua hakualgoritmia ja arviointifunktiota.
     * Siirto valitaan viimeisellä peliTilanMuutos-metodin kutsukerralla saadun laudan asetelman ja pelitilan perusteella.
     * Tekoäly ei tiedä omien nappuloidensa väriä, vaan päättelee siirrettävän värin peliTilan vuoro()-metodin perusteella.
     * Hakualgoritmia varten luodaan uusi ShakkiPeli-olio, jonka alkutilanteeksi tulee nykyinen pelitilanne.
     * @param edellinenHyväksytty false, jos edellinen ehdotettu siirto oli laiton, eikä vuoro ole vaihtunut.
     * Tämän ei pitäisi koskaan olla false, koska hakualgoritmin oletetaan palauttavan aina laillinen siirto.
     * @return tekoälyn mielestä paras siirto tai null, jos on kutsuttu luotuvuta-metodia.
     */
    
    @Override
    public ShakkiSiirto pyydäSiirto(boolean edellinenHyväksytty) {
        if(luovutus){
            luovutus = false;
            return null;
        }
        ShakkiPeli testausPeli = new ShakkiPeli(lauta, tila);
        return algoritmi.haku(testausPeli, arviointi);
    }

    
    /**
     * Kutsu tätä aina heti, kun pelissä tapahtuu siirto ja peli alussa.
     * Parametrina annettu lauta ja pelitila tallennetaan seuraavan siirron laskemista varten.
     * Annettuun lautaan ei tehdä muutoksia missään vaiheessa.
     * @param lauta Lauta, jossa on uusin peliasetelma
     * @param tila uusi pelitila
     */
    
    @Override
    public void peliTilanMuutos(ShakkiLauta lauta, PeliTila tila) {
        this.lauta = lauta;
        this.tila = tila;
    }

    
    /**
     * Tätä kutsutaan heti, kun peli päättyy.
     * Tätä voidaan kutsua myös silloin, kun pyydäSiirto-metodin suoritus on kesken.
     * Tällöin hakualgoritmi keskeytetään ja pyydäSiirto-metodista palataan välittömästi.
     * Tämä mahdollistaa pelin keskeyttämisen ulkopuolelta esimerkiksi silloin,
     * kun hakualgoritmia testattaessa haku kestää liian kauan.
     * @param loppu miten peli päättyi
     * @param selitys loppumisen syy
     */
    @Override
    public void pelinLoppu(LoppuTila loppu, String selitys) {
        algoritmi.pysäytäHaku();
    }
    
    
    /**
     * Pakottaa tekoäly luovuttamaan.
     * Seuraavalla pyydäSiirto-metodin kutsulla palautetaan null.
     */
    public void luovuta(){
        luovutus = true;
    }
    
}

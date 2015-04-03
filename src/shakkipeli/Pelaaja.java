
package shakkipeli;

/**
 * Shakkipeliin osallistuva pelaaja. Pelaaja voi olla esimerkiksi tekoäly tai käyttäjältä syötteitä lukeva käyttäjä.
 * Pelin ohjaus pyytää pelaajilta siirtoja sen mukaan, kenen vuoro on.
 * Lisäksi pelaajan peliTilanMuutos-metodia kutsutaan aina kun pelin tila tai laudan asetelma muuttuu.
 * Pelaajalla ei ole mahdollisuutta saada tietoa pelitilasta siirtoa tehdessään,
 * vaan pelaajan täytyy itse tallentaa tieto laudasta ja pelitilasta silloin, kun ne muuttuvat.
 * @author markumus
 */

public interface Pelaaja {
    
    /**
     * Pelin ohjaus kutsuu tätä, kun pelaajan vuoro tulee tai pelaajalta pyydetty edellinen siirto ei ollut laillinen.
     * @param edellinenHyväksytty Oliko edellinen siirto laillinen, eli onko vuoro vaihtunut. 
     * Jos edellinen siirto ei ollut hyväksytty, pelaajan kannattaa kokeilla eri siirtoa tai luovuttaa.
     * Ensimmäisellä siirrolla true.
     * @return pelaajan valitsema siirto. null tulkitaan luovuttamiseksi.
     */
    public ShakkiSiirto pyydäSiirto(boolean edellinenHyväksytty);
    
    /**
     * Tämän avulla pelin ohjaus kertoo pelaajalle, että pelissä on tapahtunut siirto.
     * @param lauta lauta, joka sisältää uuden asetelman
     * @param tila uusi pelitila
     */
    public void peliTilanMuutos(ShakkiLauta lauta, PeliTila tila);
    
    
    /**
     * Tämän avulla pelin ohjaus kertoo pelaajalle, että peli on loppunut.
     * @param loppu VALKEA_VOITTI, MUSTA_VOITTI tai TASAPELI
     * @param selitys tämä kertoo tarkemmin lopputilan perusteet, esim. ajan loppuminen tai pattitilanne.
     */
    public void pelinLoppu(LoppuTila loppu, String selitys);
}

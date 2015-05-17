
package pelinohjaus;

import pelinydin.LoppuTila;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiSiirto;

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
     * Ensimmäisellä siirrolla true. Jos kesken siirron hakemisen tai odottamisen
     * kutsutaan pelinLoppu-metodia, täytyy pyydäSiirto-metodista palata välittömästi.
     * @return pelaajan valitsema siirto. null tulkitaan luovuttamiseksi.
     */
    public ShakkiSiirto pyydäSiirto(boolean edellinenHyväksytty);
    
    /**
     * Tämän avulla pelin ohjaus kertoo pelaajalle, että pelissä on tapahtunut siirto.
     * Tätä kutsutaan myös pelin alussa. Tätä metodia ei kutsuta saman pelin aikana sen
     * jälkeen, kun pelinLoppu-metodia on kutsuttu.
     * Toisinsanoen aina kun tätä kutsutaan, peli on käynnissä.
     * @param lauta lauta, joka sisältää uuden asetelman
     * @param tila uusi pelitila
     */
    public void peliTilanMuutos(ShakkiLauta lauta, PeliTila tila);
    
    
    /**
     * Tämän avulla pelin ohjaus kertoo pelaajalle, että peli on loppunut.
     * Jos tätä kutsutaan samaan aikaan, kun ollaan suorittamassa pyydäSiirto-metodia,
     * pelaajan täytyy huolehtia, että sieltä palataan välittömästi.
     * @param loppu VALKEA_VOITTI, MUSTA_VOITTI tai TASAPELI
     * @param selitys tämä kertoo tarkemmin lopputilan perusteet, esim. ajan loppuminen tai pattitilanne.
     */
    public void pelinLoppu(LoppuTila loppu, String selitys);
}

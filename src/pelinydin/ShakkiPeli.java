
package pelinydin;

import java.util.LinkedList;

import static pelinydin.NappulaTyyppi.*;
import static pelinydin.Väri.*;

/**
 * Shakkipeli, johon kuuluu lauta ja pelitila. 
 * Pelissä voi tehdä siirtoja ja perua siirtoja.
 * Siirtoja ei millään tavalla tarkisteta, vaan oletetaan, että siirrot ovat laillisia.
 * Kun siirtoja tehdään, pelitila ja laudan asetelma päivittyvät niiden mukaan.
 * Myös kaikki erikoissiirrot tunnistetaan.
 * Oletuksena shakkipeli alkaa shakin virallisella alkuasetelmalla, 
 * mutta vaihtoehtoisella konstruktorilla shakkipelin voi aloittaa mistä tahansa asetelmasta ja tilanteesta.
 * @author Markus M
 */

 
public class ShakkiPeli{

    private PeliTila peliTila; 
    private final LinkedList<PeliTila> historia;
    private final ShakkiLauta lauta;
    
    /**
     * Tämän konstruktorin avulla pelin voi aloittaa mistä tahansa asetelmasta ja pelitilasta.
     * Laudaksi ei tule parametrina annettu lauta-olio, vaan kopio siitä.
     * Siten parametrina annettuun lautaan ei missään vaiheessa tehdä muutoksia.
     * @param alkuAsetelma
     * @param alkuTila 
     */
    
    public ShakkiPeli(ShakkiLauta alkuAsetelma, PeliTila alkuTila){
        historia = new LinkedList<>();
        lauta = new ShakkiLauta();
        lauta.kopioiAsetelma(alkuAsetelma);
        peliTila = alkuTila;
    }

    /**
     * Laudan asetelmaksi tulee shakin virallinen alkuasetelma
     * ja pelitilaksi alkutila, jossa yhtään siirto ei ole vielä tehty.
     */
    
    public ShakkiPeli(){
        this(AloitusAsetelma.haeLauta(), AloitusAsetelma.haeTila());
    }
    
    /**
     * Suorittaa halutun siirron laudalla ja tekee siirron aiheuttamat muutokset pelitilaan.
     * Siirron laillisuutta ei millään tavalla tarkisteta, eikä sitä kenen vuorolla nappuloita siirretään.
     * Myös tyhjät siirrot ovat mahdollisia, eli lähtö- ja kohderuutu ovat samat:
     * esim. new ShakkiSiirto(0, 0, 0, 0).
     * <h3>Muutokset laudalle:</h3><ul>
     * <li>Siirron kohderuutuun asetetaan lähtöruudussa oleva nappula (tai null)</li>
     * <li>Siirron lähtöruutu asetetaan tyhjäksi</li>
     * <li>Jos siirto on ohestalyönti, asetetaan syödyn sotilaan ruutu tyhjäksi</li>
     * <li>Jos siirto on linnoitus, siirretään torni nurkasta kuninkaan toiselle puolelle oikealle paikalleen</li>
     * <li>Jos siirto on korotussiirto, kohderuutuun asetetaan korotusupseeri</li>
     * </ul>
     * <h3>Muutokset pelitilaan:</h3> <ul>
     * <li>Luodaan uusi pelitila, ja pannaan edellinen pinoon siirtojen perumista varten</li>
     * <li>Jos siirron kohderuudussa on nappula, merkitään se syödyksi nappulaksi, 
     * muuten syödyksi nappulaksi tulee null</li>
     * <li>Jos siirretään sotilasta kaksi eteen päin, 
     * merkitään sotilaan sarake mahdolliseksi ohestalyöntisarakkeeksi seuraavaa siirtoa varten. 
     * Muuten ohestalyöntisarakkeeksi tulee -1</li>
     * <li></li>
     * <li>Jos siirrettävä nappula on torni tai kuningas, 
     * poistetaan linnoitusmahdollisuudet sellaisiin linnoituksiin,
     * joihin nappula osallistuisi, ellei siirto ole tyhjä siirto</li>
     * <li>Siirron numeroa kasvatetaan yhdellä edelliseen verrattuna</li>
     * <li>Vuoro vaihtuu vastapelaajalle</li>
     * </ul>
     * 
     * Ohestalyönti tunnistetaan siitä, että sotilas liikkuu vaakasuunnassa,
     * mutta kohderuudussa ei ole syötävää nappulaa.
     * Kuningaslinnoitus tunnistetaan siitä, 
     * että kuningas siirretään sarakkeesta 4 sarakkeeseen 6. 
     * Kuningatarlinnoitus tunnistetaan siitä,
     * että kuningas siirretään sarakkeesta 4 sarakkeeseen 2.
     * (katso koordinaatit ShakkiSiirron dokumentaatiosta)
     * @param siirto ei
     */
    
    public void teeSiirto(ShakkiSiirto siirto){
        päivitäPeliTila(siirto);
        suoritaSiirto(siirto);
    }
    
    /**
     * Peruuttaa edellisen siirron laudalla ja palauttaa pelitilan sellaiseksi, 
     * kuin se oli ennen siirtoa. Oikeastaan pelitila olioksi tulee täsmälleen sama olio,
     * joka se oli ennen siirtoa. 
     * Koska siirrot tallennetaan pinoon, 
     * on mahdollista peruuttaa kaikki siirrot takaisin pelin alkuun asti.
     * Jos pelissä ei ole tehty yhtään siirtoa tai kaikki siirrot on jo peruutettu,
     * tämä metodi ei tee mitään.
     * 
     * <h3>Muutokset laudalle:</h3>
     * <ul>
     * <li>Siirron lähtöruutuun asetetaan siirretty nappula</li>
     * <li>Jos siirto oli korotus, lähtöruutuun asetetaan sotilas</li>
     * <li>Kohderuutu asetetaan tyhjäksi</li>
     * <li>Jos siirto oli syönti, kohderuutuun asetetaan syöty nappula</li>
     * <li>Jos siirto oli ohestalyönti, kohderuutu asetetaan tyhjäksi ja syöty sotilas asetetaan oikeaan ruutuun</li>
     * <li>Jos siirto oli linnoitus, torni siirretään takaisin nurkkaan aloituspaikalleen</li>
     * </ul>
     */
    
    public void peruutaSiirto(){
        if(!historia.isEmpty()){

            palautaSiirrettyNappula();
            palautaSyötyNappula();

            peliTila = historia.pop();
        }
    }
    
    /**
     * Palauttaa peliin kuuluvan laudan.
     * Käytä tätä ainoastaan laudan tutkimiseen.
     * Älä tee lautaan muutoksia.
     * Suorituskyvyn vuoksi laudasta ei tehdä kopiota.
     * @return 
     */
    
    public ShakkiLauta haeLauta(){
        return lauta;
    }
    
    public PeliTila haePeliTila(){
        return peliTila;
    }
    
    
    private void suoritaSiirto(ShakkiSiirto siirto){
        Nappula nappula = lauta.haeRuutu(siirto.lähtöX, siirto.lähtöY);

        if(siirto.korotus != null){
            nappula = siirto.korotus;
        }
        
        lauta.asetaRuutu(siirto.kohdeX, siirto.kohdeY, nappula);
        lauta.asetaRuutu(siirto.lähtöX, siirto.lähtöY, null);
        
        if(nappula != null && nappula.tyyppi == KUNINGAS){
            siirräLinnoitusTorni(siirto);
        }
        
        if(peliTila.ohestaLyönti){
            tyhjennäOhestaLyöntiRuutu();
        }
    }
    
    private void siirräLinnoitusTorni(ShakkiSiirto kuningasSiirto){
        ShakkiSiirto siirto = kuningasSiirto;
        
        if(onkoKuningatarLinnoitus(siirto)){
            Nappula linnoitusTorni = lauta.haeRuutu(0, siirto.lähtöY);
            lauta.asetaRuutu(3, siirto.lähtöY, linnoitusTorni);
            lauta.asetaRuutu(0, siirto.lähtöY, null);
        }else if(onkoKuningasLinnoitus(siirto)){
            Nappula linnoitusTorni = lauta.haeRuutu(7, siirto.lähtöY);
            lauta.asetaRuutu(5, siirto.lähtöY, linnoitusTorni);
            lauta.asetaRuutu(7, siirto.lähtöY, null);
        }
    }
    
    private void päivitäPeliTila(ShakkiSiirto siirto){
        Nappula nappula = lauta.haeRuutu(siirto.lähtöX, siirto.lähtöY);
        Nappula syötyNappula = lauta.haeRuutu(siirto.kohdeX, siirto.kohdeY);
        int ohestaLyöntiSarake = -1;
        if(nappula != null && nappula.tyyppi == SOTILAS && Math.abs(siirto.kohdeY - siirto.lähtöY) == 2){
            ohestaLyöntiSarake = siirto.lähtöX;
        }
        boolean korotus = (siirto.korotus != null);
        boolean ohestaLyönti = (nappula != null && nappula.tyyppi == SOTILAS 
                && siirto.kohdeX != siirto.lähtöX && syötyNappula == null);
        if(ohestaLyönti){
            syötyNappula = new Nappula(SOTILAS, !peliTila.vuoro());
        }
        int linnoitusMahdollisuudet = peliTila.linnoitusMahdollisuudet
                & (~poistettavatLinnoitusMahdollisuudet(siirto));
        int siirtoNumero = peliTila.siirtoNumero + 1;
        
        historia.push(peliTila);
        peliTila = new PeliTila(siirto, syötyNappula, ohestaLyöntiSarake, 
                korotus, ohestaLyönti, linnoitusMahdollisuudet, siirtoNumero);
    }
    
    private int poistettavatLinnoitusMahdollisuudet(ShakkiSiirto siirto){
        if(siirto.lähtöX == siirto.kohdeX && siirto.kohdeY == siirto.lähtöY){
            return 0;
        }
        
        if(siirto.lähtöX == 4 && siirto.lähtöY == 0){
            return Linnoitus.valkeaKuningas | Linnoitus.valkeaKuningatar;
        }else if(siirto.lähtöX == 0 && siirto.lähtöY == 0){
            return Linnoitus.valkeaKuningatar;
        }else if(siirto.lähtöX == 7 && siirto.lähtöY == 0){
            return Linnoitus.valkeaKuningas;
        }else if(siirto.lähtöX == 4 && siirto.lähtöY == 7){
            return Linnoitus.mustaKuningas | Linnoitus.mustaKuningatar;
        }else if(siirto.lähtöX == 0 && siirto.lähtöY == 7){
            return Linnoitus.mustaKuningatar;
        }else if(siirto.lähtöX == 7 && siirto.lähtöY == 7){
            return Linnoitus.mustaKuningas;
        }else{
            return 0;
        }
    }
    
    private void tyhjennäOhestaLyöntiRuutu(){
         int x = peliTila.siirto.kohdeX;
         int y = (peliTila.vuoro() == VALKOINEN) ? 3 : 4;
         
         lauta.asetaRuutu(x, y, null);
    }
    
    private void palautaSiirrettyNappula(){
        ShakkiSiirto siirto = peliTila.siirto;
        Nappula siirrettyNappula = lauta.haeRuutu(siirto.kohdeX, siirto.kohdeY);
        if(peliTila.korotus){
            siirrettyNappula = new Nappula(SOTILAS, siirrettyNappula.väri);
        }
        lauta.asetaRuutu(siirto.lähtöX, siirto.lähtöY, siirrettyNappula);
        
        if(siirrettyNappula != null && siirrettyNappula.tyyppi == KUNINGAS){
            palautaLinnoitusTorni(siirto);
        }
    }
    
    private void palautaLinnoitusTorni(ShakkiSiirto kuninkaanSiirto){
        ShakkiSiirto siirto = kuninkaanSiirto;
        //kuningatarpuolen linnoitus
        if(onkoKuningatarLinnoitus(siirto)){
            Nappula linnoitusTorni = lauta.haeRuutu(3, siirto.kohdeY);
            lauta.asetaRuutu(3, siirto.kohdeY, null);
            lauta.asetaRuutu(0, siirto.kohdeY, linnoitusTorni);
        }
        //kuningaspuolen linnoitus
        else if(onkoKuningasLinnoitus(siirto)){
            Nappula linnoitusTorni = lauta.haeRuutu(5, siirto.kohdeY);
            lauta.asetaRuutu(5, siirto.kohdeY, null);
            lauta.asetaRuutu(7, siirto.kohdeY, linnoitusTorni);
        }
    }
    
    
    private void palautaSyötyNappula(){
        int syöntiX = peliTila.siirto.kohdeX;
        int syöntiY = peliTila.siirto.kohdeY;
        
        if(peliTila.ohestaLyönti){
            lauta.asetaRuutu(syöntiX, syöntiY, null);
            if(syöntiY == 2){
                syöntiY = 3;
            }else{
                syöntiY = 4;
            }
        }
        
        lauta.asetaRuutu(syöntiX, syöntiY, peliTila.syötyNappula);
    }
    
    private boolean onkoKuningatarLinnoitus(ShakkiSiirto kuningasSiirto){
        return kuningasSiirto.lähtöX == 4 && kuningasSiirto.kohdeX == 2;
    }
    
    private boolean onkoKuningasLinnoitus(ShakkiSiirto kuningasSiirto){
        return kuningasSiirto.lähtöX == 4 && kuningasSiirto.kohdeX == 6;
    }
    

}

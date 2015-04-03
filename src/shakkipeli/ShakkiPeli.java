
package shakkipeli;

import java.util.LinkedList;
import kayttoliittyma.KäyttöLiittymä;

import static shakkipeli.NappulaTyyppi.*;
import static shakkipeli.Väri.*;

/**
 * Shakkipeli, johon kuuluu lauta ja pelitila. 
 * Pelissä voi tehdä siirtoja ja perua siirtoja.
 * Siirtoja ei millään tavalla tarkisteta, vaan oletetaan, että siirrot ovat laillisia.
 * Kun siirtoja tehdään, pelitila ja laudan asetelma päivittyvät niiden mukaan.
 * Myös kaikki erikoissiirrot tunnistetaan.
 * Oletuksena shakkipeli alkaa shakin virallisella alkuasetelmalla, 
 * mutta vaihtoehtoisella konstruktorilla shakkipelin voi aloittaa mistä tahansa asetelmasta ja tilanteesta.
 * @author markumus
 */


public class ShakkiPeli implements LautaPeli{

    private PeliTila peliTila; 
    private final LinkedList<PeliTila> historia;
    private final ShakkiLauta lauta;
    
    
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
     * Siirron laillisuutta ei millään tavalla tarkisteta.
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
     * Muuten ohestalyöntisarakkeeksi tulee null</li>
     * <li></li>
     * <li>Jos siirrettävä nappula on torni tai kuningas, 
     * poistetaan linnoitusmahdollisuudet sellaisiin linnoituksiin,
     * joihin nappula osallistuisi, ellei siirto ole tyhjä siirto</li>
     * <li>Siirron numeroa kasvatetaan yhdellä edelliseen verrattuna</li>
     * <li>Vuoro vaihtuu vastapelaajalle</li>
     * </ul>
     * @param siirto 
     */
    
    @Override
    public void teeSiirto(ShakkiSiirto siirto){
        päivitäPeliTila(siirto);
        suoritaSiirto(siirto);
    }
    
    /**
     * Peruuttaa edellisen siirron laudalla ja palauttaa pelitilan sellaiseksi, 
     * kuin se oli ennen siirtoa. Koska siirrot tallennetaan pinoon, 
     * on mahdollista peruuttaa kaikki siirrot takaisin pelin alkuun asti.
     * 
     * <h3>Muutokset laudalle:</h3>
     * <ul>
     * <li></li>
     * <li></li>
     * <li></li>
     * <li></li>
     * </ul>
     */
    
    @Override
    public void peruutaSiirto(){
        if(!historia.isEmpty()){

            palautaSiirrettyNappula();
            palautaSyötyNappula();

            peliTila = historia.pop();
        }
    }
    
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
        
        if(nappula.tyyppi == KUNINGAS){
            siirräLinnoitusTorni(siirto);
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
        if(nappula.tyyppi == SOTILAS && Math.abs(siirto.kohdeY - siirto.lähtöY) == 2){
            ohestaLyöntiSarake = siirto.lähtöX;
        }
        boolean korotus = (siirto.korotus != null);
        boolean ohestaLyönti = (nappula.tyyppi == SOTILAS && siirto.kohdeX != siirto.lähtöX && syötyNappula == null);
        if(ohestaLyönti){
            syötyNappula = new Nappula(SOTILAS, peliTila.vuoro());
        }
        int linnoitusMahdollisuudet = peliTila.linnoitusMahdollisuudet;
        int siirtoNumero = peliTila.siirtoNumero + 1;
        
        historia.push(peliTila);
        peliTila = new PeliTila(siirto, syötyNappula, ohestaLyöntiSarake, 
                korotus, ohestaLyönti, linnoitusMahdollisuudet, siirtoNumero);
    }
    
    
    
    private void palautaSiirrettyNappula(){
        ShakkiSiirto siirto = peliTila.siirto;
        Nappula siirrettyNappula = lauta.haeRuutu(siirto.kohdeX, siirto.kohdeY);
        if(peliTila.korotus){
            siirrettyNappula = new Nappula(SOTILAS, siirrettyNappula.väri);
        }
        lauta.asetaRuutu(siirto.lähtöX, siirto.lähtöY, siirrettyNappula);
        
        if(siirrettyNappula.tyyppi == KUNINGAS){
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
            if(peliTila.vuoro() == VALKOINEN){
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
    
    public static void main(String[] args) {
        new KäyttöLiittymä();
    }
    
}



package pelinohjaus;

import java.util.List;
import pelinydin.Nappula;
import pelinydin.NappulaTyyppi;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;


/**
 * Tämän avulla voi tarkistaa shakkipelin siirtojen laillisuuden.
 * Kaikki shakin säännöt otetaan huomioon.
 * SiirtojenTarksitus-olio kannattaa luoda shakkipeli-olion luomisen jälkeen ennen pelin alkamista.
 * @author Markus
 */

public class SiirtojenTarkistus {
    private ShakkiPeli peli;
    
    /**
     * Luo SiirtojenTarkistus-olion tietylle shakkipelille.
     * Kutsu tätä ennen siirojen tekemistä.
     * Tarkistus tutkii pelin lautaa ja pelitilaa.
     * Tarkistus voi tehdä myös siirtoja pelissä mutta ne myös peruutetaan.
     * @param peli 
     */
    public SiirtojenTarkistus(ShakkiPeli peli){
        this.peli = peli;
    }
    
    
    /**
     * Kertoo onko haluttu siirto laillinen tämänhetkisessä pelitilanteessa.
     * Siirto on laillisuuteen on kolme ehtoa:
     * <ul>
     * <li>siirron täytyy kuulua SiirtojenGeneroinnin nykyisestä pelitilanteesta generoimiin siirtoihin</li>
     * <li>siirron jälkeen siirtäjän kuningas ei saa olla uhattuna</li>
     * <li>jos siirto on linnoitus, kuninkaan lähtöruutu ja kuninkaan kuljettava välissä oleva ruutu eivät saa olla uhattuna</li>
     * </ul>
     * Uhatulla ruudulla tarkoitetaan sitä, 
     * että vastapelaaja voi siirtää ruutuun jonkin nappulansa seuraavalla siirrolla.
     * Uhkaavan siirron ei tarvitse olla edes laillinen, 
     * kunhan se kuuluu SiirtojenGeneroinnin tuottamiin siirtoihin.
     * @param siirto ehdotettu siirto
     * @return true jos siirto on laillinen, muuten false
     */
    public boolean onkoLaillinen(ShakkiSiirto siirto){
        return löytyyköSiirto(siirto) && !kuningasUhattu(siirto);
    }

    private boolean löytyyköSiirto(ShakkiSiirto siirto) {
        List<ShakkiSiirto> siirrot = SiirtojenGenerointi
                .haeSiirrot(peli.haeLauta(), peli.haePeliTila());

        boolean löytyykö = false;
        
        for(ShakkiSiirto annettuSiirto : siirrot){
            if(siirto.onkoSama(annettuSiirto)){
                löytyykö = true;
            }
        }
        
        return löytyykö;
    }
    
    private boolean kuningasUhattu(ShakkiSiirto siirto){
        boolean linnoitus = onkoLinnoitusSiirto(siirto);
        
        peli.teeSiirto(siirto);
        ShakkiLauta lauta = peli.haeLauta();
        
        List<ShakkiSiirto> vastaukset = SiirtojenGenerointi
                .haeSiirrot(lauta, peli.haePeliTila());
                
        boolean kuningasUhattu = (linnoitus && UhkausTarkistus.linnoitusRuudutUhattu(vastaukset, siirto))
                || UhkausTarkistus.vastustajanKuningasSyötävänä(lauta, vastaukset);
        
        peli.peruutaSiirto();
        return kuningasUhattu;
    }

    
    private boolean onkoLinnoitusSiirto(ShakkiSiirto siirto){
        Nappula nappula = peli.haeLauta().haeRuutu(siirto.lähtöX, siirto.lähtöY);
        
        if(nappula != null && nappula.tyyppi == NappulaTyyppi.KUNINGAS 
                && Math.abs(siirto.kohdeX - siirto.lähtöX) == 2){
            return true;
        }else{
            return false;
        }
    }
    
}

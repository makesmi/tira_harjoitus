
package pelinohjaus;

import java.util.List;
import pelinydin.Nappula;
import static pelinydin.NappulaTyyppi.KUNINGAS;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;


public class UhkausTarkistus {

    
    public static boolean vastustajanKuningasSyötävänä(ShakkiLauta lauta, List<ShakkiSiirto> siirrot){
        for (ShakkiSiirto siirto : siirrot) {
            Nappula kohde = lauta.haeRuutu(siirto.kohdeX, siirto.kohdeY);
            if(kohde != null && kohde.tyyppi == KUNINGAS){
                return true;
            }
        }
        return false;
    }
    
    public static boolean omaKuningasUhattu(ShakkiPeli peli){
        peli.teeSiirto(new ShakkiSiirto(0, 0, 0, 0));
        List<ShakkiSiirto> siirrot = SiirtojenGenerointi.haeSiirrot(peli.haeLauta(), peli.haePeliTila());
        boolean uhattu = vastustajanKuningasSyötävänä(peli.haeLauta(), siirrot);
        peli.peruutaSiirto();
        return uhattu;
    }
    
    /**
     * Kertoo onko linnoitussiirron tarvitsema kuninkaan kulkema reitti uhattu.
     * Linnoituksessa kuningas kulkee aina kaksi ruutua vaakasuunnassa.
     * Tässä tarkistetaan onko lähtöruutu tai väliruutu uhattuna.
     * Linnoituksen kohderuutua ei tarkisteta tässä, koska se tarkistetaan joka tapauksessa kaikilla siirroilla.
     * @param siirrot vastustajan siirrot
     * @param linnoitus linnoitussiirto
     * @return 
     */
    
    public static boolean linnoitusRuudutUhattu(List<ShakkiSiirto> siirrot, ShakkiSiirto linnoitus){
        int keskiX = (4 + linnoitus.kohdeX) / 2;
        
        return ruutuUhattu(siirrot, linnoitus.lähtöX, linnoitus.lähtöY)
                || ruutuUhattu(siirrot, keskiX, linnoitus.lähtöY);
    }    
    
    /**
     * Tarkistaa kohdistuuko joku siirroista tiettyyn ruutuun.
     * @param siirrot vastaustajan siirrot
     * @param x
     * @param y
     * @return 
     */
    
    public static boolean ruutuUhattu(List<ShakkiSiirto> siirrot, int x, int y){
        boolean uhattu = false;
        
        for(ShakkiSiirto siirto : siirrot){
            if(siirto.kohdeX == x && siirto.kohdeY == y){
                uhattu = true;
            }
        }
        
        return uhattu;
    }
    
    public static boolean olikoLinnoitusSiirto(ShakkiPeli peli){
        ShakkiSiirto siirto = peli.haePeliTila().siirto;
        if(siirto != null && ((siirto.kohdeX - siirto.lähtöX) == 2 || (siirto.kohdeX - siirto.lähtöX) == -2)){
            Nappula nappula = peli.haeLauta().haeRuutu(siirto.kohdeX, siirto.kohdeY);
            if(nappula != null && nappula.tyyppi == KUNINGAS){
                return true;
            }
        }
        
        return false;
    }
}

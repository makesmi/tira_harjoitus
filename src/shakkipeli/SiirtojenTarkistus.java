

package shakkipeli;

import java.util.List;


public class SiirtojenTarkistus {
    private ShakkiPeli peli;
    
    public SiirtojenTarkistus(ShakkiPeli peli){
        this.peli = peli;
    }
    
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
        peli.teeSiirto(siirto);
        ShakkiLauta lauta = peli.haeLauta();
        
        List<ShakkiSiirto> vastaukset = SiirtojenGenerointi
                .haeSiirrot(lauta, peli.haePeliTila());
                
        boolean kuningasUhattu = false;

        if(onkoLinnoitusSiirto(siirto)){
            kuningasUhattu = linnoitusRuudutUhattu(vastaukset, siirto);
        }

        for(ShakkiSiirto vastaus : vastaukset){
            Nappula uhattu = lauta.haeRuutu(vastaus.kohdeX, vastaus.kohdeY);
            if(uhattu != null && uhattu.tyyppi == NappulaTyyppi.KUNINGAS){
                kuningasUhattu = true;
                break;
            }
        }
        
        peli.peruutaSiirto();
        return kuningasUhattu;
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
    
    private boolean linnoitusRuudutUhattu(List<ShakkiSiirto> siirrot, ShakkiSiirto linnoitus){
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
    
    private boolean ruutuUhattu(List<ShakkiSiirto> siirrot, int x, int y){
        boolean uhattu = false;
        
        for(ShakkiSiirto siirto : siirrot){
            if(siirto.kohdeX == x && siirto.kohdeY == y){
                uhattu = true;
            }
        }
        
        return uhattu;
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

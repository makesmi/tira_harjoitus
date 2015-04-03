
package shakkipeli;

import java.util.List;

public class LoppuTarkistus {

    private final ShakkiPeli peli;
    private final SiirtojenTarkistus tarkistus;
    
    private List<ShakkiSiirto> siirrot;

    private LoppuTila tila;
    private String selitys;
    
    public LoppuTarkistus(ShakkiPeli peli, SiirtojenTarkistus siirtoTarkistus){
        this.peli = peli;
        selitys = "";
        tila = null;
        this.tarkistus = siirtoTarkistus;
    }
    
    public boolean peliPäättynyt(){
        
        boolean siirtäjä = peli.haePeliTila().vuoro();
        
        if(!laillisiaSiirtoja()){
            if(kuningasUhattu()){
                tila = LoppuTila.voitto(!siirtäjä);
                selitys = "shakkimatti";
                return true;
            }else{
                tila = LoppuTila.TASAPELI;
                selitys = "patti";
                return true;
            }
        }else if(riittämätönMateriaali()){
            tila = LoppuTila.TASAPELI;
            selitys = "nappuloilla ei voi tehdä mattia";
            return true;
        }
        
        return false;
    }
    
    public String haeSelitys(){
        return selitys;
    }
    
    public LoppuTila haeLoppuTila(){
        return tila;
    }
    
    private boolean kuningasUhattu(){
        
        peli.teeSiirto(new ShakkiSiirto(0, 0, 0, 0));
        ShakkiLauta lauta = peli.haeLauta();
        
        boolean kuningasUhattu = false;
        
        List<ShakkiSiirto> vastaukset = SiirtojenGenerointi.haeSiirrot(lauta, peli.haePeliTila());
        for(ShakkiSiirto siirto : vastaukset){
            Nappula uhattu = lauta.haeRuutu(siirto.kohdeX, siirto.kohdeY);
            if(uhattu != null && uhattu.tyyppi == NappulaTyyppi.KUNINGAS){
                kuningasUhattu = true;
                break;
            }
        }
        
        peli.peruutaSiirto();
        
        return kuningasUhattu;
    }
    
    private boolean laillisiaSiirtoja(){
        
        siirrot = SiirtojenGenerointi.haeSiirrot(peli.haeLauta(), peli.haePeliTila());        
        
        for(ShakkiSiirto siirto : siirrot){
            if(tarkistus.onkoLaillinen(siirto)){
                return true;
            }
        }
        
        return false;
    }
    
    private boolean riittämätönMateriaali(){
        return false;
    }
}


package tekoaly;

import pelinohjaus.Pelaaja;
import pelinydin.LoppuTila;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;


public class TekoÄly implements Pelaaja{

    private ShakkiLauta lauta;
    private PeliTila tila;
    private final ArviointiFunktio arviointi;
    private final HakuAlgoritmi algoritmi;
    private boolean luovutus = false;
    
    public TekoÄly(ArviointiFunktio arviointi, HakuAlgoritmi algoritmi){
        this.arviointi = arviointi;
        this.algoritmi = algoritmi;
    }
    
    @Override
    public ShakkiSiirto pyydäSiirto(boolean edellinenHyväksytty) {
        if(luovutus){
            luovutus = false;
            return null;
        }
        ShakkiPeli testausPeli = new ShakkiPeli(lauta, tila);
        return algoritmi.haku(testausPeli, arviointi);
    }

    
    @Override
    public void peliTilanMuutos(ShakkiLauta lauta, PeliTila tila) {
        this.lauta = lauta;
        this.tila = tila;
    }

    @Override
    public void pelinLoppu(LoppuTila loppu, String selitys) {
        
    }
    
    public void luovuta(){
        luovutus = true;
    }
    
}

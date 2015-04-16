
package kayttoliittyma;

import pelinohjaus.Pelaaja;
import pelinydin.LoppuTila;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiSiirto;

public class PelaajaLiittymä implements Pelaaja{
    private PeliLauta lauta;
    private boolean luovutus = false;
    
    public PelaajaLiittymä(PeliLauta lauta){
        this.lauta = lauta;
    }
    
    @Override
    public ShakkiSiirto pyydäSiirto(boolean edellinenHyväksytty) {        
        if(!edellinenHyväksytty){
            System.out.println("Siirto ei ollut hyväksytty!!");
        }
        
        while(!luovutus){
            ShakkiSiirto siirto = lauta.haeSiirto();
            if(siirto != null){
                return siirto;
            }
            
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                
            }
            
        }
        
        luovutus = false;
        return null;
    }
    
    @Override
    public void peliTilanMuutos(ShakkiLauta lauta, PeliTila tila) {
        this.lauta.päivitäLauta(lauta, tila);
    }

    @Override
    public void pelinLoppu(LoppuTila loppu, String selitys) {
        System.out.println(loppu.toString() + ": " + selitys);
    }
    
    public void luovuta(){
        luovutus = true;
    }
}


package kayttoliittyma;

import pelinohjaus.Pelaaja;
import pelinydin.LoppuTila;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiSiirto;

public class PelaajaLiittymä implements Pelaaja{
    private PeliLauta lauta;
    private boolean luovutus = false;
    private PeliTilaPaneeli tilaPaneeli;
    private boolean pysäytys = false;
    
    public PelaajaLiittymä(PeliLauta lauta, PeliTilaPaneeli tilaPaneeli){
        this.lauta = lauta;
        this.tilaPaneeli = tilaPaneeli;
        this.pysäytys = false;
    }
        
    @Override
    public ShakkiSiirto pyydäSiirto(boolean edellinenHyväksytty) {        
        if(!edellinenHyväksytty){
            lauta.repaint();
        }
        
        while(!(luovutus || pysäytys)){
            ShakkiSiirto siirto = lauta.haeSiirto();
            if(siirto != null){
                return siirto;
            }
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                
            }
            
        }
        
        luovutus = false;
        return null;
    }
    
    @Override
    public void peliTilanMuutos(ShakkiLauta lauta, PeliTila tila) {
        this.tilaPaneeli.asetaPeliKäynnissä(true);
        pysäytys = false;
        this.lauta.päivitäLauta(lauta, tila);
        this.tilaPaneeli.päivitä(lauta, tila);
    }

    @Override
    public void pelinLoppu(LoppuTila loppu, String selitys) {
        tilaPaneeli.asetaPeliKäynnissä(false);
        pysäytys = true;
    }
    
    public void luovuta(){
        luovutus = true;
    }
}

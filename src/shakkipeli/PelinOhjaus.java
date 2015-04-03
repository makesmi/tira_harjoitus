
package shakkipeli;


public class PelinOhjaus {
    private final Pelaaja valkoinen;
    private final Pelaaja musta;
    private final ShakkiPeli peli;
    private final SiirtojenTarkistus siirtoTarkistus;
    private final LoppuTarkistus tarkistus;
        
    public PelinOhjaus(Pelaaja valkoinen, Pelaaja musta, ShakkiPeli peli, SiirtojenTarkistus siirtoTarkitus, LoppuTarkistus loppuTarkistus){
        this.valkoinen = valkoinen;
        this.musta = musta;
        this.peli = peli;
        this.siirtoTarkistus = siirtoTarkitus;
        this.tarkistus = loppuTarkistus;
        
        pelaa();
    }
    
    public PelinOhjaus(Pelaaja valkoinen, Pelaaja musta){
        this.valkoinen = valkoinen;
        this.musta = musta;
        peli = new ShakkiPeli();
        this.siirtoTarkistus = new SiirtojenTarkistus(peli);
        this.tarkistus = new LoppuTarkistus(peli, siirtoTarkistus);
        
        pelaa();
    }
        
    
    private void pelaa(){
       while(!tarkistus.peliPäättynyt()){
           kerroMuutoksestaPelaajille();
           siirto();       
       }
       
       kerroMuutoksestaPelaajille();
       kerroLoppumisestaPelaajille();
    }
    
    private void siirto(){
        Pelaaja pelaaja = haeVuorossaOlevaPelaaja();
        boolean hyväksytty = true;
        
        while(true){
            ShakkiSiirto siirto = pelaaja.pyydäSiirto(hyväksytty);
            
            if(siirtoTarkistus.onkoLaillinen(siirto)){
                peli.teeSiirto(siirto);
                return;
            }else{
                hyväksytty = false;
            }
        }
    }
    
    private Pelaaja haeVuorossaOlevaPelaaja(){
        if(peli.haePeliTila().vuoro() == Väri.VALKOINEN){
            return valkoinen;
        }else{
            return musta;
        }
    }
    
    private void kerroMuutoksestaPelaajille(){
        ShakkiLauta lauta = new ShakkiLauta();
        lauta.kopioiAsetelma(peli.haeLauta());
        
        valkoinen.peliTilanMuutos(lauta, peli.haePeliTila());
        musta.peliTilanMuutos(lauta, peli.haePeliTila());
    }
    
    private void kerroLoppumisestaPelaajille(){
        LoppuTila tila = tarkistus.haeLoppuTila();
        String selitys = tarkistus.haeSelitys();
        
        valkoinen.pelinLoppu(tila, selitys);
        musta.pelinLoppu(tila, selitys);
    }
}



package kayttoliittyma;

import javax.swing.JFrame;
import shakkipeli.AloitusAsetelma;
import shakkipeli.LoppuTila;
import shakkipeli.Pelaaja;
import shakkipeli.PeliTila;
import shakkipeli.ShakkiLauta;
import shakkipeli.ShakkiSiirto;


public class KäyttöLiittymä extends JFrame implements Pelaaja {

    PeliLauta lauta;
    
    public KäyttöLiittymä(){
        setVisible(true);
        setTitle("ShakkiTekoäly");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 964);
        
        lauta = new PeliLauta();
        lauta.päivitäLauta(AloitusAsetelma.haeLauta());
        add(lauta);
    }
    
    @Override
    public ShakkiSiirto pyydäSiirto(boolean edellinenHyväksytty) {
        repaint();
        
        while(true){
            ShakkiSiirto siirto = lauta.haeSiirto();
            if(siirto != null){
                return siirto;
            }
            
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                
            }
            
        }
    }

    @Override
    public void peliTilanMuutos(ShakkiLauta lauta, PeliTila tila) {
        this.lauta.päivitäLauta(lauta);
    }

    @Override
    public void pelinLoppu(LoppuTila loppu, String selitys) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

}

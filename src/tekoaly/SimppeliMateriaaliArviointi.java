

package tekoaly;

import pelinydin.Nappula;
import static pelinydin.NappulaTyyppi.*;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;


public class SimppeliMateriaaliArviointi implements ArviointiFunktio{
    private boolean siirtäjä;
    
    private static final double[] nappuloidenArvot;
    
    static{
        nappuloidenArvot = new double[6];
        
        nappuloidenArvot[SOTILAS.ordinal()] = 1.0;
        nappuloidenArvot[RATSU.ordinal()] = 2.9;
        nappuloidenArvot[LÄHETTI.ordinal()] = 3.2;
        nappuloidenArvot[TORNI.ordinal()] = 5.0;
        nappuloidenArvot[KUNINGATAR.ordinal()] = 9.0;
        nappuloidenArvot[KUNINGAS.ordinal()] = 1000.0;
    }
    
    @Override
    public double arvioi(ShakkiLauta lauta, PeliTila tila){
        double arvo = 0;
        siirtäjä = tila.vuoro();
        
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                Nappula ruutu = lauta.haeRuutu(x, y);
                if(ruutu != null){
                    arvo += haeNappulanArvo(ruutu);
                }
            }
        }
        
        return arvo;
    }
    
    private double haeNappulanArvo(Nappula nappula){
        double arvo = nappuloidenArvot[nappula.tyyppi.ordinal()];
        
        if(nappula.väri == siirtäjä){
            return arvo;
        }else{
            return -arvo;
        }
    }
}

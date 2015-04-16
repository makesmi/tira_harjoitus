

package tekoaly;

import java.util.Random;
import pelinydin.Nappula;
import static pelinydin.NappulaTyyppi.KUNINGAS;
import static pelinydin.NappulaTyyppi.KUNINGATAR;
import static pelinydin.NappulaTyyppi.LÄHETTI;
import static pelinydin.NappulaTyyppi.RATSU;
import static pelinydin.NappulaTyyppi.SOTILAS;
import static pelinydin.NappulaTyyppi.TORNI;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;
import pelinydin.Väri;


public class PositioArviointi implements ArviointiFunktio{

    private static final double[] nappuloidenArvot;
    private int satunnaisPaikka = 0;
    private double satunnaisTaulu[] = new double[1000];
    
    static{
        nappuloidenArvot = new double[6];
        
        nappuloidenArvot[SOTILAS.ordinal()] = 1.0;
        nappuloidenArvot[RATSU.ordinal()] = 2.9;
        nappuloidenArvot[LÄHETTI.ordinal()] = 3.2;
        nappuloidenArvot[TORNI.ordinal()] = 5.0;
        nappuloidenArvot[KUNINGATAR.ordinal()] = 9.0;
        nappuloidenArvot[KUNINGAS.ordinal()] = 1000.0;
    }       
    
    
    private static final double[][] sotilasArvot = new double[][]{
         {0,   0,   0,   0,   0,   0,   0,   0},
         {0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9, 0.9},
         {0.9, 1,   1,   1.1, 1.1, 0.9, 0.9, 0.9},
         {1,   1,   1.1, 1.4, 1.4, 1.1, 0.9, 0.9},
         {1.1, 1.2, 1.3, 1.5, 1.5, 1.3, 1.2, 1},
         {1.6, 1.6, 2,   2.5, 2.5,   2, 1.6, 1.6},
         {4,   4,   4,   4,   4,   4,   4,   4},
         {0,   0,   0,   0,   0,   0,   0,   0}
     };

    private static final double[][] ratsuArvot = new double[][]{
         {2.5, 2.7, 2.7, 2.8, 2.8, 2.7, 2.7, 2.5},
         {2.6, 2.8, 2.8, 2.9, 2.9, 2.8, 2.8, 2.6},
         {2.6, 2.8, 2.9, 3.0, 3.0, 2.9, 2.8, 2.6},
         {2.7, 2.9, 3.1, 3.3, 3.3, 3.1, 2.9, 2.7},
         {2.7, 2.9, 3.1, 3.3, 3.3, 3.1, 2.9, 2.7},
         {2.6, 2.8, 2.9, 3.0, 3.0, 2.9, 2.8, 2.6},
         {2.6, 2.8, 2.8, 2.9, 2.9, 2.8, 2.8, 2.6},
         {2.5, 2.7, 2.7, 2.8, 2.8, 2.7, 2.7, 2.5},
     };

     
    public PositioArviointi(double satunnaisKerroin){
        double suuriPoikkeama = satunnaisKerroin;
        double pieniPoikkeama = satunnaisKerroin * 0.25;
        Random satunnaisuus = new Random(System.currentTimeMillis());
        for(int p = 0; p < satunnaisTaulu.length; p++){
            if(satunnaisuus.nextDouble() < 0.3){
                satunnaisTaulu[p] = (satunnaisuus.nextDouble() - 0.5) * suuriPoikkeama;
            }else{
                satunnaisTaulu[p] = (satunnaisuus.nextDouble() - 0.5) * pieniPoikkeama;
            }
        }
    }
    
    private double kuningasArvio(ShakkiLauta lauta){
        boolean valkeaPaikallaan = false;
        boolean mustaPaikallaan = false;
        
        for(int x = 0; x < 8; x++){
            Nappula ruutu = lauta.haeRuutu(x, 0);
            if(ruutu != null && ruutu.tyyppi == KUNINGAS){
                valkeaPaikallaan = true;
            }
            ruutu = lauta.haeRuutu(x, 7);
            if(ruutu != null && ruutu.tyyppi == KUNINGAS){
                mustaPaikallaan = true;
            }
        }
        
        double arvo = 0;
        
        if(!valkeaPaikallaan){
            arvo -= 1.0;
        }
        if(!mustaPaikallaan){
            arvo += 1.0;
        }
        
        return arvo;
    }
    
    private double kehitysArvio(ShakkiLauta lauta){
        double arvot[] = {0, 0.3, 0.2, 0.1, 0, 0.23, 0.33, 0};
        double arvo = 0;
        for(int sarake = 1; sarake < 7; sarake++){
            Nappula nappula = lauta.haeRuutu(sarake, 0);
            if(nappula == null || nappula.tyyppi == TORNI){
                arvo += arvot[sarake];
            }
            nappula = lauta.haeRuutu(sarake, 7);
            if(nappula == null || nappula.tyyppi == TORNI){
                arvo -= arvot[sarake];
            }
        }
        
        Nappula ruutu = lauta.haeRuutu(6, 0);
        if(ruutu != null && ruutu.tyyppi == KUNINGAS){
            arvo += 0.7;
        }
        ruutu = lauta.haeRuutu(2, 0);
        if(ruutu != null && ruutu.tyyppi == KUNINGAS){
            arvo += 0.6;
        }
        ruutu = lauta.haeRuutu(6, 7);
        if(ruutu != null && ruutu.tyyppi == KUNINGAS){
            arvo -= 0.7;
        }
        ruutu = lauta.haeRuutu(2, 7);
        if(ruutu != null && ruutu.tyyppi == KUNINGAS){
            arvo -= 0.6;
        }
        
        return arvo;
    }
    
    @Override
    public double arvioi(ShakkiLauta lauta, PeliTila tila) {
        double arvo = satunnaisTaulu[satunnaisPaikka];
        satunnaisPaikka = (satunnaisPaikka + 1) % 1000;
        boolean siirtäjä = tila.vuoro();
        if(tila.siirtoNumero < 29){
            if(siirtäjä == Väri.VALKOINEN){
                arvo += kehitysArvio(lauta);
            }else{
                arvo -= kehitysArvio(lauta);
            }
        }
        
        if(tila.siirtoNumero < 65){
            if(siirtäjä == Väri.VALKOINEN){
                arvo += kuningasArvio(lauta);
            }else{
                arvo -= kuningasArvio(lauta);
            }
        }
        
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                Nappula ruutu = lauta.haeRuutu(x, y);
                if(ruutu != null){
                    double nappulaArvo = 0;
                    if(ruutu.tyyppi == SOTILAS){
                        if(ruutu.väri == Väri.VALKOINEN){
                            nappulaArvo = sotilasArvot[y][x];
                        }else{
                            nappulaArvo = sotilasArvot[7-y][x];
                        }
                    }else if(ruutu.tyyppi == RATSU){
                        nappulaArvo = ratsuArvot[y][x];
                    }else{
                        nappulaArvo = nappuloidenArvot[ruutu.tyyppi.ordinal()];
                    }
                    if(ruutu.väri == siirtäjä){
                        arvo += nappulaArvo;
                    }else{
                        arvo -= nappulaArvo;
                    }
                }
            }
        }
        
        return arvo;
    }
    
}

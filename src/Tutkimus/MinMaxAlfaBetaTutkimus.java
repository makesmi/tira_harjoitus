/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tutkimus;

import java.util.List;
import pelinohjaus.SiirtojenGenerointi;
import pelinydin.Nappula;
import static pelinydin.NappulaTyyppi.KUNINGAS;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;
import tekoaly.ArviointiFunktio;
import static tekoaly.HakuAlgoritmi.LAITON_TOISEN_SIIRTO;

/**
 *
 * @author Markus
 */
public class MinMaxAlfaBetaTutkimus implements TutkimusAlgoritmi{

    private final int hakuSyvyys;
    private int syöntiHakuSyvyys;
    private ShakkiPeli peli;
    private ArviointiFunktio arviointi;
    private boolean pysäytys;
    
    public MinMaxAlfaBetaTutkimus(int syvyys){
        this.hakuSyvyys = syvyys;
        this.syöntiHakuSyvyys = 2;
    }
    
    public void asetaSyöntiHakuSyvyys(int syvyys){
        this.syöntiHakuSyvyys = syvyys;
    }
    
    @Override
    public HakuPuu tutki(ShakkiPeli peli, ArviointiFunktio arviointi) {
        this.peli = peli;
        this.arviointi = arviointi;
        this.pysäytys = false;
        HakuPuu puu = new HakuPuu(peli.haeLauta(), peli.haePeliTila());
        haku(hakuSyvyys, MIN_ARVO, MAX_ARVO, puu.haeJuuri());
        return puu;
    }    
    
    private double haku(int syvyys, double alfa, double beta, PeliSolmu vanhempi){
        Nappula syöty = peli.haePeliTila().syötyNappula;
        if(syöty != null && syöty.tyyppi == KUNINGAS){
            return -10000;
        }else if(pysäytys || (syvyys <= 0 && (syöty == null || syvyys <= -syöntiHakuSyvyys)) ){
            return arviointi.arvioi(peli.haeLauta(), peli.haePeliTila());
        }else{
            List<ShakkiSiirto> siirrot = SiirtojenGenerointi.haeSiirrot(peli.haeLauta(), peli.haePeliTila());
            
            if(laitonLinnoitus(peli, siirrot)){
                vanhempi.lisääTagi("LL");
                return LAITON_TOISEN_SIIRTO;
            }
            
            for (ShakkiSiirto siirto : siirrot) {
                peli.teeSiirto(siirto);
                PeliSolmu solmu = new PeliSolmu(siirto, 0, vanhempi);
                double arvo = -haku(syvyys - 1, -beta, -alfa,  solmu);
                solmu.asetaArvo(arvo);
                peli.peruutaSiirto();

                if(arvo >= beta){
                    return beta + 1;
                }else if(arvo >= alfa){
                    alfa = arvo;
                }
            }
            
            return tarkistettuArvo(alfa, peli);
        }
    }    
    
    @Override
    public void pysäytäHaku(){
        pysäytys = true;
    }
}

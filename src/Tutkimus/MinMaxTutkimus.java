
package Tutkimus;


import java.util.List;
import pelinohjaus.SiirtojenGenerointi;
import pelinydin.Nappula;
import static pelinydin.NappulaTyyppi.KUNINGAS;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;
import tekoaly.ArviointiFunktio;


public class MinMaxTutkimus implements TutkimusAlgoritmi{
    
    private final int hakuSyvyys;
    private int syöntiHakuSyvyys;
    private boolean pysäytys;

    private ShakkiPeli peli;
    private ArviointiFunktio arviointi;
    
    public MinMaxTutkimus(int syvyys){
        this.hakuSyvyys = syvyys;
        syöntiHakuSyvyys = 2;
    }
    
    public void asetaSyöntiHakuSyvyys(int syvyys){
        this.syöntiHakuSyvyys = syvyys;
    }
    
    @Override
    public HakuPuu tutki(ShakkiPeli peli, ArviointiFunktio arviointi) {
        this.arviointi = arviointi;
        this.peli = peli;
        this.pysäytys = false;
        HakuPuu puu = new HakuPuu(peli.haeLauta(), peli.haePeliTila());
        haku(hakuSyvyys, puu.haeJuuri());
        return puu;
    }
    
    private double haku(int syvyys, PeliSolmu vanhempi){
        Nappula syöty = peli.haePeliTila().syötyNappula;
        if(syöty != null && syöty.tyyppi == KUNINGAS){
            return -10000;
        }else if( pysäytys  || (syvyys <= 0 && (syöty == null || syvyys <= -syöntiHakuSyvyys)) ){
            return arviointi.arvioi(peli.haeLauta(), peli.haePeliTila());
        }else{
            List<ShakkiSiirto> siirrot = SiirtojenGenerointi.haeSiirrot(peli.haeLauta(), peli.haePeliTila());
            double paras = -10000;
            
            if(laitonLinnoitus(peli, siirrot)){
                //vanhempi.lisääTagi("LL");
                return LAITON_TOISEN_SIIRTO;
            }
            
            for (ShakkiSiirto siirto : siirrot) {
                peli.teeSiirto(siirto);
               PeliSolmu solmu = new PeliSolmu(siirto, 0, vanhempi);
                double arvo = -haku(syvyys - 1, solmu);
                solmu.asetaArvo(arvo);
                if(arvo >= paras){
                    paras = arvo;
                }
                peli.peruutaSiirto();
            }
            
            return tarkistettuArvo(paras, peli);
        }
    }
    
    @Override
    public void pysäytäHaku(){
        pysäytys = true;
    }
    
}

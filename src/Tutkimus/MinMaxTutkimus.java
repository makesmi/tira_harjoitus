
package Tutkimus;


import java.sql.ResultSet;
import java.util.List;
import pelinohjaus.SiirtojenGenerointi;
import pelinydin.Nappula;
import static pelinydin.NappulaTyyppi.KUNINGAS;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;
import tekoaly.ArviointiFunktio;


public class MinMaxTutkimus implements TutkimusAlgoritmi{
    
    private final int hakuSyvyys;
    private int syöntiHakuSyvyys;

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
        HakuPuu puu = new HakuPuu(peli.haeLauta(), peli.haePeliTila());
        haku(hakuSyvyys, puu.haeJuuri());
        
        return puu;
    }
    
    private double haku(int syvyys, PeliSolmu vanhempi){
        Nappula syöty = peli.haePeliTila().syötyNappula;
        if(syöty != null && syöty.tyyppi == KUNINGAS){
            return -10000;
        }else if(syvyys <= 0 && (syöty == null || syvyys <= -syöntiHakuSyvyys) ){
            return arviointi.arvioi(peli.haeLauta(), peli.haePeliTila());
        }else{
            List<ShakkiSiirto> siirrot = SiirtojenGenerointi.haeSiirrot(peli.haeLauta(), peli.haePeliTila());
            double paras = -10000;
            
            if(laitonLinnoitus(peli, siirrot)){
                vanhempi.lisääTagi("LL");
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
    
    private boolean kuningasUhattu(ShakkiPeli peli){
        peli.teeSiirto(new ShakkiSiirto(0, 0, 0, 0));
        ShakkiLauta lauta = peli.haeLauta();
        List<ShakkiSiirto> siirrot = SiirtojenGenerointi.haeSiirrot(lauta, peli.haePeliTila());
        for (ShakkiSiirto siirto : siirrot) {
            Nappula kohde = lauta.haeRuutu(siirto.kohdeX, siirto.kohdeY);
            if(kohde != null && kohde.tyyppi == KUNINGAS){
                peli.peruutaSiirto();
                return true;
            }
        }
        peli.peruutaSiirto();
        return false;
    }
}

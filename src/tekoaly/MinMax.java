
package tekoaly;

import java.util.List;
import pelinohjaus.SiirtojenGenerointi;
import pelinohjaus.UhkausTarkistus;
import pelinydin.Nappula;
import static pelinydin.NappulaTyyppi.KUNINGAS;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;


public class MinMax implements HakuAlgoritmi{
    
    
    private final int hakuSyvyys;
    private int syöntiHakuSyvyys = 2;

    private ShakkiPeli peli;
    private ArviointiFunktio arviointi;
    private ShakkiSiirto parasSiirto;
    
    public MinMax(int syvyys){
        this.hakuSyvyys = syvyys;
        
    }
    
    public void asetaSyöntiHakuSyvyys(int syvyys){
        this.syöntiHakuSyvyys = syvyys;
    }
    
    @Override
    public ShakkiSiirto haku(ShakkiPeli peli, ArviointiFunktio arviointi) {
        this.arviointi = arviointi;
        this.peli = peli;
        parasSiirto = null;
        haku(hakuSyvyys);
        return parasSiirto;
    }
    
    private double haku(int syvyys){
        Nappula syöty = peli.haePeliTila().syötyNappula;
        if(syöty != null && syöty.tyyppi == KUNINGAS){
            return LAITON_SIIRTO;
        }else if(syvyys <= 0 && (syöty == null || syvyys <= -2)){
            return arviointi.arvioi(peli.haeLauta(), peli.haePeliTila());
        }else{
            List<ShakkiSiirto> siirrot = SiirtojenGenerointi.haeSiirrot(peli.haeLauta(), peli.haePeliTila());
            double paras = LAITON_SIIRTO;
            ShakkiSiirto valittuSiirto = null;
            
            if(laitonLinnoitus(peli, siirrot)){
                return LAITON_TOISEN_SIIRTO;
            }
            
            for (ShakkiSiirto siirto : siirrot) {
                peli.teeSiirto(siirto);
                double arvo = -haku(syvyys - 1);
                if(arvo >= paras){
                    paras = arvo;
                    valittuSiirto = siirto;
                }
                peli.peruutaSiirto();
            }
            
            parasSiirto = valittuSiirto;
            return tarkistettuArvo(paras, peli);
        }
    }    
}

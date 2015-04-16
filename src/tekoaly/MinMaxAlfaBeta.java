
package tekoaly;

import java.util.List;
import pelinohjaus.SiirtojenGenerointi;
import pelinydin.Nappula;
import static pelinydin.NappulaTyyppi.KUNINGAS;
import pelinydin.ShakkiPeli;
import pelinydin.ShakkiSiirto;
import static tekoaly.HakuAlgoritmi.LAITON_TOISEN_SIIRTO;



public class MinMaxAlfaBeta implements HakuAlgoritmi{

    private final int hakuSyvyys;
    private int syöntiHakuSyvyys = 2;

    private ShakkiPeli peli;
    private ArviointiFunktio arviointi;
    private ShakkiSiirto parasSiirto;
    
    public MinMaxAlfaBeta(int syvyys){
        hakuSyvyys = syvyys;
    }
    
    public void asetaSyöntiHakuSyvyys(int syvyys){
        this.syöntiHakuSyvyys = syvyys;
    }
    
    @Override
    public ShakkiSiirto haku(ShakkiPeli peli, ArviointiFunktio arviointi) {
        this.arviointi = arviointi;
        this.peli = peli;
        parasSiirto = null;
        haku(hakuSyvyys, -10000, 10000);
        return parasSiirto;
    }
    
    private double haku(int syvyys, double alfa, double beta){
        Nappula syöty = peli.haePeliTila().syötyNappula;
        if(syöty != null && syöty.tyyppi == KUNINGAS){
            return -10000;
        }else if(syvyys <= 0 && (syöty == null || syvyys <= -syöntiHakuSyvyys) ){
            return arviointi.arvioi(peli.haeLauta(), peli.haePeliTila());
        }else{
            List<ShakkiSiirto> siirrot = SiirtojenGenerointi.haeSiirrot(peli.haeLauta(), peli.haePeliTila());
            ShakkiSiirto valittuSiirto = null;
            
            if(laitonLinnoitus(peli, siirrot)){
                return LAITON_TOISEN_SIIRTO;
            }
            
            for (ShakkiSiirto siirto : siirrot) {
                peli.teeSiirto(siirto);
                double arvo = -haku(syvyys - 1, -beta, -alfa);
                peli.peruutaSiirto();

                if(arvo >= beta){
                    parasSiirto = siirto;
                    return beta + 1;
                }else if(arvo >= alfa){
                    valittuSiirto = siirto;
                    alfa = arvo;
                }
            }
            
            parasSiirto = valittuSiirto;
            return tarkistettuArvo(alfa, peli);
        }
    }
}

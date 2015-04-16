

package Tutkimus;

import pelinydin.Nappula;
import pelinydin.NappulaTyyppi;
import pelinydin.ShakkiSiirto;


public class Notaatio {
    public static String haeSiirtoNotaatio(ShakkiSiirto siirto, Nappula nappula, boolean syönti){
        String alku = haeNappulaTyyppiNotaatio(nappula);
        if(syönti){
            if(nappula.tyyppi == NappulaTyyppi.SOTILAS){
                alku += haeSarakeNotaatio(siirto.lähtöX);
            }
            alku += "x";
        }
        String kohdeRuutu = haeSarakeNotaatio(siirto.kohdeX) + haeRiviNotaatio(siirto.kohdeY);
        return alku + kohdeRuutu;
    }
    
    public static String haeNappulaTyyppiNotaatio(Nappula nappula){
        if(nappula == null)
            return "?";
        else if(nappula.tyyppi == NappulaTyyppi.SOTILAS)
            return "";
        else if(nappula.tyyppi == NappulaTyyppi.RATSU)
            return "N";
        else if(nappula.tyyppi == NappulaTyyppi.LÄHETTI)
            return "B";
        else if(nappula.tyyppi == NappulaTyyppi.TORNI)
            return "R";
        else if(nappula.tyyppi == NappulaTyyppi.KUNINGATAR)
            return "Q";
        else if(nappula.tyyppi == NappulaTyyppi.KUNINGAS)
            return "K";
        else
            return "";
    }
    
    public static String haeSarakeNotaatio(int x){
        return "" + (char)('a' + (char)x);
    }
    
    public static String haeRiviNotaatio(int y){
        return "" + (y + 1);
    }
}

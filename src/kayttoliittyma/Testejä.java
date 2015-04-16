

package kayttoliittyma;

import Tutkimus.HakuPuu;
import Tutkimus.HakuPuuSelaaja;
import Tutkimus.PeliSolmu;
import pelinydin.AloitusAsetelma;
import pelinydin.ShakkiSiirto;


public class Testejä {
    public static void hakuPuuTesti(PeliLauta lauta){
        HakuPuu puu = new HakuPuu(AloitusAsetelma.haeLauta(), AloitusAsetelma.haeTila());
        PeliSolmu solmu1 = new PeliSolmu(new ShakkiSiirto(4, 1, 4, 3), 3.0, puu.haeJuuri());
        PeliSolmu solmu2 = new PeliSolmu(new ShakkiSiirto(4, 6, 4, 4), -4.0, solmu1);
        new PeliSolmu(new ShakkiSiirto(6, 0, 5, 2), 3.0, solmu2);
        new HakuPuuSelaaja(lauta).asetaHakuPuu(puu);
    }
}

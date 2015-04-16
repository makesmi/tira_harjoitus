

package pelinohjaus;

import java.util.List;
import java.util.LinkedList;
import pelinydin.Linnoitus;
import pelinydin.Nappula;
import pelinydin.NappulaTyyppi;
import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;
import pelinydin.ShakkiSiirto;
import pelinydin.Väri;
import static pelinydin.NappulaTyyppi.*;




public class SiirtojenGenerointi{
    
    private static PeliTila tila;
    private static ShakkiLauta lauta;
    private static boolean siirtäjä;
    private static List<ShakkiSiirto> siirrot;
    
    public static List<ShakkiSiirto> haeSiirrot(ShakkiLauta lauta, PeliTila tila){
        siirrot = new LinkedList<>();
        SiirtojenGenerointi.lauta = lauta;
        SiirtojenGenerointi.tila = tila;
        siirtäjä = tila.vuoro();
        
        generoiSiirrot();
        poistaLaittomat();
        
        return siirrot;
    }

    private static void generoiSiirrot() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Nappula nappula = lauta.haeRuutu(x, y);
                if(nappula != null && nappula.väri == siirtäjä){
                    switch(nappula.tyyppi){
                        case SOTILAS :{
                            sotilasSiirrot(x, y);
                            break;
                        }case RATSU :{
                            ratsuSiirrot(x, y);
                            break;
                        }case LÄHETTI :{
                            lähettiSiirrot(x, y);
                            break;
                        }case TORNI :{
                            torniSiirrot(x, y);
                            break;
                        }case KUNINGATAR :{
                            kuningatarSiirrot(x, y);
                            break;
                        }case KUNINGAS :{
                            kuningasSiirrot(x, y);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    
    private static void sotilasSiirrot(int x, int y){
        int suunta = (siirtäjä == Väri.VALKOINEN) ? 1 : -1;
        boolean korotus = (y + suunta) == 0 || (y + suunta) == 7;
        NappulaTyyppi[] korotukset = { null };

        if(korotus){
            korotukset = new NappulaTyyppi[]{ RATSU, LÄHETTI, TORNI, KUNINGATAR };
        }
        
        for(NappulaTyyppi korotusTyyppi : korotukset){
            Nappula korotusNappula = (korotusTyyppi == null) ? null 
                    : new Nappula(korotusTyyppi, siirtäjä);
            
            //yksi eteenpäin
            if(lauta.haeRuutu(x, y + suunta) == null){
                siirrot.add(new ShakkiSiirto(x, y, x, y + suunta, korotusNappula));
            }
            
            //syönti oikealle
            if(x + 1 < 8 && onkoVastustajanNappula(x + 1, y + suunta)){
                siirrot.add(new ShakkiSiirto(x, y, x + 1, y + suunta, korotusNappula));
            }
            
            //syönti vasemmalle
            if(x - 1 >= 0 && onkoVastustajanNappula(x - 1, y + suunta)){
                siirrot.add(new ShakkiSiirto(x, y, x - 1, y + suunta, korotusNappula));
            }
        }
        
        //kaksi eteenpäin
        if(siirtäjä == Väri.VALKOINEN && y == 1 && lauta.haeRuutu(x, 2) == null && lauta.haeRuutu(x, 3) == null){
            siirrot.add(new ShakkiSiirto(x, 1, x, 3));
        }else if(siirtäjä == Väri.MUSTA && y == 6 && lauta.haeRuutu(x, 5) == null && lauta.haeRuutu(x, 4) == null){
            siirrot.add(new ShakkiSiirto(x, 6, x, 4));
        }
        
        //ohestalyönti
        int ohestaLyöntiLähtöY = (siirtäjä == Väri.VALKOINEN) ? 4 : 3;
        if(tila.ohestaLyöntiSarake == x - 1 && x > 0 && y == ohestaLyöntiLähtöY){
            siirrot.add(new ShakkiSiirto(x, y, x - 1, y + suunta));
        }else if(tila.ohestaLyöntiSarake == x + 1 && y == ohestaLyöntiLähtöY){
            siirrot.add(new ShakkiSiirto(x, y, x + 1, y + suunta));
        }
    }
    
    private static void ratsuSiirrot(int x, int y){
        siirrot.add(new ShakkiSiirto(x, y, x - 1, y + 2));
        siirrot.add(new ShakkiSiirto(x, y, x + 1, y + 2));
        siirrot.add(new ShakkiSiirto(x, y, x - 1, y - 2));
        siirrot.add(new ShakkiSiirto(x, y, x + 1, y - 2));
        siirrot.add(new ShakkiSiirto(x, y, x + 2, y - 1));
        siirrot.add(new ShakkiSiirto(x, y, x - 2, y - 1));
        siirrot.add(new ShakkiSiirto(x, y, x + 2, y + 1));
        siirrot.add(new ShakkiSiirto(x, y, x - 2, y + 1));
    }

    private static void lähettiSiirrot(int x, int y){
        for(int p = 1; x + p < 8 && y + p < 8; p++){
            siirrot.add(new ShakkiSiirto(x, y, x + p, y + p));
            if(lauta.haeRuutu(x + p, y + p) != null){
                break;
            }
        }
        
        for(int p = 1; x - p >= 0 && y + p < 8; p++){
            siirrot.add(new ShakkiSiirto(x, y, x - p, y + p));
            if(lauta.haeRuutu(x - p, y + p) != null){
                break;
            }
        }

        for(int p = 1; x + p < 8 && y - p >= 0; p++){
            siirrot.add(new ShakkiSiirto(x, y, x + p, y - p));
            if(lauta.haeRuutu(x + p, y - p) != null){
                break;
            }
        }

        for(int p = 1; x - p >= 0 && y - p >= 0; p++){
            siirrot.add(new ShakkiSiirto(x, y, x - p, y - p));
            if(lauta.haeRuutu(x - p, y - p) != null){
                break;
            }
        }
    }
    
    private static void torniSiirrot(int x, int y){
        for(int kohdeX = x - 1; kohdeX >= 0; kohdeX--){
            siirrot.add(new ShakkiSiirto(x, y, kohdeX, y));
            if(lauta.haeRuutu(kohdeX, y) != null){
                break;
            }
        }

        for(int kohdeX = x + 1; kohdeX < 8; kohdeX++){
            siirrot.add(new ShakkiSiirto(x, y, kohdeX, y));
            if(lauta.haeRuutu(kohdeX, y) != null){
                break;
            }
        }
        
        for(int kohdeY = y - 1; kohdeY >= 0; kohdeY--){
            siirrot.add(new ShakkiSiirto(x, y, x, kohdeY));
            if(lauta.haeRuutu(x, kohdeY) != null){
                break;
            }
        }

        for(int kohdeY = y + 1; kohdeY < 8; kohdeY++){
            siirrot.add(new ShakkiSiirto(x, y, x, kohdeY));
            if(lauta.haeRuutu(x, kohdeY) != null){
                break;
            }
        }    
    }
    
    private static void kuningatarSiirrot(int x, int y){
        lähettiSiirrot(x, y);
        torniSiirrot(x, y);
    }
    
    private static void kuningasSiirrot(int x, int y){
        siirrot.add(new ShakkiSiirto(x, y, x - 1, y));
        siirrot.add(new ShakkiSiirto(x, y, x - 1, y + 1));
        siirrot.add(new ShakkiSiirto(x, y, x,     y + 1));
        siirrot.add(new ShakkiSiirto(x, y, x + 1, y + 1));
        siirrot.add(new ShakkiSiirto(x, y, x + 1, y));
        siirrot.add(new ShakkiSiirto(x, y, x + 1, y - 1));
        siirrot.add(new ShakkiSiirto(x, y, x,     y - 1));
        siirrot.add(new ShakkiSiirto(x, y, x - 1, y - 1));
        
        //linnoitukset
        if(siirtäjä == Väri.VALKOINEN && tila.voikoLinnoittaa(Linnoitus.valkeaKuningas)){
            if(lauta.haeRuutu(5, 0) == null && lauta.haeRuutu(6, 0) == null){
                siirrot.add(new ShakkiSiirto(4, 0, 6, 0));
            }
        }else if(siirtäjä == Väri.MUSTA && tila.voikoLinnoittaa(Linnoitus.mustaKuningas)){
            if(lauta.haeRuutu(5, 7) == null && lauta.haeRuutu(6, 7) == null){
                siirrot.add(new ShakkiSiirto(4, 7, 6, 7));
            }
        }
        
        if(siirtäjä == Väri.VALKOINEN && tila.voikoLinnoittaa(Linnoitus.valkeaKuningatar)){
            if(lauta.haeRuutu(1, 0) == null && lauta.haeRuutu(2, 0) == null && lauta.haeRuutu(3, 0) == null){
                siirrot.add(new ShakkiSiirto(4, 0, 2, 0));
            }
        }else if(siirtäjä == Väri.MUSTA && tila.voikoLinnoittaa(Linnoitus.mustaKuningatar)){
            if(lauta.haeRuutu(1, 7) == null && lauta.haeRuutu(2, 7) == null && lauta.haeRuutu(3, 7) == null){
                siirrot.add(new ShakkiSiirto(4, 7, 2, 7));
            }
        }
    }
    
    private static void poistaLaittomat(){
        List<ShakkiSiirto> poistettavat = new LinkedList<>();
        
        for(ShakkiSiirto siirto : siirrot){
            if(siirto.kohdeX < 0 || siirto.kohdeX >= 8 || siirto.kohdeY < 0 || siirto.kohdeY >= 8){
                poistettavat.add(siirto);
            }else{
                Nappula nappula = lauta.haeRuutu(siirto.kohdeX, siirto.kohdeY);
                if(nappula != null && nappula.väri == siirtäjä){
                    poistettavat.add(siirto);
                }
            }
        }
        
        siirrot.removeAll(poistettavat);
    }
    
    private static boolean onkoVastustajanNappula(int x, int y){
        Nappula nappula = lauta.haeRuutu(x, y);
        
        return (nappula != null && nappula.väri != siirtäjä);
    }
}

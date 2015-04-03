

package shakkipeli;

import java.util.List;
import java.util.LinkedList;
import static shakkipeli.NappulaTyyppi.*;




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
            
            siirrot.add(new ShakkiSiirto(x, y, x, y + suunta, korotusNappula));
            if(x + 1 < 8 && onkoVastustajanNappula(x + 1, y + suunta)){
                siirrot.add(new ShakkiSiirto(x, y, x + 1, y + suunta, korotusNappula));
            }
            if(x - 1 >= 0 && onkoVastustajanNappula(x - 1, y + suunta)){
                siirrot.add(new ShakkiSiirto(x, y, x - 1, y + suunta, korotusNappula));
            }
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

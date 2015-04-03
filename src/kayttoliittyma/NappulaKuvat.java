

package kayttoliittyma;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import javax.imageio.ImageIO;
import shakkipeli.Nappula;
import shakkipeli.NappulaTyyppi;
import static shakkipeli.NappulaTyyppi.*;
import shakkipeli.Väri;



public class NappulaKuvat {
    private static HashMap<NappulaTyyppi, BufferedImage> valkoiset;
    private static HashMap<NappulaTyyppi, BufferedImage> mustat;
    
    static{
        try {
            lataaKuvat();
        } catch (Exception e) {
            throw new RuntimeException("kuvia ei voitu ladata");
        }
    }
    
    
    public static BufferedImage haeKuva(Nappula nappula){
        if(nappula.väri == Väri.VALKOINEN){
            return valkoiset.get(nappula.tyyppi);
        }else{
            return mustat.get(nappula.tyyppi);
        }
    }
    
    //lataaminen resourcesta:
    //ShakkiPeli.class.getResourceAsStream("kuvat/nappulat.png")

    
    private static void lataaKuvat() throws Exception{
        valkoiset = new HashMap<>();
        mustat = new HashMap<>();
        BufferedImage yhteisKuva = ImageIO.read(new File("kuvat/nappulat.png"));
        
        mustat.put(TORNI, yhteisKuva.getSubimage(0, 0, 100, 100));
        mustat.put(RATSU, yhteisKuva.getSubimage(100, 0, 100, 100));
        mustat.put(LÄHETTI, yhteisKuva.getSubimage(200, 0, 100, 100));
        mustat.put(KUNINGATAR, yhteisKuva.getSubimage(300, 0, 100, 100));
        mustat.put(KUNINGAS, yhteisKuva.getSubimage(400, 0, 100, 100));
        mustat.put(SOTILAS, yhteisKuva.getSubimage(500, 0, 100, 100));
        
        valkoiset.put(TORNI, yhteisKuva.getSubimage(0, 100, 100, 100));
        valkoiset.put(RATSU, yhteisKuva.getSubimage(100, 100, 100, 100));
        valkoiset.put(LÄHETTI, yhteisKuva.getSubimage(200, 100, 100, 100));
        valkoiset.put(KUNINGATAR, yhteisKuva.getSubimage(300, 100, 100, 100));
        valkoiset.put(KUNINGAS, yhteisKuva.getSubimage(400, 100, 100, 100));
        valkoiset.put(SOTILAS, yhteisKuva.getSubimage(500, 100, 100, 100));
    }
}

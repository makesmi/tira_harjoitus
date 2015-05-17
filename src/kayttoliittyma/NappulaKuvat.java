

package kayttoliittyma;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import javax.imageio.ImageIO;
import pelinydin.Nappula;
import pelinydin.NappulaTyyppi;
import static pelinydin.NappulaTyyppi.*;
import pelinydin.Väri;



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
        if(nappula == null){
            return null;
        }
        
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
        int koko = PeliLauta.ruutuKoko;
        int leveys = koko * 6;
        int korkeus = koko * 2;
        Image skaalattu = yhteisKuva.getScaledInstance(leveys, korkeus, BufferedImage.SCALE_SMOOTH);
        yhteisKuva = new BufferedImage(leveys, korkeus, BufferedImage.TYPE_INT_ARGB);
        yhteisKuva.createGraphics().drawImage(skaalattu, 0, 0, null);
        
        mustat.put(TORNI, yhteisKuva.getSubimage(4*koko, koko, koko, koko));
        mustat.put(RATSU, yhteisKuva.getSubimage(3*koko, koko, koko, koko));
        mustat.put(LÄHETTI, yhteisKuva.getSubimage(2*koko, koko, koko, koko));
        mustat.put(KUNINGATAR, yhteisKuva.getSubimage(koko, koko, koko, koko));
        mustat.put(KUNINGAS, yhteisKuva.getSubimage(0, koko, koko, koko));
        mustat.put(SOTILAS, yhteisKuva.getSubimage(5*koko, koko, koko, koko));
        
        valkoiset.put(TORNI, yhteisKuva.getSubimage(4*koko, 0, koko, koko));
        valkoiset.put(RATSU, yhteisKuva.getSubimage(3*koko, 0, koko, koko));
        valkoiset.put(LÄHETTI, yhteisKuva.getSubimage(2*koko, 0, koko, koko));
        valkoiset.put(KUNINGATAR, yhteisKuva.getSubimage(koko, 0, koko, koko));
        valkoiset.put(KUNINGAS, yhteisKuva.getSubimage(0, 0, koko, koko));
        valkoiset.put(SOTILAS, yhteisKuva.getSubimage(5*koko, 0, koko, koko));
    }
}


package pelinydin;

import static pelinydin.NappulaTyyppi.*;
import static pelinydin.Väri.*;


/**
 * Tämän avulla voidaan shakkilauta ja pelitila asettaa vastaamaan shakkipelin virallista alkuasetelmaa ja tilannetta.
 * @author markumus
 */

public class AloitusAsetelma {
    
    /**
     * Palauttaa laudan, jossa nappulat on aseteltu ruutuihin shakkipelin aloitusasetelman mukaan.
     * @return 
     */
    
    public static ShakkiLauta haeLauta(){
        ShakkiLauta lauta = new ShakkiLauta();
        
        NappulaTyyppi upseeriRivi[] = 
            { TORNI, RATSU, LÄHETTI, KUNINGATAR, KUNINGAS, LÄHETTI, RATSU, TORNI };
        
        for(int x = 0; x < 8; x++){
            Nappula valkoinenUpseeri = new Nappula(upseeriRivi[x], VALKOINEN);
            Nappula valkoinenSotilas = new Nappula(SOTILAS, VALKOINEN);
            Nappula mustaSotilas = new Nappula(SOTILAS, MUSTA);
            Nappula mustaUpseeri = new Nappula(upseeriRivi[x], MUSTA);
                    
            lauta.asetaRuutu(x, 0, valkoinenUpseeri);
            lauta.asetaRuutu(x, 1, valkoinenSotilas);
            lauta.asetaRuutu(x, 6, mustaSotilas);
            lauta.asetaRuutu(x, 7, mustaUpseeri);
        }
        
        return lauta;
    }
    
    /**
     * Palauttaa shakin alkutilannetta vastaavan pelitilan.
     * @return Pelissä ei ole vielä ollut siirtoja, joten edellinen siirto ja syöty nappula ovat null.
     * Ohestalyöntimahdollisuutta ei ole ensimmäisellä siirrolla, joten ohestalyöntisarake on -1.
     * Edelleen korotus ja ohestalyönti ovat false.
     * Alkutilanteessa mitään linnoitukseen osallistuvia nappuloita ei ole siirretty, 
     * joten kaikki linnoitukset ovat mahdollisia.
     * Siirron numero on alussa 0.
     */
    
    public static PeliTila haeTila(){
        return new PeliTila(null, null, -1, false, false, Linnoitus.kaikki, 0);
    }
    
}


package Tutkimus;

import pelinydin.PeliTila;
import pelinydin.ShakkiLauta;


public class HakuPuu {
    private final PeliSolmu juuri;
    private final ShakkiLauta aloitusLauta;
    private final PeliTila aloitusTila;

    public HakuPuu(ShakkiLauta aloitusLauta, PeliTila aloitusTila) {
        this.aloitusLauta = new ShakkiLauta();
        this.aloitusLauta.kopioiAsetelma(aloitusLauta);
        this.aloitusTila = aloitusTila;
        this.juuri = new PeliSolmu(null, 0, null);
    }
    
    public PeliSolmu haeJuuri(){
        return juuri;
    }

    public ShakkiLauta haeAloitusLauta() {
        return aloitusLauta;
    }

    public PeliTila haeAloitusTila() {
        return aloitusTila;
    }
}

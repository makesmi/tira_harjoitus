

package Tutkimus;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import pelinydin.ShakkiSiirto;


public class PeliSolmu {
    private final ShakkiSiirto siirto;
    private double arvo;
    private String tagit;

    private final PeliSolmu vanhempi;
    private final List<PeliSolmu> lapset;
    
    public PeliSolmu(ShakkiSiirto siirto, double arvo, PeliSolmu vanhempi){
        this.siirto = siirto;
        asetaArvo(arvo);
        this.tagit = "";
        
        this.vanhempi = vanhempi;
        if(vanhempi != null){
            vanhempi.lisääLapsi(this);
        }
        lapset = new LinkedList<>();
    }
        
    public void asetaArvo(double arvo){
        this.arvo = arvo;
    }
    
    public void lisääTagi(String teksti){
        this.tagit += teksti + " ";
    }
    
    private void lisääLapsi(PeliSolmu solmu){
        lapset.add(solmu);
    }
    
    public List<PeliSolmu> haeLapset(){
        return lapset;
    }
    
    public PeliSolmu haeVanhempi(){
        return vanhempi;
    }
    
    public double haeArvo(){
        return arvo;
    }
    
    public ShakkiSiirto haeSiirto(){
        return siirto;
    }
    
    public String haeTagit(){
        return tagit;
    }
    
    /**
     * Polun ensimmäiseksi solmuksi tulee juurisolmu.
     * @return 
     */
    
    public LinkedList<PeliSolmu> haePolkuJuuresta(){
        LinkedList<PeliSolmu> polku = new LinkedList<>();
        for(PeliSolmu solmu = this; solmu != null; solmu = solmu.vanhempi){
            polku.addFirst(solmu);
        }
        
        return polku;
   }
    
    public void järjestäLapset(){
        lapset.sort(new Comparator<PeliSolmu>() {
            @Override public int compare(PeliSolmu s1, PeliSolmu s2) {
                return Double.compare(s2.haeArvo(), s1.haeArvo());
            }
        });
    }
    
    public int solmujaYhteensä(){
        int solmuja = 1;
        for (PeliSolmu lapsi : lapset) {
            solmuja += lapsi.solmujaYhteensä();
        }
        
        return solmuja;
    }
    
    private int lapsellisiaSolmuja(){
        if(!lapset.isEmpty()){
            int laskuri = 1;
            for (PeliSolmu lapsi : lapset) {
                laskuri += lapsi.lapsellisiaSolmuja();
            }
            return laskuri;
        }else{
            return 0;
        }
    }
    
    public int solmujaTasossaKeskimäärin(){
        return solmujaYhteensä() / lapsellisiaSolmuja();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;


/**
 *
 * @author 12205010
 */
public class PreRegistro {
    private Jogador jogador1;
    private Jogador jogador2;
    private boolean umJaFoiUsado = false;
    
    public PreRegistro(Jogador J1, Jogador J2){
        jogador1 = J1;
        jogador2 = J2;        
        
        umJaFoiUsado = false;
    }

    public Jogador getJogador1() {
        return jogador1;
    }

    public void setJogador1(Jogador jogador1) {
        this.jogador1 = jogador1;
    }

    public Jogador getJogador2() {
        return jogador2;
    }

    public void setJogador2(Jogador jogador2) {
        this.jogador2 = jogador2;
    }

    public boolean isUmJaFoiUsado() {
        return umJaFoiUsado;
    }

    public void setUmJaFoiUsado(boolean umJaFoiUsado) {
        this.umJaFoiUsado = umJaFoiUsado;
    }
    
}

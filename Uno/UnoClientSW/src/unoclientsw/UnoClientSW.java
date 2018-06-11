/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoclientsw;

/**
 *
 * @author pedro
 */
public class UnoClientSW {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    private static int compraCarta(int id) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.compraCarta(id);
    }

    private static int ehMinhaVez(int id) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.ehMinhaVez(id);
    }

    private static int encerraPartida(int id) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.encerraPartida(id);
    }

    private static String hello(java.lang.String name) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.hello(name);
    }

    private static int jogaCarta(int id, int posicao, int cor) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.jogaCarta(id, posicao, cor);
    }

    private static String mostraMao(int id) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.mostraMao(id);
    }

    private static String obtemCartaMesa(int id) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.obtemCartaMesa(id);
    }

    private static int obtemCorAtiva(int id) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.obtemCorAtiva(id);
    }

    private static int obtemNumCartas(int id) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.obtemNumCartas(id);
    }

    private static int obtemNumCartasBaralho(int id) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.obtemNumCartasBaralho(id);
    }

    private static int obtemNumCartasOponente(int arg0) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.obtemNumCartasOponente(arg0);
    }

    private static String obtemOponente(int id) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.obtemOponente(id);
    }

    private static int obtemPontos(int id) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.obtemPontos(id);
    }

    private static int obtemPontosOponente(int id) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.obtemPontosOponente(id);
    }

    private static int registraJogador(java.lang.String nome) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.registraJogador(nome);
    }

    private static int temPartida(int id) {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        org.me.unows.UnoWS port = service.getUnoWSPort();
        return port.temPartida(id);
    }
    
}

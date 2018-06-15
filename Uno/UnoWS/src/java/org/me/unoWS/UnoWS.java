/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.unoWS;

import classes.Baralho;
import classes.Carta;
import classes.Jogador;
import classes.Partida;
import classes.PreRegistro;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author pedro
 */
@WebService(serviceName = "UnoWS")
public class UnoWS {

    /**
     * This is a sample web service operation
     */

    private static final long serialVersionUID = -3090969950757986494L;
    private Map<Integer, Partida> Dict;
    private LinkedList<Jogador> JogadoresRegistrados;
    private LinkedList<PreRegistro> PreRegistros;
    private Jogador JogadorEmEspera;

    private int id = 0;

    
    public UnoWS(){

        Dict = new HashMap<Integer, Partida>();
        JogadoresRegistrados = new LinkedList<>();
        PreRegistros = new LinkedList<>();
        JogadorEmEspera = null;
    }
    
    //ATUALIZADO: metodo criado de acordo com a definicao do T2
    @WebMethod(operationName = "preRegistro")
    public int preRegistro(@WebParam(name = "nome1") String Nome1, @WebParam(name = "id1") int Id1, @WebParam(name = "nome2") String Nome2, @WebParam(name = "id2") int Id2){
        Jogador j1 = new Jogador(Id1, Nome1);
        Jogador j2 = new Jogador(Id2, Nome2);
        PreRegistro pre = new PreRegistro(j1, j2);
        PreRegistros.add(pre);
        /*Partida p = new Partida(j1, j2);
        System.out.println("Partida criada entre jogadores " + j1.getUsuario() + " e " + j2.getUsuario());
        Dict.put(j1.getId(), p);
        Dict.put(j2.getId(), p);
        if(Id1 > id) id = Id1;
        if(Id2 > id) id = Id2;
        */
        return 0;
    }
    @WebMethod(operationName = "registraJogador")
    public int registraJogador(@WebParam(name = "nome") String Nome){
        if (JogadoresRegistrados.size() >= 1000) {
            return -2; //maximo de jog. alcancados
        }
        boolean existe = false;
        boolean existePreRegistro = false;
        boolean preRegistroIsJ2 = false;
        //percorre a lista verificando se ja existe jogador registrado com esse usuario
        if (JogadoresRegistrados.size() > 0) {
            for (int i = 0; i < JogadoresRegistrados.size(); i++) {
                if (JogadoresRegistrados.get(i).getUsuario().equals(Nome)) {
                    existe = true;
                }
            }
        }
        //ATUALIZADO: vendo se os jogadores nao estao no pre-registros
        if (PreRegistros.size() > 0) {
            for (int i = 0; i < PreRegistros.size(); i++) {
                if (PreRegistros.get(i) != null) {
                    PreRegistro pre = PreRegistros.get(i);
                    if(pre.getJogador1() != null && pre.getJogador1().getUsuario().equals(Nome)){                        
                        Jogador j = pre.getJogador1();
                        System.out.println("Jogador " + j.getUsuario() + " registrado.");
                        Partida p = new Partida(j, null);
                        Dict.put(j.getId(), p);
                        JogadoresRegistrados.add(j);
                        return j.getId();                        
                    }
                    if(pre.getJogador2() != null && pre.getJogador2().getUsuario().equals(Nome)){
                        Jogador j = pre.getJogador2();
                        System.out.println("Jogador " + j.getUsuario() + " registrado.");
                        Partida p = Dict.get(pre.getJogador1().getId());
                        p.setJ2(j);
                        Dict.put(j.getId(), p);
                        JogadoresRegistrados.add(j);
                        PreRegistros.remove(i);
                        return j.getId();
                    }
                }
            }
        }

        //se nao existe, cria o jogador, e se tem jogador em espera, comeca uma partida com ambos
        //se nao tem jogador em espera, coloca o jogador recem criado em espera
        if (!existe) {
            Jogador j = new Jogador(id++, Nome);
            System.out.println("Jogador " + j.getUsuario() + " registrado.");
            if (JogadorEmEspera == null) {
                JogadorEmEspera = j;
            } else {
                Partida p = new Partida(JogadorEmEspera, j);
                System.out.println("Partida criada entre jogadores " + JogadorEmEspera.getUsuario() + " e " + j.getUsuario());
                Dict.put(JogadorEmEspera.getId(), p);
                Dict.put(j.getId(), p);
                JogadorEmEspera = null;
            }
            JogadoresRegistrados.add(j);
            return j.getId();
        }

        return -1;
    }

    @WebMethod(operationName = "encerraPartida")
    public int encerraPartida(@WebParam(name = "id") int Id){
        if (JogadorEmEspera != null) {
            if (JogadorEmEspera.getId() == Id) {
                return -1;
            }
        }

        //busca a partida do jogador e finaliza ela
        if (Dict.containsKey(Id)) {
            Partida p = Dict.get(Id);
            if (p == null) {
                return -1;
            }
            p.finalizarPartida();
            return 0;
        }
        return -1;
    }

    @WebMethod(operationName = "temPartida")
    public int temPartida(@WebParam(name = "id") int Id){
        if (JogadorEmEspera != null) { // se � o jogador em espera, retorna que n�o existe partida
            if (JogadorEmEspera.getId() == Id) {
                return 0;
            }
        }

        if (Dict.containsKey(Id)) {
            //busca partida do jogador
            Partida p = Dict.get(Id);

            if (p != null) {
                //se for o J1, que � o primeiro registrado, retorna 1, se nao, 2
                if (p.getJ1().getId() == Id) {
                    return 1;
                }
                return 2;
            }
        }

        return -1;
    }

    @WebMethod(operationName = "obtemOponente")
    public String obtemOponente(@WebParam(name = "id") int Id){
        //busca a partida do jogador
        if (Dict.containsKey(Id)) {
            Partida p = Dict.get(Id);
            if (p == null) {
                return "";
            }
            //se ele for o jogador J1 retorna o usuario do J2, se nao, o usuario do J1
            if (p.getJ1().getId() == Id) {
                return p.getJ2().getUsuario();
            }
            return p.getJ1().getUsuario();
        }
        return "";
    }

    @WebMethod(operationName = "ehMinhaVez")
    public int ehMinhaVez(@WebParam(name = "id") int Id){
        if (JogadorEmEspera != null) {
            if (JogadorEmEspera.getId() == Id) {
                return -2;
            }
        }

        if (Dict.containsKey(Id)) {
            Partida p = Dict.get(Id);

            if (p == null) {
                return -1;
            }

            //se existe um vencedor, verifica quem venceu
            if (p.getVencedor() != null) {
                if (p.getVencedor().getId() == Id) {
                    return 2;
                } else {
                    return 3;
                }
            } else if (p.isEmpate()) {
                return 4;
            } else {
                if (p.isVezJ1()) { // se � a vez do J1 e o jogador � J1, retorna 1, se n�o, 0
                    if (p.getJ1().getId() == Id) {
                        return 1;
                    } else {
                        return 0;
                    }
                } else { //n�o � a vez do J1, se o jogador � o J1 vai retornar 0, se n�o, 1
                    if (p.getJ1().getId() == Id) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            }
        }

        //TODO: implementar os WO.
        return -1;
    }

    @WebMethod(operationName = "obtemNumCartasBaralho")
    public int obtemNumCartasBaralho(@WebParam(name = "id") int Id){
        if (JogadorEmEspera != null) {
            if (JogadorEmEspera.getId() == Id) {
                return -2;
            }
        }

        if (Dict.containsKey(Id)) { //busca o baralho da partida e busca o numero de cartas que contem nele
            Partida p = Dict.get(Id);
            if (p == null) {
                return -1;
            }
            Baralho b = p.getBar();
            if (b == null) {
                return -1;
            }
            return b.getNumeroCartas();
        }

        return -1;
    }

    @WebMethod(operationName = "obtemNumCartas")
    public int obtemNumCartas(@WebParam(name = "id") int Id){
        if (JogadorEmEspera != null) {
            if (JogadorEmEspera.getId() == Id) {
                return -2;
            }
        }

        if (Dict.containsKey(Id)) { //busca a partida do jogador
            Partida p = Dict.get(Id);
            if (p == null) {
                return -1;
            }
            Jogador aux; //variavel auxiliar
            if (p.getJ1().getId() == Id) {
                aux = p.getJ1();
                return aux.getMao().size(); //retorna o numero de cartas na mao se ele for o j1
            } else {
                aux = p.getJ2();
                return aux.getMao().size(); //retorna o numero de cartas na mao se ele for o j2
            }
        }

        return -1;
    }

    @WebMethod(operationName = "obtemNumCartasOponente")
    public int obtemNumCartasOponente(int Id) throws RemoteException {
        //essa fun��o � igual a obtemNumCartas, mas retorna as cartas do oponente
        if (JogadorEmEspera != null) {
            if (JogadorEmEspera.getId() == Id) {
                return -2;
            }
        }

        if (Dict.containsKey(Id)) {
            Partida p = Dict.get(Id);
            if (p == null) {
                return -1;
            }
            Jogador aux;
            if (p.getJ1().getId() == Id) {
                aux = p.getJ2();
                return aux.getMao().size();
            } else {
                aux = p.getJ1();
                return aux.getMao().size();
            }
        }

        return -1;
    }

    @WebMethod(operationName = "mostraMao")
    public String mostraMao(@WebParam(name = "id") int Id){
        if (JogadorEmEspera != null) {
            if (JogadorEmEspera.getId() == Id) {
                return "";
            }
        }

        if (Dict.containsKey(Id)) {
            LinkedList<Carta> mao; //variavel para a m�o do jogador
            String aux = ""; //variavel auxiliar que vai ser o retorno
            Partida p = Dict.get(Id);
            if (p == null) {
                return "";
            }
            if (p.getJ1().getId() == Id) { //se o ID for do J1, � a m�o dele que vamos mostrar, se n�o a do J2
                mao = p.getJ1().getMao();
            } else {
                mao = p.getJ2().getMao();
            }

            Carta c = null; //variavel auxiliar para a carta, para percorrer a m�o do jogador

            //for que vai da primeira carta at� a �ltima na m�o do jogador
            for (int i = 0; i < mao.size(); i++) {
                c = mao.get(i); //pega a carta em i
                if (c.equals(mao.getLast())) { //se for a ultima, entra nessa rotina que � igual a de baixo, mas sem o "|" no final.
                    for (int j = 0; j < c.getNumeracao().length; j++) { //percorre a lista de caracteres da parte "numerica" da carta
                        aux = aux + c.getNumeracao()[j];
                    }
                    aux = aux + '/';
                    for (int j = 0; j < c.getCor().length; j++) { //percorre a lista de caracteres da parte da cor da carta
                        aux = aux + c.getCor()[j];
                    }
                } else {
                    for (int j = 0; j < c.getNumeracao().length; j++) {
                        aux = aux + c.getNumeracao()[j];
                    }
                    aux = aux + '/';
                    for (int j = 0; j < c.getCor().length; j++) {
                        aux = aux + c.getCor()[j];
                    }
                    aux = aux + '|'; //adiciona o "|" que serve como divis�o entre as cartas
                }
            }

            return aux;
        }

        return "";
    }

    @WebMethod(operationName = "obtemCartaMesa")
    public String obtemCartaMesa(@WebParam(name = "id") int Id){
        if (JogadorEmEspera != null) {
            if (JogadorEmEspera.getId() == Id) {
                return "";
            }
        }

        if (Dict.containsKey(Id)) {
            Partida p = Dict.get(Id);
            if (p == null) {
                return "";
            }

            if (p.getDescartes().size() == 0) {
                return "";
            }
            //obtem o size-1(ultima posicao) dos descartes, ou seja, a ultima carta
            Carta c = p.getDescartes().get(p.getDescartes().size() - 1);

            //variavel auxiliar pra armazenar as informa��es das cartas e retornar no final
            String aux = "";

            //pega a parte "numerica" da carta
            for (int j = 0; j < c.getNumeracao().length; j++) {
                aux = aux + c.getNumeracao()[j];
            }
            aux = aux + '/';
            //pega a parte da cor da carta
            for (int j = 0; j < c.getCor().length; j++) {
                aux = aux + c.getCor()[j];
            }
            return aux;
        }
        return "";
    }

    @WebMethod(operationName = "obtemCorAtiva")
    public int obtemCorAtiva(@WebParam(name = "id") int Id){
        if (JogadorEmEspera != null) {
            if (JogadorEmEspera.getId() == Id) {
                return -2;
            }
        }

        if (Dict.containsKey(Id)) {
            Partida p = Dict.get(Id);
            if (p == null) {
                return -1;
            }

            if (p.getCorAtual().length > 0) {
                //pega na variavel que armazena a cor atual da partida
                char[] cor = p.getCorAtual();
                if (cor.length == 2) {
                    if (cor[0] == 'A') {
                        //se come�a com A, o segundo caracter s� pode ser Z ou M
                        if (cor[1] == 'z') {
                            return 0;
                        }
                        if (cor[1] == 'm') {
                            return 1;
                        }
                    }
                    if (cor[0] == 'V') {
                        //se come�a com V, o segundo caracter s� pode ser D ou M
                        if (cor[1] == 'd') {
                            return 2;
                        }
                        if (cor[1] == 'm') {
                            return 3;
                        }
                    }
                }
            }
        }

        return -1;
    }

    @WebMethod(operationName = "compraCarta")
    public int compraCarta(@WebParam(name = "id") int Id){
        if (JogadorEmEspera != null) {
            if (JogadorEmEspera.getId() == Id) {
                return -2;
            }
        }

        if (Dict.containsKey(Id)) {
            Partida p = Dict.get(Id);
            if (p == null) {
                return -1;
            }
            //verifica se o ID corresponde ao do jogador 1
            if (p.getJ1().getId() == Id) {
                //verifica se � a vez do J1
                if (p.isVezJ1()) {
                    //verifica se o jogador j� n�o comprou nessa rodada
                    if (!p.isJogadorJaComprou()) {
                        //busca a carta no topo do baralho
                        Carta aux = p.getBar().compraCarta();
                        //se a carta � null, � porque acabou o baralho
                        if (aux == null) {
                            //se o baralho acabou, termina a partida
                            p.finalizarPartida();
                        }
                        if (aux != null) {
                            //adiciona a carta na m�o do jogador
                            p.getJ1().getMao().add(aux);
                            //marca que o jogador j� comprou nessa rodada e n�o pode comprar mais
                            p.setJogadorJaComprou(true);
                            //marca a carta comprada para verifica��o posterior caso ele queira descarta-la
                            p.setCartaComprada(aux);
                            //ATUALIZADO: pula a jogada desse jogador após comprar a carta
                            p.setVezJ1(!p.isVezJ1());
                            return 1;
                        }
                        return -1;
                    } else {
                        //esse codigo nao eh mais usado, mas vou deixar aqui para evitar bugs
                        //se o jogador j� comprou, pula a vez dele
                        p.setVezJ1(!p.isVezJ1());
                        p.setJogadorJaComprou(false);
                    }
                }
                //ATUALIZADO: nao eh a vez do jogador, retorna -3
                else{
                    return -3;
                }
            } else {//mesma logica, mas para o J2
                if (!p.isVezJ1()) {
                    if (!p.isJogadorJaComprou()) {
                        Carta aux = p.getBar().compraCarta();
                        if (aux == null) {
                            p.finalizarPartida();
                        }
                        if (aux != null) {
                            p.getJ2().getMao().add(aux);
                            p.setJogadorJaComprou(true);
                            p.setCartaComprada(aux);
                            //ATUALIZADO: pula a jogada desse jogador após comprar a carta
                            p.setVezJ1(!p.isVezJ1());
                            return 1;
                        }
                        return -1;
                    } else {
                        //se o jogador j� comprou, pula a vez dele
                        p.setVezJ1(!p.isVezJ1());
                        p.setJogadorJaComprou(false);
                    }
                }
                //ATUALIZADO: nao eh a vez do jogador, retorna -3
                else{
                    return -3;
                }
            }

        }

        return -1;
    }

    @WebMethod(operationName = "jogaCarta")
    public int jogaCarta(@WebParam(name = "id") int Id, @WebParam(name = "posicao") int Pos, @WebParam(name = "cor") int Cor){
        if (JogadorEmEspera != null) {
            if (JogadorEmEspera.getId() == Id) {
                return -2;
            }
        }

        if (Dict.containsKey(Id)) {
            Partida p = Dict.get(Id);
            boolean trocaVez = true;
            if (p == null) {
                return -2;
            }
            if (p.getJ1().getId() == Id) { //verifica se o jogador � o J1 ou o J2
                if (p.isVezJ1()) { //verifica se � a vez do J1
                    //verifica se a posi��o informada est� de acordo com a m�o do jogador
                    if (Pos < 0 || Pos >= p.getJ1().getMao().size()) {
                        //ATUALIZADO: -4 para parametros errados.
                        return -4;
                    } else {
                        //verifica se a carta que o jogador quer descartar pode mesmo ser descartada
                        boolean jogavel = p.verificaCartaJogavel(p.getJ1().getMao().get(Pos));
                        if (!jogavel) {
                            return 0;
                        } else {
                            Carta c = p.getJ1().getMao().get(Pos);
                            //verifica se a carta � um coringa e se for, se o n�mero da cor passado por parametro est� ok
                            if (c.getCor()[0] == '*' && (Cor > 4 || Cor < 0)) {
                                //ATUALIZADO: -4 para parametros errados.
                                return -4;
                            }

                            //verifica se a carta � um compra 4, se for, percorre a m�o do jogador
                            //verificando se n�o tem otura carta que pode ser jogada no lugar do compra 4
                            //ATUALIZADO: codigo comentado, pois agora o jogador pode jogar o C4 a
                            //qualquer momento.
                            /*if (c.getNumeracao().length > 1) {
                                if (c.getNumeracao()[1] == '4') {
                                    for (int i = 0; i < p.getJ1().getMao().size(); i++) {
                                        Carta aux = p.getJ1().getMao().get(i);
                                        //se � um compra 4, ignora
                                        if (aux.getNumeracao().length > 1) {
                                            if (aux.getNumeracao()[1] == '4') {
                                                continue;
                                            }
                                        }
                                        if (p.verificaCartaJogavel(aux)) {
                                            return 0;
                                        }
                                    }
                                }
                            }*/

                            //remove a carta da m�o do jogador, adiciona nos descartes,
                            //troca a vez do jogador, armazena a cor e a numera��o atual para fins posteriores
                            p.getJ1().getMao().remove(Pos);
                            p.getDescartes().push(c);
                            p.setCorAtual(c.getCor());
                            p.setNumeroAtual(c.getNumeracao());
                            p.setJogadorJaComprou(false);

                            //agora a verificacao de cartas especiais
                            //coringa
                            if (c.getCor()[0] == '*') {
                                //+4
                                if (c.getNumeracao()[1] == '4') {
                                    p.J2Compra4();
                                    //ATUALIZADO: C4 nao pula mais a vez do proximo jogador
                                    //trocaVez = false;
                                }
                                switch (Cor) {
                                    case 0:
                                        p.setCorAtual(new char[]{'A', 'z'});
                                        p.setPrimeiraCartaCoringa(false);
                                        break;

                                    case 1:
                                        p.setCorAtual(new char[]{'A', 'm'});
                                        p.setPrimeiraCartaCoringa(false);
                                        break;

                                    case 2:
                                        p.setCorAtual(new char[]{'V', 'd'});
                                        p.setPrimeiraCartaCoringa(false);
                                        break;

                                    case 3:
                                        p.setCorAtual(new char[]{'V', 'm'});
                                        p.setPrimeiraCartaCoringa(false);
                                        break;
                                }

                            } //+2
                            else if (c.getNumeracao()[0] == '+') {
                                p.J2Compra2();
                                trocaVez = false;
                            } //pula vez ou inverte
                            else if (c.getNumeracao()[0] == 'P' || c.getNumeracao()[0] == 'I') {
                                trocaVez = false;
                            }
                            p.setPrimeiraCartaCoringa(false);
                            if (p.getJ1().getMao().size() == 0) {
                                p.finalizarPartida();
                            }

                            if (trocaVez) {
                                p.setVezJ1(!p.isVezJ1());
                            }

                            return 1;
                        }

                    }
                } else {
                    return -4;
                }
            } else if (p.getJ2().getId() == Id) { //mesma logica, mas agora com o J2
                if (!p.isVezJ1()) {
                    if (Pos < 0 || Pos >= p.getJ2().getMao().size()) {
                        //ATUALIZADO: -4 para parametros errados.
                        return -4;
                    } else {
                        boolean jogavel = p.verificaCartaJogavel(p.getJ2().getMao().get(Pos));
                        if (!jogavel) {
                            return 0;
                        } else {
                            Carta c = p.getJ2().getMao().get(Pos);
                            if (c.getCor()[0] == '*' && (Cor > 4 || Cor < 0)) {
                                //ATUALIZADO: -4 para parametros errados.
                                return -4;
                            }
                            //ATUALIZADO: retirada verificao de jogador possuir outra opcao de carta jogavel
                            /*if (c.getNumeracao().length > 1) {
                                if (c.getNumeracao()[1] == '4') {
                                    for (int i = 0; i < p.getJ2().getMao().size(); i++) {
                                        Carta aux = p.getJ2().getMao().get(i);
                                        if (aux.getNumeracao().length > 1) {
                                            if (aux.getNumeracao()[1] == '4') {
                                                continue;
                                            }
                                        }
                                        if (p.verificaCartaJogavel(aux)) {
                                            return 0;
                                        }
                                    }
                                }
                            }*/
                            p.getJ2().getMao().remove(Pos);
                            p.getDescartes().push(c);
                            p.setCorAtual(c.getCor());
                            p.setNumeroAtual(c.getNumeracao());
                            p.setJogadorJaComprou(false);

                            //agora a verificacao de cartas especiais
                            //coringa
                            if (c.getCor()[0] == '*') {
                                //+4
                                if (c.getNumeracao()[1] == '4') {
                                    p.J1Compra4();
                                    //ATUALIZADO: C4 nao pula mais a vez do proximo jogador
                                    //trocaVez = false;
                                }
                                switch (Cor) {
                                    case 0:
                                        p.setCorAtual(new char[]{'A', 'z'});
                                        p.setPrimeiraCartaCoringa(false);
                                        break;

                                    case 1:
                                        p.setCorAtual(new char[]{'A', 'm'});
                                        p.setPrimeiraCartaCoringa(false);
                                        break;

                                    case 2:
                                        p.setCorAtual(new char[]{'V', 'd'});
                                        p.setPrimeiraCartaCoringa(false);
                                        break;

                                    case 3:
                                        p.setCorAtual(new char[]{'V', 'm'});
                                        p.setPrimeiraCartaCoringa(false);
                                        break;
                                }

                            } //+2
                            else if (c.getNumeracao()[0] == '+') {
                                p.J1Compra2();
                                trocaVez = false;
                            } //pula vez
                            else if (c.getNumeracao()[0] == 'P' || c.getNumeracao()[0] == 'I') {
                                trocaVez = false;
                            }
                            p.setPrimeiraCartaCoringa(false);
                            if (p.getJ1().getMao().size() == 0) {
                                p.finalizarPartida();
                            }

                            if (trocaVez) {
                                p.setVezJ1(!p.isVezJ1());
                            }

                            return 1;
                        }
                    }
                } else {
                    return -4;
                }
            }
        } else {
            return -1;
        }

        return 0;
    }

    @WebMethod(operationName = "obtemPontos")
    public int obtemPontos(@WebParam(name = "id") int Id){
        if (JogadorEmEspera != null) {
            if (JogadorEmEspera.getId() == Id) {
                return -2;
            }
        }

        if (Dict.containsKey(Id)) {
            Partida p = Dict.get(Id);
            if (p == null) {
                return -2;
            }
            //verifica se o vencedor � null e se empate � false, a partida n�o acabou
            if (p.getVencedor() == null && p.isEmpate() == false) {
                return -3;
            }
            if (p.getJ1().getId() == Id) {
                return p.getPontuacaoJ1();
            } else {
                return p.getPontuacaoJ2();
            }
        } else {
            return -1;
        }
    }

    @WebMethod(operationName = "obtemPontosOponente")
    public int obtemPontosOponente(@WebParam(name = "id") int Id){
        if (JogadorEmEspera != null) {
            if (JogadorEmEspera.getId() == Id) {
                return -2;
            }
        }

        if (Dict.containsKey(Id)) {
            Partida p = Dict.get(Id);
            if (p == null) {
                return -2;
            }
            //verifica se o vencedor � null e se empate � false, a partida n�o acabou
            if (p.getVencedor() == null && p.isEmpate() == false) {
                return -3;
            }
            if (p.getJ1().getId() == Id) {
                return p.getPontuacaoJ2();
            } else {
                return p.getPontuacaoJ1();
            }
        } else {
            return -1;
        }

    }
}

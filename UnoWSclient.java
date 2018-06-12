/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unowsclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 *
 * @author roland
 */
public class UnoWSclient {

    static org.me.unows.UnoWS port;
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        org.me.unows.UnoWS_Service service = new org.me.unows.UnoWS_Service();
        port = service.getUnoWSPort();
        
        executaTeste("Uno-0000",false);
        // executaTeste("Uno-1000",false);

        // String[] testePar3 = {"Uno-3000","Uno-3250","Uno-3500","Uno-3750"};
        // executaTesteParalelo(testePar3);
    }
    
    private static void executaTesteParalelo(String[] rad) throws IOException {
        int numTestes = rad.length;
        Thread[] threads = new Thread[numTestes];
        for (int i=0;i<numTestes;++i) {
            String r = rad[i];
            threads[i] = new Thread() {
                @Override
                public void run() {
                    try {
                        executaTeste(r,false);
                    } catch (IOException e) {
                        e.printStackTrace(System.err);
                    }
                }
            };
        }
        for (int i=0;i<numTestes;++i)
            threads[i].start();
        for (int i=0;i<numTestes;++i) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                System.err.println("Falha ao esperar por teste ("+rad[i]+").");
                e.printStackTrace(System.err);
            }
        }
    }

    private static void executaTeste(final String rad,final boolean contagem) throws IOException {
        String inFile = rad+".in";
        FileInputStream is = new FileInputStream(new File(inFile));
        System.setIn(is);

        String outFile = rad+".out";
        FileWriter outWriter = new FileWriter(outFile);
        try (PrintWriter out = new PrintWriter(outWriter)) {
            Scanner leitura = new Scanner(System.in);
            int numOp = leitura.nextInt();
            for (int i=0;i<numOp;++i) {
                if (contagem)
                    System.out.print("\r"+rad+": "+(i+1)+"/"+numOp);
                int op = leitura.nextInt();
                String parametros = leitura.next();
                String param[] = parametros.split(":",-1);
                switch(op) {
                    case 0:
                        if (param.length!=4)
                            erro(inFile,i+1);
                        else
                            out.println(port.preRegistro(param[0],Integer.parseInt(param[1]),param[2],Integer.parseInt(param[3])));
                        break;
                    case 1:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.registraJogador(param[0]));
                        break;
                    case 2:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.encerraPartida(Integer.parseInt(param[0])));
                        break;
                    case 3:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.temPartida(Integer.parseInt(param[0])));
                        break;
                    case 4:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemOponente(Integer.parseInt(param[0])));
                        break;
                    case 5:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.ehMinhaVez(Integer.parseInt(param[0])));
                        break;
                    case 6:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemNumCartasBaralho(Integer.parseInt(param[0])));
                        break;
                    case 7:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemNumCartas(Integer.parseInt(param[0])));
                        break;
                    case 8:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemNumCartasOponente(Integer.parseInt(param[0])));
                        break;
                    case 9:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.mostraMao(Integer.parseInt(param[0])));
                        break;
                    case 10:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemCartaMesa(Integer.parseInt(param[0])));
                        break;
                    case 11:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemCorAtiva(Integer.parseInt(param[0])));
                        break;
                    case 12:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.compraCarta(Integer.parseInt(param[0])));
                        break;
                    case 13:
                        if (param.length!=3)
                            erro(inFile,i+1);
                        else
                            out.println(port.jogaCarta(Integer.parseInt(param[0]),Integer.parseInt(param[1]),Integer.parseInt(param[2])));
                        break;
                    case 14:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemPontos(Integer.parseInt(param[0])));
                        break;
                    case 15:
                        if (param.length!=1)
                            erro(inFile,i+1);
                        else
                            out.println(port.obtemPontosOponente(Integer.parseInt(param[0])));
                        break;
                    default:
                        erro(inFile,i+1);
                }
            }
            if (contagem)
                System.out.println("... terminado!");
            else
                System.out.println(rad+": "+numOp+"/"+numOp+"... terminado!");
            out.close();
            leitura.close();
        }
    }
    
    private static void erro(String arq,int operacao) {
        System.err.println("Entrada invalida: erro na operacao "+operacao+" do arquivo "+arq);
        System.exit(1);
    }
}


package classes;

import java.util.Random;
import java.util.Stack;

public class Baralho {
	private Stack<Carta> cartas;

	public Baralho() {
		cartas = new Stack<Carta>();
		
		int valor = 0;
		char[] cor = new char[2];
		Carta c;
		//percorre 4 vezes, uma pra cada cor
		for(int i = 0; i < 4; i++) {
			//verifica qual a cor das cartas de acordo com o i
			if(i == 0) {
				cor = new char[] {'A', 'z'};
			}
			else if(i == 1) {
				cor = new char[] {'A', 'm'};
			}
			else if(i == 2) {
				cor = new char[] {'V', 'd'};
			}
			else if(i == 3) {
				cor = new char[] {'V', 'm'};
			}
			
			//cria o 0 daquela cor
			c = new Carta(new char[] {'0'}, cor, valor);
			valor++;
			cartas.push(c);
			
			//cria as cartas numericas da cor duas vezes
			for(int j = 1; j < 10; j++) {		
				c = new Carta(new char[] {(char)(j+'0')}, cor, valor);
				valor++;
				cartas.push(c);
				c = new Carta(new char[] {(char)(j+'0')}, cor, valor);
				valor++;
				cartas.push(c);
			}
			
			//cria as cartas especiais
			c = new Carta(new char[] {'P', 'u'}, cor, valor);
			valor++;
			cartas.push(c);
			c = new Carta(new char[] {'P', 'u'}, cor, valor);
			valor++;
			cartas.push(c);
			c = new Carta(new char[] {'I', 'n'}, cor, valor);
			valor++;
			cartas.push(c);
			c = new Carta(new char[] {'I', 'n'}, cor, valor);
			valor++;
			cartas.push(c);
			c = new Carta(new char[] {'+', '2'}, cor, valor);
			valor++;
			cartas.push(c);
			c = new Carta(new char[] {'+', '2'}, cor, valor);
			valor++;
			cartas.push(c);
		}
		
		//cria coringas e +4
		c = new Carta(new char[] {'C', 'g'}, new char[] {'*'}, valor);
		valor++;
		cartas.push(c);
		c = new Carta(new char[] {'C', 'g'}, new char[] {'*'}, valor);
		valor++;
		cartas.push(c);
		c = new Carta(new char[] {'C', 'g'}, new char[] {'*'}, valor);
		valor++;
		cartas.push(c);
		c = new Carta(new char[] {'C', 'g'}, new char[] {'*'}, valor);
		valor++;
		cartas.push(c);
		c = new Carta(new char[] {'C', '4'}, new char[] {'*'}, valor);
		valor++;
		cartas.push(c);
		c = new Carta(new char[] {'C', '4'}, new char[] {'*'}, valor);
		valor++;
		cartas.push(c);
		c = new Carta(new char[] {'C', '4'}, new char[] {'*'}, valor);
		valor++;
		cartas.push(c);
		c = new Carta(new char[] {'C', '4'}, new char[] {'*'}, valor);
		valor++;
		cartas.push(c);
		
	}
	
	public Carta compraCarta() {
		if(!cartas.empty()) {
			return cartas.pop();
		}
		return null;
	}
	
	public void embaralhar(int jogador1, int jogador2) {
		Random gerador = new Random(jogador1+jogador2);
		int totalCartas = 108;
		// Embaralhamento
		for(int c=0;c<totalCartas;++c) { 
		    int outra = gerador.nextInt(totalCartas); 
		    //int aux = baralho[c]; virou:
		    Carta aux = cartas.get(c); 
		    //cartas[c] = cartas.[outra]; virou:
		    cartas.set(c, cartas.get(outra)); 
		    //cartas[outra] = aux; virou:
		    cartas.set(outra, aux); 
		} 
		for(int c=0;c<totalCartas*totalCartas;c++) { 
		    int c1 = gerador.nextInt(totalCartas); 
		    int c2 = gerador.nextInt(totalCartas); 
		    //int aux = baralho[c1]; virou:
		    Carta aux = cartas.get(c1); 
		    //baralho[c1] = baralho[c2]; virou:
		    cartas.set(c1, cartas.get(c2)); 
		    //baralho[c2] = aux; virou:
		    cartas.set(c2, aux); 
		}
	}
	
	public int getNumeroCartas() {
		return cartas.size();
	}
	
	
	public Stack<Carta> getCartas() {
		return cartas;
	}

	public void setCartas(Stack<Carta> cartas) {
		this.cartas = cartas;
	}
}

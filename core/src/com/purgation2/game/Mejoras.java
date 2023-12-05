package com.purgation2.game;

import java.util.ArrayList;

public class Mejoras {
	private long[] mejorasJugador;
	private long[] mejorasEnemigo;
	private long[] mejorasJefe;
	private ArrayList<Long> umbral;
	private final Jugador player;
	private int mejorasTotales;
	private int mejoraActual;

	public Mejoras(Jugador jugador) {
		umbral = new ArrayList<>();
		umbral.add(50L);
		extenderUmbral();
		player = jugador;
		mejorasJugador = new long[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		mejorasEnemigo = new long[]{0,0,0,0,10};
		mejorasJefe = new long[]{0,0,0,0,1};
		mejorasTotales=0;
		mejoraActual=0;
	}

	public long[] mejorarEstadisticasJugador() {

		if (player.getPuntajeTotal() >= umbral.get(mejorasTotales)) {
			int mejorasConcedidas =0;
			for (int i = umbral.size()-1; i >= mejoraActual; i--) {
				if (player.getPuntajeTotal()>=umbral.get(i)){
					mejorasConcedidas=i+1-mejoraActual;
				}
			}

			mejorasTotales+=mejorasConcedidas;

			System.out.println("Mejora "+mejorasTotales);
			System.out.println(player.getPuntajeTotal()+"/"+ umbral.get(mejorasTotales));
			System.out.println(player.contadorKills());

			if (mejorasTotales%3==0){//cada 3 mejoras

				mejorasJugador[4] = 1; //daño critico
				mejorasJugador[5] = 100; // invencibilidad extra
				mejorasJugador[7] = 25; //velocidad de balas extra
			}

			while (mejorasConcedidas!=0){

				mejorasJugador[0] += player.vidaMaxima/3; //* vida maxima extra
				mejorasJugador[1] += 1; //+regeneracion x segundo
				mejorasJugador[2] += player.damage/3; //daño extra
				mejorasJugador[3] += 1; //probabilidad de critico (x 100)
				mejorasJugador[6] +=50; //velocidad extra
				if (mejorasTotales<=10) {
					mejorasJugador[8] += 2; // reduccion de cadencia de disparo
				}
				mejorasConcedidas--;
			}
		}else {
			mejorasJugador = new long[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		}
		if (mejorasTotales>=10){
			mejorasJugador[9] = 1;//balas perforantes
		}
		return mejorasJugador;
	}

	public long[] mejorarEstadisiticasEnemigo() {
		mejorasEnemigo[0]+=10; // vida maxima adicional
		mejorasEnemigo[1]+=5; // daño adicional

		if (player.getPuntajeTotal() >= umbral.get(mejorasTotales)) {
			if (mejorasTotales % 2 == 0) {
				mejorasEnemigo[2] += 5; //velocidad adicional
			}
		}
		mejorasEnemigo[3] += 5; //spawn adicional
		mejorasEnemigo[4]+=1; // chance de atacar adicional (x 100.000)
		return mejorasEnemigo;
	}
	public long[] mejorarEstadisticasJefe() {
		mejorasJefe[0]+= 100L *mejoraActual; // vida maxima adicional
		mejorasJefe[1]+=5; // daño adicional
		if (player.getPuntajeTotal() >= umbral.get(mejorasTotales)) {
			if (mejorasTotales % 2 == 0) {
				mejorasJefe[2] += 5; // velocidad adicional
				mejorasJefe[3] += 5; // cadencia de ataque adicional
				mejorasJefe[4] += 1; // spawn adicional
			}
		}
		return mejorasJefe;
	}
	public void extenderUmbral(){
		for (int i = 0; i < 15; i++) {
			umbral.add(umbral.get(umbral.size()-1)*3);
		}
	}
}
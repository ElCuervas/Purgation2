package com.purgation2.game;

import java.util.ArrayList;

public class Mejoras {
	private long[] mejorasJugador;
	private ArrayList<Long> ptosProximaMejoraJugador;
	private Jugador player;
	private int mejorasTotales;

	public Mejoras(Jugador jugador) {
		ptosProximaMejoraJugador= new ArrayList<>();
		ptosProximaMejoraJugador.add(50L);
		player = jugador;
		mejorasJugador = new long[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		mejorasTotales=0;
	}

	public long[] mejorarEstadisticasJugador() {
		if (player.getPuntajeTotal() >= ptosProximaMejoraJugador.get(mejorasTotales)) {
			int mejorasConcedidas = (int) (player.getPuntajeTotal() / ptosProximaMejoraJugador.get(mejorasTotales));

			for (int i = 0 ; i < mejorasConcedidas ; i++){// por cada mejora el limite se multiplica x 3
				ptosProximaMejoraJugador.add(ptosProximaMejoraJugador.get(mejorasTotales)*3);
			}

			mejorasTotales+=mejorasConcedidas;

			if (mejorasTotales%3==0){//cada 3 mejoras

				mejorasJugador[4] = 1; //daño critico
				mejorasJugador[5] = 100; // invencibilidad extra
				mejorasJugador[6] = 10; //velocidad extra
				mejorasJugador[7] = 25; //velocidad de balas extra
			}

			while (mejorasConcedidas!=0){
				System.out.println("Mejora N°"+mejorasTotales);

				mejorasJugador[0] = 10L * mejorasConcedidas; //* vida extra
				mejorasJugador[1] = 1; //+regeneracion x segundo
				mejorasJugador[2] = 10L * mejorasConcedidas; //daño extra
				mejorasJugador[3] = 1; //probabilidad de critico (x 100)
				if (mejorasTotales<=50) {
					mejorasJugador[8] = 1; // reduccion de cadencia de disparo
				}
				mejorasConcedidas--;
			}
		}
		if (mejorasTotales>=10){
			mejorasJugador[9] = 1;//balas perforantes
		}
		return mejorasJugador;
	}

	public void MejorarEstadisiticasEnemigo() {
	}

	public void MejorarEstadisticasMinions() {
	}

	public void MejorarEstadisticasJefe() {
	}

}
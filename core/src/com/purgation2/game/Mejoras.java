package com.purgation2.game;

import java.util.ArrayList;

public class Mejoras {
	private long[] mejorasJugador;
	private long[] mejorasEnemigo;
	private long[] mejorasJefe;
	private ArrayList<Long> ptosProximaMejoraJugador;
	private Jugador player;
	private int mejorasTotales;

	public Mejoras(Jugador jugador) {
		ptosProximaMejoraJugador= new ArrayList<>();
		ptosProximaMejoraJugador.add(50L);
		player = jugador;
		mejorasJugador = new long[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		mejorasEnemigo = new long[]{0,0,0,0,0};
		mejorasJefe = new long[]{0,0,0,0,0};
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

				mejorasJugador[0] = 10L * mejorasTotales; //* vida extra
				mejorasJugador[1] = 1; //+regeneracion x segundo
				mejorasJugador[2] = 10L * mejorasTotales; //daño extra
				mejorasJugador[3] = 1; //probabilidad de critico (x 100)
				if (mejorasTotales<=50) {
					mejorasJugador[8] = 1; // reduccion de cadencia de disparo
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
		mejorasEnemigo[0]+=10; // vida adicional
		mejorasEnemigo[1]+=5; // daño adicional

		if (player.getPuntajeTotal() >= ptosProximaMejoraJugador.get(mejorasTotales)) {
			if (mejorasTotales % 2 == 0) {
				mejorasEnemigo[2] += 5; //velocidad adicional
				mejorasEnemigo[3] += 5; //spawn adicional
			}
		}
		mejorasEnemigo[4]+=1; // chance de atacar adicional (x 100.000)
		return mejorasEnemigo;
	}
	public long[] mejorarEstadisticasJefe() {
		mejorasJefe[0]+=10; // vida adicional
		mejorasJefe[1]+=5; // daño adicional
		if (player.getPuntajeTotal() >= ptosProximaMejoraJugador.get(mejorasTotales)) {
			if (mejorasTotales % 4 == 0) {
				mejorasJefe[2] += 5; // velocidad adicional
				mejorasJefe[3] += 5; // cadencia de ataque adicional
				mejorasJefe[4] += 1; // spawn adicional
			}
		}
		return mejorasJefe;
	}


	public void mejorarEstadisticasMinions() {

	}
}
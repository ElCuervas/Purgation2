package com.purgation2.game;
public class Jugador extends Entidad {

	private double probabilidadCritico;
	private long dañoCritico;
	private double regenacion;
	private long puntajeTotal;

	public Jugador(float x, float y, float width, float height, String rutaTextura) {
		super(x, y, width, height, rutaTextura);
		this.probabilidadCritico=0;
		this.dañoCritico=2;
	}

	@Override
	public void moverse() {


	}

	@Override
	public void atacar() {

	}
}
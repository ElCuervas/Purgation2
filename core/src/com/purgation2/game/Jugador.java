package com.purgation2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class Jugador extends Entidad {

	private double probabilidadCritico;
	private long dañoCritico;
	private double regenacion;
	private long puntajeTotal;

	public Jugador(float x, float y, float width, float height, String rutaTextura) {
		super(x, y, width, height, rutaTextura);
		this.probabilidadCritico=0;
		this.dañoCritico=2;
		this.regenacion=1;
		this.puntajeTotal=0;
	}

	@Override
	public void moverse() {


	}

	@Override
	public void atacar(OrthographicCamera camera) {
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			spawnbala(touchPos.x-30, touchPos.y-30 );
		}
	}
	private void spawnbala(float targetX, float targetY) {
		float playerCenterX = x + 8;
		float playerCenterY =y + 8;
		Bala bala = new Bala(playerCenterX, playerCenterY,targetX,targetY);
	}
}
package com.purgation2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Jugador extends Entidad {

	private double probabilidadCritico;
	private long dañoCritico;
	private double regenacion;
	private long puntajeTotal;
	private Sprite sprite;

	public Jugador(float x, float y, float width, float height, String rutaTextura) {
		super(x, y, width, height, rutaTextura);
		this.probabilidadCritico=0;
		this.dañoCritico=2;
		this.regenacion=1;
		this.puntajeTotal=0;
		sprite=new Sprite(textura);
	}

	@Override
	public void moverse() {


	}

	@Override
	public Bala atacar(OrthographicCamera camera) {
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			Bala bala = new Bala(x+ 8, y+ 8,touchPos.x-30,touchPos.y-30);
			return bala;
		}
		return null;
	}



	public void controlls() {
		if (Gdx.input.isKeyPressed(21)) {
			x -= this.velocidad * Gdx.graphics.getDeltaTime();
			this.x = -1.0F;
		} else if (Gdx.input.isKeyPressed(22)) {
			x += this.velocidad * Gdx.graphics.getDeltaTime();
			this.x = 1.0F;
		}

		if (Gdx.input.isKeyPressed(20)) {
			y -= this.velocidad * Gdx.graphics.getDeltaTime();
			this.y = -1.0F;
		} else if (Gdx.input.isKeyPressed(19)) {
			y += this.velocidad * Gdx.graphics.getDeltaTime();
			this.y = 1.0F;
		}

		this.actualizarDireccion(this.x);
	}

	private void actualizarDireccion(float direccionX) {
		if (direccionX > 0.0F) {
			this.sprite.setFlip(false, false);
		} else if (direccionX < 0.0F) {
			this.sprite.setFlip(true, false);
		}

	}
}
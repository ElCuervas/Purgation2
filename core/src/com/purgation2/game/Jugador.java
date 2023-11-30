package com.purgation2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

import java.awt.*;

public class Jugador extends Entidad {

	private double probabilidadCritico;
	private long dañoCritico;
	private double regenacion;
	private long puntajeTotal;
	public Sprite sprite;

	private long flip=1;

	public Jugador(float x, float y, float width, float height, Texture image) {
		super(x, y, width, height, image);
		this.probabilidadCritico=0;
		this.dañoCritico=2;
		this.regenacion=1;
		this.puntajeTotal=0;
		sprite=new Sprite(textura);
		sprite.setSize(width, height);
	}

	@Override
	public void moverse() {


	}

	@Override
	public Bala atacar(OrthographicCamera camera) {
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			Bala bala = new Bala(hitBox.x, hitBox.y+ 8, touchPos.x-30,touchPos.y-30);
			return bala;
		}
		return null;
	}



	public void controlls() {

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			hitBox.x -= this.velocidad * Gdx.graphics.getDeltaTime();
			flip = -1;
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			hitBox.x += this.velocidad * Gdx.graphics.getDeltaTime();
			flip = 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			hitBox.y -= this.velocidad * Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			hitBox.y += this.velocidad * Gdx.graphics.getDeltaTime();
		}
		this.actualizarDireccion(flip);
	}

	private void actualizarDireccion(float direccion) {
		if (direccion ==1) {
			this.sprite.setFlip(false, false);
		} else if (direccion == -1) {
			this.sprite.setFlip(true, false);
		}

	}
}
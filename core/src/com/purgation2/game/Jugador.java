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

	public Jugador(float x, float y, float width, float height, Texture image) {
		super(x, y, width, height, image);
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
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			x -= this.velocidad * Gdx.graphics.getDeltaTime();
			this.x = -1.0F;
		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			x += this.velocidad * Gdx.graphics.getDeltaTime();
			this.x = 1.0F;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			y -= this.velocidad * Gdx.graphics.getDeltaTime();
			this.y = -1.0F;
		} else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			y += this.velocidad * Gdx.graphics.getDeltaTime();
			this.y = 1.0F;
		}
		sprite.setPosition(x,y);
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
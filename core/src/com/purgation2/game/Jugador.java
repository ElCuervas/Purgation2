package com.purgation2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Iterator;


public class Jugador extends Entidad {

	private double probabilidadCritico;
	private long dañoCritico;
	private double regenacion;
	private long puntajeTotal;
	public Sprite sprite;
	private long tiempoUltimoAtaque;
	private long cadenciaDisparo = 500;
	private ArrayList<Bala> balasJugador;
	Sound soundbala;

	private long flip=1;

	public Jugador(float x, float y, float width, float height, Texture image) {
		super(x, y, width, height, image);
		this.probabilidadCritico=0;
		this.dañoCritico=2;
		this.regenacion=1;
		this.puntajeTotal=0;
		soundbala=Gdx.audio.newSound(Gdx.files.internal("bala.wav"));
		tiempoUltimoAtaque = 0;
		sprite=new Sprite(textura);
		sprite.setSize(width, height);
		balasJugador = new ArrayList<>();
	}
	public void renderizar( SpriteBatch batch) {
		batch.draw(sprite, hitBox.x, hitBox.y, hitBox.width, hitBox.height);

		for (Bala bala : balasJugador) {
			batch.draw(bala.getSprite(), bala.x, bala.y, bala.width, bala.height);
		}

		Iterator<Bala> iter = balasJugador.iterator();
		while (iter.hasNext()) {
			Bala bala = iter.next();
			bala.actualizar(Gdx.graphics.getDeltaTime());
			if (bala.x < 0 || bala.y < 0 || bala.x > 5000 || bala.y > 5000) {
				iter.remove();
			}
		}
	}

	@Override
	public void atacar(OrthographicCamera camera, Texture balaTexture) {
		long tiempoActual = System.currentTimeMillis();
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)&& tiempoActual - tiempoUltimoAtaque > cadenciaDisparo) {
			soundbala.play();
			tiempoUltimoAtaque=tiempoActual;
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			Bala nuevaBala = new Bala(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height / 2, touchPos.x - 30, touchPos.y - 30, balaTexture);
			balasJugador.add(nuevaBala);
			nuevaBala.setVelocidad(nuevaBala.getVelocidad()+1000);
		}
	}
	@Override
	public void moverse() {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			hitBox.x -= this.velocidad * Gdx.graphics.getDeltaTime();
			flip = -1;
		} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			hitBox.x += this.velocidad * Gdx.graphics.getDeltaTime();
			flip = 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			hitBox.y -= this.velocidad * Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
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
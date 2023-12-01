package com.purgation2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Iterator;


public class Jugador extends Entidad {

	private double probabilidadCritico;
	private long dañoCritico;
	private double regenacion;
	private long puntajeTotal;
	public Sprite sprite;
	private Animation<TextureRegion> spriteAnimation;
	private Texture textureAnimation;
	float stateTime;
	private long tiempoUltimoAtaque;
	private long cadenciaDisparo = 500;
	private ArrayList<Bala> balasJugador;
	Sound soundbala;

	private long flip=1;

	public Jugador(float x, float y, float width, float height, Texture image) {
		super(x, y, width, height, image);
		this.probabilidadCritico=0;
		this.dañoCritico=2;
		daño=50;
		this.regenacion=1;
		this.puntajeTotal=0;

		soundbala=Gdx.audio.newSound(Gdx.files.internal("bala.wav"));

		tiempoUltimoAtaque = 0;
		sprite=new Sprite(textura);
		textureAnimation = new Texture(Gdx.files.internal("Player_animation.png"));
		TextureRegion[][] tmp = TextureRegion.split(textureAnimation,textureAnimation.getWidth()/4, textureAnimation.getHeight());
		TextureRegion[] animation = new TextureRegion[4];
		int index=0;
		for (int i = 0; i < 1; i++) {
			for (int j = 0; j < 4; j++) {
				animation[index++]=tmp[i][j];
			}
		}
		spriteAnimation = new Animation<>(0.2f, animation);
		stateTime=0f;

		sprite.setSize(width, height);
		balasJugador = new ArrayList<>();
	}
	public void renderizar( SpriteBatch batch) {
		sprite.setPosition(hitBox.x, hitBox.y);
		sprite.setSize(hitBox.width, hitBox.height);
		Sprite frameactual = new Sprite(spriteAnimation.getKeyFrame(stateTime, true));
		if (flip == 1) {
			frameactual.setFlip(false, false);
		} else if (flip == -1) {
			frameactual.setFlip(true, false);
		}
		sprite.setRegion(frameactual);
		stateTime+=Gdx.graphics.getDeltaTime();
		for (Bala bala : balasJugador) {
			batch.draw(bala.getSprite(), bala.x, bala.y, bala.width, bala.height);
		}
		sprite.draw(batch);

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

	public ArrayList<Bala> getBalasJugador() {
		return balasJugador;
	}

	public void removerBala(Bala bala) {
		balasJugador.remove(bala);
	}

	@Override
	public void moverse() {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			hitBox.x -= this.velocidad * Gdx.graphics.getDeltaTime();
			this.flip = -1;
		} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			hitBox.x += this.velocidad * Gdx.graphics.getDeltaTime();
			this.flip = 1;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			hitBox.y -= this.velocidad * Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			hitBox.y += this.velocidad * Gdx.graphics.getDeltaTime();
		}
	}

}
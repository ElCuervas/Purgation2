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
import java.util.Iterator;


public class Jugador extends Entidad {

	private double probabilidadCritico;
	private long dañoCritico;
	private double regenacion;
	private long tiempoInvencivilidad;
	private long puntajeTotal;
	public Sprite sprite;
	private Animation<TextureRegion> spriteAnimation;
	private Texture textureAnimation;
	float stateTime;
	private long tiempoUltimoAtaque;
	private long cadenciaDisparo = 200;
	private OrthographicCamera camaraJuego;
	Sound soundbala;

	private long flip=1;

	public Jugador(float x, float y, float width, float height, Texture image) {
		super(x, y, width, height, image);
		this.probabilidadCritico=0.1;
		this.dañoCritico=2;
		daño=10;
		this.regenacion=1;
		this.tiempoInvencivilidad=1;
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
	}
	public void renderizar( SpriteBatch batch, OrthographicCamera camera,Texture texturaBala) {

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
		for (Bala bala : balasEntidad) {
			batch.draw(bala.getSprite(), bala.x, bala.y, bala.width, bala.height);
		}
		sprite.draw(batch);

		Iterator<Bala> iter = balasEntidad.iterator();
		while (iter.hasNext()) {
			Bala bala = iter.next();
			bala.actualizar(Gdx.graphics.getDeltaTime());
			if (bala.x < 0 || bala.y < 0 || bala.x > 5000 || bala.y > 5000) {
				iter.remove();
			}
		}
		camaraJuego = camera;
		moverse();
		atacar(texturaBala);

	}

	@Override
	public void atacar( Texture balaTexture) {
		long tiempoActual = System.currentTimeMillis();
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)&& tiempoActual - tiempoUltimoAtaque > cadenciaDisparo) {
			soundbala.play();
			tiempoUltimoAtaque=tiempoActual;
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camaraJuego.unproject(touchPos);
			Bala nuevaBala = new Bala(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height / 2, touchPos.x - 30, touchPos.y - 30, balaTexture,getDaño());
			balasEntidad.add(nuevaBala);
			nuevaBala.setVelocidad(nuevaBala.getVelocidad()+1000);
		}
	}
	public void recibirDaño(Entidad enemigo) {



		Iterator<Bala> iterBalas = balasEntidad.iterator();
		while (iterBalas.hasNext()) {
			Bala proyectil = iterBalas.next();
			if (proyectil.overlaps(hitBox)) {
				vida -= enemigo.getDaño();
					iterBalas.remove();
			}
		}
	}



	@Override
	public long getDaño() {
		return dañoJugador();
	}

	private long dañoJugador() {
		double probabilidad = Math.random();
		if (probabilidad < probabilidadCritico) {
			return (super.getDaño() * dañoCritico);
		} else {
			return super.getDaño();
		}
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
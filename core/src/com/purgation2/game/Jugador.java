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
	private long tiempoInvencivilidad;
	private long tiempoUltimoDaño=1000;
	private long tiempoUltimoDash;
	private long tiempoDash=400;
	private long puntajeTotal;
	public Sprite sprite;
	private Animation<TextureRegion> spriteAnimation;
	private Animation<TextureRegion> spriteAnimationStatic;
	private float tiempoParpadeo;
	private Texture textureAnimation;
	float stateTime;
	private long tiempoUltimoAtaque;
	private long cadenciaDisparo = 200;
	private OrthographicCamera camaraJuego;
	private ArrayList<Bala> balasJugador;
	Sound soundbala;
	Sound dashsound;
	private long flip=1;
	private long dashkey=0;

	public Jugador(float x, float y, float width, float height, Texture image) {
		super(x, y, width, height);
		this.probabilidadCritico=0.1;
		this.dañoCritico=2;
		this.daño=20;
		this.regenacion=1;
		this.tiempoInvencivilidad=2000;
		this.puntajeTotal=0;

		soundbala=Gdx.audio.newSound(Gdx.files.internal("bala.wav"));
		dashsound=Gdx.audio.newSound(Gdx.files.internal("dash.ogg"));
		dashsound.setVolume(1,4f);

		tiempoUltimoAtaque = 0;
		tiempoUltimoDash = 0;
		sprite=new Sprite(image);
		textureAnimation=image;
		TextureRegion[][] tmp = TextureRegion.split(textureAnimation,textureAnimation.getWidth()/6, textureAnimation.getHeight());
		TextureRegion[] animation = new TextureRegion[4];
		TextureRegion[] animationStatic = new TextureRegion[2];
		int index=0;
		for (int i = 0; i < 4; i++) {
			animation[index++]=tmp[0][i];
		}
		int index2=0;
		for (int i = 4; i < 6; i++) {
			animationStatic[index2++]=tmp[0][i];
		}
		spriteAnimation = new Animation<>(0.15f, animation);
		spriteAnimationStatic = new Animation<>(0.4f,animationStatic);
		stateTime=0f;

		sprite.setSize(width, height);
	}
	public void renderizar( SpriteBatch batch, OrthographicCamera camera,Texture texturaBala) {

		sprite.setPosition(hitBox.x, hitBox.y);
		sprite.setSize(hitBox.width, hitBox.height);
		stateTime+=Gdx.graphics.getDeltaTime();
		for (Bala bala : balasEntidad) {
			batch.draw(bala.getSprite(), bala.x, bala.y, bala.width, bala.height);
		}
		sprite.draw(batch);
		barravida.dibujarBarraVida(batch);

		if(esInvecible()){
			pestañeo();
		}

		Iterator<Bala> iter = balasEntidad.iterator();
		while (iter.hasNext()) {
			Bala bala = iter.next();
			bala.actualizar(Gdx.graphics.getDeltaTime());
			if (bala.x < 0 || bala.y < 0 || bala.x > 5000 || bala.y > 5000) {
				iter.remove();
			}
		}
		camaraJuego = camera;
		dash();
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
	@Override
	public void recibirDaño(Entidad enemigo) {
		if(enemigo.hitBox.overlaps(hitBox) && !esInvecible()){
			vida-=enemigo.getDaño();
			tiempoUltimoDaño=System.currentTimeMillis();
		}
		Iterator<Bala> iterBalas = enemigo.balasEntidad.iterator();
		while (iterBalas.hasNext()) {
			Bala proyectil = iterBalas.next();
			if (proyectil.overlaps(hitBox)&& !esInvecible()) {
				vida -= proyectil.getDañoBala();
				tiempoUltimoDaño=System.currentTimeMillis();
				iterBalas.remove();
			}
		}
	}

	public boolean esInvecible(){
		return System.currentTimeMillis()-tiempoUltimoDaño<tiempoInvencivilidad;
	}
	private void pestañeo() {
		if (tiempoParpadeo <= 5.0f) {
			if (tiempoParpadeo % 0.5f < 0.25f) {
				sprite.setColor(1, 1, 1, 0.5f);
			} else {
				sprite.setColor(1, 1, 1, 1);
			}
			tiempoParpadeo += Gdx.graphics.getDeltaTime();
		} else {
			sprite.setColor(1, 1, 1, 1);
			tiempoParpadeo = 0;
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
		if(Gdx.input.isKeyPressed(Input.Keys.A)||Gdx.input.isKeyPressed(Input.Keys.D)||Gdx.input.isKeyPressed(Input.Keys.S)||Gdx.input.isKeyPressed(Input.Keys.W)){
			if (Gdx.input.isKeyPressed(Input.Keys.A)) {
				hitBox.x -= this.velocidad * Gdx.graphics.getDeltaTime();
				this.flip = -1;
				this.dashkey=-1;
			} else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
				hitBox.x += this.velocidad * Gdx.graphics.getDeltaTime();
				this.flip = 1;
				this.dashkey=1;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.S)) {
				hitBox.y -= this.velocidad * Gdx.graphics.getDeltaTime();
				this.dashkey=-2;
			} else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
				hitBox.y += this.velocidad * Gdx.graphics.getDeltaTime();
				this.dashkey=2;
			}
			Sprite frameactual = new Sprite(spriteAnimation.getKeyFrame(stateTime, true));
			if (flip == 1) {
				frameactual.setFlip(false, false);
			} else if (flip == -1) {
				frameactual.setFlip(true, false);
			}
			sprite.setRegion(frameactual);
		}else{
			Sprite frame = new Sprite(spriteAnimationStatic.getKeyFrame(stateTime, true));
			if (flip == 1) {
				frame.setFlip(false, false);
			} else if (flip == -1) {
				frame.setFlip(true, false);
			}
			sprite.setRegion(frame);
		}

	}
	public void dash(){
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !(System.currentTimeMillis() - tiempoUltimoDash < tiempoDash)) { //dash
			//Sprite frameactual = new Sprite(spriteAnimation.getKeyFrame(stateTime, true));
			if (dashkey == 1) {
				hitBox.x += 400;
				camaraJuego.translate(400,0);

				//frameactual.setFlip(false, false);
			} else if (dashkey == -1) {
				hitBox.x -= 400;
				camaraJuego.translate(-400,0);
				//frameactual.setFlip(true, false);
			} else if (dashkey== 2 ) {
				hitBox.y += 400;
			} else if (dashkey == -2) {
				hitBox.y -= 400;
			}
			dashsound.play();
			tiempoUltimoDash=System.currentTimeMillis();
		}
	}
}
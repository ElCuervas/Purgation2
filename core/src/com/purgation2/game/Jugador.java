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
	private long[] mejorasJugador;
	private double probabilidadCritico;
	private long criticalDamage;
	private double regenacion;
	private long tiempoInvencivilidad;
	private long timeLastDamage =1000;
	private long tiempoUltimoDash;
	private long tiempoDash=400;
	private long puntajeTotal;
	public Sprite sprite;
	private Animation<TextureRegion> spriteAnimation;
	private Animation<TextureRegion> spriteAnimationStatic;
	private Animation<TextureRegion> dashAnimation;
	private Animation<TextureRegion> dashupAnimation;
	private Animation<TextureRegion> dashdownAnimation;
	private Animation<TextureRegion> balaEspecial;
	private float tiempoParpadeo;
	private Texture barratexture;
	float stateTime;
	private long tiempoUltimoAtaque;
	private long cadenciaDisparo;
	private OrthographicCamera camaraJuego;
	Sound soundbala;
	Sound soundbalaespecial;
	Sound dashsound;
	private long flip=1;
	private long dashkey=0;
	private long contadorKillsEnemigos;
	private long contadorKillsJefes;
	public Jugador(float x, float y, float width, float height, Texture image,Animation<TextureRegion> balaespecial) {
		super(x, y, width, height);
		barratexture=new Texture(Gdx.files.internal("barraplayer.png"));
		Barravida(barratexture,-1775,550,4);
		this.probabilidadCritico=0.02;
		this.criticalDamage =2;
		this.damage =20;
		this.regenacion=1;
		this.tiempoInvencivilidad=1500;
		this.cadenciaDisparo=500;
		this.puntajeTotal=0;
		this.contadorKillsEnemigos=0;
		this.contadorKillsJefes=0;
		this.balaEspecial=balaespecial;
		setVelocidad(200);
		setVelocidad(0);
		mejorasJugador = new long[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		soundbala=Gdx.audio.newSound(Gdx.files.internal("bala.wav"));
		dashsound=Gdx.audio.newSound(Gdx.files.internal("dash.ogg"));
		soundbalaespecial=Gdx.audio.newSound(Gdx.files.internal("balaespecial.mp3"));
		dashsound.setVolume(1,4f);

		tiempoUltimoAtaque = 0;
		tiempoUltimoDash = 0;
		sprite=new Sprite(image);
		TextureRegion[][] tmp = TextureRegion.split(image,image.getWidth()/14, image.getHeight());
		TextureRegion[] animation = new TextureRegion[4];
		TextureRegion[] animationStatic = new TextureRegion[2];
		TextureRegion[] dash = new TextureRegion[3];
		TextureRegion[] dashup = new TextureRegion[3];
		TextureRegion[] dashDown = new TextureRegion[3];
		int index=0;
		for (int i = 0; i < 4; i++) {
			animation[index++]=tmp[0][i];
		}
		index=0;
		for (int i = 3; i < 5; i++) {
			animationStatic[index++]=tmp[0][i];
		}
		index=0;
		for (int i = 5; i <8; i++) {
			dash[index++]=tmp[0][i];
		}
		index=0;
		for (int i = 8; i < 11; i++) {
			dashup[index++]=tmp[0][i];
		}
		index=0;
		for (int i = 11; i < 14; i++) {
			dashDown[index++]=tmp[0][i];
		}
		spriteAnimation = new Animation<>(0.15f, animation);
		spriteAnimationStatic = new Animation<>(0.4f,animationStatic);
		dashAnimation = new Animation<>(0.7f,dash);
		dashupAnimation = new Animation<>(0.7f,dashup);
		dashdownAnimation = new Animation<>(0.7f,dashDown);
		stateTime=0f;

		sprite.setSize(width, height);
	}
	public void mejorarEstadisticas( long[] estadisticasAdicionales) {
		mejorasJugador=estadisticasAdicionales;
		setVidaMaxima(mejorasJugador[0]);
		this.vida +=mejorasJugador[0];
		this.regenacion+=mejorasJugador[1];
		this.damage +=mejorasJugador[2];
		this.probabilidadCritico+= (double) mejorasJugador[3] /100;
		this.criticalDamage +=mejorasJugador[4];
		this.tiempoInvencivilidad+=mejorasJugador[5];
		this.velocidad+=mejorasJugador[6];
		this.cadenciaDisparo-= mejorasJugador[8];
	}
	public void renderizar( SpriteBatch batch, OrthographicCamera camera,Texture texturaBala) {

		sprite.setPosition(hitBox.x, hitBox.y);
		sprite.setSize(hitBox.width, hitBox.height);
		stateTime+=Gdx.graphics.getDeltaTime();
		Sprite balaactual = new Sprite(balaEspecial.getKeyFrame(stateTime, true));
		for (Bala bala : balasEntidad) {
			if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
				bala.setPerforante(true);
				bala.sprite.setRegion(balaactual);
				float balaW=bala.width*3;
				float balaH=bala.height*3;
				batch.draw(bala.sprite, bala.x, bala.y, balaW, balaH);
			}else{
				float balaW=bala.width;
				float balaH=bala.height;
				bala.setPerforante(false);
				batch.draw(bala.sprite, bala.x, bala.y, balaW, balaH);
			}
		}
		sprite.draw(batch);
		barravida.dibujarBarraVida(batch);

		if(esInvecible()){
			titilar();
		}else{
			sprite.setColor(1,1,1,1);
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
		moverse();
		atacar(texturaBala);
	}

	@Override
	public void atacar( Texture balaTexture) {
		long tiempoActual = System.currentTimeMillis();
		if ((Gdx.input.isButtonPressed(Input.Buttons.LEFT)||Gdx.input.isButtonPressed(Input.Buttons.RIGHT))&& tiempoActual - tiempoUltimoAtaque > cadenciaDisparo) {
			if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
				soundbalaespecial.play();
			} else if ((Gdx.input.isButtonPressed(Input.Buttons.LEFT))){
				soundbala.play();
			}
			tiempoUltimoAtaque=tiempoActual;
			Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camaraJuego.unproject(touchPos);
			Bala nuevaBala = new Bala(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height / 2, touchPos.x - 30, touchPos.y - 30, balaTexture, getDamage());
			balasEntidad.add(nuevaBala);
			nuevaBala.setVelocidad(nuevaBala.getVelocidad() + 750 + mejorasJugador[7]);
		}
	}
	@Override
	public void takeDamage(Entidad enemigo) {
		if(enemigo.hitBox.overlaps(hitBox) && !esInvecible()){
			vida-=enemigo.getDamage();
			timeLastDamage =System.currentTimeMillis();
		}
		Iterator<Bala> iterBalas = enemigo.balasEntidad.iterator();
		while (iterBalas.hasNext()) {
			Bala proyectil = iterBalas.next();
			if (proyectil.overlaps(hitBox)&& !esInvecible()) {
				vida -= proyectil.getBulletDamage();
				timeLastDamage =System.currentTimeMillis();
				iterBalas.remove();
			}
		}
	}
	public void regenerarVida() {
		if (vida<vidaMaxima){
			vida+=regenacion;
		}
	}

	public boolean esInvecible(){
		return System.currentTimeMillis()- timeLastDamage <tiempoInvencivilidad;
	}
	private void titilar() {
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
	public long getDamage() {
		return playerDamage();
	}


	private long playerDamage() {
		double probabilidad = Math.random();
		if (probabilidad < probabilidadCritico) {
			return (super.getDamage() * criticalDamage);
		} else {
			return super.getDamage();
		}
	}

	public long getPuntajeTotal() {
		return puntajeTotal;
	}

	public void setPuntaje(boolean jefe) {
		if (jefe){
			puntajeTotal += 500;
			contadorKillsJefes++;
		}else {
			puntajeTotal += 10;
			contadorKillsEnemigos++;
		}

	}

	@Override
	public void moverse() {
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
			dash();
		}else{
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
	}
	public void dash(){
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !(System.currentTimeMillis() - tiempoUltimoDash < tiempoDash)) { //dash
			Sprite frameactual = new Sprite(dashAnimation.getKeyFrame(stateTime, false));
			Sprite frameactualup = new Sprite(dashupAnimation.getKeyFrame(stateTime, false));
			Sprite frameactualdown = new Sprite(dashdownAnimation.getKeyFrame(stateTime, false));
			if (dashkey == 1) {
				hitBox.x += 400;
				camaraJuego.translate(400,0);
				frameactual.setFlip(false, false);
				sprite.setRegion(frameactual);
			} else if (dashkey == -1) {
				hitBox.x -= 400;
				camaraJuego.translate(-400,0);
				frameactual.setFlip(true, false);
				sprite.setRegion(frameactual);
			} else if (dashkey== 2 ) {
				hitBox.y += 400;
				sprite.setRegion(frameactualup);
			} else if (dashkey == -2) {
				hitBox.y -= 400;
				sprite.setRegion(frameactualdown);
			}
			dashsound.play();
			tiempoUltimoDash=System.currentTimeMillis();
		}
	}
	public String contadorKills(){
		return "{Enemigos: "+contadorKillsEnemigos+", Jefes: "+contadorKillsJefes+"}";
	}


}
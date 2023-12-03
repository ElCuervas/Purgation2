package com.purgation2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Iterator;

public class Jefe extends Enemigo {
	private long delayAtaque;
	Texture cubo;
	private long tiempoUltimoAtaque=0;

	public Jefe(float x, float y, float width, float height, Jugador player,Animation<TextureRegion> animation, long vida) {
		super(x, y, width, height,player,animation,vida);
		delayAtaque = 500;
		this.hitBox.width=hitBox.width*5;
		this.hitBox.height=hitBox.height*5;
	}
	public void renderizar(SpriteBatch batch) {
		stateTime+= Gdx.graphics.getDeltaTime();
		Sprite frame = new Sprite(animation.getKeyFrame(stateTime, true));
		batch.draw(frame, hitBox.x, hitBox.y,hitBox.width, hitBox.height);
		moverse();
		barravida.dibujarBarraVida(batch);
		for (Bala bala : balasEntidad) {
			batch.draw(bala.getSprite(), bala.x, bala.y, bala.width, bala.height);
		}
		Iterator<Bala> iter = balasEntidad.iterator();
		while (iter.hasNext()) {
			Bala bala = iter.next();
			bala.actualizar(Gdx.graphics.getDeltaTime());
			if (bala.x < 0 || bala.y < 0 || bala.x > 5000 || bala.y > 5000) {
				iter.remove();
			}
		}
	}
	@Override
	public void atacar( Texture bala) {
		long tiempoActual = System.currentTimeMillis();
		if (tiempoActual - tiempoUltimoAtaque > delayAtaque) {
				tiempoUltimoAtaque = tiempoActual;
			    Bala nuevaBala = new Bala(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height / 2, target.hitBox.x + target.hitBox.width / 2, target.hitBox.y + target.hitBox.height / 2, bala, getDaño());
			    nuevaBala.setVelocidad(nuevaBala.getVelocidad() + 500);
			    balasEntidad.add(nuevaBala);
			    target.recibirDaño(this);
		}
	}

	public void ataqueEspecial(SpriteBatch batch) {//posible adicion

	}

}
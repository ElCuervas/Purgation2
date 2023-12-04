package com.purgation2.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Bala extends Rectangle {
	private long dañoBala;
	public Sprite sprite;
	private long velocidad;
	private float angulo;
	private boolean perforante;

	public Bala(float x, float y, float targetX, float targetY,Texture bala,long daño) {
		super(x, y, 64, 64);
		sprite = new Sprite(bala);
		this.dañoBala=daño;
		this.velocidad = 1000;
		angulo = MathUtils.atan2(targetY - y, targetX - x);
		perforante=false;
	}

	public long getDañoBala() {
		return dañoBala;
	}

	public boolean isPerforante() {
		return perforante;
	}

	public void actualizar(float delta) {
		float xSpeed = velocidad * MathUtils.cos(angulo);
		float ySpeed = velocidad * MathUtils.sin(angulo);
		x += xSpeed * delta;
		y += ySpeed * delta;
	}
	public long getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(long velocidadExtra) {
		this.velocidad +=velocidadExtra;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setPerforante(boolean perforante) {
		this.perforante = perforante;
	}
}
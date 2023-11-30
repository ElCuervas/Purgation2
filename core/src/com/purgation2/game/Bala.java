package com.purgation2.game;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Bala extends Rectangle {
	public float velocidad;
	public float angulo;

	public Bala(float x, float y, float targetX, float targetY) {
		super(x, y, 64, 64);
		this.velocidad = 500;
		angulo = MathUtils.atan2(targetY - y, targetX - x);
	}

	public void actualizar(float delta) {
		float xSpeed = velocidad * MathUtils.cos(angulo);
		float ySpeed = velocidad * MathUtils.sin(angulo);

		x += xSpeed * delta;
		y += ySpeed * delta;
	}
}
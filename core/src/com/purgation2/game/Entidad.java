package com.purgation2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entidad implements movimientos {
	Rectangle hitBox;
	protected long vida;
	protected long daño;
	protected long velocidad;
	protected Texture textura;

	public Entidad(float x, float y, float width, float height,String rutaTextura) {
		hitBox=new Rectangle(x,y,width,height);
		textura = new Texture(Gdx.files.internal(rutaTextura));
		this.vida = 100;
		this.daño = 10;
		this.velocidad = 100;
	}

	public long getVida() {
		return vida;
	}

	public void setVida(long vida) {
		this.vida = vida;
	}

	public long getDaño() {
		return daño;
	}

	public void setDaño(long daño) {
		this.daño = daño;
	}

	public long getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(long velocidad) {
		this.velocidad = velocidad;
	}
}
package com.purgation2.game;

import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public abstract class Entidad implements movimientos {
	protected BarraDeVida barravida;
	Rectangle hitBox;
	protected long vida;
	protected long daño;
	protected long velocidad;
	protected ArrayList<Bala> balasEntidad;

	public Entidad(float x, float y, float width, float height,long vida) {
		hitBox=new Rectangle(x,y,width,height);
		this.vida = vida;
		this.daño = 10;
		this.velocidad = 300;
		barravida = new BarraDeVida(this);
		balasEntidad = new ArrayList<>();
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

	public void setVelocidad(long velocidadExtra) {
		this.velocidad+= velocidadExtra;
	}
}
package com.purgation2.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public abstract class Entidad implements movimientos {
	protected BarraDeVida barravida;
	protected Rectangle hitBox;
	protected long vidaMaxima;
	protected long vida;
	protected long damage;
	protected long velocidad;
	protected ArrayList<Bala> balasEntidad;

	public Entidad(float x, float y, float width, float height) {
		hitBox=new Rectangle(x,y,width,height);
		this.vidaMaxima = 100;
		this.vida = vidaMaxima;
		this.damage = 10;
		this.velocidad = 300;
		balasEntidad = new ArrayList<>();
	}
	public long getVida() {
		return vida;
	}

	public void setVidaMaxima(long vidaAdicional) {
		this.vidaMaxima +=vidaAdicional;
	}

	public long getDamage() {
		return damage;
	}

	public void setDamage(long extraDamage) {
		this.damage += extraDamage;
	}

	public void setVelocidad(long velocidadExtra) {
		this.velocidad+= velocidadExtra;
	}

	public void Barravida(Texture barravida,float modificacionX,float modificacionY,float multiplicador) {
		this.barravida = new BarraDeVida(this,barravida,modificacionX,modificacionY,multiplicador);
	}
}
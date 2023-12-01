package com.purgation2.game;

import com.badlogic.gdx.graphics.Texture;

public class Jefe extends Enemigo {
	public Jefe(float x, float y, float width, float height, Texture image, Jugador player) {
		super(x, y, width, height, image,player);
	}

	public void ataqueEspecial() {
	}

}
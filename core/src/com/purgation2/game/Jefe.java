package com.purgation2.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Jefe extends Enemigo {
	Animation<TextureRegion> animation;

	public Jefe(float x, float y, float width, float height, Jugador player,Animation<TextureRegion> animation) {
		super(x, y, width, height,player,animation);
	}

	public void ataqueEspecial(SpriteBatch batch) {

	}

}
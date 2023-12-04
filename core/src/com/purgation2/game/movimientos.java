package com.purgation2.game;

import com.badlogic.gdx.graphics.Texture;

public interface movimientos {

	void moverse();

	void atacar( Texture bala);
	void takeDamage(Entidad entidad);

}
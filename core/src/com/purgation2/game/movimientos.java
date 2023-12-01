package com.purgation2.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public interface movimientos {

	void moverse();

	void atacar(OrthographicCamera camera, Texture bala);

}
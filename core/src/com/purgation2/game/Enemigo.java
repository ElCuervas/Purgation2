package com.purgation2.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class Enemigo extends Entidad {
    public Enemigo(float x, float y, float width, float height, Texture image) {
        super(x, y, width, height, image);
    }

    @Override
    public void moverse() {

    }

    @Override
    public void atacar(OrthographicCamera camera, Texture bala) {

    }
}
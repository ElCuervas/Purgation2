package com.purgation2.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class Enemigo extends Entidad {
    public Enemigo(float x, float y, float width, float height, String rutaTextura) {
        super(x, y, width, height, rutaTextura);
    }

    @Override
    public void moverse() {

    }

    @Override
    public Bala atacar(OrthographicCamera camera) {

        return null;
    }

}
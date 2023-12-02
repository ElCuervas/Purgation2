package com.purgation2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Iterator;


public class Enemigo extends Entidad {
    private Jugador target;

    public Enemigo(float x, float y, float width, float height, Texture image, Jugador player) {
        super(x, y, width, height, image);
        this.target = player;
        this.velocidad = 300;
    }
    public void renderizar(SpriteBatch batch) {
        batch.draw(textura, hitBox.x, hitBox.y, hitBox.width*2,hitBox.height*2);
        moverse();
        getBarravida().dibujarBarraVida(batch);
    }

    @Override
    public void moverse() {
        float deltaX = target.hitBox.x - hitBox.x;
        float deltaY = target.hitBox.y - hitBox.y;

        float length = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if (length > 0) {
            deltaX /= length;
            deltaY /= length;
        }
        hitBox.x += deltaX * velocidad * Gdx.graphics.getDeltaTime();
        hitBox.y += deltaY * velocidad * Gdx.graphics.getDeltaTime();

    }

    public void recibirDaño() {
        Iterator<Bala> iterBalas = target.getBalasJugador().iterator();
        while (iterBalas.hasNext()) {
            Bala proyectil = iterBalas.next();
            if (proyectil.overlaps(hitBox)) {
                vida -= target.getDaño();
                if (!proyectil.isPerforante()) {
                    iterBalas.remove();
                }
            }
        }
    }

    @Override
    public void atacar(OrthographicCamera camera, Texture bala) {

    }
}
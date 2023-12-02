package com.purgation2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Iterator;


public class Enemigo extends Entidad {
    private double probabilidadAtaque;
    private Jugador target;

    public Enemigo(float x, float y, float width, float height, Texture image, Jugador player) {
        super(x, y, width, height, image);
        this.target = player;
        this.velocidad = 300;
        probabilidadAtaque=0;
    }
    public void renderizar(SpriteBatch batch) {
        batch.draw(textura, hitBox.x, hitBox.y, hitBox.width*2,hitBox.height*2);
        moverse();
        barravida.dibujarBarraVida(batch);

        for (Bala bala : balasEntidad) {
            batch.draw(bala.getSprite(), bala.x, bala.y, bala.width, bala.height);
        }

        Iterator<Bala> iter = balasEntidad.iterator();
        while (iter.hasNext()) {
            Bala bala = iter.next();
            bala.actualizar(Gdx.graphics.getDeltaTime());
            if (bala.x < 0 || bala.y < 0 || bala.x > 5000 || bala.y > 5000) {
                iter.remove();
            }
        }

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
    @Override
    public void recibirDaño(Entidad jugador) {
        Iterator<Bala> iterBalas = jugador.balasEntidad.iterator();
        while (iterBalas.hasNext()) {
            Bala proyectil = iterBalas.next();
            if (proyectil.overlaps(hitBox)) {
                vida -= proyectil.getDañoBala();
                if (!proyectil.isPerforante()) {
                    iterBalas.remove();
                }
            }
        }
    }

    @Override
    public void atacar( Texture bala) {
        if (chanceAtacar()) {
            Bala nuevaBala = new Bala(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height / 2, target.hitBox.x + target.hitBox.width / 2, target.hitBox.y + target.hitBox.height / 2, bala, getDaño());
            nuevaBala.setVelocidad(nuevaBala.getVelocidad() + 500);
            balasEntidad.add(nuevaBala);
        }
        target.recibirDaño(this);
    }
    private boolean chanceAtacar() {
        double probabilidad = Math.random();
        return probabilidad < probabilidadAtaque;
    }

}
package com.purgation2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Iterator;


public class Enemigo extends Entidad {
    private double probabilidadAtaque;
    protected Jugador target;
    Animation<TextureRegion> animation;
    protected float stateTime;

    public Enemigo(float x, float y, float width, float height, Jugador player, Animation<TextureRegion> animation) {
        super(x, y, width, height);
        Barravida(new Texture(Gdx.files.internal("barra vida.png")),0,0,1);
        this.animation=animation;
        this.target = player;
        probabilidadAtaque=0.0001;
        stateTime=0f;
    }
    public void renderizar(SpriteBatch batch) {
        stateTime+=Gdx.graphics.getDeltaTime();
        Sprite frame = new Sprite(animation.getKeyFrame(stateTime, true));
        batch.draw(frame, hitBox.x, hitBox.y,hitBox.width*2, hitBox.height*2);
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
    public void takeDamage(Entidad jugador) {
        Iterator<Bala> iterBalas = jugador.balasEntidad.iterator();
        while (iterBalas.hasNext()) {
            Bala proyectil = iterBalas.next();
            if (proyectil.overlaps(hitBox)&& !esInvecible()) {
                vida -= proyectil.getBulletDamage();
                if (!proyectil.isPerforante()) {
                    iterBalas.remove();
                }
            }
        }
    }
    public boolean esInvecible(){
        return System.currentTimeMillis()- timeLastDamage <tiempoInvencivilidad;
    }
    @Override
    public void atacar( Texture bala) {
        if (chanceAtacar()) {
            Bala nuevaBala = new Bala(hitBox.x + hitBox.width / 2, hitBox.y + hitBox.height / 2, target.hitBox.x + target.hitBox.width / 2, target.hitBox.y + target.hitBox.height / 2, bala, getDamage());
            balasEntidad.add(nuevaBala);
        }
        target.takeDamage(this);
    }
    private boolean chanceAtacar() {
        double probabilidad = Math.random();
        return probabilidad < probabilidadAtaque;
    }

    public void setProbabilidadAtaque(long mejoraProbabilidad) {
        this.probabilidadAtaque+= (double) mejoraProbabilidad /100000;
    }
}
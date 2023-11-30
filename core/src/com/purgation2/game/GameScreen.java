package com.purgation2.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ScreenUtils;
import sun.jvm.hotspot.utilities.Assert;

import javax.print.attribute.standard.MediaSize;
import java.util.*;
public class GameScreen implements Screen {

	final Setup game;
	OrthographicCamera camera;
	Sprite mapa;
	AssetManager asset;
	Jugador player1;
	Collection<Bala> balasJugador;
	Collection<Bala> balasJefe;
	Collection<minion> minions;
	private double[] mejorasMinion;
	private double[] mejorasJugador;
	private double[] mejorasEnemigo;
	private double[] mejorasJefe;

	public GameScreen(final Setup game){
		this.game=game;
		asset = new AssetManager();
		asset.load("mapa.png", Texture.class);
		asset.finishLoading();

		mapa = new Sprite((Texture) asset.get("mapa.png"));
		mapa.setPosition(0,0);
		mapa.setSize(1000,1000);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(50*(h/w), 50);
		camera.position.set(600, 480, 0); //x player, y player
		camera.zoom=25;
		camera.update();
	}
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);

		game.batch.begin();
		mapa.draw(game.batch);
		game.batch.end();

	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = 30f;
		camera.viewportHeight = 30f * height/width;
		camera.update();
	}
	@Override
	public void show() {

	}
	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}









	public void generarEnemigo() {
		// TODO - implement GameScreen.generarEnemigo
		throw new UnsupportedOperationException();
	}

	public void generarJefe() {
		// TODO - implement GameScreen.generarJefe
		throw new UnsupportedOperationException();
	}

	public void generarMinion() {
		// TODO - implement GameScreen.generarMinion
		throw new UnsupportedOperationException();
	}

}
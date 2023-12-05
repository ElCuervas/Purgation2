package com.purgation2.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class Menu implements Screen {
	final Setup game;

	OrthographicCamera camera;
	Texture fondo;
	Texture fondoGameOver;
	GameScreen juego;
	boolean reiniciarJuego;
	private boolean gameOverTriggered = false;
	private float gameOverTimer = 0f;
	private static final float gameOverDelay = 5f;

	public Menu(final Setup game,boolean reinicio) {
		this.game = game;
		fondo=new Texture(Gdx.files.internal("fondo.png"));
		fondoGameOver=new Texture(Gdx.files.internal("Fondo GameOver.png"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		reiniciarJuego =reinicio;
	}
	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();

		if (!reiniciarJuego){
			game.batch.draw(fondo,0,0,camera.viewportWidth,camera.viewportHeight);
			if (Gdx.input.isTouched()) {
				game.setScreen(juego = new GameScreen(game));
				dispose();
			}
		}else {
			game.batch.draw(fondoGameOver,0,0,camera.viewportWidth,camera.viewportHeight);
			if (!gameOverTriggered) {
				gameOverTimer = 0f;
				gameOverTriggered = true;
			}
			gameOverTimer += Gdx.graphics.getDeltaTime();

			if (gameOverTimer >= gameOverDelay) {
				reiniciarJuego =false;
			}
		}

		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {

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


}

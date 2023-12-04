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
	GameScreen juego;

	public Menu(final Setup game) {
		this.game = game;
		fondo=new Texture(Gdx.files.internal("fondo.png"));
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		juego = new GameScreen(game);
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
		game.batch.draw(fondo,0,0,camera.viewportWidth,camera.viewportHeight);
		game.batch.end();
		if (Gdx.input.isTouched()) {
			game.setScreen(juego);
			dispose();
		}
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

	public void iniciarJuego() {
	}
	public void cerrarJuego() {
	}

}

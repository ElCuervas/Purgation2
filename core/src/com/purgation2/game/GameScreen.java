package com.purgation2.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.*;
public class GameScreen implements Screen {
	final Setup game;
	OrthographicCamera camera;
	Sprite mapa;
	Sprite borde;
	AssetManager asset;
	Sound DeadSound;
	Sound DeadPlayer;
	Music SongMusic;
	Jugador player1;
	ArrayList<Enemigo> enemigos;
	ArrayList<Jefe> jefes;
	float stateTime;
	private long[] mejorasEnemigo = {0, 0, 0, 0, 10};
	private long[] mejorasJefe = {0, 0, 0, 0, 1};
	private final Mejoras mejoras;
	public GameScreen(final Setup game) {
		this.game = game;
		asset = new AssetManager();
		asset.load("mapa.png", Texture.class);
		asset.load("borde.png", Texture.class);
		asset.load("Player.png", Texture.class);
		asset.load("enemigo.png", Texture.class);
		asset.load("bala.png", Texture.class);
		asset.load("jefe.png", Texture.class);
		asset.load("charged.png", Texture.class);
		asset.finishLoading();
		player1 = new Jugador(2500, 2500, 64 * 3, 64 * 3, ((Texture) asset.get("Player.png")), animador((Texture) asset.get("charged.png"), 7, 0.1f, 0));
		enemigos = new ArrayList<>();
		jefes = new ArrayList<>();
		mejoras = new Mejoras(player1);

		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				generarEnemigo(animador((Texture) asset.get("enemigo.png"), 3, 0.2f, 0));
			}
		}, 5, 5);
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				generarJefe(animador((Texture) asset.get("jefe.png"), 4, 0.1f, 0));
			}
		}, 5, 10);
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				mejorasEnemigo = mejoras.mejorarEstadisiticasEnemigo();
				mejorasJefe = mejoras.mejorarEstadisticasJefe();
			}
		}, 6, 5);
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				player1.mejorarEstadisticas(mejoras.mejorarEstadisticasJugador());
				player1.regenerarVida();
			}
		}, 0, 1);

		stateTime = 0f;

		mapa = new Sprite((Texture) asset.get("mapa.png"));
		mapa.setPosition(0, 0);
		mapa.setSize(5000, 5000);

		borde = new Sprite((Texture) asset.get("borde.png"));
		borde.setPosition(-2000, -2000);
		borde.setSize(10000, 10000);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera(50 * (h / w), 50);
		camera.position.set(player1.hitBox.x + (player1.hitBox.width / 2), player1.hitBox.y + (player1.hitBox.height / 2), 0);//x player, y player
		camera.zoom = 60;
		updateCameraPosition();

		DeadSound = Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));
		DeadPlayer = Gdx.audio.newSound(Gdx.files.internal("deadplayer.mp3"));
		SongMusic = Gdx.audio.newMusic(Gdx.files.internal("songfond.mp3"));
		SongMusic.setVolume(0.4f);
		SongMusic.setLooping(true);
		SongMusic.play();
	}

	@Override
	public void render(float delta) {

		ScreenUtils.clear(0, 0, 0.2f, 1);
		handleInput();
		stateTime += Gdx.graphics.getDeltaTime();

		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		if (player1.vida <= 0) {
			SongMusic.stop();
			DeadPlayer.play();
			game.setScreen(new Menu(game,true));
		} else {
			borde.draw(game.batch);
			mapa.draw(game.batch);

			for (Enemigo enemigo : enemigos) {
				enemigo.renderizar(game.batch);
				enemigo.atacar((Texture) asset.get("bala.png"));
			}
			separacion();
			separacionjugador();
			for (Jefe jefe : jefes) {
				jefe.renderizar(game.batch);
				jefe.atacar((Texture) asset.get("bala.png"));
			}
			if (player1.vida <= 0) {
				for (Enemigo enemigo : enemigos) {
					enemigo.velocidad = 0;
				}
				for (Jefe jefe : jefes) {
					jefe.velocidad = 0;
				}
				SongMusic.stop();
				DeadPlayer.play();
			}
			player1.renderizar(game.batch, camera, (Texture) asset.get("bala.png"));//renderizado individual jugador
			limiteMapa();
			removerEnemigos();
		}
		game.batch.end();

	}

	private void removerEnemigos() {
		Iterator<Enemigo> iterEnemigos = enemigos.iterator();
		while (iterEnemigos.hasNext()) {
			Enemigo enemigoActivo = iterEnemigos.next();
			enemigoActivo.takeDamage(player1);
			if (enemigoActivo.getVida() <= 0) {
				player1.setPuntaje(false);
				DeadSound.play();
				iterEnemigos.remove();
			} else if (enemigoActivo.hitBox.x < 0 || enemigoActivo.hitBox.x > 5000 || enemigoActivo.hitBox.y < 0 || enemigoActivo.hitBox.y > 5000) {
				iterEnemigos.remove();
			}
		}

		Iterator<Jefe> iterJefes = jefes.iterator();
		while (iterJefes.hasNext()) {
			Jefe jefeVivo = iterJefes.next();
			jefeVivo.takeDamage(player1);
			if (jefeVivo.getVida() <= 0) {
				player1.setPuntaje(true);
				DeadSound.play();
				iterJefes.remove();
			}
		}
	}

	public void limiteMapa() {
		if (player1.hitBox.x < 0)
			player1.hitBox.x = 0;
		if (player1.hitBox.x > 5000 - player1.hitBox.width)
			player1.hitBox.x = 5000 - player1.hitBox.width;
		if (player1.hitBox.y < 0)
			player1.hitBox.y = 0;
		if (player1.hitBox.y > 5000 - player1.hitBox.height)
			player1.hitBox.y = 5000 - player1.hitBox.height;

		if (camera.position.x < 0)
			camera.position.x = 0;
		if (camera.position.x > 5000 - player1.hitBox.width)
			camera.position.x = 5000 - player1.hitBox.width;
		if (camera.position.y < 0)
			camera.position.y = 0;
		if (camera.position.y > 5000 - player1.hitBox.height)
			camera.position.y = 5000 - player1.hitBox.height;
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = 60f;
		camera.viewportHeight = 60f * height / width;
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
		asset.dispose();
		DeadSound.dispose();
	}

	public void generarEnemigo(Animation<TextureRegion> animation) {
		int margen = 1000;
		for (long i = 0; i < mejorasEnemigo[4]; i++) {
			Enemigo nuevoEnemigo = new Enemigo(crearCordenadaX(margen), crearCordenadaY(margen), 64 * 3, 64 * 3, player1, animation);
			nuevoEnemigo.setVidaMaxima(mejorasEnemigo[0]);
			nuevoEnemigo.setDamage(mejorasEnemigo[1]);
			nuevoEnemigo.setVelocidad(mejorasEnemigo[2]);
			nuevoEnemigo.setProbabilidadAtaque(mejorasEnemigo[3]);
			enemigos.add(nuevoEnemigo);
		}
	}

	public void generarJefe(Animation<TextureRegion> animation) {
		int margen = 1000;
		for (int i = 0; i < mejorasJefe[4]; i++) {
			Jefe jefe = new Jefe(crearCordenadaX(margen), crearCordenadaY(margen), 64 * 3, 64 * 3, player1, animation);
			jefe.setVidaMaxima(400 + mejorasJefe[0]);
			jefe.setDamage(20+mejorasJefe[1]);
			jefe.setVelocidad(mejorasJefe[2]);
			jefe.setDelayAtaque(mejorasJefe[3]);
			jefes.add(jefe);
		}
	}

	private float crearCordenadaX(int margen) {
		float spawnX;
		do {
			spawnX = MathUtils.random(0, 5000);
		} while (spawnX > camera.position.x - margen - camera.viewportWidth / 2 && spawnX < margen + camera.position.x + camera.viewportWidth / 2);
		return spawnX;
	}

	private float crearCordenadaY(int margen) {
		float spawnY;
		do {
			spawnY = MathUtils.random(0, 5000);
		} while (spawnY > camera.position.y - margen - camera.viewportHeight / 2 && spawnY < margen + camera.position.y + camera.viewportHeight / 2);
		return spawnY;
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.translate(-11, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			camera.translate(11, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			camera.translate(0, -11, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			camera.translate(0, 11, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			pause();
		} else if (Gdx.input.isKeyPressed(Input.Keys.I)) {
			resume();
		}
		camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 5000);
		camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 5000);
		updateCameraPosition();
	}

	private void updateCameraPosition() {
		boolean playerOnLeftEdge = player1.hitBox.x <= camera.viewportWidth / 2f;
		boolean playerOnRightEdge = player1.hitBox.x >= 5000 - camera.viewportWidth / 2f;
		boolean playerOnBottomEdge = player1.hitBox.y <= camera.viewportHeight / 2f;
		boolean playerOnTopEdge = player1.hitBox.y >= 5000 - camera.viewportHeight / 2f;
		if (!playerOnRightEdge && !playerOnLeftEdge) {
			camera.position.x = player1.hitBox.x + player1.hitBox.width / 2;
		}
		if (!playerOnTopEdge && !playerOnBottomEdge) {
			camera.position.y = player1.hitBox.y + player1.hitBox.height / 2;
		}
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
	}

	public Animation<TextureRegion> animador(Texture imagen, int cantframe, float fotogramas, int comienzoframe) {
		TextureRegion[][] tmp = TextureRegion.split(imagen, imagen.getWidth() / cantframe, imagen.getHeight());
		TextureRegion[] animation = new TextureRegion[cantframe];
		int index = 0;
		for (int i = comienzoframe; i < cantframe; i++) {
			animation[index++] = tmp[0][i];
		}
		return new Animation<>(fotogramas, animation);
	}
	private void separacion(){
		for (int i = 0; i < enemigos.size(); i++) {
			Enemigo enemigoA = enemigos.get(i);
			for (int j = i + 1; j < enemigos.size(); j++) {
				Enemigo enemigoB = enemigos.get(j);
				float distancia = calcularDistancia(enemigoA.hitBox.x, enemigoA.hitBox.y, enemigoB.hitBox.x, enemigoB.hitBox.y);
				float distanciaMinima = 200;
				if (distancia < distanciaMinima) {
					separarEnemigos(enemigoA, enemigoB);
				}
			}
		}
	}
	public float calcularDistancia(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	private void separarEnemigos(Enemigo enemigoA, Enemigo enemigoB) {
		float deltaX = enemigoB.hitBox.x - enemigoA.hitBox.x;
		float deltaY = enemigoB.hitBox.y - enemigoA.hitBox.y;
		float distancia = calcularDistancia(enemigoA.hitBox.x, enemigoA.hitBox.y, enemigoB.hitBox.x, enemigoB.hitBox.y);
		float distanciaSegura = 200;

		float traslacionX = (distanciaSegura - distancia) * (deltaX / distancia);
		float traslacionY = (distanciaSegura - distancia) * (deltaY / distancia);

		enemigoA.hitBox.x -= traslacionX / 2;
		enemigoA.hitBox.y -= traslacionY / 2;

		enemigoB.hitBox.x += traslacionX / 2;
		enemigoB.hitBox.y += traslacionY / 2;
	}
	private void separacionjugador(){
		for (Enemigo enemigoA : enemigos) {
			float distancia = calcularDistancia(enemigoA.hitBox.x, enemigoA.hitBox.y, player1.hitBox.x, player1.hitBox.y);
			float distanciaMinima = 250;
			if (distancia < distanciaMinima) {
				separarjugador(enemigoA);
			}
		}
	}
	private void separarjugador(Enemigo enemigoA) {
		float deltaX = player1.hitBox.x - enemigoA.hitBox.x;
		float deltaY = player1.hitBox.y - enemigoA.hitBox.y;
		float distancia = calcularDistancia(enemigoA.hitBox.x, enemigoA.hitBox.y, player1.hitBox.x, player1.hitBox.y);
		float distanciaSegura = 250;

		float traslacionX = (distanciaSegura - distancia) * (deltaX / distancia);
		float traslacionY = (distanciaSegura - distancia) * (deltaY / distancia);

		enemigoA.hitBox.x -= traslacionX / 2;
		enemigoA.hitBox.y -= traslacionY / 2;

		player1.hitBox.x += traslacionX / 2;
		player1.hitBox.y += traslacionY / 2;
	}
}
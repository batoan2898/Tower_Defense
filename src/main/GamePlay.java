package main;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GamePlay {
    private AnimationTimer timer;
    private GraphicsContext gc;
    private MediaPlayer mediaPlayer;
    private MediaPlayer music;
    private MediaPlayer build;
    private MediaPlayer normal;
    private MediaPlayer sniper;
    private MediaPlayer machine;

    private List<TowerObject>  towerObjects  = new ArrayList<>();
    private List<EnemyObject>  enemyObjects  = new ArrayList<>();
    private List<EnemyObject> temp = Run.enemy();

    private final List<Moutain> moutainsSmall = Moutain.moutainsSmall();
    private final List<Moutain> moutainsBig   = Moutain.moutainsBig();
    private List<Moutain> storeButton;

    private MainMenu mainMenu;
    private CreateTower  ct = new CreateTower();
    private CreateBullet cb = new CreateBullet();
    private Enemy e = new Enemy();
    private Run run = new Run();

    private int flagBuild = 0, flagSound = 1, flagMusic = 1, flagTime = 1;
    private int sx, sy;
    private int[] time = new int[10];
    private int timeE = 0;
    private int index = 0;
    private  int sizeOfEnemy = temp.size();

    private Stage window = new Stage();
    private Group root = new Group();
    private Image musicImg = new Image("file:src/Image/MusicOn.png");

    void start(Stage stage) {
        Arrays.fill(time, 100);

        mainMenu = new MainMenu();
        window = stage;

        Canvas canvas = new Canvas(1020, 600);
        gc = canvas.getGraphicsContext2D();


        Label hp = new Label(); Label money = new Label(); Label wave = new Label();
        hp.setText(String.valueOf(run.HP)); money.setText(String.valueOf(run.MONEY));
        wave.setText("WAVE  " + String.valueOf(index) + "/56");

        hp.setFont(Font.loadFont("file:src/Font/FSAhkioThin.otf", 25));
        money.setFont(Font.loadFont("file:src/Font/FSAhkioThin.otf", 25));
        wave.setFont(Font.loadFont("file:src/Font/FSAhkioThin.otf", 18));

        hp.setTextFill(Color.WHEAT);    money.setTextFill(Color.WHEAT); wave.setTextFill(Color.WHEAT);
        hp.setLayoutX(80); hp.setLayoutY(18);
        money.setLayoutX(155); money.setLayoutY(18);
        wave.setLayoutX(93); wave.setLayoutY(54);

        Button soundButton = new Button();   soundButton.setLayoutX(960); soundButton.setLayoutY(80);
        soundButton.setPrefSize(50, 50);    soundButton.setOpacity(0);
        soundButton.setOnMouseClicked((eventSound) -> {
            if (flagSound == 1) {
                flagSound = 0;
            } else {
                flagSound = 1;
            }
        });

        Music();
        music.play();
        Button musicButton = new Button();   musicButton.setLayoutX(960); musicButton.setLayoutY(80);
        musicButton.setPrefSize(50, 50);    musicButton.setOpacity(0);
        musicButton.setOnMouseClicked((eventMusic) -> {
            if (flagMusic == 1) {
                flagMusic = 0;
                musicImg = new Image("file:src/Image/MusicOff.png");
                music.pause();
            } else {
                flagMusic = 1;
                musicImg = new Image("file:src/Image/MusicOn.png");
                music.play();
            }
        });

        Button PauButton = new Button();
        PauButton.setLayoutX(950); PauButton.setLayoutY(10); PauButton.setPrefSize(50, 50); PauButton.setOpacity(0);
        PauButton.setOnMouseClicked((eventPause) -> {
            if (flagTime == 1) {
                timer.stop();
                music.pause();
                Image playImg = new Image("file:src/Image/Play.png");
                gc.drawImage(playImg, 960, 10);
                flagTime = 0;
            } else {
                timer.start();
                music.play();
                flagTime = 1;
            }
        });
        root.getChildren().addAll(canvas, hp, money, wave, PauButton, soundButton, musicButton);

        for (int i=0; i<moutainsSmall.size(); i++) {
            root.getChildren().addAll(moutainsBig.get(i).button, moutainsSmall.get(i).button);
            int i2 = i;
            moutainsSmall.get(i).button.setOnMouseEntered((event) -> {
                if ( moutainsSmall.get(i2).empty ) {
                    flagBuild = 1;
                    sx = moutainsSmall.get(i2).x;
                    sy = moutainsSmall.get(i2).y;

                    storeButton = Moutain.StoreButton(sx, sy);
                    root.getChildren().addAll(storeButton.get(0).button, storeButton.get(1).button, storeButton.get(2).button);

                    storeButton.get(0).button.setOnMouseClicked((event2) -> {
                        if (run.MONEY >= Config.NorT_Price) {
                            if (flagSound==1) SoundBuild();
                            towerObjects.add(ct.creNormalTower(moutainsSmall.get(i2).x, moutainsSmall.get(i2).y));
                            moutainsSmall.get(i2).empty = false;
                            run.MONEY -= Config.NorT_Price;
                            flagBuild = 0;
                        }
                    });
                    storeButton.get(1).button.setOnMouseClicked((event3) -> {
                        if (run.MONEY >= Config.SniT_Price) {
                            if (flagSound==1) SoundBuild();
                            towerObjects.add(ct.creSniperTower(moutainsSmall.get(i2).x, moutainsSmall.get(i2).y));
                            moutainsSmall.get(i2).empty = false;
                            run.MONEY -= Config.SniT_Price;
                            flagBuild = 0;
                        }
                    });
                    storeButton.get(2).button.setOnMouseClicked((event4) -> {
                        if (run.MONEY >= Config.MacT_Price) {
                            if (flagSound==1) SoundBuild();
                            towerObjects.add(ct.creMachineTower(moutainsSmall.get(i2).x, moutainsSmall.get(i2).y));
                            moutainsSmall.get(i2).empty = false;
                            run.MONEY -= Config.MacT_Price;
                            flagBuild = 0;
                        }
                    });
                }
                storeButton.clear();
            });
            moutainsBig.get(i).button.setOnMouseExited((event) -> {
                flagBuild = 0;
            });
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Tower Defense");
        stage.show();

        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                drawMap(gc);
                render();
                update();
                checkGameOver(gc);
                if (flagBuild == 1) drawStore(gc, sx, sy);

                hp.setText(String.valueOf(run.HP)); money.setText(String.valueOf(run.MONEY));
                wave.setText("WAVE  " + String.valueOf(index) + "/56");


                if (timeE >= 450 && index < sizeOfEnemy) {
                    enemyObjects.add(temp.get(index++));
                    timeE = 0;
                }

                int iE = 0;
                while (iE < enemyObjects.size()) {
                    if (enemyObjects.get(iE).health <= 0) {
                        run.MONEY += enemyObjects.get(iE).reward;
                        enemyObjects.remove(iE);
                    } else if (enemyObjects.get(iE).pass) {
                        enemyObjects.remove(iE);
                        run.HP--;
                    } else iE++;
                }


                for (int iT = 0; iT < towerObjects.size(); iT++) {
                    iE = 0;
                    if (time[iT] > towerObjects.get(iT).spaw) {
                        while (iE < enemyObjects.size()) {

                            int xPreEnemy = e.getPresentCoordinates(enemyObjects.get(iE)).x;
                            int yPreEnemy = e.getPresentCoordinates(enemyObjects.get(iE)).y;
                            double distance = Road.distance(towerObjects.get(iT).x+30, towerObjects.get(iT).y+50, xPreEnemy, yPreEnemy);
                            if (distance <= towerObjects.get(iT).range) {
                                time[iT] = 0;


                                if (towerObjects.get(iT).ID == 1) {
                                    if (flagSound==1) SoundNormal();
                                    towerObjects.get(iT).bulletofTower.add(cb.creNormalBullet(towerObjects.get(iT).x, towerObjects.get(iT).y, enemyObjects.get(iE)));
                                }
                                if (towerObjects.get(iT).ID == 2) {
                                    if (flagSound==1) SoundSniper();
                                    towerObjects.get(iT).bulletofTower.add(cb.creSniperBullet(towerObjects.get(iT).x, towerObjects.get(iT).y, enemyObjects.get(iE)));
                                }
                                if (towerObjects.get(iT).ID == 3) {
                                    if (flagSound==1) SoundMachine();
                                    towerObjects.get(iT).bulletofTower.add(cb.creMachineBullet(towerObjects.get(iT).x, towerObjects.get(iT).y, enemyObjects.get(iE)));
                                }
                                break;
                            }
                            iE++;
                        }
                    }
                    int iB = 0;
                    while (iB < towerObjects.get(iT).bulletofTower.size()) {
                        if (towerObjects.get(iT).bulletofTower.get(iB).hit) {
                            towerObjects.get(iT).bulletofTower.get(iB).enemy.health -= towerObjects.get(iT).bulletofTower.get(iB).damage;
                            towerObjects.get(iT).bulletofTower.remove(iB);
                        } else iB++;
                    }
                    timeE += 2;
                    time[iT]++;
                }
                timeE += 2;
            }

        };
        timer.start();

    }
    private void checkGameOver(GraphicsContext gc) {
        if (run.HP <= 0) {
            timer.stop();
            if (flagSound==1)
                try {
                Media media = new Media(getClass().getResource("/Sound/Defeat.mp3").toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);
                mediaPlayer.setVolume(0.5);
                } catch (Exception e) {}

            Image VictoryImg = new Image("file:src/Image/Defeat.png");
            gc.drawImage(VictoryImg, 330, 100, 360, 400);
            Continue();
        }
        if (index==sizeOfEnemy && enemyObjects.isEmpty()) {
            timer.stop();
            if (flagSound==1)
                try {
                Media media = new Media(getClass().getResource("/Sound/Victory.mp3").toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);
                mediaPlayer.setVolume(0.5);
            } catch (Exception e) {}

            Image VictoryImg = new Image("file:src/Image/Victory.png");
            gc.drawImage(VictoryImg, 206, 50, 608, 500);
            Continue();
        }
    }

    private void Continue() {
        Button ContiButton = new Button();
        ContiButton.setLayoutX(455);    ContiButton.setLayoutY(400);
        ContiButton.setPrefSize(110, 25);
        ContiButton.setOpacity(0);

        Image ContiImg = new Image("file:src/Image/Continue.png");
        ImageView ContiIv = new ImageView(ContiImg);
        ContiIv.setLayoutX(455); ContiIv.setLayoutY(400);

        ContiButton.setOnMouseEntered((eventContinue) -> {
            Bloom bloom = new Bloom();
            bloom.setThreshold(0.5);
            ContiIv.setEffect(bloom);

            ContiButton.setOnMouseExited((Event2) -> {
                bloom.setThreshold(1);
                ContiIv.setEffect(bloom);
            });
        });

        ContiButton.setOnMouseClicked((eventContinue) -> {
            music.stop();
            mainMenu.start(window);
        });

        root.getChildren().addAll(ContiIv, ContiButton);
    }

    private void drawStore(GraphicsContext gc, int x, int y) {
        Image store = new Image("file:src/Image/Store.png");
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        ImageView iv = new ImageView(store);
        iv.setFitWidth(130);
        iv.setFitHeight(130);
        Image st = iv.snapshot(params, null);
        gc.drawImage(st, x-24, y-5);
    }

    private void drawMap(GraphicsContext gc) {
        gc.drawImage(new Image("file:src/Image/Map3.png"), 0, 0);
        gc.drawImage(new Image("file:src/Image/PlayerInfo2.png"), 20, 10, 210, 75);
        gc.drawImage(new Image("file:src/Image/Pause.png"), 960, 10);
        gc.drawImage(musicImg, 960, 80);
    }

    private void update() {
        towerObjects.forEach(bT -> bT.bulletofTower.forEach(GameObject::update));
        enemyObjects.forEach(GameObject::update);
        towerObjects.forEach(GameObject::update);
    }

    private void render() {
        for(int i=enemyObjects.size()-1; i>=0; i--) {
            enemyObjects.get(i).render(gc);
        }

        towerObjects.forEach(g -> g.render(gc));

        for (TowerObject towerObject : towerObjects) {
            for (int n = 0; n < towerObject.bulletofTower.size(); n++) {
                towerObject.bulletofTower.get(n).render(gc);
            }
        }
    }

    private void Music() {
        try {
            Media media = new Media(getClass().getResource("/Sound/TheAvengers-AlanSilvestri_3zzk.mp3").toURI().toString());
            music = new MediaPlayer(media);
            music.setVolume(0.2);
        } catch (Exception e) {}
    }
    private void SoundBuild() {
        try {
            Media media = new Media(getClass().getResource("/Sound/Build.mp3").toURI().toString());
            build = new MediaPlayer(media);
            build.setAutoPlay(true);
            build.setVolume(0.6);
        } catch (Exception e) {}
    }
    private void SoundNormal() {
        try {
            Media media = new Media(getClass().getResource("/Sound/Normal.mp3").toURI().toString());
            normal = new MediaPlayer(media);
            normal.setAutoPlay(true);
            normal.setVolume(0.1);
        } catch (Exception e) {}
    }
    private void SoundSniper() {
        try {
            Media media = new Media(getClass().getResource("/Sound/Sniper.mp3").toURI().toString());
            sniper = new MediaPlayer(media);
            sniper.setAutoPlay(true);
            sniper.setVolume(0.2);
        } catch (Exception e) {}
    }
    private void SoundMachine() {
        try {
            Media media = new Media(getClass().getResource("/Sound/Machine.mp3").toURI().toString());
            machine = new MediaPlayer(media);
            machine.setAutoPlay(true);
            machine.setVolume(0.1);
        } catch (Exception e) {}
    }
}

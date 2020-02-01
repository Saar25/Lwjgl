package game;

import engine.components.*;
import engine.components.ai.AIComponent;
import engine.components.ai.ZombieAIComponent;
import engine.components.animation.SimpleAnimationComponent;
import engine.components.physics.GravityComponent;
import engine.components.physics.TerrainCollisionComponent;
import engine.entity.Entity;
import engine.entity.EntityBlueprint;
import engine.gameengine.Loader;
import engine.models.Model;
import engine.models.Skin;
import engine.particles.ParticleSystem;
import engine.particles.placers.CircleParticlePlacer;
import engine.rendering.Camera;
import engine.skybox.SkyBox;
import engine.terrain.World;
import engine.terrain.lowpoly.LowPolyWorld;
import engine.terrain.multitexture.MultiTextureTerrainWorld;
import engine.util.node.Node;
import engine.water.WaterTile;
import glfw.input.Keyboard;
import opengl.textures.CubeMapTexture;
import opengl.textures.Texture2D;
import tegui.objects.TImage;

class WorldLoader {

    private final Loader loader;

    private long last;

    WorldLoader(Loader loader) {
        this.loader = loader;
        this.last = System.currentTimeMillis();
    }

    Node loadEntities(World world) throws Exception {
        Node entities = new Node();

        Skin skin = loader.loadSkin("/textures/horse.png");
        Model model = loader.loadModel("/objModels/horse.obj");
        EntityBlueprint horse = new EntityBlueprint(model, skin);

        skin = loader.loadSkin("/textures/lowPolyTree.png");
        model = loader.loadModel("/objModels/lowPolyTree.obj");
        EntityBlueprint tree1 = new EntityBlueprint(model, skin);

        skin = loader.loadSkin("/textures/tree.png");
        model = loader.loadModel("/objModels/tree.obj");
        EntityBlueprint tree2 = new EntityBlueprint(model, skin);

        skin = loader.loadSkin("/textures/barrel.png");
        model = loader.loadModel("/objModels/barrel.obj");
        EntityBlueprint barrelBp = new EntityBlueprint(model, skin);

        model = loader.loadModel("/objModels/fern.obj");
        EntityBlueprint[] fernModels = new EntityBlueprint[4];
        for (int i = 0; i < fernModels.length; i++) {
            skin = loader.loadSkin("/textures/fern.png")
                    .setTextureIndex(i)
                    .setTransparent(true)
                    .setTextureRows(2);
            fernModels[i] = new EntityBlueprint(model, skin);
        }

        for (int i = 0; i < Configs.TERRAIN_SIZE; i++) {
            float x = (float) Math.random() * Configs.TERRAIN_SIZE - Configs.TERRAIN_SIZE / 2;
            float z = (float) Math.random() * Configs.TERRAIN_SIZE - Configs.TERRAIN_SIZE / 2;
            float h = world.getHeight(x, z);
            if (h >= 0) {
                double rand = Math.random();
                if (rand < .5f) {
                    Entity entity = tree1.createInstance();
                    entity.getTransform().setPosition(x, h, z);
                    entities.attachChild(entity);
                } else {
                    Entity entity = tree2.createInstance();
                    entity.getTransform().setPosition(x, h, z);
                    entity.getTransform().setScale(10);
                    entities.attachChild(entity);
                }
            }

            x = (float) Math.random() * Configs.TERRAIN_SIZE - Configs.TERRAIN_SIZE / 2;
            z = (float) Math.random() * Configs.TERRAIN_SIZE - Configs.TERRAIN_SIZE / 2;
            h = world.getHeight(x, z);
            if (h > 0) {
                Entity entity = fernModels[(int) (Math.random() * 4)].createInstance();
                entity.getTransform().setPosition(x, world.getHeight(x, z), z);
                entities.attachChild(entity);
            }
        }

        float h = world.getHeight(20, 20);
        Entity barrel = barrelBp.createInstance();
        barrel.getTransform().setPosition(20, h, 20);
        entities.attachChild(barrel);

        horse.getComponents().add(() -> new AIComponent());
        horse.getComponents().add(() -> new GravityComponent());
        horse.getComponents().add(() -> new MovementComponent());
        horse.getComponents().add(() -> new AutoJumpComponent());
        horse.getComponents().add(() -> new SimpleAnimationComponent());
        horse.getComponents().add(() -> new TerrainCollisionComponent(world));
        horse.getComponents().add(() -> new ActiveMovementComponent(30, 5, 10, 2));

        Entity h1 = horse.createInstance();
        h1.getTransform().setPosition(-5, 10, 20);

        Entity h2 = horse.createInstance();
        h2.getTransform().setPosition(+5, 10, 20);

        entities.attachChild(h1);
        entities.attachChild(h2);

        printAfterLoad("entities");

        return entities;
    }

    Entity loadPlayer(World world, Keyboard keyboard, Camera camera) throws Exception {
        final Skin skin = loader.loadSkin("/textures/player.png");
        final Model model = loader.loadModel("/objModels/player.obj");
        final Entity player = new Entity(model, skin);

        player.getComponents().add(new GravityComponent());
        player.getComponents().add(new MovementComponent());
        player.getComponents().add(new TerrainCollisionComponent(world));
        player.getComponents().add(new KeyboardInputComponent(keyboard));
        player.getComponents().add(new ActiveMovementComponent(30, 10, 50, 2));
        player.getComponents().add(new BackFaceComponent(camera.getTransform()));
        player.getComponents().add(new FloatingComponent(-5f));
        printAfterLoad("player");
        return player;
    }

    Node loadZombies(World world, Entity player, int count) throws Exception {
        if (count == 0) {
            return new Node();
        }

        Skin skin = loader.loadSkin("/textures/player.png");
        Model model = loader.loadModel("/objModels/player.obj");
        EntityBlueprint zombieBp = new EntityBlueprint(model, skin);

        zombieBp.getComponents().add(() -> new GravityComponent());
        zombieBp.getComponents().add(() -> new MovementComponent());
        zombieBp.getComponents().add(() -> new AutoJumpComponent());
        zombieBp.getComponents().add(() -> new SimpleAnimationComponent());
        zombieBp.getComponents().add(() -> new TerrainCollisionComponent(world));
        zombieBp.getComponents().add(() -> new ActiveMovementComponent(20, 10, 20, 2));
        zombieBp.getComponents().add(() -> new ZombieAIComponent(player.getTransform()));

        Node zombies = new Node();
        for (int i = 0; i < count; i++) {
            float x = (float) Math.random() * 200 + 20;
            float z = (float) Math.random() * 200 + 20;
            Entity zombie = zombieBp.createInstance();
            zombie.getTransform().setPosition(x, 30, z);
            zombies.attachChild(zombie);
        }

        printAfterLoad("zombies");

        return zombies;
    }

    World loadTerrains() throws Exception {
        final boolean multiTexture = true;
        final World world;

        if (multiTexture) {
            final MultiTextureTerrainWorld mttWorld = new MultiTextureTerrainWorld(Configs.TERRAIN_SIZE);
            mttWorld.setBlendMap(Texture2D.of("/terrains/blendMap2.png"));
            mttWorld.setDTexture(Texture2D.of("/terrains/grass.png"));
            mttWorld.setRTexture(Texture2D.of("/terrains/mud.png"));
            mttWorld.setGTexture(Texture2D.of("/terrains/grassFlowers.png"));
            mttWorld.setBTexture(Texture2D.of("/terrains/path.png"));
            world = mttWorld;
        } else {
            world = new LowPolyWorld(Configs.TERRAIN_SIZE);
        }

        world.addTerrain(+0, +0, -1);
        world.addTerrain(-1, +0, -1);
        world.addTerrain(-1, +0, +0);
        world.addTerrain(+0, +0, +0);

        printAfterLoad("terrains");

        return world;
    }

    Node loadWaterTiles() throws Exception {
        final Node waterTiles = new Node();

        WaterTile.defaultDudvMap = Texture2D.of("/maps/waterDudvMap.png");
        WaterTile.defaultNormalsMap = Texture2D.of("/maps/waterNormalMap.png");

        printAfterLoad("water tiles");

        return waterTiles;
    }

    Node loadSkyBoxes() throws Exception {
        Node skyBoxes = new Node();
        CubeMapTexture texture = CubeMapTexture.builder()
                .positiveX("/skyBoxes/right.png")
                .negativeX("/skyBoxes/left.png")
                .positiveY("/skyBoxes/top.png")
                .negativeY("/skyBoxes/bottom.png")
                .positiveZ("/skyBoxes/front.png")
                .negativeZ("/skyBoxes/back.png")
                .create();
        SkyBox skyBox = new SkyBox(texture);
        skyBoxes.attachChild(skyBox);

        printAfterLoad("sky boxes");

        return skyBoxes;
    }

    Node loadParticleSystems() throws Exception {
        final Node particleSystems = new Node();

        Skin skin = loader.loadSkin("/textures/particleFire.png").setTextureRows(4);
        particleSystems.attachChild(new ParticleSystem(new CircleParticlePlacer(10), skin));

        skin = loader.loadSkin("/textures/particleStar.png");
        particleSystems.attachChild(new ParticleSystem(new CircleParticlePlacer(100), skin));

        printAfterLoad("particles");

        return particleSystems;
    }

    Node loadImages() throws Exception {
        Node images = new Node();

        TImage image = new TImage(Texture2D.of("/textures/HealthBar.png"));
        image.getStyle().position.set(300, 600);
        image.getStyle().dimensions.set(600, 200);
        images.attachChild(image);

        printAfterLoad("images");

        return images;
    }

    private void printAfterLoad(String loaded) {
        System.out.print("Loaded " + loaded + ".");
        for (int i = 0; i < 15 - loaded.length(); i++) {
            System.out.print(" ");
        }
        long current = System.currentTimeMillis();
        System.out.println("Time in seconds: " + (current - last) / 1000d);
        last = current;
    }

}

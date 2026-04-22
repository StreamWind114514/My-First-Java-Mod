package testmod;

import arc.*;
import arc.util.*;
import arc.graphics.g2d.TextureRegion;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
import mindustry.Vars;
import testmod.content.Turret;

import java.util.Map;
import java.util.HashMap;

public class TestMod extends Mod {
    public static final Map<String, TextureRegion> cardImages = new HashMap<>();
    
    public TestMod() {
        Log.info("TestMod constructor called.");
    }
    
    @Override
    public void init() {
        Log.info("TestMod init() called. Game is fully loaded.");
        loadAllCardImages();
        // TODO
    }
    
    @Override
    public void loadContent() {
        Log.info("TestMod loadContent() called. Loading custom content...");
        Turret.load();
    }
    
    public static void loadAllCardImages() {
        String modName = Vars.mods.getMod(TestMod.class).name;
        String[] suits = {"clubs", "diamonds", "hearts", "spades"};
        for (String suit : suits) {
            for (int value = 2; value <= 14; value++) {
                String regionName = modName + "-" + suit + value;
                TextureRegion region = Core.atlas.find(regionName);
                if (region.found()) {
                    cardImages.put(suit + value, region);
                    Log.info("Image: " + regionName + " loaded successfully");
                } else {
                    Log.warn("Missing card image: " + regionName);
                }
            }
        }
        TextureRegion bregion = Core.atlas.find(modName + "-cardback");
        if (bregion.found()) {
            cardImages.put("cardback", bregion);
        } else {
            Log.warn("Missing cardback image: " + modName + "-cardback");
        }
    }
}
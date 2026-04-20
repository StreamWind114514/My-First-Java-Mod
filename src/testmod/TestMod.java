package testmod;

import arc.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;

import testmod.content.Turret;

public class TestMod extends Mod {

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
        public static final Map<String, TextureRegion> cardImages = new HashMap<>();
        String modName = Vars.mods.getMod(TestMod.class).name;
        String[] suits = {"clubs", "diamonds", "hearts", "spades"};
        for (String suit : suits) {
            for (int value = 2; value <= 14; value++) {
                String regionName = modName + "-" + suit + value;
                TextureRegion region = Core.atlas.find(regionName);
                if (region.found()) {
                    cardImages.put(suit + value, region);
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
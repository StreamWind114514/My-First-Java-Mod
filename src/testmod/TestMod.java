package testmod;

import arc.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;

import testmod.content.PokerTurrt;

public class TestMod extends Mod {

    public TestMod() {
        Log.info("TestMod constructor called.");
        
    }
    @Override
    public void init() {
        Log.info("TestMod init() called. Game is fully loaded.");
        // TODO
    }
    @Override
    public void loadContent() {
        Log.info("TestMod loadContent() called. Loading custom content...");
        PokerTurrt.load();
    }
}
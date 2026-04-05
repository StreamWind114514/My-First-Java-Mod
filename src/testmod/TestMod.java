package testmod;

import arc.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;

public class TestMod extends Mod {

    public TestMod() {
        Log.info("TestMod constructor called.");
        Events.on(ClientLoadEvent.class, e -> {
            // 延迟10帧后显示对话框，确保UI已经准备好
            Time.runTask(10f, () -> {
                BaseDialog dialog = new BaseDialog("my_first_dialog");  // 对话框标题
                dialog.cont.add("Welcome to my mod!").row();   // 显示文字
                dialog.cont.button("OK", dialog::hide).size(100f, 50f);
                dialog.show();
            });
        });
    }
    @Override
    public void init() {
        Log.info("TestMod init() called. Game is fully loaded.");
        // TODO
    }
    @Override
    public void loadContent() {
        Log.info("TestMod loadContent() called. Loading custom content...");
        ModItems.load();
    }
}
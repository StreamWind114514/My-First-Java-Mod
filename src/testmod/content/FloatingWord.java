package testmod.content;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.math.Align;
import arc.util.*;
import mindustry.entities.Effect;
import mindustry.graphics.Lines;
import mindustry.ui.Fonts;
import mindustry.graphics.Pal;

public class FloatingWord {
    public static Effect floatingText = new Effect(60f, 80f, e -> {
        if (!(e.data instanceof String)) return;
        String text = (String) e.data;
        Draw.color(e.color);
        Font font = Fonts.outline;
        font.setColor(e.color);
        float yOffset = (1f - e.fin(Interp.pow2Out)) * 20f; // 向上飘
        font.draw(text, e.x, e.y + yOffset, Align.center);
        Draw.color();
});
}
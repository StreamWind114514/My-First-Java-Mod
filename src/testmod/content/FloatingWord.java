package testmod.content;

import mindustry.entities.effect.Effect;
import mindustry.graphics.Draw;
import mindustry.graphics.Lines;
import arc.graphics.Color;
import arc.math.Interp;

public class FloatingWorld {
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
package testmod.content;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.math.Interp;
import arc.util.*;
import mindustry.entities.Effect;
import arc.graphics.g2d.Lines;
import mindustry.ui.Fonts;
import mindustry.graphics.Pal;

public static class FloatingWord {
    public static Effect floatingText = new Effect(60f, 80f, e -> {
        if (!(e.data instanceof Object[])) return;
        Object[] data = (Object[]) e.data;
        String text = (String) data[0];
        Color color = (Color) data[1];
        Draw.color(color);
        Font font = Fonts.outline;
        font.setColor(color);
        float yOffset = (1f - e.fin(Interp.pow2Out)) * 24f;
        font.draw(text, e.x, e.y + yOffset, 0.5f, Align.center);
        Draw.color();
    });
}
package testmod.content;

import arc.graphics.g2d.Font;
import arc.graphics.g2d.GlyphLayout;
import arc.math.Interp;
import arc.graphics.Color;
import mindustry.entities.Effect;
import mindustry.ui.Fonts;

public class FloatingWord {
    private static final GlyphLayout layout = new GlyphLayout();

    public static Effect floatingText = new Effect(60f, 80f, e -> {
        if (!(e.data instanceof Object[] data)) return;
        String text = (String) data[0];
        Color color = (Color) data[1];

        Font font = Fonts.outline;
        font.setColor(color);
        layout.setText(font, text);
        float drawX = e.x - layout.width / 2f;
        float yOffset = e.fout(Interp.pow2Out) * 24f;
        font.draw(text, drawX, e.y + yOffset);
    });
}
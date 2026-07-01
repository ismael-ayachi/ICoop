package ch.epfl.cs107.icoop.handler.menu;

import ch.epfl.cs107.play.engine.actor.ImageGraphics;
import ch.epfl.cs107.play.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

/**
 * Provides a base class for menus with three states (default, left, right),
 * rendering different images based on the current state.
 */
public abstract class AbstractMenu implements Menu {
    protected String[] images = new String[3];
    protected int currentState = 0;

    /**
     * Creates an AbstractMenu with specific images for each state.
     * @param defaultImage (String): File path for the default menu state image, not null
     * @param leftImage (String): File path for the left menu state image, not null
     * @param rightImage (String): File path for the right menu state image, not null
     */
    public AbstractMenu(String defaultImage, String leftImage, String rightImage) {
        this.images[0] = defaultImage;
        this.images[1] = leftImage;
        this.images[2] = rightImage;
    }

    @Override
    public void draw(Canvas canvas) {
        float width = canvas.getTransform().getX().getX();
        float height = canvas.getTransform().getY().getY();

        float ratio = canvas.getWidth() / (float) canvas.getHeight();
        if (ratio > 1)
            height = width / ratio;
        else
            width = height * ratio;
        Vector anchor = canvas.getTransform().getOrigin().sub(new Vector( width / 2, height / 2));

        ImageGraphics menuGraphics = new ImageGraphics(
                ResourcePath.getForeground(images[currentState]), width, height,
                new RegionOfInterest(0, 0, 1024, 1024), anchor, 1, 2001); //
        menuGraphics.draw(canvas);
    }
}
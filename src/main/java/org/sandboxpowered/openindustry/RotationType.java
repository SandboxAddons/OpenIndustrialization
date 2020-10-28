package org.sandboxpowered.openindustry;

import org.sandboxpowered.api.state.Properties;
import org.sandboxpowered.api.state.property.Property;
import org.sandboxpowered.api.util.Direction;

public enum RotationType {
    NONE,
    HORIZONTAL(Properties.HORIZONTAL_FACING, Direction.NORTH),
    ALL(Properties.FACING, Direction.UP);

    private final Property<Direction> property;
    private final Direction defaultValue;

    RotationType(Property<Direction> property, Direction defaultValue) {
        this.property = property;
        this.defaultValue = defaultValue;
    }

    RotationType() {
        this(null, null);
    }

    public Property<Direction> getProperty() {
        return property;
    }

    public Direction getDefaultValue() {
        return defaultValue;
    }
}

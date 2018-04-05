package com.omerozer.knit.components;

/**
 *
 * This class represents a unique {@link com.omerozer.knit.MemoryEntity} inside {@link com.omerozer.knit.components.graph.UsageGraph}
 * At it's core, it holds a {@link Short} value to differentiate entities. Each entity created will be assigned a different ComponentTag
 * with a unique {@link Short} . These get created incrementally.
 *
 * @author Omer Ozer
 */

public final class ComponentTag {

    private static Short baseTag = Short.MIN_VALUE;

    /**
     * Static method responsible for creation of unique tags. Each tag starts from {@code Short.MIN_VALUE}.
     * @return Returns a unique ComponentTag with a unique {@link Short} tag.
     */
    public static ComponentTag getNewTag(){
        return new ComponentTag(baseTag++);
    }

    /**
     * Resets baseTag to {@code Short.MIN_VALUE}
     */
    public static void reset(){
        baseTag = Short.MIN_VALUE;
    }

    private Short tag;

    private ComponentTag(Short tag){
        this.tag = tag;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ComponentTag that = (ComponentTag) object;

        return tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }

    /**
     * Returns the core {@link Short} tag.
     * @return the {@link Short} tag.
     */
    public Short getTag(){
        return tag;
    }
}

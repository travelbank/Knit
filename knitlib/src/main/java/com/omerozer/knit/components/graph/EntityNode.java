package com.omerozer.knit.components.graph;

import com.omerozer.knit.components.ComponentTag;

import java.util.LinkedHashSet;
import java.util.Set;


/**
 *
 * Node that is used by the {@link UsageGraph} .
 * Contains the associated {@link ComponentTag} and {@link EntityType} for each component
 * and a {@link Set} of entities that this entity depends on.
 * @see UsageGraph
 * @author Omer Ozer
 */

public class EntityNode {
    public ComponentTag tag;
    public EntityType type;
    public Set<EntityNode> next = new LinkedHashSet<>();
    public EntityNode(ComponentTag componentTag,EntityType entityType){
        this.tag = componentTag;
        this.type = entityType;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        EntityNode that = (EntityNode) object;

        return tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }
}

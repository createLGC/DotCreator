package com.kt.dotcreator.store;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ColorData.class, name = "colorData"),
    @JsonSubTypes.Type(value = ImageData.class, name = "imageData")
})
public abstract class SquareData {}

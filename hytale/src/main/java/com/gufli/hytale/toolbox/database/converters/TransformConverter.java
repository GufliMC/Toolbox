package com.gufli.hytale.toolbox.database.converters;

import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.math.vector.Transform;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.bson.BsonDocument;


@Converter(autoApply = true)
public class TransformConverter implements AttributeConverter<Transform, String> {

    @Override
    public String convertToDatabaseColumn(Transform attribute) {
        return Transform.CODEC.encode(attribute, new ExtraInfo()).toJson();
    }

    @Override
    public Transform convertToEntityAttribute(String dbData) {
        return Transform.CODEC.decode(BsonDocument.parse(dbData), new ExtraInfo());
    }
}

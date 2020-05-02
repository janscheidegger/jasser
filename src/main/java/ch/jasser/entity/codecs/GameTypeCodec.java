package ch.jasser.entity.codecs;

import ch.jasser.entity.GameType;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class GameTypeCodec implements Codec<GameType> {
    @Override
    public GameType decode(BsonReader bsonReader, DecoderContext decoderContext) {
        return GameType.valueOf(bsonReader.readString());

    }

    @Override
    public void encode(BsonWriter bsonWriter, GameType type, EncoderContext encoderContext) {
        bsonWriter.writeString(type.name());

    }

    @Override
    public Class<GameType> getEncoderClass() {
        return GameType.class;
    }
}

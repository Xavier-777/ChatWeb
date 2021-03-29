package com.mei.agora.media;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}

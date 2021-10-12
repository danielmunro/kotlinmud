package kotlinmud.startup.parser

enum class TokenType {
    Section,
    ContentType,
    ID,
    Name,
    Brief,
    Description,
    Keyword,
    Direction,
    MaxAmountInRoom,
    MaxAmountInGame,
    ItemId,
    MobId,
    RoomId,
    Props,
}
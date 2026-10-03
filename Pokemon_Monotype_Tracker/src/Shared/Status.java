package Shared;

public enum Status {
    LoginSuccess,
    LoginFailure,
    LoginRequired,
    ErrorUsernameAlreadyExists,
    RegisterSuccess,
    ErrorAlreadyLoggedIn,
    LoginUnknownUser,
    TypeQuerySuccess,
    KnownMoveQuerySuccess,
    PokemonIDQuerySuccess,
    RunQuerySuccess,
    AddRunSuccess,
    UpdateRunSuccess,
    DeleteRunSuccess,
    ErrorInvalidPokedexID,
    ErrorUnknownPokemonType,
    ErrorDatabaseAccess,
    ErrorNetwork,
    ErrorRunQuery,
    ErrorAddRun,
    ErrorUpdateRun,
    ErrorDeleteRun,
    UnknownStatus
}
